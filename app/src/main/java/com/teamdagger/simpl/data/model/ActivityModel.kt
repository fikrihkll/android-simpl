package com.teamdagger.simpl.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


data class ActivityModel(
    var id:Int,
    var type:String,
    var desc:String?,
    var balance:Long,
    var source:String,
    var sourceId:Int,
    var dateTime:String,
    var userId:Int,
)
