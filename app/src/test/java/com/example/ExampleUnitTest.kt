package com.example

import com.example.engine.SsmBackendEngine
import com.example.model.ResolutionStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun scenario1_directBalancedCounsel_returnsClearCounsel() {
        val scenario1 = SsmBackendEngine.acceptanceScenarios.first { it.id == "scenario_1_direct" }
        val result = SsmBackendEngine.evaluate(scenario1.input, SsmBackendEngine.defaultGuarantees)
        assertEquals(ResolutionStatus.ClearCounsel, result.counselOutput.status)
        assertTrue(result.blindSpotPassed)
        assertEquals(9, result.guaranteesUpheldCount)
    }

    @Test
    fun scenario2_compromiseRecovery_returnsCompromiseReached() {
        val scenario2 = SsmBackendEngine.acceptanceScenarios.first { it.id == "scenario_2_compromise" }
        val result = SsmBackendEngine.evaluate(scenario2.input, SsmBackendEngine.defaultGuarantees)
        assertEquals(ResolutionStatus.CompromiseReached, result.counselOutput.status)
    }

    @Test
    fun scenario3_safetyPause_returnsPausedForSafety() {
        val scenario3 = SsmBackendEngine.acceptanceScenarios.first { it.id == "scenario_3_safety" }
        val result = SsmBackendEngine.evaluate(scenario3.input, SsmBackendEngine.defaultGuarantees)
        assertEquals(ResolutionStatus.PausedForSafety, result.counselOutput.status)
        assertFalse(result.blindSpotPassed)
    }

    @Test
    fun guaranteeBreach_alwaysTriggersSafetyPause() {
        val scenario1 = SsmBackendEngine.acceptanceScenarios.first { it.id == "scenario_1_direct" }
        val breachedGuarantees = SsmBackendEngine.defaultGuarantees.mapIndexed { index, g ->
            if (index == 0) g.copy(isUpheld = false) else g
        }
        val result = SsmBackendEngine.evaluate(scenario1.input, breachedGuarantees)
        assertEquals(ResolutionStatus.PausedForSafety, result.counselOutput.status)
        assertEquals(8, result.guaranteesUpheldCount)
    }
}
