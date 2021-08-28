package com.teamdagger.simpl.data.room.safe_progress

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wallet_safe_progress")
data class WalletSafeProgress(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    var id:Int,
    @ColumnInfo(name = "wallet_id")
    var walletId:Int,
    @ColumnInfo(name = "wallet_balance")
    var wallet_balance:Long,
    @ColumnInfo(name = "wantobuy_id")
    var wanToBuyId:Int,
)
