package com.quannm18.assignmentquannmph14304_networking.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.quannm18.assignmentquannmph14304_networking.network.DeleteListener
import com.quannm18.assignmentquannmph14304_networking.view.adapter.FinishedListAdapter
import com.quannm18.assignmentquannmph14304_networking.view.repository.TodoRepository
import com.quannm18.assignmentquannmph14304_networking.viewmodel.MainViewModel
import com.quannm18.assignmentquannmph14304_networking.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.fragment_finished.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class FinishedFragment : Fragment(), DeleteListener {
    private val mList: MutableLiveData<MutableList<NoteModel>> = MutableLiveData()
    private val mAdapter = FinishedListAdapter()

    private lateinit var mainViewModel: MainViewModel
    private lateinit var todoRepository: TodoRepository
    private lateinit var mainViewModelFactory: MainViewModelFactory
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getListNote(requireContext(), Hawk.get<Bundle>("login").getString("mUsername").toString())

        todoRepository = TodoRepository()
        mainViewModelFactory = MainViewModelFactory(todoRepository)
        mainViewModel = ViewModelProvider(this, mainViewModelFactory)[MainViewModel::class.java]
        return inflater.inflate(R.layout.fragment_finished, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rcvTodoFinished.apply {
            adapter = mAdapter
            addItemDecoration(DividerItemDecoration(requireContext(),DividerItemDecoration.VERTICAL))
            layoutManager = LinearLayoutManager(requireContext())
        }
        mList.observe(requireActivity()){
            mAdapter.initData(it,this)
            mAdapter.notifyDataSetChanged()
        }

    }
    fun getListNote(context: Context, username: String) {
        var mTempList: MutableList<NoteModel> = mutableListOf()
        val baseUrl =
            "https://quannm18.000webhostapp.com/assignment/select_note_p.php?username=$username"
        val queue: RequestQueue = Volley.newRequestQueue(context)
        CoroutineScope(Dispatchers.IO).launch {
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
                            if (mNoteModel.status=="1"){
                                mTempList.add(mNoteModel)
                            }
                            Log.e("TAG", "onCreate: ${mTempList.size}")
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    mList.postValue(mTempList)
                },
                {
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

    override fun delete(id: String) {
        if (id!=null){
            mainViewModel.deleteTodo(id)

            mainViewModel.myDeleteTodoList.observe(this){
                if (it.isSuccessful){
                    Toast.makeText(requireContext(),"${it.body()}",Toast.LENGTH_SHORT).show()
                    getListNote(requireContext(), Hawk.get<Bundle>("login").getString("mUsername").toString())
                }
            }
        }

    }
}