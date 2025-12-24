package com.example.projectmanagement

import android.app.Application
import androidx.room.Room
import com.example.projectmanagement.data.database.ProjectDatabase

class ProjectApplication : Application() {
    val database: ProjectDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            ProjectDatabase::class.java,
            ProjectDatabase.DATABASE_NAME
        ).build()
    }
}

