package com.teamdagger.simpl.ui.edit_balance

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamdagger.simpl.data.model.BankModel
import com.teamdagger.simpl.data.model.WalletModel
import com.teamdagger.simpl.databinding.LayoutSliderAccountBinding
import com.teamdagger.simpl.util.UtilFun

class SliderCardAdapter(private val context: Context) : RecyclerView.Adapter<SliderCardAdapter.CustomVH>() {

    private var listBank = mutableListOf<BankModel>()
    private var listWallet = mutableListOf<WalletModel>()

    private var isBankSelected = true

    class CustomVH(view : LayoutSliderAccountBinding):RecyclerView.ViewHolder(view.root) {
        val tvName = view.tvNameAccount
        val tvBalance = view.tvBalanceAccount
        val tvRek = view.tvRekAccount
        val tvValidate = view.tvValidateAccount
        val ivBankLogo = view.ivBankLogo
        val ivBackground = view.ivBackgroundCard

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderCardAdapter.CustomVH {
        return CustomVH(LayoutSliderAccountBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: CustomVH, position: Int) {
        if(isBankSelected){
            holder.tvName.text = listBank[position].name
            holder.tvRek.text = listBank[position].rek
            holder.tvBalance.text = "Rp${UtilFun.parseBalance(listBank[position].balance)}"
        }else{
            holder.tvName.text = listWallet[position].name
            holder.tvRek.visibility = View.GONE
            holder.tvBalance.text = "Rp${UtilFun.parseBalance(listWallet[position].balance)}"
        }



    }

    fun getBankSize() : Int = listBank.size

    fun getWalletSize() : Int = listWallet.size

    fun getBank(position: Int):BankModel{
        return listBank[position]
    }

    fun getWallet(position: Int):WalletModel{
        return listWallet[position]
    }

    fun changeType(isBank:Boolean){
        isBankSelected = isBank
        notifyDataSetChanged()
    }

    fun setNewDataBank(newBank:MutableList<BankModel>){
        listBank.clear()
        listBank.addAll(newBank)
        notifyDataSetChanged()
    }

    fun setNewDataWallet(newWallet:MutableList<WalletModel>){
        listWallet.clear()
        listWallet.addAll(newWallet)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = if(isBankSelected) listBank.size else listWallet.size
}