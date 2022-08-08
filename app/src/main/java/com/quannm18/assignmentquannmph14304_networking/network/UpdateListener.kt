package com.quannm18.assignmentquannmph14304_networking.network

import com.quannm18.assignmentquannmph14304_networking.model.NoteModel

interface UpdateListener {
    fun update(noteModel: NoteModel)
}