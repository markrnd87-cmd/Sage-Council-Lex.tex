package com.example

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.model.ConflictPreference
import com.example.model.GoalMode
import com.example.model.ImpactScale
import com.example.model.SituationInput

/**
 * Top-level package entry point for [SituationInputForm] implementing the [SituationInput] data model.
 * Delegates to [com.example.ui.components.SituationInputForm].
 */
@Composable
fun SituationInputForm(
    input: SituationInput,
    onInputChange: (SituationInput) -> Unit,
    modifier: Modifier = Modifier,
    blindSpotPassed: Boolean = true,
    blindSpotNotice: String? = null,
    guaranteesUpheld: Int = 9,
    onRestoreGuarantees: (() -> Unit)? = null
) {
    com.example.ui.components.SituationInputForm(
        input = input,
        onInputChange = onInputChange,
        modifier = modifier,
        blindSpotPassed = blindSpotPassed,
        blindSpotNotice = blindSpotNotice,
        guaranteesUpheld = guaranteesUpheld,
        onRestoreGuarantees = onRestoreGuarantees
    )
}

@Composable
fun SituationInputForm(
    description: String,
    selectedGoal: GoalMode,
    selectedScale: ImpactScale,
    selectedConflictPreference: ConflictPreference,
    onDescriptionChange: (String) -> Unit,
    onSelectGoal: (GoalMode) -> Unit,
    onSelectScale: (ImpactScale) -> Unit,
    onSelectConflictPreference: (ConflictPreference) -> Unit,
    modifier: Modifier = Modifier,
    blindSpotPassed: Boolean = true,
    blindSpotNotice: String? = null,
    guaranteesUpheld: Int = 9,
    onRestoreGuarantees: (() -> Unit)? = null
) {
    com.example.ui.components.SituationInputForm(
        description = description,
        selectedGoal = selectedGoal,
        selectedScale = selectedScale,
        selectedConflictPreference = selectedConflictPreference,
        onDescriptionChange = onDescriptionChange,
        onSelectGoal = onSelectGoal,
        onSelectScale = onSelectScale,
        onSelectConflictPreference = onSelectConflictPreference,
        modifier = modifier,
        blindSpotPassed = blindSpotPassed,
        blindSpotNotice = blindSpotNotice,
        guaranteesUpheld = guaranteesUpheld,
        onRestoreGuarantees = onRestoreGuarantees
    )
}
