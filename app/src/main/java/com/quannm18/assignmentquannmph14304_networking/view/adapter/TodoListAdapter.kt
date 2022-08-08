package com.quannm18.assignmentquannmph14304_networking.view.adapter

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.quannm18.assignmentquannmph14304_networking.R
import com.quannm18.assignmentquannmph14304_networking.model.NoteModel
import com.quannm18.assignmentquannmph14304_networking.network.UpdateListener
import kotlinx.android.synthetic.main.dialog_update.view.*
import kotlinx.android.synthetic.main.row_todo_home.view.*

class TodoListAdapter : RecyclerView.Adapter<TodoListAdapter.TodoHolder>() {
    private var mList: MutableList<NoteModel> = mutableListOf()
    private lateinit var mListener: UpdateListener

    class TodoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(mItem: NoteModel) {
            itemView.btnRowTodoHome.setText("${mItem.title}")
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoHolder {


        return TodoHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_todo_home, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TodoHolder, position: Int) {
        val mItem = mList[position]
        holder.bind(mItem)

        holder.itemView.setOnClickListener {
            val mDialog: Dialog = Dialog(it.context)
            val mViewDialog =
                LayoutInflater.from(it.context).inflate(R.layout.dialog_update, null)
            mViewDialog.edTitleUpdate.setText("${mItem.title}")
            mViewDialog.edDesUpdate.setText("${mItem.description}")

            mViewDialog.btnUpdate.setOnClickListener {
                val mTitle = mViewDialog.edTitleUpdate.text.toString()
                val mDes = mViewDialog.edDesUpdate.text.toString()
                val isFinished = mViewDialog.chkFinished.isChecked
                var finish = ""
                finish = if (isFinished) {
                    "1"
                } else {
                    "0"
                }
                val mNote = NoteModel(
                    date = mItem.date,
                    description = mDes,
                    id = mItem.id,
                    priority_level = mItem.priority_level,
                    status = finish,
                    title = mTitle,
                    username = mItem.username
                )

                mListener.update(mNote)
                mDialog.dismiss()

            }
            mDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            mDialog.setContentView(mViewDialog)
            mDialog.create()
            mDialog.show()
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun initData(mList: MutableList<NoteModel>, mListener: UpdateListener) {
        this.mList = mList
        this.mListener = mListener
    }
}