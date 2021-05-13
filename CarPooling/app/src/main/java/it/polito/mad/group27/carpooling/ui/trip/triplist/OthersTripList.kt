package it.polito.mad.group27.carpooling.ui.trip.triplist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import it.polito.mad.group27.carpooling.R
import it.polito.mad.group27.carpooling.TripFilter
import it.polito.mad.group27.carpooling.getLogTag
import it.polito.mad.group27.carpooling.ui.trip.Option
import it.polito.mad.group27.carpooling.ui.trip.Trip
import it.polito.mad.group27.carpooling.ui.trip.TripDB
import java.util.*

class OthersTripList(
    menu: Int = R.menu.others_trip_list_menu,
): BaseTripList(
    menu = menu,
) {

    private val query = queryBase  // .whereNotEqualTo("ownerUid", currentUserUid)  // 2 inequalities on 2 fields in 1 query are invalid. wut.
    override val options = FirestoreRecyclerOptions.Builder<TripDB>()
        .setQuery(query, TripDB::class.java)
        .build()
    private lateinit var tripFilter: TripFilter
    private val defaultTripFilter = TripFilter()
    private lateinit var checkedChips: MutableMap<String, Boolean>
    private lateinit var chipGroup: ChipGroup

    override fun customizeCardView(tripViewHolder: BaseTripList.TripViewHolder, trip: Trip) {
        // the trip is full
        if (trip.acceptedUsersUids.size == trip.totalSeats) {
            tripViewHolder.topRightButtonShadow.visibility = View.INVISIBLE
            tripViewHolder.topRightButton.visibility = View.INVISIBLE
            return
        }
        var icon: Int? = null
        if (trip.interestedUsersUids.contains(currentUserUid) || trip.acceptedUsersUids.contains(currentUserUid)) {
            icon = R.drawable.ic_baseline_done_24
            tripViewHolder.topRightButton.setOnClickListener {
                Toast.makeText(requireContext(), getString(R.string.warning_message_alreadybooked), Toast.LENGTH_LONG).show()
            }
        } else {
            icon = R.drawable.ic_baseline_add_24
            tripViewHolder.topRightButton.setOnClickListener {
                trip.interestedUsersUids.add(currentUserUid)
                coll.document(trip.id!!).set(trip.toTripDB())
                    .addOnSuccessListener {
                        icon = R.drawable.ic_baseline_done_24
                        Toast.makeText(requireContext(), getString(R.string.success_message_booked), Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener {
                        icon = R.drawable.ic_baseline_add_24
                        Toast.makeText(requireContext(), getString(R.string.warning_message_failedbooking), Toast.LENGTH_LONG).show()
                    }
                }
        }
        tripViewHolder.topRightButtonShadow.setImageResource(icon!!)
        tripViewHolder.topRightButton.setImageResource(icon!!)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_search -> {
                val bundle = bundleOf("filter" to tripFilter)
                Log.d(getLogTag(), "opening trips filter fragment with bundle: $bundle")
                findNavController().navigate(R.id.action_othersTripList_to_tripFilterFragment, bundle)
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        checkedChips = mutableMapOf(
            "from" to false,
            "to" to false,
            "priceMin" to false,
            "priceMax" to false,
            "dateTime" to false,
            "luggage" to false,
            "animals" to false,
            "smoke" to false,
        )
        tripFilter = arguments?.getParcelable("filter") ?: TripFilter()
        if (tripFilter.from == "") tripFilter.from = null
        if (tripFilter.to == "") tripFilter.to = null
        Log.d(getLogTag(), "OthersTripList trip filter: $tripFilter")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun customizeTripList(view: View) {
        val fab: FloatingActionButton = view.findViewById(R.id.fab)
        chipGroup = view.findViewById(R.id.trip_filter_chip_group)
        fab.visibility = View.GONE
        if (tripFilter != defaultTripFilter) {
            addChipsForEnabledFilters()
            Log.d(getLogTag(), "trip filter is applied, showing chips: ${chipGroup.checkedChipIds}")
            chipGroup.visibility = View.VISIBLE
        }
    }

    private fun addChipsForEnabledFilters() {

        fun addChip(text: String, lambda: (View) -> Unit) {
            val chip = layoutInflater.inflate(R.layout.chip_filter, null) as Chip
            chip.text = text
            chip.setOnClickListener(lambda)
            chipGroup.addView(chip)
        }

        var k = ""

        if (tripFilter.from != defaultTripFilter.from) {
            k = "from"
            checkedChips[k] = true
            addChip(tripFilter.from!!) {
                checkedChips[k] = !checkedChips[k]!!
                Log.d(getLogTag(), "chip from (${tripFilter.from}) toggled: ${checkedChips[k]}")
            }
        }

        if (tripFilter.to != defaultTripFilter.to) {
            k = "to"
            checkedChips[k] = true
            addChip(tripFilter.to!!) {
                checkedChips[k] = !checkedChips[k]!!
                Log.d(getLogTag(), "chip to (${tripFilter.to}) toggled: ${checkedChips[k]}")
            }
        }

        if (tripFilter.priceMin != defaultTripFilter.priceMin) {
            k = "priceMin"
            checkedChips[k] = true
            addChip(tripFilter.priceMin.toString()) {
                checkedChips[k] = !checkedChips[k]!!
                Log.d(getLogTag(), "chip priceMin (${tripFilter.priceMin}) toggled: ${checkedChips[k]}")
            }
        }

        if (tripFilter.priceMax != defaultTripFilter.priceMax) {
            k = "priceMax"
            checkedChips[k] = true
            addChip(tripFilter.priceMax.toString()) {
                checkedChips[k] = !checkedChips[k]!!
                Log.d(getLogTag(), "chip priceMax (${tripFilter.priceMax}) toggled: ${checkedChips[k]}")
            }
        }

        if (tripFilter.dateTime != defaultTripFilter.dateTime) {
            k = "dateTime"
            checkedChips[k] = true
            addChip(tripFilter.dateTime.toString()) {
                checkedChips[k] = !checkedChips[k]!!
                Log.d(getLogTag(), "chip dateTime (${tripFilter.dateTime}) toggled: ${checkedChips[k]}")
            }
        }

        for (opt in Option.values()) {
            if (tripFilter.options.contains(opt) && tripFilter.options[opt] != defaultTripFilter.options[opt]) {
                val optionName = opt.name.toLowerCase(Locale.ROOT)
                checkedChips[optionName] = true
                addChip(optionName.capitalize(Locale.ROOT)) {
                    checkedChips[optionName] = !checkedChips[optionName]!!
                    Log.d(getLogTag(), "chip option $optionName toggled: ${checkedChips[optionName]}")
                }
            }
        }
    }

    override fun isFilteredOut(trip: Trip): Boolean {

        fun applyTripFilter(): Boolean {
            if (tripFilter.from != null)
                trip.from.contains(tripFilter.from!!, ignoreCase = true) || return false
            if (tripFilter.to != null)
                trip.to.contains(tripFilter.to!!, ignoreCase = true) || return false
            if (trip.price != null)
                trip.price!! >= tripFilter.priceMin || return false
                trip.price!! <= tripFilter.priceMax || return false
            if (tripFilter.dateTime != null)
                trip.startDateTime >= tripFilter.dateTime!! || return false
            for (opt in Option.values()) {
                if (tripFilter.options.contains(opt) && tripFilter.options[opt] == true) {
                    trip.options.contains(opt) || return false
                }
            }
            return true
        }

        return !(trip.ownerUid != currentUserUid &&
                trip.advertised &&
                applyTripFilter())
    }
}