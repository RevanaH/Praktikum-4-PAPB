package com.example.praktikum4

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class todoList(
    var id: String = "",
    val text: String,
    var checkBox: Boolean = false
){
    constructor() : this("", "", false)
}