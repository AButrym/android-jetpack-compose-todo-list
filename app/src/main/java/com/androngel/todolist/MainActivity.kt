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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    val focusManager = LocalFocusManager.current

    val itemList = remember { mutableStateListOf<String>().apply { addAll(readData(myContext)) } }
    var todoName by remember { mutableStateOf("") }

    var clickedItemIndex by remember { mutableStateOf(0) }
    var clickedItem by remember { mutableStateOf("") }
    var updateDialogStatus by remember { mutableStateOf(false) }
    var deleteDialogStatus by remember { mutableStateOf(false) }
    var textDialogStatus by remember { mutableStateOf(false) }

    @Composable
    fun update(itemList: List<String>) {
        writeData(itemList, myContext)
    }
    // subscribe to autoupdate
    update(itemList)

    Column(modifier = Modifier.fillMaxSize()) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {

            TextField(
                value = todoName,
                onValueChange = { todoName = it },
                label = { Text("Enter TODO") },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedLabelColor = Color.Green,
                    unfocusedLabelColor = Color.White,
                    containerColor = MaterialTheme.colorScheme.primary,
                    textColor = Color.White,
                    cursorColor = Color.White
                ),
                shape = MaterialTheme.shapes.small,
                modifier = Modifier
                    .border(2.dp, Color.Black, MaterialTheme.shapes.small)
                    .weight(7F) //3F for the button weight
                    .height(60.dp),
                textStyle = TextStyle(textAlign = TextAlign.Center)
            )

            Button(
                onClick = {
                    if (todoName.isNotEmpty()) {
                        itemList.add(todoName)
                        todoName = ""
                        focusManager.clearFocus()
                    } else {
                        Toast.makeText(myContext, "Please enter a TODO", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .weight(3F)
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.green),
                    contentColor = Color.White
                ),
                shape = MaterialTheme.shapes.small,
                border = BorderStroke(2.dp, Color.Black)
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
                            .padding(bottom = 2.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.White
                        ),
                        shape = MaterialTheme.shapes.extraSmall
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
                                        clickedItem = item
                                        textDialogStatus = true
                                    }
                            )

                            Row {
                                IconButton(
                                    onClick = {
                                        updateDialogStatus = true
                                        clickedItemIndex = index
                                        clickedItem = item
                                    }
                                ) {
                                    Icon(
                                        Icons.Filled.Edit,
                                        contentDescription = "edit",
                                        tint = Color.White
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        deleteDialogStatus = true
                                        clickedItemIndex = index
                                    }
                                ) {
                                    Icon(
                                        Icons.Filled.Delete,
                                        contentDescription = "delete",
                                        tint = Color.White
                                    )
                                }
                            }

                        }
                    }

                }
            )
        }

        if (deleteDialogStatus) {

            AlertDialog(
                onDismissRequest = { deleteDialogStatus = false },
                title = {
                    Text(text = "Delete")
                },
                text = {
                    Text(text = "Do you want to delete this item from the list?")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            itemList.removeAt(clickedItemIndex)
                            deleteDialogStatus = false
                            Toast.makeText(
                                myContext,
                                "Item is removed from the list.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    ) {
                        Text(text = "YES")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { deleteDialogStatus = false }) {
                        Text(text = "NO")
                    }
                }
            )
        }

        if (updateDialogStatus) {

            AlertDialog(
                onDismissRequest = { updateDialogStatus = false },
                title = {
                    Text(text = "Update")
                },
                text = {
                    TextField(
                        value = clickedItem,
                        onValueChange = { clickedItem = it }
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            itemList[clickedItemIndex] = clickedItem
                            updateDialogStatus = false
                            Toast.makeText(myContext, "Item is updated.", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Text(text = "YES")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { updateDialogStatus = false }) {
                        Text(text = "NO")
                    }
                }
            )
        }

        if (textDialogStatus) {

            AlertDialog(
                onDismissRequest = { textDialogStatus = false },
                title = {
                    Text(text = "TODO Item")
                },
                text = {
                    Text(text = clickedItem)
                },
                confirmButton = {
                    TextButton(
                        onClick = {

                            textDialogStatus = false

                        }
                    ) {
                        Text(text = "OK")
                    }
                }
            )
        }
    }
}