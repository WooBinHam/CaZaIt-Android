package org.cazait.cazait_android.ui.view.cafelist

import MarginItemDecoration
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.cazait.cazait_android.CAFE_ITEM_KEY
import org.cazait.cazait_android.R
import org.cazait.cazait_android.REQUEST_LOCATION_PERMISSION
import org.cazait.cazait_android.data.Resource
import org.cazait.cazait_android.data.model.Cafe
import org.cazait.cazait_android.data.model.Cafes
import org.cazait.cazait_android.data.model.remote.response.CafeListResponse
import org.cazait.cazait_android.databinding.FragmentCafeListBinding
import org.cazait.cazait_android.ui.adapter.CafeListItemAdapter
import org.cazait.cazait_android.ui.base.BaseFragment
import org.cazait.cazait_android.ui.util.SingleEvent
import org.cazait.cazait_android.ui.util.extension.observe
import org.cazait.cazait_android.ui.util.extension.observeEvent
import org.cazait.cazait_android.ui.util.extension.toGone
import org.cazait.cazait_android.ui.util.extension.toVisible
import org.cazait.cazait_android.ui.view.cafelist.info.CafeInformationActivity
import org.cazait.cazait_android.ui.viewmodel.CafeListViewModel
import kotlin.math.roundToInt

@AndroidEntryPoint
class CafeListFragment : BaseFragment<FragmentCafeListBinding, CafeListViewModel>() {
    override val viewModel: CafeListViewModel by viewModels()

    override val layoutResourceId: Int
        get() = R.layout.fragment_cafe_list

    private lateinit var adapter: CafeListItemAdapter
    override fun initBeforeBinding() {
        // 뷰모델을 lifeCycle 에 종속시킨다. lifeCycle 동안 옵저버 역할을 하게 된다.
        binding.lifecycleOwner = this
    }

    override fun initView() {
        getUserLocation()
    }

    override fun initAfterBinding() {
        // viewModel.refreshCafeList()
        observeViewModel()
    }

    private fun observeViewModel() {
        observe(viewModel.cafesLiveData, ::handleCafeList)
        observeEvent(viewModel.userLocation, ::handleUserLocation)
        observeEvent(viewModel.openCafeDetails, ::navigateToDetailsScreen)
    }

    private fun handleCafeList(status: Resource<CafeListResponse>) {
        when (status) {
            is Resource.Loading -> showLoadingView()
            is Resource.Success -> status.data.let {
                Log.d("CafeListFragment", "${status.data.message} ${status.data.result}")
                when (status.data.result) {
                    "SUCCESS" -> {
                        val cafes = convertCafeListResponseToCafes(it)
                        bindRVCafeListData(cafes = cafes)
                    }
                    "FAIL" -> {
                        viewModel.refreshCafeList()
                    }
                }
            }
            is Resource.Error -> {
                showDataView(false)
                status.message.let {
                    viewModel.showToastMessage(it)
                }
            }
        }
    }

    private fun bindRVCafeListData(cafes: Cafes) {
        val spaceDecoration =
            MarginItemDecoration(resources.getDimension(R.dimen.cafe_item_space).roundToInt())

        if (cafes.cafesList.isNotEmpty()) {
            adapter = CafeListItemAdapter(viewModel, cafes.cafesList)
            binding.rvCafeList.adapter = adapter
            binding.rvCafeList.addItemDecoration(spaceDecoration)
            showDataView(true)
        } else {
            showDataView(false)
        }
    }

    private fun showLoadingView() {
        binding.pbLoading.toVisible()
        binding.tvNoData.toGone()
        binding.rvCafeList.toGone()
    }

    private fun showDataView(isShow: Boolean) {
        binding.tvNoData.visibility = if (isShow) GONE else VISIBLE
        binding.rvCafeList.visibility = if (isShow) VISIBLE else GONE
        binding.pbLoading.toGone()
    }

    private fun navigateToDetailsScreen(navigateEvent: SingleEvent<Cafe>) {
        navigateEvent.getContentIfNotHandled().let {
            if(it != null) {
                val nextScreenIntent = Intent(context, CafeInformationActivity::class.java).apply {
                    putExtra(CAFE_ITEM_KEY, it)
                }
                startActivity(nextScreenIntent)
            }
        }
    }

    private fun handleUserLocation(locationEvent: SingleEvent<Location>) {
        locationEvent.getContentIfNotHandled().let {
            viewModel.refreshCafeList()
        }
    }

    private fun convertCafeListResponseToCafes(cafeListResponse: CafeListResponse): Cafes {
        // data[0]인 이유는 0번째 페이지이기 때문임 만일 페이지 수가 넘어가면 1씩 증가시켜서 추가해줘야 함
        val cafeList = cafeListResponse.data[0].map {
            Cafe(
                it.cafeId,
                it.name,
                it.distance,
                it.address,
                it.congestionStatus
            )
        }.toList()
        return Cafes(ArrayList(cafeList))
    }

    private fun getUserLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(requireActivity(),arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        } else {
            viewModel.getUserLocation()
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                viewModel.getUserLocation()
            } else {
                // Permission denied, show a message to the user
                viewModel.showToastMessage("카페 목록을 불러오기 위해 위치 접근 권한이 필요합니다")
            }
        }
    }
}