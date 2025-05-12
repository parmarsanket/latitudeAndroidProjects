package com.sanket.mvvmstructure.ui.fragment.mainFragment

import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayoutMediator
import com.sanket.mvvmstructure.R
import com.sanket.mvvmstructure.databinding.MainFragmentBinding
import com.sanket.mvvmstructure.ui.Adapter.MyViewPagerAdapter
import com.sanket.mvvmstructure.ui.viewmodel.Appviewmodel
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
/**
 * A simple [Fragment] subclass.
 * Use the [BlankFragment3.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class MainScreenFragment : Fragment(), OnMapReadyCallback {
    // TODO: Rename and change types of parameters

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: Appviewmodel by activityViewModels()
    private lateinit var mMap :GoogleMap

    private var param1: String? = null
    private var param2: String? = null


    lateinit var bottomSheetBehavior: BottomSheetBehavior<*>




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val navHostFragment = childFragmentManager
            .findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavView?.setupWithNavController(navController)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        viewModel.loginSuccess.observe(viewLifecycleOwner)
        {
            if(it==false)
            {
                view.findNavController().navigate(R.id.action_main_fragment_to_login_fragment,
                null, NavOptions.Builder().setPopUpTo(R.id.login_fragment, true).build())
            }
        }

        // Setup BottomSheet only if it's available in the current layout (portrait or landscape)
//        binding.bottomSheetLayout?.let { layout ->
//            bottomSheetBehavior = BottomSheetBehavior.from(layout)
//
//            val topMarginDp = 30
//            val topMarginPx = TypedValue.applyDimension(
//                TypedValue.COMPLEX_UNIT_DIP,
//                topMarginDp.toFloat(),
//                resources.displayMetrics
//            ).toInt()
//
//            val screenHeight = Resources.getSystem().displayMetrics.heightPixels
//            val maxSheetHeight = screenHeight - topMarginPx
//
//            layout.layoutParams?.height = maxSheetHeight
//            layout.requestLayout()
//
//            bottomSheetBehavior.peekHeight = screenHeight / 2
//
//            // Observe the ViewModel's bottomSheetState to maintain persistent state
//            lifecycleScope.launchWhenStarted {
//                viewModel.bottomSheetState.collect { state ->
//                    if (bottomSheetBehavior.state != state) {
//                        bottomSheetBehavior.state = state
//                    }
//                }
//            }
//        }


        // Setup ViewPager and TabLayout only if present
//        binding.viewPager.let { viewPager ->
//            val adapter = MyViewPagerAdapter(this)
//            viewPager.adapter = adapter
//
//            binding.tabLayout.let { tabLayout ->
//                TabLayoutMediator(tabLayout, viewPager) { tab, position ->
//                    tab.text = "Tab ${position + 1}"
//                }.attach()
//            }
//        }
    }








    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment3.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainScreenFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap=googleMap
        val sampleLocation = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sampleLocation).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sampleLocation, 10f))

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun expandBottomSheet() {
        if (::bottomSheetBehavior.isInitialized) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    fun collapseBottomSheet() {
        if (::bottomSheetBehavior.isInitialized) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }


}