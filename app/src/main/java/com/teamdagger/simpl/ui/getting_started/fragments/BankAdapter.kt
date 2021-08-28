package com.teamdagger.simpl.ui.getting_started.fragments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamdagger.simpl.data.model.BankModel
import com.teamdagger.simpl.data.model.WalletModel
import com.teamdagger.simpl.databinding.LayoutBalanceBinding

class BankAdapter(private val context: Context, private val isForBank:Boolean):RecyclerView.Adapter<BankAdapter.CustomVH>() {

    private var listDataBank = mutableListOf<BankModel>()
    private var listDataWallet = mutableListOf<WalletModel>()

    class CustomVH(binding : LayoutBalanceBinding):RecyclerView.ViewHolder(binding.root) {
        val tvName = binding.tvBankNameLtbalance
        val tvRek = binding.tvRekLtbalance
        val tvBalance = binding.tvBalanceLtbalance
        val btnRemove = binding.btnRemoveLtbalance
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankAdapter.CustomVH {
        return CustomVH(LayoutBalanceBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: BankAdapter.CustomVH, position: Int) {
        if(isForBank){
            holder.tvName.text = listDataBank[position].name
            holder.tvRek.text = listDataBank[position].rek
            holder.tvBalance.text = "Rp."+listDataBank[position].balance.toString()
            holder.btnRemove.setOnClickListener {
                notifyItemRemoved(position)
                listDataBank.removeAt(position)
            }
        }else{
            holder.tvRek.visibility = View.GONE
            holder.tvName.text = listDataWallet[position].name
            holder.tvBalance.text = "Rp."+listDataWallet[position].balance.toString()
            holder.btnRemove.setOnClickListener {
                notifyItemRemoved(position)
                listDataWallet.removeAt(position)
            }
        }
    }

    fun addDataBank(model : BankModel){
        listDataBank.add(model)
        notifyDataSetChanged()
    }

    fun addDataWallet(model : WalletModel){
        listDataWallet.add(model)
        notifyDataSetChanged()
    }

    fun getListBank():List<BankModel>{
        return listDataBank
    }

    fun getListWallet():List<WalletModel>{
        return listDataWallet
    }

    override fun getItemCount(): Int {
        return if(isForBank)
            listDataBank.size
        else
            listDataWallet.size
    }
}