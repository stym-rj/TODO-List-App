package com.example.project2_todolistapp.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


/* 2. DAO (Data Access Object)
*       -> helps accessing DB without writing a lot of code. (we have to do this in new kotlin class file.)
 */

// to create a DAO, we need to create an interface.
@Dao        // we need to add this annotation to declare it as a Dao interface
interface TodoDao {
    // in this interface, we can do CRUD operations (create, read, update, delete)

    @Insert(onConflict = OnConflictStrategy.REPLACE)        // this "@Insert" is to tell the room DB that this is an insert function & "onConflict" means when there will be any type of conflict in the database, then it will replace that data
    fun insertTodo(todo: Todo)

    @Query("select * from notes_table where isDone=0 ORDER BY id DESC;")     // we already defined "notes_table" in our 'TodoDao' file.
    fun fetchAllTodos(): MutableList<Todo>

    @Query("DELETE from notes_table where isDone=1")
    fun deleteDoneTodos()

    @Delete
    fun deleteTodo(todo: Todo)

    @Update(entity = Todo::class)
    fun updateTodos(todo: Todo)

    // there can be several other annotations also, such as "@Delete, @Update, etc"
}