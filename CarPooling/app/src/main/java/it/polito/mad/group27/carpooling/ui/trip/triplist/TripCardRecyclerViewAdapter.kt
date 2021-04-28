package it.polito.mad.group27.carpooling.ui.trip.triplist

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import it.polito.mad.group27.carpooling.R
import it.polito.mad.group27.carpooling.ui.trip.Hour
import it.polito.mad.group27.carpooling.ui.trip.Trip
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

import java.text.DateFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * [RecyclerView.Adapter] that can display a [Trip].
 */
class TripCardRecyclerViewAdapter(
    private val values: MutableList<Trip>,
    private val navController: NavController,
): RecyclerView.Adapter<TripCardRecyclerViewAdapter.ViewHolder>() {

    private val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
    private val priceFormat: NumberFormat = NumberFormat.getCurrencyInstance(Locale.ITALY)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_trip, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        val bundle = bundleOf("trip" to Json.encodeToString(item))
        val bundleParcelable = bundleOf("trip" to item)
        holder.carImageView.setOnClickListener { navController.navigate(R.id.action_tripList_to_tripDetailsFragment, bundleParcelable) }

        if(item.carImageUri ==null) {
            holder.carImageView.setColorFilter(Color.argb(34, 68, 68, 68))
            holder.carImageView.setImageResource(R.drawable.ic_baseline_directions_car_24)
        } else {
            holder.carImageView.setImageURI(item.carImageUri)
            holder.carImageView.colorFilter = null
        }
        holder.priceTextView.text = priceFormat.format(item.price)


        holder.editButton.setOnClickListener { navController.navigate(R.id.action_tripList_to_tripEditFragment, bundle) }
        holder.departureTextView.text = item.from
        holder.destinationTextView.text = item.to
        holder.hourDepartureTextView.text = Hour(item.startDateTime[Calendar.HOUR], item.startDateTime[Calendar.MINUTE]).toString()
        holder.dateDepartureTextView.text = dateFormat.format(item.startDateTime.time)
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val carImageView: ImageView = view.findViewById(R.id.car_image)
        val priceTextView: TextView = view.findViewById(R.id.price_text)
        val editButton: ImageButton = view.findViewById(R.id.edit_button)
        val departureTextView: TextView = view.findViewById(R.id.departure_text)
        val destinationTextView: TextView = view.findViewById(R.id.destination_text)
        val hourDepartureTextView: TextView = view.findViewById(R.id.hour_departure_text)
        val dateDepartureTextView: TextView = view.findViewById(R.id.date_departure_text)

    }
}