package com.quannm18.assignmentquannmph14304_networking.view.activity

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.orhanobut.hawk.Hawk
import com.quannm18.assignmentquannmph14304_networking.R
import com.quannm18.assignmentquannmph14304_networking.view.adapter.MyViewPagerAdapter
import com.quannm18.assignmentquannmph14304_networking.view.repository.TodoRepository
import com.quannm18.assignmentquannmph14304_networking.viewmodel.MainViewModel
import com.quannm18.assignmentquannmph14304_networking.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var myViewPagerAdapter: MyViewPagerAdapter

    private lateinit var mRepository: TodoRepository
    private lateinit var mainViewModelFactory: MainViewModelFactory
    private lateinit var mainViewModel: MainViewModel


    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Hawk.init(this).build()
        mRepository = TodoRepository()
        mainViewModelFactory = MainViewModelFactory(mRepository)
        mainViewModel = ViewModelProvider(this, mainViewModelFactory)[MainViewModel::class.java]
        myViewPagerAdapter = MyViewPagerAdapter(this)
        vpMain.adapter = myViewPagerAdapter
        window.statusBarColor = R.color.color_green
        window.navigationBarColor = R.color.color_green
        bottomNavigationView.selectedItemId = R.id.home
        vpMain.currentItem = 1
        bottomNavigationView.background = null
        bottomNavigationView.backgroundTintList = null

        setBottomNavigation()

    }

    fun setBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.finished -> {
                    updateBottomNavBarColor(R.color.color_main)
                    vpMain.currentItem = 0
                    true
                }
                R.id.user -> {
                    updateBottomNavBarColor(R.color.color_main)
                    vpMain.currentItem = 2
                    true
                }
                R.id.home -> {
                    updateBottomNavBarColor(R.color.color_main)
                    vpMain.currentItem = 1
                    true
                }
                else -> {
                    true
                }
            }
        }
        vpMain.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        bottomNavigationView.selectedItemId = R.id.finished
                    }
                    1 -> {
                        bottomNavigationView.selectedItemId = R.id.home
                    }
                    2 -> {
                        bottomNavigationView.selectedItemId = R.id.user
                    }
                }
                super.onPageSelected(position)
            }
        })
        fab.setOnClickListener {
            vpMain.currentItem = 1
        }
    }

    private val bottomNavBarStateList = arrayOf(
        intArrayOf(android.R.attr.state_checked),
        intArrayOf(-android.R.attr.state_checked)
    )

    private fun updateBottomNavBarColor(currentSelectedColor: Int) {
        val colorList = intArrayOf(
            ContextCompat.getColor(this, currentSelectedColor),
            ContextCompat.getColor(this, R.color.color_green)
        )
        val colorStateList = ColorStateList(bottomNavBarStateList, colorList)
        bottomNavigationView.itemIconTintList = colorStateList
        bottomNavigationView.itemTextColor = colorStateList
        bottomNavigationView.itemBackground
    }
}