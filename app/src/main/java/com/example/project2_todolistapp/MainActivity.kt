package com.example.project2_todolistapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.GONE
import androidx.recyclerview.widget.RecyclerView.VISIBLE
import androidx.room.Room
import com.example.project2_todolistapp.databinding.ActivityMainBinding
import com.example.project2_todolistapp.databinding.BottomSheetBinding
import com.example.project2_todolistapp.db.Todo
import com.example.project2_todolistapp.db.TodoListDatabase
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.Date
import kotlin.concurrent.thread


// [X] TODO 4: Create a ViewHolder for the Recycler View
// [x] TODO 5: Create an Adapter for the Recycler View
// [x] TODO 6: Handle Click events on the ToDos
// [x] TODO 7: Add a Floating Action Button
// TODO 8: Create a Dialog Box to create a ToDo (Bottom Sheet Optional)
// TODO 9: Build a DBHelper class with (Entities, DAOs, Database and TypeConverters)
// TODO 10: Push new ToDos in the DB
// TODO 11: Whenever the App is launched sync your data with DB

// Optional TODOs
// 1. Create a user login/signup flow
// 2. Add a side navigation bar
// 3. Add a profile section where users can set the profile (Profile Pic, Name, DOB, Bio, etc.)
// 4. Push all todos data in Firebase (if user logs in from another device)
// 5. Add search feature
// 6. Add filter by date feature
// 7. Add section in Recycler View, on the basis of Date
// 8. Add reminders on Todos that have a deadline
// 9. Add new screen to display the tasks that are done

class MainActivity : AppCompatActivity(), TodoStateChangedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: TodoListDatabase
    private lateinit var adapter: TodoListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)      // for disabling night  mode


        adapter = TodoListAdapter(binding.rvTodoList, mutableListOf(), this)
        binding.rvTodoList.layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
        binding.rvTodoList.adapter = adapter


        binding.fabAddTodoList.setOnClickListener {
            showBottomSheet()
        }

        thread {
            /* we should never load the database on main thread because it locks the UI for a longer period of time.
             So, therefore we crated a separate thread so, it would run in background. */
            database = Room.databaseBuilder(
                this@MainActivity,
                TodoListDatabase::class.java,
                "todoListDB"
            ).build()
            /* here, we need to build our database. we are passing the main activity,
        *  and then we need to pass our abstract class which is defining how everything is clubbed together,
        * and then we are giving our database a name "todoListDB".
        */

//            val todo = Todo(       // creating sample data for a to-do
//                title = "Second title",
//                desc = "Second todo description",
//                date = Date(System.currentTimeMillis())
//            )
//            database.todoDao().insertTodo(todo)         // passing the data of to-do


            val listOfAllTodos = database.todoDao().fetchAllTodos()
            adapter.updateData(listOfAllTodos)


            // setting delete to-do button
//            binding.btnDelete.setOnClickListener {
//                val listOfAllTodos2 = database.todoDao().fetchAllTodos()
//                adapter.updateData(listOfAllTodos2)
//            }
        }
    }


    private fun showBottomSheet() {
        val bottomSheetBinding = BottomSheetBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(bottomSheetBinding.root)


        bottomSheetBinding.btnDone.setOnClickListener {
            // add to-do in the db
            if (bottomSheetBinding.tietTitle.text.isNullOrBlank()) {
                bottomSheetBinding.tilTitle.error = "Please give a Title to your TODO"
                return@setOnClickListener
            }
            else {
                bottomSheetBinding.tilTitle.isErrorEnabled = false
            }

            if (bottomSheetBinding.tietDesc.text.isNullOrBlank()) {
                bottomSheetBinding.tilDesc.error = "Description can't be empty!"
                return@setOnClickListener
            }
            else {
                bottomSheetBinding.tilDesc.isErrorEnabled = false
            }


            val todo = Todo(
                title = bottomSheetBinding.tietTitle.text.toString(),
                desc = bottomSheetBinding.tietDesc.text.toString(),
                date = Date(System.currentTimeMillis())
            )
            adapter.addData(todo)


            thread {
                database.todoDao().insertTodo(todo)
            }

            dialog.dismiss()
        }
        dialog.show()
    }


    private fun cbClicked() {
        binding.btnDelete.visibility = VISIBLE
    }

    private fun cbNotClicked() {
        binding.btnDelete.visibility = GONE
    }

    private fun dataRemoved(index: Int) {

    }

    override fun onCheckStateChange(position: Int, listOfTodos: MutableList<Todo>) {
        listOfTodos[position].isDone = !listOfTodos[position].isDone
        adapter.updateData(listOfTodos)

        if(listOfTodos[position].isDone) {
            cbClicked()
        }
        else if (!listOfTodos[position].isDone) {
            cbNotClicked()
        }


        binding.btnDelete.setOnClickListener {
            thread {
                database.todoDao().deleteTodo(listOfTodos[position])
                listOfTodos.removeAt(position)
                adapter.notifyItemRemoved(position)
            }
            adapter.updateData(listOfTodos)
            binding.btnDelete.visibility = GONE
        }
    }
}
