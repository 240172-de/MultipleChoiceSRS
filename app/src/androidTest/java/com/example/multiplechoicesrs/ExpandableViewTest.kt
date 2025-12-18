package com.example.multiplechoicesrs

import androidx.compose.material3.Text
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.multiplechoicesrs.view.custom.ExpandableView
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ExpandableViewTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun initTrueContentTest() {
        val content = "Content"

        composeTestRule.setContent {
            ExpandableView(
                title = "Title",
                initialExpandedState = true
            ) {
                Text(content)
            }
        }

        composeTestRule
            .onNodeWithText(content)
            .assertExists()
    }

    @Test
    fun initFalseContentTest() {
        val content = "Content"

        composeTestRule.setContent {
            ExpandableView(
                title = "Title",
                initialExpandedState = false
            ) {
                Text(content)
            }
        }

        composeTestRule
            .onNodeWithText(content)
            .assertDoesNotExist()
    }

    @Test
    fun toggleShowContentTest() {
        val content = "Content"

        composeTestRule.setContent {
            ExpandableView(
                title = "Title",
                initialExpandedState = false
            ) {
                Text(content)
            }
        }

        composeTestRule
            .onNodeWithText(content)
            .assertDoesNotExist()

        //Open
        composeTestRule.onRoot().performClick()

        composeTestRule
            .onNodeWithText(content)
            .assertExists()

        //Close
        composeTestRule.onRoot().performClick()

        composeTestRule
            .onNodeWithText(content)
            .assertDoesNotExist()
    }
}