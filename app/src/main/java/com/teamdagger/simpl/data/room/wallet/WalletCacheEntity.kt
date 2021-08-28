package com.teamdagger.simpl.data.room.wallet

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wallet")
data class WalletCacheEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id:Int,
    @ColumnInfo(name = "name")
    var name:String,
    @ColumnInfo(name="user_id")
    var userId:Int,
    @ColumnInfo(name="balance")
    var balance:Long
)
