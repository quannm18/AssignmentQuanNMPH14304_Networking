package com.quannm18.assignmentquannmph14304_networking.network

import com.quannm18.assignmentquannmph14304_networking.model.NoteModel
import com.quannm18.assignmentquannmph14304_networking.model.UserModel
import retrofit2.Response
import retrofit2.http.*

interface TodoInterface {
    // user
    @FormUrlEncoded
    @POST("/lab/login.php/")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String,
    ): Response<String>


    @FormUrlEncoded
    @POST("/lab/insert_post.php/")
    suspend fun insertPost(
        @Field("fullname") fullname: String,
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("email") email: String,
    ): Response<String>

    @GET("/lab/select.php")
    suspend fun select(): Response<MutableList<UserModel>>


    @FormUrlEncoded
    @POST("/lab/delete_post_p.php/")
    suspend fun deletePost(
        @Field("username") username: String,
    ): Response<String>

    @FormUrlEncoded
    @POST("/lab/update_post_p.php/")
    suspend fun updatePost(
        @Field("username") username: String,
        @Field("fullname") fullname: String
    ): Response<String>

    //todolist
    @GET("/assignment/select_note_p.php")
    suspend fun selectTodoByUN(
        @Path(value = "username") username: String
    ): Response<MutableList<NoteModel>>


    @FormUrlEncoded
    @POST("/assignment/insert_note_p.php")
    suspend fun insertTodo(
        @Field("title") title: String,
        @Field("description") description: String,
        @Field("date") date: String,
        @Field("priority_level") priority_level: String,
        @Field("username") username: String,
        @Field("status") status: String
    ): Response<String>

    @FormUrlEncoded
    @POST("/assignment/update_note_p.php")
    suspend fun updateTodo(
        @Field("title") title: String,
        @Field("description") description: String,
        @Field("date") date: String,
        @Field("priority_level") priority_level: String,
        @Field("username") username: String,
        @Field("status") status: String,
        @Field("id") id: String
    ): Response<String>
    @FormUrlEncoded
    @POST("/assignment/delete_note_p.php")
    suspend fun deleteTodo(
        @Field("id") id: String
    ): Response<String>
}