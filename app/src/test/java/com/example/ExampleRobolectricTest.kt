package com.example

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextReplacement
import androidx.test.core.app.ApplicationProvider
import com.example.model.ConflictPreference
import com.example.model.GoalMode
import com.example.model.ImpactScale
import com.example.model.SituationInput
import com.example.ui.components.SituationInputForm
import com.example.ui.theme.SageCounselTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Sage Counsel", appName)
    }

    @Test
    fun `situationInputForm updates description and taxonomy selections`() {
        var currentInput by mutableStateOf(
            SituationInput(
                description = "Initial situation",
                goal = GoalMode.StabilizeExisting,
                scale = ImpactScale.PersonalOrTeam,
                onConflict = ConflictPreference.OfferCompromise
            )
        )

        composeTestRule.setContent {
            SageCounselTheme {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    SituationInputForm(
                        input = currentInput,
                        onInputChange = { currentInput = it }
                    )
                }
            }
        }

        composeTestRule.onNodeWithTag("situation_input_form").assertIsDisplayed()
        composeTestRule.onNodeWithTag("situation_summary_input")
            .performScrollTo()
            .performTextReplacement("Cross-team roadmap dilemma")
        assertEquals("Cross-team roadmap dilemma", currentInput.description)

        composeTestRule.onNodeWithTag("goal_option_CreateNewPath")
            .performScrollTo()
            .performClick()
        assertEquals(GoalMode.CreateNewPath, currentInput.goal)

        composeTestRule.onNodeWithTag("scale_option_OrganizationWide")
            .performScrollTo()
            .performClick()
        assertEquals(ImpactScale.OrganizationWide, currentInput.scale)

        composeTestRule.onNodeWithTag("conflict_option_PauseAndFlag")
            .performScrollTo()
            .performClick()
        assertEquals(ConflictPreference.PauseAndFlag, currentInput.onConflict)
    }
}
