package com.quannm18.assignmentquannmph14304_networking.view.repository

import com.quannm18.assignmentquannmph14304_networking.model.NoteModel
import com.quannm18.assignmentquannmph14304_networking.model.UserModel
import com.quannm18.assignmentquannmph14304_networking.network.RetrofitInstance
import retrofit2.Response
import retrofit2.http.Field

class TodoRepository {
    suspend fun getLogin(username: String, password: String): Response<String> {
        return RetrofitInstance.api.login(username, password)
    }

    suspend fun insertPost(
        fullname: String,
        username: String,
        password: String,
        email: String
    ): Response<String> {
        return RetrofitInstance.api.insertPost(fullname, username, password, email)
    }

    suspend fun deletePost(username: String): Response<String> {
        return RetrofitInstance.api.deletePost(username)
    }

    suspend fun updatePost(fullname: String, username: String): Response<String> {
        return RetrofitInstance.api.updatePost(fullname = fullname, username = username)
    }

    suspend fun select(): Response<MutableList<UserModel>> {
        return RetrofitInstance.api.select()
    }

    suspend fun selectNoteByUN(username: String): Response<MutableList<NoteModel>> {
        return RetrofitInstance.api.selectTodoByUN(username = username)
    }

    suspend fun insertTodo(
        title: String,
        description: String,
        date: String,
        priorityLevel: String,
        username: String,
        status: String
    ): Response<String> {
        return RetrofitInstance.api.insertTodo(title = title,
            description = description,
            date = date,
            priority_level = priorityLevel,
            username = username,
        status = "0")
    }
    suspend fun updateTodo(
        title: String,
        description: String,
        date: String,
        priorityLevel: String,
        username: String,
        status: String,
        id: String,
    ): Response<String> {
        return RetrofitInstance.api.updateTodo(title = title,
            description = description,
            date = date,
            priority_level = priorityLevel,
            username = username,
            id = id,
            status = status)
    }
    suspend fun deleteTodo(
        id: String
    ): Response<String> {
        return RetrofitInstance.api.deleteTodo(
            id = id)
    }
}