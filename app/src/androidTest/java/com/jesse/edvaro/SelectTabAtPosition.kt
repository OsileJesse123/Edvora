package com.jesse.edvaro

import android.view.View
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.google.android.material.tabs.TabLayout
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf

//This is a custom ViewAction which can be used to select a tab from the TabLayout.
class SelectTabAtPosition(private val tabIndex: Int): ViewAction {
    override fun getConstraints(): Matcher<View> = allOf(isDisplayed(),
        isAssignableFrom(TabLayout::class.java))

    override fun getDescription() = "with tab at index $tabIndex"

    override fun perform(uiController: UiController?, view: View?) {
        val tabLayout = view as TabLayout
        val tabAtIndex: TabLayout.Tab = tabLayout.getTabAt(tabIndex) ?: throw PerformException.Builder()
            .withCause(Throwable("No tab at index $tabIndex"))
            .build()
        tabAtIndex.select()
    }

}