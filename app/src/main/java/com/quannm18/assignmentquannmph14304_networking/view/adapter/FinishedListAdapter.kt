package com.quannm18.assignmentquannmph14304_networking.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.quannm18.assignmentquannmph14304_networking.R
import com.quannm18.assignmentquannmph14304_networking.model.NoteModel
import com.quannm18.assignmentquannmph14304_networking.network.DeleteListener
import kotlinx.android.synthetic.main.row_finished.view.*
import kotlinx.android.synthetic.main.row_todo_home.view.*
import kotlinx.android.synthetic.main.row_todo_home.view.btnRowTodoHome

class FinishedListAdapter : RecyclerView.Adapter<FinishedListAdapter.TodoHolder>() {
    private var mList : MutableList<NoteModel> = mutableListOf()
    private lateinit var mListener : DeleteListener
    class TodoHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(mItem: NoteModel) {
            itemView.btnRowTodoHome.setText("${mItem.title}")


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoHolder {
        return TodoHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_finished,parent,false))
    }

    override fun onBindViewHolder(holder: TodoHolder, position: Int) {
        val mItem = mList[position]
        holder.bind(mItem)
        holder.itemView.btnCloseRow.setOnClickListener {
            mListener.delete(mItem.id)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }
    fun initData(mList : MutableList<NoteModel> , mDeleteListener: DeleteListener) {
        this.mList = mList
        this.mListener = mDeleteListener
    }
}