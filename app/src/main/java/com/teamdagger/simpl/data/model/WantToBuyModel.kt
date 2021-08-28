package com.teamdagger.simpl.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class WantToBuyModel(
    var id:Int,
    var name:String,
    var desc:String?,
    var progress:Long,
    var price:Long,
    var pict:String?,
    var timestamp:String,
    var deadline:String?,
    var userId:Int,
    var hasDone:Boolean
)
