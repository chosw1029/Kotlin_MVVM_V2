package com.nextus.kotlinmvvmexample.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.nextus.kotlinmvvmexample.BR
import com.nextus.kotlinmvvmexample.R
import com.nextus.kotlinmvvmexample.shared.analytics.AnalyticsHelper
import javax.inject.Inject

/**
 * To be implemented by components that host top-level navigation destinations.
 */
interface NavigationHost {

    /** Called by MainNavigationFragment to setup it's toolbar with the navigation controller. */
    fun registerToolbarWithNavigation(toolbar: Toolbar, title: String?)
}

/**
 * To be implemented by main navigation destinations shown by a [NavigationHost].
 */
interface NavigationDestination {

    /** Called by the host when the user interacts with it. */
    fun onUserInteraction() {}
}


abstract class BaseFragment<B: ViewDataBinding>(
    @LayoutRes private val layoutResId: Int
) : Fragment(), NavigationDestination {

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    private lateinit var mViewDataBinding: B
    protected var navigationHost: NavigationHost? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is NavigationHost) {
            navigationHost = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        navigationHost = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewDataBinding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        return mViewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val host = navigationHost ?: return
        val mainToolbar: Toolbar = view.findViewById(R.id.toolbar) ?: return
        mainToolbar.apply {
            host.registerToolbarWithNavigation(this, title.toString())
        }
    }

    fun initBinding(viewModel: ViewModel) {
        mViewDataBinding.setVariable(BR.viewModel, viewModel)
        mViewDataBinding.lifecycleOwner = viewLifecycleOwner
        mViewDataBinding.executePendingBindings()
    }

    fun getBinding(): B = mViewDataBinding
}