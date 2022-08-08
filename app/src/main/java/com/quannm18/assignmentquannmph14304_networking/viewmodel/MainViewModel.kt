package com.quannm18.assignmentquannmph14304_networking.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quannm18.assignmentquannmph14304_networking.model.NoteModel
import com.quannm18.assignmentquannmph14304_networking.model.UserModel
import com.quannm18.assignmentquannmph14304_networking.view.repository.TodoRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val repository: TodoRepository) : ViewModel() {
    val myRepository: MutableLiveData<Response<String>> = MutableLiveData()
    val myInsertRepository: MutableLiveData<Response<String>> = MutableLiveData()
    val myDeleteRepository: MutableLiveData<Response<String>> = MutableLiveData()
    val myUpdateRepository: MutableLiveData<Response<String>> = MutableLiveData()
    val myListUserModel: MutableLiveData<Response<MutableList<UserModel>>> = MutableLiveData()
    val myInsertTodoList: MutableLiveData<Response<String>> = MutableLiveData()
    val myUpdateTodoList: MutableLiveData<Response<String>> = MutableLiveData()
    val myDeleteTodoList: MutableLiveData<Response<String>> = MutableLiveData()

    val myNoteFromUNList: MutableLiveData<Response<MutableList<NoteModel>>> = MutableLiveData()
    val myNotePostList: MutableLiveData<Response<MutableList<NoteModel>>> = MutableLiveData()
    fun getLogin(username: String, password: String) {
        viewModelScope.launch {
            val repository = repository.getLogin(username, password)
            myRepository.value = repository
        }
    }

    fun signUp(fullname: String, username: String, password: String, email: String) {
        viewModelScope.launch {
            val repository = repository.insertPost(fullname, username, password, email)
            myInsertRepository.value = repository
        }
    }

    fun postUpdate(fullname: String, username: String) {
        viewModelScope.launch {
            val repository = repository.updatePost(fullname = fullname, username = username)
            myUpdateRepository.value = repository
        }
    }

    fun postDelete(username: String) {
        viewModelScope.launch {
            val repository = repository.deletePost(username)
            myDeleteRepository.value = repository
        }
    }

    fun selectInsert() {
        viewModelScope.launch {
            val repository = repository.select()
            myListUserModel.value = repository
        }
    }

    fun selectNoteByUsername(username: String) {
        viewModelScope.launch {
            val repository = repository.selectNoteByUN(username)
            myNoteFromUNList.value = repository
        }
    }

    fun insertTodo(
        title: String,
        description: String,
        date: String,
        priorityLevel: String,
        username: String,
        status: String
    ) {
        viewModelScope.launch {
            val repository = repository.insertTodo(
                title = title,
                description = description,
                date = date,
                priorityLevel = priorityLevel,
                username = username,
                status = "0"
            )
            myInsertTodoList.value = repository
        }
    }

    fun updateTodo(
        title: String,
        description: String,
        date: String,
        priorityLevel: String,
        username: String,
        status: String,
        id: String,
    ) {
        viewModelScope.launch {
            val repository = repository.updateTodo(
                title = title,
                description = description,
                date = date,
                priorityLevel = priorityLevel,
                username = username,
                status = status,
                id = id

            )
            myUpdateTodoList.value = repository
        }
    }

    fun deleteTodo(
        id: String
    ) {
        viewModelScope.launch {
            val repository = repository.deleteTodo(
                id = id
            )
            myDeleteTodoList.value = repository
        }
    }
}