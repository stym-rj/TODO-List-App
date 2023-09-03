package com.example.project2_todolistapp

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.project2_todolistapp.databinding.ListItemBinding
import com.example.project2_todolistapp.db.Todo
import com.example.project2_todolistapp.db.TodoListDatabase
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import kotlin.concurrent.thread

// (Completed here!) TODO 4: Create a ViewHolder for the Recycler View
@Suppress("DEPRECATION")
class TodoListViewHolder(
    private val itemBinding: ListItemBinding
): RecyclerView.ViewHolder(itemBinding.root) {

    @RequiresApi(Build.VERSION_CODES.O)
    fun bindData(listOfTodos: MutableList<Todo>, position: Int, listener: TodoStateChangedListener) {
        itemBinding.cbItemTodo.isChecked = listOfTodos[position].isDone
        itemBinding.itemTodoTitle.text = listOfTodos[position].title
        itemBinding.itemTodoDesc.text = listOfTodos[position].desc

        val pattern = "yyyy-MM-dd HH:mm"
        val simpleDateFormat = SimpleDateFormat(pattern)
        val date = simpleDateFormat.format(listOfTodos[position].date)
        itemBinding.itemTodoDate.text = date

        itemView.setOnClickListener {
            listener.onCheckStateChange(position, listOfTodos)
        }

    }

}

// (Completed Here!) TODO 5: Create an Adapter for the Recycler View
class TodoListAdapter(
    private val recyclerView: RecyclerView,
    private var listOfTodos: MutableList<Todo>,
    private val listener: TodoStateChangedListener
): RecyclerView.Adapter<TodoListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListViewHolder {
        return TodoListViewHolder(
            ListItemBinding.inflate(
                LayoutInflater
                    .from(parent.context),
                parent,
                false
            )
        )
}
    override fun getItemCount() = listOfTodos.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: TodoListViewHolder, position: Int) {
        holder.bindData(listOfTodos, position, listener)
    }

    fun updateData(newList: MutableList<Todo>) {
        listOfTodos = newList
        notifyDataSetChanged()
    }

    fun addData(todo: Todo) {
        listOfTodos.add(0, todo)
        notifyItemInserted(0)
        recyclerView.scrollToPosition(0)
    }
}


// (Completed here!) TODO 6: Handle Click events on the ToDos
// adding functionality for checkbox
interface TodoStateChangedListener {
    fun onCheckStateChange(position: Int, listOfTodos: MutableList<Todo>)
}