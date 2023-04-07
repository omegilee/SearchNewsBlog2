package com.example.searchnewsblog.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * lsh 2023.04.03
 */
@Entity(
    tableName = "SearchKeywordEntity"
)
data class SearchKeywordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String = "",
    val createAt: String = "",
    val updateAt: String =""
)
