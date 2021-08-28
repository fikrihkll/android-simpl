package com.teamdagger.simpl.data.room.safe_progress

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bank_safe_progress")
data class BankSafeProgress(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    var id:Int,
    @ColumnInfo(name = "bank_id")
    var bankId:Int,
    @ColumnInfo(name = "bank_balance")
    var bank_balance:Long,
    @ColumnInfo(name = "wantobuy_id")
    var wanToBuyId:Int,
)
