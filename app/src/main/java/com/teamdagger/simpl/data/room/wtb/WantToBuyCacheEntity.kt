package com.teamdagger.simpl.data.room.wtb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wantobuy")
data class WantToBuyCacheEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id:Int,
    @ColumnInfo(name = "name")
    var name:String,
    @ColumnInfo(name = "description")
    var desc:String?,
    @ColumnInfo(name="progress")
    var progress:Long,
    @ColumnInfo(name="price")
    var price:Long,
    @ColumnInfo(name = "pict")
    var pict:String?,
    @ColumnInfo(name="timestamp")
    var timestamp:String,
    @ColumnInfo(name="deadline")
    var deadline:String?,
    @ColumnInfo(name="user_id")
    var userId:Int,
    @ColumnInfo(name="has_done")
    var hasDone:Boolean,
)
