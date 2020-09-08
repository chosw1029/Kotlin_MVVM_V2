package com.nextus.kotlinmvvmexample.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.google.android.material.navigation.NavigationView
import com.nextus.kotlinmvvmexample.R
import com.nextus.kotlinmvvmexample.shared.analytics.AnalyticsHelper
import com.nextus.kotlinmvvmexample.ui.base.NavigationHost
import com.nextus.kotlinmvvmexample.util.HeightTopWindowInsetsListener
import com.nextus.kotlinmvvmexample.util.NoopWindowInsetsListener
import com.nextus.kotlinmvvmexample.util.navigationItemBackground
import com.nextus.kotlinmvvmexample.widget.HashtagIoDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationHost {

    companion object {
        /** Key for an int extra defining the initial navigation target. */
        const val EXTRA_NAVIGATION_ID = "extra.NAVIGATION_ID"

        private const val NAV_ID_NONE = -1

        private val TOP_LEVEL_DESTINATIONS = setOf(
            R.id.navigation_home,
            R.id.navigation_my_page
        )
    }

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    private lateinit var content: ConstraintLayout
    private lateinit var navigation: NavigationView
    private lateinit var navController: NavController
    private var navHostFragment: NavHostFragment? = null

    private lateinit var statusScrim: View

    private var currentNavId = NAV_ID_NONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initNavigationDrawer()
        initBottomNavigationBar()

        if (savedInstanceState == null) {
            // default to showing Home
            val initialNavId = intent.getIntExtra(EXTRA_NAVIGATION_ID, R.id.navigation_home)
            navigation.setCheckedItem(initialNavId) // doesn't trigger listener
            navigateTo(initialNavId)
        }
    }

    private fun initView() {
        content = findViewById(R.id.content_container)
        content.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        // Make the content ViewGroup ignore insets so that it does not use the default padding
        content.setOnApplyWindowInsetsListener(NoopWindowInsetsListener)

        statusScrim = findViewById(R.id.status_bar_scrim)
        statusScrim.setOnApplyWindowInsetsListener(HeightTopWindowInsetsListener)
    }

    private fun initNavigationDrawer() {
        navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment?

        navController = findNavController(R.id.nav_host_fragment)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            currentNavId = destination.id
            val isTopLevelDestination = TOP_LEVEL_DESTINATIONS.contains(destination.id)
            val lockMode = if (isTopLevelDestination) {
                bottom_navigation.visibility = View.VISIBLE
                DrawerLayout.LOCK_MODE_UNLOCKED
            } else {
                bottom_navigation.visibility = View.GONE
                DrawerLayout.LOCK_MODE_LOCKED_CLOSED
            }
            drawer.setDrawerLockMode(lockMode)
        }

        navigation = findViewById(R.id.navigation)
        navigation.apply {
            // Add the #io19 decoration
            val menuView = findViewById<RecyclerView>(R.id.design_navigation_view)?.apply {
                addItemDecoration(HashtagIoDecoration(context))
            }
            // Update the Navigation header view to pad itself down
            /*navHeaderBinding.root.doOnApplyWindowInsets { v, insets, padding ->
                v.updatePadding(top = padding.top + insets.systemWindowInsetTop)
                // NavigationView doesn't dispatch insets to the menu view, so pad the bottom here.
                menuView?.updatePadding(bottom = insets.systemWindowInsetBottom)
            }
            addHeaderView(navHeaderBinding.root)*/

            itemBackground = navigationItemBackground(context)

            setupWithNavController(navController)
        }
    }

    private fun initBottomNavigationBar() {
        bottom_navigation.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            navigateTo(item.itemId)
            true
        }

        bottom_navigation.setupWithNavController(navController)
    }

    override fun registerToolbarWithNavigation(toolbar: Toolbar) {
        val appBarConfiguration = AppBarConfiguration(TOP_LEVEL_DESTINATIONS, drawer)
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currentNavId = navigation.checkedItem?.itemId ?: NAV_ID_NONE
    }

    private fun navigateTo(navId: Int) {
        if (navId == currentNavId) {
            return // user tapped the current item
        }
        navController.navigate(navId)
    }
}
