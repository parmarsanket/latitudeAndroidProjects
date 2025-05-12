// ScrollBehaviorHelper.kt
package com.sanket.mvvmstructure.util

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior

class ScrollBehaviorHelper(
    private val recyclerView: RecyclerView,
    private val bottomSheetBehavior: BottomSheetBehavior<*>
) {
    init {
        attachScrollListener()
    }

    private fun attachScrollListener() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var scrollingUp = false
            private var scrollingDown = false

            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                scrollingUp = dy < 0
                scrollingDown = dy > 0
                Log.d("BottomSheetTracker", "Scrolled: dy=$dy, scrollingUp=$scrollingUp, scrollingDown=$scrollingDown")
            }

            override fun onScrollStateChanged(rv: RecyclerView, newState: Int) {
                super.onScrollStateChanged(rv, newState)

                val layoutManager = rv.layoutManager as? LinearLayoutManager ?: return
                val currentState = bottomSheetBehavior.state
                if (currentState == BottomSheetBehavior.STATE_SETTLING) return

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val firstVisible = layoutManager.findFirstCompletelyVisibleItemPosition()
                    val totalItemCount = layoutManager.itemCount

                    if (scrollingUp && firstVisible <= 1 && currentState != BottomSheetBehavior.STATE_COLLAPSED) {
                        Log.d("BottomSheetTracker", "Scrolled UP near top — COLLAPSING")
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    } else if (scrollingDown && totalItemCount > 0 && currentState != BottomSheetBehavior.STATE_EXPANDED) {
                        Log.d("BottomSheetTracker", "Scrolled DOWN — EXPANDING")
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                }
            }
        })
    }
}
