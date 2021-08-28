package com.teamdagger.simpl.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamdagger.simpl.data.model.WantToBuyModel
import com.teamdagger.simpl.databinding.LayoutWantToBuyBinding
import com.teamdagger.simpl.ui.update_safe.safe_detail.SafeDetailFragment
import com.teamdagger.simpl.util.UtilFun

class SafeHomeAdapter (private val context: Context):RecyclerView.Adapter<SafeHomeAdapter.CustomVH>(){

    private var listData = mutableListOf<WantToBuyModel>()
    private var listener : ((WantToBuyModel) -> Unit)? = null
    private var listenerEdit : ((WantToBuyModel) -> Unit)? = null

    class CustomVH(view : LayoutWantToBuyBinding):RecyclerView.ViewHolder(view.root) {
        val tvTitle = view.tvTitleSafe
        val tvDesc = view.tvDescSafe
        val tvProgress = view.tvProgressSafe
        val pgb = view.pgbSafe
        val btnAdd = view.btnEditSafe
        val cb = view.checkBox
        val root = view.safeRoot
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomVH {
        return CustomVH(LayoutWantToBuyBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: CustomVH, position: Int) {
        var data = listData[position]
        holder.tvTitle.text = data.name

        if(data.deadline!=null)
            holder.tvDesc.text = "Finish it before "+data.deadline

        holder.tvProgress.text = "Rp.${UtilFun.parseBalance(data.progress)} / Rp.${UtilFun.parseBalance(data.price)}"

        try{
            var progress :Int = (data.progress.toInt())/((data.price/100).toInt())
            holder.pgb.max = 100
            holder.pgb.setProgress(progress)
        }catch (e :Exception){

        }

        holder.btnAdd.setOnClickListener {
            listener?.invoke(listData[position])
        }

        holder.root.setOnClickListener {
            listenerEdit?.invoke(listData[position])
        }

    }

    fun onAddSaveClicked(listener : ((WantToBuyModel) -> Unit)){
        this.listener = listener
    }

    fun onRootClicked(listenerEdit : ((WantToBuyModel) -> Unit)){
        this.listenerEdit = listenerEdit
    }

    fun setNewData(newData : MutableList<WantToBuyModel>){
        listData.clear()
        listData.addAll(newData)
    }

    override fun getItemCount(): Int = listData.size
}