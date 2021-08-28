package com.teamdagger.simpl.ui.edit_balance

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamdagger.simpl.data.model.DescModel
import com.teamdagger.simpl.databinding.LayoutDescBinding

class SuggestionAdapter(private val context: Context):RecyclerView.Adapter<SuggestionAdapter.CustomVH>() {

    private var listData = mutableListOf<DescModel>()
    private var listenerClick : ((String)->Unit)? = null

    class CustomVH (view:LayoutDescBinding):RecyclerView.ViewHolder(view.root){

        val tv = view.tvDesc

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomVH {
        return CustomVH(LayoutDescBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: CustomVH, position: Int) {
        holder.tv.text = listData[position].descName

        holder.tv.setOnClickListener {
            listenerClick?.invoke(listData[position].descName)
        }
    }

    fun onClick(listenerClick : (String)-> Unit){
        this.listenerClick = listenerClick
    }

    fun setData(newData:List<DescModel>){
        listData.clear()
        listData.addAll(newData)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = listData.size
}