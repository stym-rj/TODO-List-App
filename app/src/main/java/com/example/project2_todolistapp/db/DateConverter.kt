package com.example.project2_todolistapp.db

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.Date


/* 3. TypeConverter
*       -> this is used when we want to add some custom data types in our db.
*               -> we have used this here because databases don't support any datatype for dates
*                       -> some of the data types which db has : int, character, decimal, VARCHAR(for strings), etc
 */
class DateConverter {
    // to convert date to long -> when pushing to db
    @TypeConverter
    fun fromDateToLong(date: Date): Long {
        return date.time
    }

    // to convert long to date -> when fetching from db
    @TypeConverter
    fun fromLongToDate(timeStamp: Long): Date {
        return Date(timeStamp)
    }
}