package com.teamdagger.simpl.data.room.bank

import androidx.room.ColumnInfo

data class SumCacheEntity(
    @ColumnInfo(name = "balance")
    var balance : Long
)