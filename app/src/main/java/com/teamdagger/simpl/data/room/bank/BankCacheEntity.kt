package com.teamdagger.simpl.data.room.bank

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bank")
data class BankCacheEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id:Int,
    @ColumnInfo(name = "name")
    var name:String,
    @ColumnInfo(name = "user_id")
    var userId:Int,
    @ColumnInfo(name="balance")
    var balance:Long,
    @ColumnInfo(name="rek")
    var rek:String
)
