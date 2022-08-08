package com.quannm18.assignmentquannmph14304_networking.view.fragment

import android.annotation.SuppressLint
import android.app.ListActivity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.orhanobut.hawk.Hawk
import com.quannm18.assignmentquannmph14304_networking.R
import com.quannm18.assignmentquannmph14304_networking.model.NoteModel
import com.quannm18.assignmentquannmph14304_networking.model.UserModel
import com.quannm18.assignmentquannmph14304_networking.view.activity.ChangePasswordActivity
import com.quannm18.assignmentquannmph14304_networking.view.activity.LoginActivity
import com.quannm18.assignmentquannmph14304_networking.view.adapter.TodoListAdapter
import com.quannm18.assignmentquannmph14304_networking.view.repository.TodoRepository
import com.quannm18.assignmentquannmph14304_networking.viewmodel.MainViewModel
import com.quannm18.assignmentquannmph14304_networking.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_user.*
import kotlinx.android.synthetic.main.fragment_user.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class UserFragment : Fragment() {
    private lateinit var mRepository: TodoRepository
    private lateinit var mFactory: MainViewModelFactory
    private lateinit var mainViewModel: MainViewModel

    var mListNote: MutableList<NoteModel> = mutableListOf()

    companion object{
        var mNoteLiveData = MutableLiveData<MutableList<NoteModel>>()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Hawk.init(requireContext()).build()
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRepository = TodoRepository()
        mFactory = MainViewModelFactory(mRepository)
        mainViewModel = ViewModelProvider(this, mFactory)[MainViewModel::class.java]


        val mBundleLogin = Hawk.get<Bundle>("login")
        if (mBundleLogin!=null){
            val mUsername = mBundleLogin.getString("mUsername")
            val mPassword = mBundleLogin.getString("mPassword")
            getListNote(requireContext(),mUsername.toString())
            mNoteLiveData.observe(requireActivity()){
                tvTotalNoteUser.setText("Total: ${it.size}")
                var mFinished = 0
                var mPending = 0
                it.forEach {
                    if (it.status=="1"){
                        mFinished++
                    }
                    if (it.status=="0"){
                        mPending++
                    }
                }
                tvFinishedNoteUser.setText("Finished: ${mFinished}")
                tvPendingNoteUser.setText("Pending: ${mPending}")
            }
            view.edUsernameUser.apply {
                setText("$mUsername")
                setTextColor(R.color.color_main)
            }
            view.edFullNameUser.apply {
                setText("Welcome back")
                setTextColor(R.color.color_main)
            }
        }

        btnLogOut.setOnClickListener {
            startActivity(Intent(it.context,LoginActivity::class.java))
            requireActivity().finish()
        }

        btnChangePassword.setOnClickListener {
            startActivity(Intent(it.context,ChangePasswordActivity::class.java))
            requireActivity().finish()
        }


    }
    fun getTodoListFromApp() {
        mainViewModel.myNoteFromUNList.observe(requireActivity()) { res ->
            if (res.isSuccessful) {
                res.body()?.let { it ->
                    mListNote.clear()
                    mListNote.addAll(it)
                }
            }
        }
    }

    fun getListNote(context: Context,username: String) {
        var mList: MutableList<NoteModel> = mutableListOf()
        val baseUrl = "https://quannm18.000webhostapp.com/assignment/select_note_p.php?username=$username"
        val queue: RequestQueue = Volley.newRequestQueue(context)
        CoroutineScope(Dispatchers.IO).launch{
            val arrayRequest = JsonArrayRequest(
                baseUrl,
                {
                    for (i in 0 until it.length()) {
                        try {
                            val mObject: JSONObject = it.getJSONObject(i)
                            val mNoteModel = NoteModel(
                                id = mObject.getString("id"),
                                title = mObject.getString("title"),
                                description = mObject.getString("description"),
                                date = mObject.getString("date"),
                                priority_level = mObject.getString("priority_level"),
                                status = mObject.getString("status"),
                                username = mObject.getString("username")
                            )
                            mList.add(mNoteModel)
                            Log.e("TAG", "onCreate: ${mList.size}")
                        } catch (e: Exception) {

                            tvTotalNoteUser.visibility = View.INVISIBLE
                            tvFinishedNoteUser.setHint("Your list is empty!")
                            tvPendingNoteUser.visibility = View.INVISIBLE
                            e.printStackTrace()
                        }
                    }
                    mNoteLiveData.postValue(mList)
                },
                {
                    tvTotalNoteUser.visibility = View.INVISIBLE
                    tvFinishedNoteUser.setHint("Your list is empty!")
                    tvFinishedNoteUser.gravity = Gravity.CENTER
                    tvPendingNoteUser.visibility = View.INVISIBLE
                    it.printStackTrace()
                }
            )
            queue.add(arrayRequest)
        }
    }
    override fun onResume() {
        super.onResume()
        getListNote(requireContext(), Hawk.get<Bundle>("login").getString("mUsername").toString())
    }
}