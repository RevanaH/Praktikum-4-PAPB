package com.example.praktikum4

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

private lateinit var auth: FirebaseAuth
private lateinit var database: FirebaseDatabase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoPage(navController: NavController, modifier: Modifier = Modifier){
    var activity by remember { mutableStateOf("") }
    val savedActivities = remember { mutableStateListOf<todoList>() }
    var userEmail: String = ""
    var userId: String = ""
    val context = LocalContext.current
    val googleSignIn = remember { GoogleSignIn(context, navController) }

    fun readFunc(){
        val currentUser = auth.currentUser
        if ( currentUser != null){
            val myRef = database.getReference("notes").child(currentUser.uid)
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    savedActivities.clear()
                    for (activitySnapshots in snapshot.children) {
                        val task = activitySnapshots.getValue(todoList::class.java)
                        if (task != null) {
                            task.id = activitySnapshots.key ?: ""
                            savedActivities.add(task)
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Failed to load tasks", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    fun saveFunc(){
        val newTask = todoList(text = activity, checkBox = false)
        val myRef = database.getReference("notes").child(auth.uid!!).push()
        myRef.setValue(newTask).addOnSuccessListener {
            activity = ""
            Toast
                .makeText(context, "Task successfully added to your list", Toast.LENGTH_LONG)
                .show()
        }
    }
    fun updateFunc(task: todoList) {
        if (auth.currentUser != null) {
            val myRef = database.getReference("notes").child(auth.uid!!).child(task.id)
            myRef.setValue(task)
                .addOnSuccessListener {
                    if (task.checkBox) {
                        Toast.makeText(context, "Task Completed", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Task Undone", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    fun deleteFunc(task: todoList) {
        val myRef = database.getReference("notes").child(auth.uid!!).child(task.id)
        myRef.removeValue().addOnSuccessListener {
            Toast.makeText(context, "Task has been removed", Toast.LENGTH_SHORT).show()
        }
    }
    if (!LocalInspectionMode.current){
        auth = Firebase.auth
        val currentUser = auth.currentUser
        if(currentUser != null){
            userEmail = currentUser.email.toString()
            userId = currentUser.uid
        }
        database = Firebase.database
    }
    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = Color(0xFF0F97FF),
                    titleContentColor = MaterialTheme.colorScheme.primary,

                    ),
                title = {
                    Text(text = "TODO LIST",
                        modifier = Modifier
                            .fillMaxWidth(),
                        fontSize = 20.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.ExtraBold)
                },
            )
        },
        content
        = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(paddingValues)
            ) {
                Text(
                    text = "Selamat Datang",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, top = 15.dp),
                    fontSize = 14.sp
                )
                Text(
                    text = userEmail,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    fontSize = 18.sp,
                    color = Color.Gray
                )
                Text(
                    text = userId,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    fontSize = 16.sp,
                    color = Color.LightGray
                )
                Button(
                    onClick = {
                        auth.signOut()
                        googleSignIn.signOutWithGoogle()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .absolutePadding(
                            20.dp,
                            10.dp,
                            20.dp,
                            0.dp
                        ),
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF65E5CA)
                    ),
                ){
                    Text(text = "Logout")
                }
                TextField(
                    value = activity,
                    onValueChange = { activity = it },
                    shape = RoundedCornerShape(7.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .absolutePadding(
                            20.dp,
                            15.dp,
                            20.dp,
                            0.dp
                        )
                )
                Spacer(modifier = Modifier.height(15.dp))
                Button(
                    onClick = {
                        if (activity.isNotEmpty()){
                           saveFunc()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .absolutePadding(
                            20.dp,
                            0.dp,
                            20.dp,
                            0.dp
                        ),
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0F97FF)
                    ),
                ){
                    Text(text = "Save")
                }
                readFunc()
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(savedActivities.size) { index ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .absolutePadding(
                                    10.dp,
                                    0.dp,
                                    10.dp,
                                    0.dp
                                ),
                        ) {

                            Checkbox(
                                checked = savedActivities[index].checkBox,
                                onCheckedChange = { checked ->
                                    savedActivities[index].checkBox = checked
                                    updateFunc(savedActivities[index])
                                },
                                colors = CheckboxDefaults.colors(
                                    checkmarkColor = Color.White,
                                    checkedColor = Color(0xFF0F97FF)
                                )
                            )

                            Text(
                                text = savedActivities[index].text,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            IconButton(
                                onClick = {
                                    deleteFunc(savedActivities[index])
                                    savedActivities.removeAt(index)
                                },
                                modifier = Modifier.align(Alignment.CenterVertically)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete")
                            }
                        }
                    }
                }
            }
        }
    )
}