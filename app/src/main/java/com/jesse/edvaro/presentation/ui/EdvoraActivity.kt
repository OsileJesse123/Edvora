package com.jesse.edvaro.presentation.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.jesse.edvaro.R
import com.jesse.edvaro.databinding.EdvoraActivityBinding
import com.jesse.edvaro.databinding.FilterCustomDialogBinding
import com.jesse.edvaro.presentation.viewModel.EdvoraActivityViewModel
import com.jesse.edvaro.utils.formatReceivedText
//import com.jesse.edvaro.utils.obtainViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.viewModels

@AndroidEntryPoint
class EdvoraActivity: AppCompatActivity() {

    private lateinit var binding: EdvoraActivityBinding
    //private lateinit var viewModel: EdvoraActivityViewModel
    private val viewModel: EdvoraActivityViewModel by viewModels()
    private lateinit var recyclerAdapter: EdvoraRideAdapter
    private val filterBinding: FilterCustomDialogBinding by lazy {
        FilterCustomDialogBinding.inflate(layoutInflater)
    }
    private var stateText = ""
    private var cityText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EdvoraActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lifecycleOwner = this

        //viewModel = this.obtainViewModel(EdvoraActivityViewModel::class.java)

        binding.edvoraViewModel = viewModel

        binding.executePendingBindings()

        viewModel.user.observe(this) {
            viewModel.setTheNearestRide(it)
        }

        viewModel.nearestRides.observe(this) {
            recyclerAdapter = EdvoraRideAdapter(
                it,
                this@EdvoraActivity
            ) { stationPath ->
                viewModel.getDistance(stationPath)
            }
            viewModel.setUpcomingAndPastRides(it)
            viewModel.setCitiesAndStates(it)
            viewModel.setTheStatesToCities(it)

            viewModel.updateCitiesAndStatesList()
            setupRecyclerView(recyclerAdapter)
        }

        viewModel.citiesAndStatesList.observe(this){
                citiesAndStatesList ->
            setupTextInputLayout(citiesAndStatesList)
        }

        setupFilterButton()

        viewModel.numberOfRides.observe(this){
                rides ->
            rides?.let {
                setupTabLayout(rides[0], rides[1])
            }
        }
    }


    private fun setupTabLayout(upcomingRidesNumber: Int, pastRidesNumber: Int){
        binding.rideCategoryTab.apply{
            addOnTabSelectedListener(
                object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        tab?.let {
                            when(it.position){
                                0 -> {
                                        if(!viewModel.isFiltered){
                                            recyclerAdapter.setRides(viewModel.nearestRides.value)
                                        }
                                }
                                1 -> {
                                    recyclerAdapter.setRides(viewModel.upcomingRides)
                                }
                                2 -> {
                                    recyclerAdapter.setRides(viewModel.pastRides)
                                }
                            }
                            recyclerAdapter.notifyDataSetChanged()
                            Log.d("IsItFiltered", "isFiltered: ${viewModel.isFiltered}")
                        }
                    }
                    override fun onTabUnselected(tab: TabLayout.Tab?) {}

                    override fun onTabReselected(tab: TabLayout.Tab?) {}
                }
            )
            val upcomingTab = getTabAt(1)
            val pastTab = getTabAt(2)

            upcomingTab?.let {
                if(upcomingRidesNumber != 0){
                    it.orCreateBadge
                    it.badge?.number = upcomingRidesNumber
                }
            }
            pastTab?.let {
                if(pastRidesNumber != 0){
                    it.orCreateBadge
                    it.badge?.number = pastRidesNumber
                }
            }
        }

    }

    private fun setupFilterButton(){
        binding.filterButton.apply{
            setOnClickListener {
                binding.dimLayout.visibility = View.VISIBLE
                createAndGetPopupWindow().showAtLocation(it, Gravity.CENTER, 90, -250)
            }
        }
    }

    private fun createAndGetPopupWindow() : PopupWindow {
        val height = resources.getDimensionPixelSize(R.dimen.custom_filter_height)
        val width = resources.getDimensionPixelSize(R.dimen.custom_filter_width)
        return PopupWindow(filterBinding.root, width, height, true).apply {
            setOnDismissListener {

                if(stateText.isNotEmpty() || cityText.isNotEmpty()){
                    val filteredRides = viewModel
                    .filterListByStatesAndOrCities(cityText, stateText)
                    recyclerAdapter?.let {
                        recyclerAdapter.setRides(filteredRides)
                        recyclerAdapter.notifyDataSetChanged()
                    }
                    val nearestRideTab = binding.rideCategoryTab.getTabAt(0)
                    binding.rideCategoryTab.selectTab(nearestRideTab, true)
                    viewModel.isFiltered = false
                    resetDropDownMenuHint()
                }
                binding.dimLayout.visibility = View.GONE

            }
            elevation = 5F
        }
    }

    private fun setupTextInputLayout(citiesAndStatesList: MutableList<List<String>>?){

        val stateDropDownMenu = filterBinding.stateDropdownMenu.editText as? AutoCompleteTextView
        val cityDropDownMenu = filterBinding.cityDropdownMenu.editText as? AutoCompleteTextView

        citiesAndStatesList?.let {
            //These lists will hold the data(cities and states) that will be sent to the ViewModel
            //for filtering purposes
            val states = citiesAndStatesList[1]
            val cities = citiesAndStatesList[0]

            //These lists will hold data that will be displayed to the user on the
            // AutoCompleteTextView
            val arrayAdapterStates = states.map { formatReceivedText(it, 8)}
            val arrayAdapterCities = cities.map { formatReceivedText(it, 8)}

            val stateAdapter = ArrayAdapter(this, R.layout.list_item, arrayAdapterStates)
            val cityAdapter = CityArrayAdapter(this, R.layout.list_item, arrayAdapterCities)

            stateDropDownMenu?.let { autoCompleteTextView ->
                autoCompleteTextView.setAdapter(stateAdapter)
                autoCompleteTextView.dropDownHeight = resources
                    .getDimensionPixelSize(R.dimen.dropdown_height)
                autoCompleteTextView.onItemClickListener =
                    AdapterView.OnItemClickListener { _, _, position, _ ->
                    stateText = states[position]
                    val updatedCities = viewModel.setTheCityDropDownList(position, stateText)
                    cityAdapter.updateItemsList(updatedCities)
                    cityDropDownMenu?.dropDownHeight = getDropDownFinalHeight(cityAdapter.count)
                }
            }
            cityDropDownMenu?.let {
                autoCompleteTextView ->
                autoCompleteTextView.setAdapter(cityAdapter)
                autoCompleteTextView.dropDownHeight = resources
                    .getDimensionPixelSize(R.dimen.dropdown_height)
                autoCompleteTextView.setDropDownBackgroundResource(R.color.dropdown_menu_color)
                autoCompleteTextView.onItemClickListener =
                    AdapterView.OnItemClickListener { _, _, position, _ ->
                    cityText = cityAdapter.getItem(position) ?: ""
                }
            }
        }
    }

    private fun resetDropDownMenuHint(){
        (filterBinding.stateDropdownMenu.editText as? AutoCompleteTextView)?.let{
            it.setText(resources.getString(R.string.state), false)
            stateText = ""
        }
        (filterBinding.cityDropdownMenu.editText as? AutoCompleteTextView)?.let{
            it.setText(resources.getString(R.string.city), false)
            val cityAdapter = (it.adapter as CityArrayAdapter)
            cityAdapter.updateItemsList(viewModel.cities)
            it.dropDownHeight = getDropDownFinalHeight(cityAdapter.count)
            cityText = ""
        }
    }

    private fun setupRecyclerView(recyclerAdapter: EdvoraRideAdapter){
        binding.edvoraRecycler.apply{
            layoutManager = LinearLayoutManager(this@EdvoraActivity,
                LinearLayoutManager.VERTICAL, false)
            adapter = recyclerAdapter
        }
    }

    inner class CityArrayAdapter(context: Context, resource: Int, var cities: List<String>):
        ArrayAdapter<String>(context, resource, cities){
        override fun getCount(): Int = cities.size

        fun updateItemsList(cities: List<String>){
            clear()
            addAll(cities)
            notifyDataSetChanged()
        }
    }

    private fun getDropDownFinalHeight(itemCount: Int): Int{
       val defaultHeight = resources.getDimensionPixelSize(R.dimen.dropdown_height)
        val finalHeight: Int
        return when(itemCount) {
           1 -> {
               finalHeight = defaultHeight / resources.getDimensionPixelSize(R.dimen.item_count_1)
               finalHeight
           }
           2 -> {
               finalHeight = defaultHeight / resources.getDimensionPixelSize(R.dimen.item_count_2)
               finalHeight
           }
           else -> defaultHeight
       }
    }
}
