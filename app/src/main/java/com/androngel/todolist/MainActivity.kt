package com.androngel.todolist

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.androngel.todolist.ui.theme.TODOListTheme
import com.androngel.todolist.R

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TODOListTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainPage()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage() {

    val myContext = LocalContext.current

    val todoName = remember {
        mutableStateOf("")
    }
    val itemList = readData(myContext)
    val focusManager = LocalFocusManager.current
    val deleteDialogStatus = remember {
        mutableStateOf(false)
    }
    val clickedItemIndex = remember {
        mutableStateOf(0)
    }
    val updateDialogStatus = remember {
        mutableStateOf(false)
    }
    val clickedItem = remember {
        mutableStateOf("")
    }
    val textDialogStatus = remember {
        mutableStateOf(false)
    }

    Column(modifier = Modifier.fillMaxSize()) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            TextField(
                value = todoName.value,
                onValueChange = {
                    todoName.value = it
                },
                label = { Text(text = "Enter TODO") },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedLabelColor = Color.Green,
                    unfocusedLabelColor = Color.White,
                    containerColor = MaterialTheme.colorScheme.primary,
                    textColor = Color.White,
                    cursorColor = Color.White
                ),
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier
                    .border(1.dp, Color.Black, RoundedCornerShape(5.dp))
                    .weight(7F) //3F for the button weight
                    .height(60.dp),
                textStyle = TextStyle(textAlign = TextAlign.Center)

            )

            Spacer(modifier = Modifier.width(5.dp))

            Button(
                onClick = {
                          if (todoName.value.isNotEmpty()){
                              itemList.add(todoName.value)
                              writeData(itemList,myContext)
                              todoName.value = ""
                              focusManager.clearFocus()
                          }else{
                              Toast.makeText(myContext,"Please enter a TODO",Toast.LENGTH_SHORT).show()
                          }
                },
                modifier = Modifier
                    .weight(3F)
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.green),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(5.dp),
                border = BorderStroke(1.dp, Color.Black)
            ) {
                Text(text = "Add", fontSize = 20.sp)
            }

        }

        LazyColumn {

            items(
                count = itemList.size,
                itemContent = { index ->

                    val item = itemList[index]

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 1.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(0.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Text(
                                text = item,
                                color = Color.White,
                                fontSize = 18.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .width(300.dp)
                                    .clickable {
                                        clickedItem.value = item
                                        textDialogStatus.value = true
                                    }
                            )

                            Row() {
                                IconButton(
                                    onClick = {
                                        updateDialogStatus.value = true
                                        clickedItemIndex.value = index
                                        clickedItem.value = item
                                    }
                                ) {
                                    Icon(
                                        Icons.Filled.Edit,
                                        contentDescription = "edit",
                                        tint = Color.White)
                                }

                                IconButton(
                                    onClick = {
                                        deleteDialogStatus.value = true
                                        clickedItemIndex.value = index
                                    }
                                ) {
                                    Icon(
                                        Icons.Filled.Delete,
                                        contentDescription = "delete",
                                        tint = Color.White)
                                }
                            }

                        }
                    }

                }
            )

        }

        if (deleteDialogStatus.value){

            AlertDialog(
                onDismissRequest = { deleteDialogStatus.value = false },
                title = {
                    Text(text = "Delete")
                },
                text = {
                    Text(text = "Do you want to delete this item from the list?")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            itemList.removeAt(clickedItemIndex.value)
                            writeData(itemList,myContext)
                            deleteDialogStatus.value = false
                            Toast.makeText(myContext, "Item is removed from the list.",Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Text(text = "YES")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { deleteDialogStatus.value = false }) {
                        Text(text = "NO")
                    }
                }
            )

        }

        if (updateDialogStatus.value){

            AlertDialog(
                onDismissRequest = { updateDialogStatus.value = false },
                title = {
                    Text(text = "Update")
                },
                text = {
                    TextField(
                        value = clickedItem.value,
                        onValueChange = {clickedItem.value = it}
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            itemList[clickedItemIndex.value] = clickedItem.value
                            writeData(itemList,myContext)
                            updateDialogStatus.value = false
                            Toast.makeText(myContext, "Item is updated.",Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Text(text = "YES")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { updateDialogStatus.value = false }) {
                        Text(text = "NO")
                    }
                }
            )

        }

        if (textDialogStatus.value){

            AlertDialog(
                onDismissRequest = { textDialogStatus.value = false },
                title = {
                    Text(text = "TODO Item")
                },
                text = {
                    Text(text = clickedItem.value)
                },
                confirmButton = {
                    TextButton(
                        onClick = {

                            textDialogStatus.value = false

                        }
                    ) {
                        Text(text = "OK")
                    }
                }
            )

        }

    }

}