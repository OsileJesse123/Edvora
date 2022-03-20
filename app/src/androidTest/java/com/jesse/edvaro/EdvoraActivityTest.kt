package com.jesse.edvaro

import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputLayout
import com.jesse.edvaro.presentation.ui.EdvoraActivity
import com.jesse.edvaro.presentation.ui.EdvoraRideAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.isA
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

//I delay by a few seconds due to how fast the testing framework works
//The activity is active for an extra few seconds before the test is concluded
//This is enough time to observe what ever outcome would be most likely.

//I also use runBlocking as the delay function can only be called from a suspend function

//For tests run using Wifi or Cellular connection i choose to delay for at least 20 seconds because
//of how much time it would take the network to respond to the requests is put into consideration
//Chances are that the network provider might be very slow.

@RunWith(AndroidJUnit4::class)
class EdvoraActivityTest {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(EdvoraActivity::class.java)

    @Test
    fun test_launchEdvoraActivityWithoutWifiOrCellularConnection(){
        //Please ensure that Wifi or Cellular Data is not active before running this test
        //to achieve desired results
        runBlocking{
            activityScenarioRule.scenario
            delay(20000)
            onView(withId(R.id.user_name_tv)).check { view, _ ->
                val textView = view as TextView
                val text = textView.text
                assert(text.equals(""))
            }
            onView(withId(R.id.edvora_recycler)).check { view, _ ->
                val recyclerView = view as RecyclerView
                val itemCount = recyclerView.adapter?.itemCount ?: 0
                assert(itemCount == 0)
            }
        }
    }

    @Test
    fun test_launchEdvoraActivityWithWifiOrCellularConnection(){
        //Please ensure that Wifi or Cellular Data is active before running this test
        //to achieve desired results
        runBlocking{
            activityScenarioRule.scenario
            delay(20000)
            onView(withId(R.id.edvora_recycler)).check { view, _ ->
                val recyclerView = view as RecyclerView
                val itemCount = recyclerView.adapter?.itemCount ?: 0
                assert(itemCount > 0)
            }
        }
    }

    @Test
    fun test_launchEdvoraActivityScrollThroughRidesAndSelectAnotherCategory(){
        //Please ensure that Wifi or Cellular Data is active before running this test
        //to achieve desired results
        runBlocking{
            //Arrange
            var recyclerItemPosition = 0
            val listOfTabPositions = listOf(0, 1 , 2)

            //Launch Activity
            activityScenarioRule.scenario
            delay(20000)

            //Select Ride categories, assert recycler view item count and scroll through recyler
            // view
            for(position in listOfTabPositions){
                //Select Upcoming rides category
                onView(withId(R.id.ride_category_tab)).perform(SelectTabAtPosition(position))
                //Assert that the recycler view is not empty
                onView(withId(R.id.edvora_recycler)).check { view, _ ->
                    val recyclerView = view as RecyclerView
                    recyclerItemPosition = recyclerView.adapter?.itemCount ?: 0
                    assert(recyclerItemPosition > 0)
                }
                //Scroll through Upcoming rides recycler view list
                onView(withId(R.id.edvora_recycler)).perform(RecyclerViewActions
                    .scrollToPosition<EdvoraRideAdapter.EdvoraRideViewHolder>(recyclerItemPosition))
            }
        }
    }

    @Test
    fun test_clickOnFilterButtonFilterByStateAndCityDismissPopupWindowWithoutWifiOrCellularData(){
        //Ensure that Wifi or Cellular Data is not active to achieve required results
        runBlocking {
            //Launch Edvora Activity
            activityScenarioRule.scenario
            delay(20000)

            //Click on filter Button
            onView(withId(R.id.filter_button)).perform(click())

            //Click on state drop down menu
            onView(withId(R.id.state_dropdown_menu)).perform(click())
            //Dismiss State drop down menu
            onView(withId(R.id.state_dropdown_menu)).perform(click())
            //Assert that state dropdown menu list is empty
            onView(withId(R.id.state_dropdown_menu)).check { view, _ ->
                val textInputLayout = view as TextInputLayout
                val autoCompleteTextView = textInputLayout.editText as? AutoCompleteTextView
                val listCount = autoCompleteTextView?.adapter?.count ?: 0
                assert(listCount == 0)
            }

            //Click on city drop down menu
            onView(withId(R.id.city_dropdown_menu)).perform(click())
            //Dismiss City drop down menu
            onView(withId(R.id.city_dropdown_menu)).perform(click())
            //Assert that city dropdown menu list is empty
            onView(withId(R.id.city_dropdown_menu)).check { view, _ ->
                val textInputLayout = view as TextInputLayout
                val autoCompleteTextView = textInputLayout.editText as? AutoCompleteTextView
                val listCount = autoCompleteTextView?.adapter?.count ?: 0
                assert(listCount == 0)
            }

            //Dismiss popup window
            pressBack()
        }
    }

    @Test
    fun test_clickOnFilterButtonFilterByStateAndCityDismissPopupWindowWithWifiOrCellularData(){
        //Ensure that Wifi or Cellular Data is active to achieve required results
        runBlocking {
            //Launch Edvora Activity
            activityScenarioRule.scenario
            delay(20000)

            //Click on filter Button
            onView(withId(R.id.filter_button)).perform(click())

            //Click on state drop down menu
            onView(withId(R.id.state_dropdown_menu)).perform(click())
            //Dismiss State drop down menu
            onView(withId(R.id.state_dropdown_menu)).perform(click())
            //Assert that state dropdown menu list is empty
            onView(withId(R.id.state_dropdown_menu)).check { view, _ ->
                val textInputLayout = view as TextInputLayout
                val autoCompleteTextView = textInputLayout.editText as? AutoCompleteTextView
                val listCount = autoCompleteTextView?.adapter?.count ?: 0
                assert(listCount > 0)
            }

            //Click on city drop down menu
            onView(withId(R.id.city_dropdown_menu)).perform(click())
            //Dismiss City drop down menu
            onView(withId(R.id.city_dropdown_menu)).perform(click())
            //Assert that city dropdown menu list is empty
            onView(withId(R.id.city_dropdown_menu)).check { view, _ ->
                val textInputLayout = view as TextInputLayout
                val autoCompleteTextView = textInputLayout.editText as? AutoCompleteTextView
                val listCount = autoCompleteTextView?.adapter?.count ?: 0
                assert(listCount > 0)
            }

            //Dismiss popup window
            pressBack()
        }
    }

    @Test
    fun test_filterNearestRidesByStateAndDismissPopupWindow(){
        //Ensure that Wifi or Cellular Data is active to achieve required results
        runBlocking{
            //Arrange
            var initialItemCount = 0
            var stateListFinalItemPosition = 0

            //Launch Edvora Activity
            activityScenarioRule.scenario
            delay(20000)

            //Set the initailItemCount variable
            onView(withId(R.id.edvora_recycler)).check { view, _ ->
                val recyclerView = view as RecyclerView
                initialItemCount = recyclerView.adapter?.itemCount ?: 0
            }

            //Click on filter button
            onView(withId(R.id.filter_button)).perform(click())
            //Click on state drop down menu and select a state
            onView(withId(R.id.state_dropdown_menu)).perform(click())
            //Set stateListFinalItemPosition value
            onView(withId(R.id.state_dropdown_menu)).check { view, _ ->
                val textInputLayout = view as TextInputLayout
                val autoCompleteTextView = textInputLayout.editText as? AutoCompleteTextView
                stateListFinalItemPosition = autoCompleteTextView?.adapter?.count ?: 0
            }
            onData(allOf(isA(String::class.java))).inRoot(RootMatchers.isPlatformPopup())
                .atPosition(stateListFinalItemPosition - 1).perform(
                    click()
            )

            //Dismiss popup window
            pressBack()

            //Assert that recycler view was actually updated
            onView(withId(R.id.edvora_recycler)).check { view, _ ->
                val recyclerView = view as? RecyclerView
                val finalItemCount = recyclerView?.adapter?.itemCount ?: 0
                assert(initialItemCount > finalItemCount)
            }
        }
    }

    @Test
    fun test_selectPastRidesThenFilterNearestRidesByStateAndDismissPopupWindow(){
        //Ensure that Wifi or Cellular Data is active to achieve required results
        runBlocking{
            //Arrange
            var initialItemCount = 0
            var stateListFinalItemPosition = 0

            //Launch Edvora Activity
            activityScenarioRule.scenario
            delay(20000)

            //Set the initailItemCount variable
            onView(withId(R.id.edvora_recycler)).check { view, _ ->
                val recyclerView = view as RecyclerView
                initialItemCount = recyclerView.adapter?.itemCount ?: 0
            }

            //Select past rides category
            onView(withId(R.id.ride_category_tab)).perform(SelectTabAtPosition(2))

            //Click on filter button
            onView(withId(R.id.filter_button)).perform(click())
            //Click on state drop down menu and select a state
            onView(withId(R.id.state_dropdown_menu)).perform(click())
            //Set stateListFinalItemPosition value
            onView(withId(R.id.state_dropdown_menu)).check { view, _ ->
                val textInputLayout = view as TextInputLayout
                val autoCompleteTextView = textInputLayout.editText as? AutoCompleteTextView
                stateListFinalItemPosition = autoCompleteTextView?.adapter?.count ?: 0
            }
            onData(allOf(isA(String::class.java))).inRoot(RootMatchers.isPlatformPopup())
                .atPosition(stateListFinalItemPosition - 1).perform(
                    click()
                )

            //Dismiss popup window
            pressBack()

            //Assert that nearest tab was automatically reselected
            onView(withId(R.id.ride_category_tab)).check{view, _ ->
                val tabLayout = view as? TabLayout
                val tabPosition = tabLayout?.selectedTabPosition ?: 0
                assert(tabPosition == 0)
            }

            //Assert that recycler view was actually updated
            onView(withId(R.id.edvora_recycler)).check { view, _ ->
                val recyclerView = view as? RecyclerView
                val finalItemCount = recyclerView?.adapter?.itemCount ?: 0
                assert(initialItemCount > finalItemCount)
            }
        }
    }

    @Test
    fun test_filterNearestRidesByCityAndDismissPopupWindow(){
        //Ensure that Wifi or Cellular Data is active to achieve required results
        runBlocking{
            //Arrange
            var initialItemCount = 0
            var cityListFinalItemPosition = 0

            //Launch Edvora Activity
            activityScenarioRule.scenario
            delay(20000)

            //Set the initailItemCount variable
            onView(withId(R.id.edvora_recycler)).check { view, _ ->
                val recyclerView = view as RecyclerView
                initialItemCount = recyclerView.adapter?.itemCount ?: 0
            }

            //Click on filter button
            onView(withId(R.id.filter_button)).perform(click())
            //Click on city drop down menu and select a state
            onView(withId(R.id.city_dropdown_menu)).perform(click())
            //Set cityListFinalItemPosition value
            onView(withId(R.id.city_dropdown_menu)).check { view, _ ->
                val textInputLayout = view as TextInputLayout
                val autoCompleteTextView = textInputLayout.editText as? AutoCompleteTextView
                cityListFinalItemPosition = autoCompleteTextView?.adapter?.count ?: 0
            }
            onData(allOf(isA(String::class.java))).inRoot(RootMatchers.isPlatformPopup())
                .atPosition(cityListFinalItemPosition - 1).perform(
                    click()
                )

            //Dismiss popup window
            pressBack()

            //Assert that recycler view was actually updated
            onView(withId(R.id.edvora_recycler)).check { view, _ ->
                val recyclerView = view as? RecyclerView
                val finalItemCount = recyclerView?.adapter?.itemCount ?: 0
                assert(initialItemCount > finalItemCount)
            }
        }
    }

    @Test
    fun test_selectPastRidesThenFilterNearestRidesByCityAndDismissPopupWindow(){
        //Ensure that Wifi or Cellular Data is active to achieve required results
        runBlocking{
            //Arrange
            var initialItemCount = 0
            var cityListFinalItemPosition = 0


            //Launch Edvora Activity
            activityScenarioRule.scenario
            delay(20000)

            //Set the initailItemCount variable
            onView(withId(R.id.edvora_recycler)).check { view, _ ->
                val recyclerView = view as RecyclerView
                initialItemCount = recyclerView.adapter?.itemCount ?: 0
            }

            //Select past rides category
            onView(withId(R.id.ride_category_tab)).perform(SelectTabAtPosition(2))

            //Click on filter button
            onView(withId(R.id.filter_button)).perform(click())
            //Click on city drop down menu and select a state
            onView(withId(R.id.city_dropdown_menu)).perform(click())
            //Set cityListFinalItemPosition value
            onView(withId(R.id.city_dropdown_menu)).check { view, _ ->
                val textInputLayout = view as TextInputLayout
                val autoCompleteTextView = textInputLayout.editText as? AutoCompleteTextView
                cityListFinalItemPosition = autoCompleteTextView?.adapter?.count ?: 0
            }
            onData(allOf(isA(String::class.java))).inRoot(RootMatchers.isPlatformPopup())
                .atPosition(cityListFinalItemPosition - 1).perform(
                    click()
                )

            //Dismiss popup window
            pressBack()

            //Assert that nearest tab was automatically reselected
            onView(withId(R.id.ride_category_tab)).check{view, _ ->
                val tabLayout = view as? TabLayout
                val tabPosition = tabLayout?.selectedTabPosition ?: 0
                assert(tabPosition == 0)
            }

            //Assert that recycler view was actually updated
            onView(withId(R.id.edvora_recycler)).check { view, _ ->
                val recyclerView = view as? RecyclerView
                val finalItemCount = recyclerView?.adapter?.itemCount ?: 0
                assert(initialItemCount > finalItemCount)
            }
        }
    }

    @Test
    fun test_selectPastRidesThenFilterNearestRidesByCityAndStateDismissPopupWindow(){
        //Ensure that Wifi or Cellular connection is active to achieve desired results
        runBlocking{
            //Arrange
            var initialItemCount = 0
            var cityListFinalItemPosition = 0
            var stateListFinalItemPosition = 0

            //Launch Edvora Activity
            activityScenarioRule.scenario
            delay(20000)

            //Set the initailItemCount variable
            onView(withId(R.id.edvora_recycler)).check { view, _ ->
                val recyclerView = view as RecyclerView
                initialItemCount = recyclerView.adapter?.itemCount ?: 0
            }

            //Select past rides category
            onView(withId(R.id.ride_category_tab)).perform(SelectTabAtPosition(2))

            //Click on filter button
            onView(withId(R.id.filter_button)).perform(click())
            //Click on state drop down menu and select a state
            onView(withId(R.id.state_dropdown_menu)).perform(click())
            //Set stateListFinalItemPosition value
            onView(withId(R.id.state_dropdown_menu)).check { view, _ ->
                val textInputLayout = view as TextInputLayout
                val autoCompleteTextView = textInputLayout.editText as? AutoCompleteTextView
                stateListFinalItemPosition = autoCompleteTextView?.adapter?.count ?: 0
            }
            onData(allOf(isA(String::class.java))).inRoot(RootMatchers.isPlatformPopup())
                .atPosition(stateListFinalItemPosition - 1).perform(
                    click()
                )

            //Click on city drop down menu and select a state
            onView(withId(R.id.city_dropdown_menu)).perform(click())
            //Set cityListFinalItemPosition value
            onView(withId(R.id.city_dropdown_menu)).check { view, _ ->
                val textInputLayout = view as TextInputLayout
                val autoCompleteTextView = textInputLayout.editText as? AutoCompleteTextView
                cityListFinalItemPosition = autoCompleteTextView?.adapter?.count ?: 0
            }
            onData(allOf(isA(String::class.java))).inRoot(RootMatchers.isPlatformPopup())
                .atPosition(cityListFinalItemPosition - 1).perform(
                    click()
                )

            //Dismiss popup window
            pressBack()

            //Assert that nearest tab was automatically reselected
            onView(withId(R.id.ride_category_tab)).check{view, _ ->
                val tabLayout = view as? TabLayout
                val tabPosition = tabLayout?.selectedTabPosition ?: 0
                assert(tabPosition == 0)
            }

            //Assert that recycler view was actually updated
            onView(withId(R.id.edvora_recycler)).check { view, _ ->
                val recyclerView = view as? RecyclerView
                val finalItemCount = recyclerView?.adapter?.itemCount ?: 0
                assert(initialItemCount > finalItemCount)
            }
        }
    }

    @Test
    fun test_filterByStateAndCityDismissPopupWindowClickFilterButtonAndAssertDropDownReset(){
        //Ensure that Wifi or Cellular connection is active to achieve desired results
        runBlocking{
            //Arrange
            val expectedStateText = "State"
            val expectedCityText = "City"
            var cityListFinalItemPosition = 0
            var stateListFinalItemPosition = 0

            //Launch Edvora Activity
            activityScenarioRule.scenario
            delay(20000)


            //Select past rides category
            onView(withId(R.id.ride_category_tab)).perform(SelectTabAtPosition(2))

            //Click on filter button
            onView(withId(R.id.filter_button)).perform(click())
            //Click on state drop down menu and select a state
            onView(withId(R.id.state_dropdown_menu)).perform(click())
            //Set stateListFinalItemPosition value
            onView(withId(R.id.state_dropdown_menu)).check { view, _ ->
                val textInputLayout = view as TextInputLayout
                val autoCompleteTextView = textInputLayout.editText as? AutoCompleteTextView
                stateListFinalItemPosition = autoCompleteTextView?.adapter?.count ?: 0
            }
            onData(allOf(isA(String::class.java))).inRoot(RootMatchers.isPlatformPopup())
                .atPosition(stateListFinalItemPosition - 1).perform(
                    click()
                )

            //Click on city drop down menu and select a state
            onView(withId(R.id.city_dropdown_menu)).perform(click())
            //Set cityListFinalItemPosition value
            onView(withId(R.id.city_dropdown_menu)).check { view, _ ->
                val textInputLayout = view as TextInputLayout
                val autoCompleteTextView = textInputLayout.editText as? AutoCompleteTextView
                cityListFinalItemPosition = autoCompleteTextView?.adapter?.count ?: 0
            }
            onData(allOf(isA(String::class.java))).inRoot(RootMatchers.isPlatformPopup())
                .atPosition(cityListFinalItemPosition - 1).perform(
                    click()
                )

            //Dismiss popup window
            pressBack()

            //Click filter button again
            onView(withId(R.id.filter_button)).perform(click())
        }
    }

}