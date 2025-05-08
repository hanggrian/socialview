package com.example.static

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayout

class ShopFragment : BottomSheetDialogFragment() {
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val root = inflater.inflate(R.layout.fragment_shop, container, false)
        tabLayout = root.findViewById(R.id.tabLayout)

        tabLayout.addTab(tabLayout.newTab().setText("Offers"))
        tabLayout.addTab(tabLayout.newTab().setText("Socials"))
        tabLayout.addTab(tabLayout.newTab().setText("Description"))

        return root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            BottomSheetBehavior
                .from(
                    dialog.findViewById<FrameLayout>(
                        com.google.android.material.R.id.design_bottom_sheet,
                    ),
                ).setState(STATE_EXPANDED)
        }
        return dialog
    }

    companion object {
        const val TAG = "com.example.static.ShopFragment"
    }
}
