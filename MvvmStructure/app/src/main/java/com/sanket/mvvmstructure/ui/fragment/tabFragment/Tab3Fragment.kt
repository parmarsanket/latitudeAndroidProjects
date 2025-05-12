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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.sanket.mvvmstructure.R
import com.sanket.mvvmstructure.databinding.FragmentTab1Binding
import com.sanket.mvvmstructure.databinding.FragmentTab2Binding
import com.sanket.mvvmstructure.databinding.FragmentTab3Binding
import com.sanket.mvvmstructure.ui.viewmodel.Appviewmodel
import kotlinx.coroutines.launch

class Tab3Fragment : Fragment(R.layout.fragment_tab3) {

    private lateinit var binding: FragmentTab3Binding
    private val viewModel2: Appviewmodel by activityViewModels()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewBinding
        binding = FragmentTab3Binding.bind(view)
        // Observe bottomSheetState from the ViewModel

        // Set any views here
        binding.tab3Text.text = "This is Tab 3"
    }
}
