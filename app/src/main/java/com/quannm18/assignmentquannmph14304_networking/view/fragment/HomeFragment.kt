package com.quannm18.assignmentquannmph14304_networking.view.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
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
import com.quannm18.assignmentquannmph14304_networking.network.UpdateListener
import com.quannm18.assignmentquannmph14304_networking.view.adapter.TodoListAdapter
import com.quannm18.assignmentquannmph14304_networking.view.repository.TodoRepository
import com.quannm18.assignmentquannmph14304_networking.viewmodel.MainViewModel
import com.quannm18.assignmentquannmph14304_networking.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.dialog_empty.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*

class HomeFragment : Fragment(), UpdateListener {
    private val mList: MutableLiveData<MutableList<NoteModel>> = MutableLiveData()
    private val mAdapter: TodoListAdapter = TodoListAdapter()
    private lateinit var mainViewModel: MainViewModel
    private lateinit var todoRepository: TodoRepository
    private lateinit var mainViewModelFactory: MainViewModelFactory
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Hawk.init(requireContext()).build()
        view.edTitle.setHint(getString(R.string.add_your_new_todo))

        rcvTodoList.visibility = View.VISIBLE
        todoRepository = TodoRepository()
        mainViewModelFactory = MainViewModelFactory(todoRepository)
        mainViewModel = ViewModelProvider(this, mainViewModelFactory)[MainViewModel::class.java]
        getListNote(requireContext(), Hawk.get<Bundle>("login").getString("mUsername").toString())

        setShowForm(false)
        mList.observe(requireActivity()) {
            it?.let {
                mAdapter.initData(it, this)
                mAdapter.notifyDataSetChanged()

                if (it.size <= 0) {
                    setShowForm(false)
                    val cdt = object : CountDownTimer(2000, 1000) {
                        override fun onTick(p0: Long) {

                        }

                        override fun onFinish() {
                            val mDialog: Dialog = Dialog(this@HomeFragment.requireContext())
                            val mViewDialog =
                                LayoutInflater.from(requireContext())
                                    .inflate(R.layout.dialog_empty, null)
                            mViewDialog.btnDialogOK.setOnClickListener {
                                mDialog.dismiss()
                            }
                            mViewDialog.btnDialogCancel.setOnClickListener {
                                mDialog.dismiss()
                            }
                            mDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                            mDialog.setContentView(mViewDialog)
                            mDialog.create()
                            mDialog.show()

                            edTitle.visibility = View.VISIBLE
                            btnAddToList.visibility = View.VISIBLE
                            tvQuotesHome.visibility = View.GONE
                            btnAddNote.visibility = View.GONE
                        }
                    }.start()
                } else {
                    rcvTodoList.visibility = View.VISIBLE
                    tvQuotesHome.visibility = View.GONE
                }
            }
        }




        view.btnAddToList.setOnClickListener {
            setShowForm(true)
            view.edTitle.setHint("Title")
            rcvTodoList.visibility = View.GONE
        }

        view.edTime.setOnClickListener {
            showDatePickerDialog(view.edTime)
        }
        view.rcvTodoList.apply {
            adapter = mAdapter
            addItemDecoration(
                DividerItemDecoration(
                    requireActivity(),
                    DividerItemDecoration.VERTICAL
                )
            )
            layoutManager = LinearLayoutManager(requireActivity())
        }

        var priorityLevel = ""
        edLevel.setOnCheckedChangeListener { _, i ->
            when (i) {
                2131231274 -> {
                    Toast.makeText(requireContext(), "Easy", Toast.LENGTH_SHORT).show()
                    priorityLevel = "easy"
                }
                2131231276 -> {
                    Toast.makeText(requireContext(), "Medium", Toast.LENGTH_SHORT).show()
                    priorityLevel = "medium"
                }
                2131231275 -> {
                    Toast.makeText(requireContext(), "Hard", Toast.LENGTH_SHORT).show()
                    priorityLevel = "hard"
                }
            }
        }
        btnAddNote.setOnClickListener {
            val title = edTitle.text.toString()
            val description = edDescription.text.toString()
            val time = edTime.text.toString()
            val status = "0"
            val username = Hawk.get<Bundle>("login").getString("mUsername").toString()

            val mNoteModel = NoteModel(
                id = "0",
                title = title,
                description = description,
                date = time,
                priority_level = priorityLevel,
                status = status,
                username = username
            )
            Log.e("TAG", "onViewCreated: $mNoteModel")
            mainViewModel.insertTodo(
                title = title,
                description = description,
                date = time,
                priorityLevel = priorityLevel,
                username = username,
                status = "0"
            )
        }
        mainViewModel.myInsertTodoList.observe(requireActivity()) {
            if (it.isSuccessful) {
                if (it.body()?.contains("công") == true) {
                    Toast.makeText(requireContext(), "Thêm thành công!", Toast.LENGTH_SHORT).show()
                    setShowForm(false)
                    rcvTodoList.visibility = View.VISIBLE
                    getListNote(
                        requireContext(),
                        Hawk.get<Bundle>("login").getString("mUsername").toString()
                    )
                } else {
                    Toast.makeText(requireContext(), "Lỗi thêm!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun showDatePickerDialog(edtTime: EditText) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { view, yearC, monthOfYearC, dayOfMonthC ->

                // Display Selected date in textbox
                edtTime.setText(convertTime(yearC, monthOfYearC, dayOfMonthC))

            },
            year,
            month,
            day
        )

        dpd.show()
    }

    fun convertTime(month: Int, day: Int, year: Int): String {
        val calendar = Calendar.getInstance()
        val dateString = String.format("%02d-%02d-%d", month, day, year)
        return dateString
    }

    fun setShowForm(isShow: Boolean) {
        if (isShow) {
            edDescription.visibility = View.VISIBLE
            edTime.visibility = View.VISIBLE
            edLevel.visibility = View.VISIBLE
            btnAddNote.visibility = View.VISIBLE
            rcvTodoList.visibility = View.VISIBLE
        } else {
            edDescription.visibility = View.GONE
            edTime.visibility = View.GONE
            edLevel.visibility = View.GONE
            rcvTodoList.visibility = View.GONE
            btnAddNote.visibility = View.GONE
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
                            if (mNoteModel.status == "0") {
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

    override fun update(noteModel: NoteModel) {
        if (noteModel != null) {
            mainViewModel.updateTodo(
                title = noteModel.title,
                description = noteModel.description,
                date = noteModel.date,
                priorityLevel = noteModel.priority_level,
                username = noteModel.username,
                status = noteModel.status,
                id = noteModel.id
            )
            Log.e("TAG", "update: ${noteModel.title} ${noteModel.id}", )
            mainViewModel.myUpdateTodoList.observe(this){
                if (it.isSuccessful){
                    Toast.makeText(requireContext(),"${it.body()}",Toast.LENGTH_SHORT).show()
                    getListNote(requireContext(), Hawk.get<Bundle>("login").getString("mUsername").toString())
                }
            }

        }
    }
}