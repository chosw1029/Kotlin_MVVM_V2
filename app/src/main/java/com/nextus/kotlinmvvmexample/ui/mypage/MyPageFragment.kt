package com.nextus.kotlinmvvmexample.ui.mypage

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import com.nextus.kotlinmvvmexample.R
import com.nextus.kotlinmvvmexample.databinding.FragmentMyPageBinding
import com.nextus.kotlinmvvmexample.ui.base.BaseFragment
import com.nextus.kotlinmvvmexample.util.signin.SignInHandler
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {

    @Inject
    lateinit var signInHandler: SignInHandler

    private val myPageViewModel: MyPageViewModel by viewModels()

    var isShowMenu = true
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding(myPageViewModel)
        initView()
    }

    private fun initView() {

    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.navigation_setting)?.let {
            it.isVisible = isShowMenu
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.navigation_setting -> {
                myPageViewModel.onClickSetting(getBinding().root)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.my_page_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onResume() {
        super.onResume()
        navigationHost?.registerToolbarWithNavigation(getBinding().toolbar, getString(R.string.title_my_page))
    }
}