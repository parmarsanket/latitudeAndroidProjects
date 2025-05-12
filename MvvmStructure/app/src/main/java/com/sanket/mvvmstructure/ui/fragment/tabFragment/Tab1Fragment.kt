package com.sanket.mvvmstructure.ui.fragment.tabFragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.sanket.mvvmstructure.R
import com.sanket.mvvmstructure.databinding.FragmentTab1Binding
import com.sanket.mvvmstructure.ui.Adapter.TodoAdapter
import com.sanket.mvvmstructure.ui.fragment.mainFragment.MainScreenFragment
import com.sanket.mvvmstructure.ui.fragment.mainFragment.StationViewModel
import com.sanket.mvvmstructure.ui.state.StationUiState
import com.sanket.mvvmstructure.ui.viewmodel.Appviewmodel
import com.sanket.mvvmstructure.util.ScrollBehaviorHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class Tab1Fragment : Fragment(R.layout.fragment_tab1) {

    interface OnRecyclerViewReadyListener {
        fun onRecyclerViewReady(recyclerView: RecyclerView)
    }

    var recyclerViewReadyListener: OnRecyclerViewReadyListener? = null
    private val viewModel: StationViewModel by activityViewModels()
    private val viewModel2: Appviewmodel by activityViewModels()
    private lateinit var binding: FragmentTab1Binding

    // 👇 Add reference to BottomSheetBehavior
    private var bottomSheetBehavior: BottomSheetBehavior<*>? = null

    // 👇 Setter for behavior from MainScreenFragment
    fun setBottomSheetBehavior(behavior: BottomSheetBehavior<*>) {
        bottomSheetBehavior = behavior
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTab1Binding.bind(view)

        val bottomNav = requireActivity().findViewById<View>(R.id.bottomNavView)
        val extraPadding = resources.getDimensionPixelSize(R.dimen.bottom_padding_extra)

        bottomNav.post {
            binding.rvItems.setPadding(0, 0, 0, bottomNav.height + extraPadding)
            binding.rvItems.clipToPadding = false
        }

        // ✅ Disable nested scrolling so BottomSheet can handle gestures
        binding.rvItems.isNestedScrollingEnabled = false

        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect { state ->
                when (state) {
                    is StationUiState.Success -> {
                        val adapter = TodoAdapter(state.stations){
                            viewModel.title.value = it.n
                            viewModel.location.value = LatLng(it.lt.toDouble(),it.lg.toDouble())
                            bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
                        }
                        binding.rvItems.adapter = adapter
                        binding.rvItems.layoutManager = LinearLayoutManager(requireContext())

                        binding.rvItems.addOnScrollListener(object :
                            RecyclerView.OnScrollListener() {
                            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                                val layoutManager = rv.layoutManager as LinearLayoutManager
                                val firstVisible =
                                    layoutManager.findFirstCompletelyVisibleItemPosition()
                                val canDrag = firstVisible == 0
                                bottomSheetBehavior?.isDraggable = canDrag
                            }
                        })

                        recyclerViewReadyListener?.onRecyclerViewReady(binding.rvItems)
                    }

                    else -> {}
                }
            }
        }
    }


    fun getRecyclerView(): RecyclerView {
        return binding.rvItems
    }

}

