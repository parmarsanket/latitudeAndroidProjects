package com.sanket.mvvmstructure.ui.fragment.menufragment

import android.content.res.Resources
import android.icu.text.CaseMap.Title
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.sanket.mvvmstructure.R
import com.sanket.mvvmstructure.databinding.FragmentHomeBinding
import com.sanket.mvvmstructure.ui.Adapter.MyViewPagerAdapter
import com.sanket.mvvmstructure.ui.fragment.mainFragment.StationViewModel
import com.sanket.mvvmstructure.ui.fragment.tabFragment.Tab1Fragment
import com.sanket.mvvmstructure.ui.viewmodel.Appviewmodel
import com.sanket.mvvmstructure.util.ScrollBehaviorHelper

class homeFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: Appviewmodel by activityViewModels()
    private val viewModel2: StationViewModel by activityViewModels()
    private lateinit var mMap: GoogleMap
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private var suppressTabListener: Boolean = false
    private var savedBottomSheetState: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedBottomSheetState = savedInstanceState?.getInt("bottomSheetState")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Map setup
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        // Setup BottomSheet
        binding.bottomSheetLayout?.let { layout ->
            bottomSheetBehavior = BottomSheetBehavior.from(layout)

            // Adjust layout parameters
            val topMarginDp = 30
            val topMarginPx = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                topMarginDp.toFloat(),
                resources.displayMetrics
            ).toInt()

            val screenHeight = Resources.getSystem().displayMetrics.heightPixels
            val maxSheetHeight = screenHeight - topMarginPx

            layout.layoutParams?.height = maxSheetHeight
            layout.requestLayout()

            bottomSheetBehavior.peekHeight = screenHeight / 2

            // Set initial state
            layout.post {
                bottomSheetBehavior.state = savedBottomSheetState ?: BottomSheetBehavior.STATE_COLLAPSED
            }

            // Setup ViewPager and Tabs
            setupViewPagerAndTabs()
        }
    }

private lateinit var viewPagerAdapter: MyViewPagerAdapter

    private fun setupViewPagerAndTabs() {
        viewPagerAdapter = MyViewPagerAdapter(this)
        binding.viewPager.adapter = viewPagerAdapter

        // Set tab names
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = "Tab ${position + 1}"
        }.attach()

        // Attach ScrollBehaviorHelper to Tab1Fragment's RecyclerView
        viewPagerAdapter.tab1Fragment.recyclerViewReadyListener =
            object : Tab1Fragment.OnRecyclerViewReadyListener {
                override fun onRecyclerViewReady(recyclerView: RecyclerView) {
                    ScrollBehaviorHelper(recyclerView, bottomSheetBehavior)
                }
            }

        // Collapse bottom sheet on tab change
//        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab?) {
//                if (!suppressTabListener) {
//                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
//                }
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab?) {}
//            override fun onTabReselected(tab: TabLayout.Tab?) {
//                if (!suppressTabListener && tab?.position == 0) {
//                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
//                }
//            }
//        })

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {}
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("bottomSheetState", bottomSheetBehavior.state)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        viewModel2.location.observe(viewLifecycleOwner) { location ->
            val title = viewModel2.title.value
            if (::mMap.isInitialized && title != null) {
                mMap.clear()
                mMap.addMarker(MarkerOptions().position(location!!).title(title))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 18f))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
