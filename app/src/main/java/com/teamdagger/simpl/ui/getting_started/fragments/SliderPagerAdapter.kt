package com.teamdagger.simpl.ui.getting_started.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class SliderPagerAdapter(fa: FragmentActivity,private val listFa:List<Fragment>) : FragmentStateAdapter(fa) {

    var listener : ((Int)->Unit)? = null

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment = listFa[position]


}