package org.cazait.cazait_android.ui.view.mypage

import android.content.Intent
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.cazait.cazait_android.R
import org.cazait.cazait_android.databinding.FragmentMyPageBinding
import org.cazait.cazait_android.ui.base.BaseFragment
import org.cazait.cazait_android.ui.base.BaseViewModel
import org.cazait.cazait_android.ui.view.recently.CafeRecentlyActivity
import org.cazait.cazait_android.ui.view.signup.SignUpActivity

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding, BaseViewModel>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_my_page

    override val viewModel: BaseViewModel by viewModels()

    override fun initAfterBinding() {

    }

    override fun initBeforeBinding() {

    }


    override fun initView() {
        //클릭 시 CafeRecentlyActivity로 넘어갑니다.

        binding.myPageArrowFront2.setOnClickListener {
            val intent = Intent(getActivity(), CafeRecentlyActivity::class.java)
            startActivity(intent)
        }
    }
}


