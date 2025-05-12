package com.sanket.mvvmstructure.ui.Adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sanket.mvvmstructure.ui.fragment.tabFragment.Tab1Fragment
import com.sanket.mvvmstructure.ui.fragment.tabFragment.Tab2Fragment
import com.sanket.mvvmstructure.ui.fragment.tabFragment.Tab3Fragment

//class MyViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
//
//    override fun getItemCount(): Int = 3 // Number of tabs
//
//    override fun createFragment(position: Int): Fragment {
//        return when (position) {
//            0 -> Tab1Fragment()
//            1 -> Tab2Fragment()
//            2 -> Tab3Fragment()
//            else -> throw IllegalStateException("Unexpected position $position")
//        }
//    }
//}
class MyViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    val tab1Fragment = Tab1Fragment()
    val tab2Fragment = Tab2Fragment()
    val tab3Fragment = Tab3Fragment()

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> tab1Fragment
            1 -> tab2Fragment
            2 -> tab3Fragment
            else -> throw IllegalArgumentException("Invalid tab index")
        }
    }
}