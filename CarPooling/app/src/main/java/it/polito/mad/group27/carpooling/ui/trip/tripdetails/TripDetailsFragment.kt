package it.polito.mad.group27.carpooling.ui.trip.tripdetails

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.polito.mad.group27.carpooling.R
import it.polito.mad.group27.carpooling.getLogTag
import it.polito.mad.group27.carpooling.ui.BaseFragmentWithToolbar
import it.polito.mad.group27.carpooling.ui.trip.Trip
import java.text.DateFormat
import java.util.*

class TripDetailsFragment : BaseFragmentWithToolbar(R.layout.trip_details_fragment,
    R.menu.show_menu, null) {
    // TODO insert title customized (Trip to .... ) (?)
    private lateinit var viewModel: TripDetailsViewModel
    private lateinit var dropdownListButton : LinearLayout

    private lateinit var trip : Trip
    private lateinit var seatsView : TextView
    private lateinit var dateView : TextView
    private lateinit var priceView : TextView
    private lateinit var departureHour : TextView
    private lateinit var departureLocation : TextView
    private lateinit var destinationHour : TextView
    private lateinit var destinationLocation : TextView

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TripDetailsViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.trip_details_fragment, container, false)

        if (view is RecyclerView) {
            with(view) {
                adapter = TripStopsViewAdapter(trip.stops)
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trip = arguments?.getParcelable<Trip>("trip") ?: Trip()

        Log.d(getLogTag(),"got from bundle: $trip")

        dropdownListButton = view.findViewById(R.id.startTripView)

        seatsView = view.findViewById(R.id.showTripSeats)
        dateView = view.findViewById(R.id.showTripDate)
        priceView = view.findViewById(R.id.showTripPrice)
        departureHour = view.findViewById(R.id.departureTimeDetails)
        departureLocation = view.findViewById(R.id.departureNameDetails)
        destinationHour = view.findViewById(R.id.tripStopTime)
        destinationLocation = view.findViewById(R.id.tripStopName)

        seatsView.text = "${trip.availableSeats}/${trip.totalSeats}"
        dateView.text =  DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault()).format(trip.date)
        priceView.text = trip.price.toString()
        departureHour.text = trip.startHour.toString()
        departureLocation.text = trip.from
        destinationHour.text = trip.endHour.toString()
        destinationLocation.text = trip.to

        val recyclerView = view.findViewById<RecyclerView>(R.id.tripStopList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = TripStopsViewAdapter(trip.stops)


        dropdownListButton.setOnClickListener {
            val arrowImageView : ImageView = view.findViewById(R.id.expandButton)
            val recyclerView = view.findViewById<RecyclerView>(R.id.tripStopList)

            Log.d(getLogTag(),"Touched route list")

            recyclerView.visibility =
                    when(recyclerView.visibility) {
                View.GONE -> {
                    arrowImageView.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
                    View.VISIBLE
                }
                else ->{
                    arrowImageView.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
                    View.GONE
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.edit_menu_button -> {
                Log.d(getLogTag(),"Passing bundle of $trip")
                findNavController().navigate(
                        R.id.action_tripDetailsFragment_to_tripEditFragment,
                        bundleOf("trip" to trip))
            }
            else-> return super.onOptionsItemSelected(item)
        }
        return true
    }

}