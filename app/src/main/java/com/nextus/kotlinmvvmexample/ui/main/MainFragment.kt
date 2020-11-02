package com.nextus.kotlinmvvmexample.ui.main

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.nextus.kotlinmvvmexample.R
import com.nextus.kotlinmvvmexample.databinding.FragmentMainBinding
import com.nextus.kotlinmvvmexample.ui.base.BaseFragment
import com.nextus.kotlinmvvmexample.ui.home.HomeFragment
import com.nextus.kotlinmvvmexample.ui.mypage.MyPageFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_main.*

@AndroidEntryPoint
class MainFragment: BaseFragment<FragmentMainBinding>(R.layout.fragment_main), BottomNavigationView.OnNavigationItemSelectedListener {

    private val homeFragment = HomeFragment()
    private val myPageFragment = MyPageFragment()
    private var activeFragment: Fragment = homeFragment

    private val mainViewModel: MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding(mainViewModel)
        initViewPager()
        initBottomNavigationBar()

        subscribeBackPressEvent()
    }

    private fun initViewPager() {
        if(mainViewModel.firstInit) {
            childFragmentManager.beginTransaction().apply {
                add(R.id.container, homeFragment, "HomeFragment")
                add(R.id.container, myPageFragment, "MyPageFragment").hide(myPageFragment)
            }.commit()

            mainViewModel.firstInit = false
        }
    }

    private fun initBottomNavigationBar() {
        bottom_navigation.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
        bottom_navigation.setOnNavigationItemSelectedListener(this)
    }

    private fun subscribeBackPressEvent() {
        // This callback will only be called when MyFragment is at least Started.
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            // Handle the back button event
            if(activeFragment != homeFragment)
                bottom_navigation.selectedItemId = R.id.navigation_home
            else
                doubleBackExitToast()
        }
    }

    private var backPressedTime: Long = 0
    private fun doubleBackExitToast() {
        val backToast = Toast.makeText(requireContext(), R.string.close_app, Toast.LENGTH_LONG)
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel()
            requireActivity().finish()
        } else {
            backToast.show()
        }

        backPressedTime = System.currentTimeMillis()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.navigation_home -> {
                childFragmentManager.beginTransaction().hide(activeFragment).show(homeFragment).commit()
                activeFragment = homeFragment
                true
            }
            R.id.navigation_my_page -> {
                childFragmentManager.beginTransaction().hide(activeFragment).show(myPageFragment).commit()
                activeFragment = myPageFragment
                true
            }
            else -> { false }
        }
    }
}