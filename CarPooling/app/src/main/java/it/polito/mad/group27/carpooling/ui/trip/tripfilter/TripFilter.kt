package it.polito.mad.group27.carpooling.ui.trip.tripfilter

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import it.polito.mad.group27.carpooling.R
import it.polito.mad.group27.carpooling.ui.BaseFragmentWithToolbar

class TripFilter : BaseFragmentWithToolbar(R.layout.trip_filter_fragment, R.menu.edit_menu, R.string.trip_edit_title) {


    private lateinit var viewModel: TripFilterViewModel



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TripFilterViewModel::class.java)
        // TODO: Use the ViewModel
    }

}