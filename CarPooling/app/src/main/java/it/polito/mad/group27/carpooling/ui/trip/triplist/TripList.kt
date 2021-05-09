package it.polito.mad.group27.carpooling.ui.trip.triplist

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.navigation.fragment.findNavController
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import it.polito.mad.group27.carpooling.R
import it.polito.mad.group27.carpooling.ui.trip.TripDB


class TripList: BaseTripList() {

    private val query = queryBase.whereEqualTo("ownerUid", currentUserUid)
    private val options = FirestoreRecyclerOptions.Builder<TripDB>()
        .setQuery(query, TripDB::class.java)
        .build()

    override fun setTopRightButtonIconAndOnClickListener(tripViewHolder: TripViewHolder, bundle: Bundle) {
        val icon = R.drawable.ic_baseline_edit_24
        tripViewHolder.topRightButtonShadow.setImageResource(icon)
        tripViewHolder.topRightButton.setImageResource(icon)
        tripViewHolder.topRightButton.setOnClickListener {
            findNavController().navigate(R.id.action_tripList_to_tripEditFragment, bundle)
        }
    }

    override fun setAdapter(recyclerView: RecyclerView) {
        adapter = TripFirestoreRecyclerAdapter(options)
        recyclerView.adapter = adapter
    }
}