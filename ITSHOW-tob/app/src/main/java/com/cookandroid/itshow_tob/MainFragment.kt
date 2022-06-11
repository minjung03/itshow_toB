package com.cookandroid.itshow_tob

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.main.*

class MainFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.main, container, false)

        val main_serch = view.findViewById<ImageView>(R.id.main_serch)

        main_serch.setOnClickListener{
            val intent = Intent(activity, Search::class.java)
            activity?.startActivity(intent)
        }

        val vp = view.findViewById<ViewPager>(R.id.main_viewPager)
        val adapter = PagerAdapter(parentFragmentManager)
        adapter.addFragment(Fragment1(), "생활")
        adapter.addFragment(Fragment1(), "쇼핑몰")
        adapter.addFragment(Fragment1(), "음식")
        adapter.addFragment(Fragment1(), "잡화")
        adapter.addFragment(Fragment1(), "의류")
        adapter.addFragment(Fragment1(), "화장품")
        vp.setAdapter(adapter)

        val tab = view.findViewById<TabLayout>(R.id.tabs)
        tab.setupWithViewPager(vp)

        val images = ArrayList<Int>()
        images.add(R.drawable.main_daily_supplies)
        images.add(R.drawable.main_shopping)
        images.add(R.drawable.main_delivery)
        images.add(R.drawable.main_sundries)
        images.add(R.drawable.main_clothing)
        images.add(R.drawable.main_cosmetics)

        for (i in 0..5) tab.getTabAt(i)!!.setIcon(images[i])

        return view
    }
}
