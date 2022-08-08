package com.quannm18.assignmentquannmph14304_networking.model

import com.google.gson.annotations.SerializedName

data class NoteModel(
    val date: String,
    val description: String,
    val id: String,
    val priority_level: String,
    val status: String,
    val title: String,
    val username: String
)