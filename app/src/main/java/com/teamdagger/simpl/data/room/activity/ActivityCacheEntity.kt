package com.teamdagger.simpl.data.room.activity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activity")
data class ActivityCacheEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id:Int,
    @ColumnInfo(name = "type")
    var type:String,
    @ColumnInfo(name = "description")
    var desc:String?,
    @ColumnInfo(name="balance")
    var balance:Long,
    @ColumnInfo(name="source")
    var source:String,
    @ColumnInfo(name="source_id")
    var sourceId:Int,
    @ColumnInfo(name = "datetime")
    var dateTime:String,
    @ColumnInfo(name = "user_id")
    var userId:Int,
)
