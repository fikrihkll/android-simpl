package com.teamdagger.simpl.util

import java.text.SimpleDateFormat
import java.util.*

object UtilFun {

    var userId = -1

    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
    val sdfDate = SimpleDateFormat("yyyy-MM-dd")

    const val TYPE_IN = "IN"
    const val TYPE_OUT = "OUT"

    const val SOURCE_BANK = "BANK"
    const val SOURCE_WALLET = "WALLET"
    const val SOURCE_SAFE = "SAFE"

    fun parseBalance(balance:Long):String{
        var str = balance.toString()
        var newStr = ""

        var counter = 0
        for(i in str.length-1 downTo 0){
            counter++
            newStr+=str[i]
            if(counter==3 && i!=0){
                counter=0
                newStr+="."
            }
        }

        return newStr.reversed()
    }

    fun timestamp():String{
        var cal = Calendar.getInstance()
        var str = sdf.format(cal.time)
        return str
    }

}