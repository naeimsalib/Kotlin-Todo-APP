package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener{
            override fun onItemLongClicked(position: Int) {
                // 1. Remove Item From the List
                listOfTasks.removeAt(position)
                //2.Notify The Adapter that out data set has changed
                adapter.notifyDataSetChanged()

                saveItems()
            }
        }
        //1. Let's detect when eh users clicks on the add button.
//        findViewById<Button>(R.id.button).setOnClickListener{
//            // Code in here is going to be executed whent eh user clicks on a button
//            Log.i("Caren", "User clicked on button")
//        }


        loadItems()

        // look up the recyclerView in the layout
       val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        //Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        // set up the button and input field, so that the user can enter a task and add it to the list
        val inputTextField = findViewById<EditText>(R.id.addTaskField)
        //Get a reference to the button
        // and then set an onClick listsener
        findViewById<Button>(R.id.button).setOnClickListener{
            // 1. Grab the text that the user has inputted in @id/addTaskField
            val userInputtedTask = inputTextField.text.toString()

            // 2. Add the string to our list of tasks
            listOfTasks.add(userInputtedTask)

            //Notify the adapter that our data has been updated
            adapter.notifyItemInserted(listOfTasks.size - 1)
            // 3. Reset text field
            inputTextField.setText("")

            saveItems()
        }
    }
    
    //Save the Data that the user has input
    //by writing and reading from a file

    //Get the file we need
    fun getDataFile() : File{
        return File(filesDir, "data.txt")
    }
    //Load the item by reading every line in the data file
    fun loadItems(){
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException){
            ioException.printStackTrace()
        }
    }
    //save items by writing them into our data file
    fun saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException){
            ioException.printStackTrace()
        }
    }
}