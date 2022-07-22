package com.jesse.edvaro.presentation.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jesse.edvaro.R
import com.jesse.edvaro.data.model.Ride
import com.jesse.edvaro.databinding.RideCardItemBinding

class EdvoraRideAdapter(private var rides: List<Ride>?, private val context: Context,
                        private val getDistance:((List<Int>) -> Int)):
    RecyclerView.Adapter<EdvoraRideAdapter.EdvoraRideViewHolder>() {

    fun setRides(ridesParam: List<Ride>?){
        rides = ridesParam
    }
    fun getRides(): List<Ride>{
        return rides ?: listOf()
    }

    class EdvoraRideViewHolder(private val binding: RideCardItemBinding):
        RecyclerView.ViewHolder(binding.root){

        private val rideId = binding.rideIdTv
        private val originStation = binding.originStationTv
        private val stationPath = binding.stationPathTv
        private val date = binding.dateTv
        private val distance = binding.distanceTv

        fun setData(ride: Ride?, context: Context, getDistance:((List<Int>) -> Int)){
            ride?.let{
                binding.ride = ride
                binding.executePendingBindings()
                rideId.text = context.getString(R.string.ride_id, ride.id.toString())
                originStation.text = context.getString(R.string.origin_station,
                    ride.originStationCode.toString())
                stationPath.text = context.getString(R.string.station_path, ride.stationPath)
                date.text = context.getString(R.string.date, ride.date)
                distance.text = context.getString(R.string.distance,
                    getDistance(ride.stationPath).toString())
            }
        }

        companion object {
                fun from(parent: ViewGroup): EdvoraRideViewHolder {
                    val layoutInflater = LayoutInflater.from(parent.context)
                    val binding = RideCardItemBinding.inflate(layoutInflater)
                    return EdvoraRideViewHolder(binding)
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EdvoraRideViewHolder {
        return EdvoraRideViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: EdvoraRideViewHolder, position: Int) {
        holder.setData(rides?.get(position), context, getDistance)
    }

    override fun getItemCount(): Int = rides?.size ?: 0

}