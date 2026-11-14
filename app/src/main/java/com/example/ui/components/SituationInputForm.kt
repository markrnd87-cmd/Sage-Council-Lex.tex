package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.ConflictPreference
import com.example.model.GoalMode
import com.example.model.ImpactScale
import com.example.model.SituationInput
import com.example.ui.theme.SafetyPauseContainerLight
import com.example.ui.theme.SafetyPauseCrimsonLight

/**
 * Clean Product Taxonomy intake form implementing [SituationInput].
 *
 * Includes an [OutlinedTextField] for `description`, `SingleChoiceSegmentedButtonRow`
 * (`SegmentedButton`) controls for `GoalMode` and `ConflictPreference`, and a
 * `RadioButton` group (`selectableGroup`) for `ImpactScale`.
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
    SituationInputForm(
        description = input.description,
        selectedGoal = input.goal,
        selectedScale = input.scale,
        selectedConflictPreference = input.onConflict,
        onDescriptionChange = { newDesc ->
            onInputChange(input.copy(description = newDesc))
        },
        onSelectGoal = { newGoal ->
            onInputChange(input.copy(goal = newGoal))
        },
        onSelectScale = { newScale ->
            onInputChange(input.copy(scale = newScale))
        },
        onSelectConflictPreference = { newConflict ->
            onInputChange(input.copy(onConflict = newConflict))
        },
        modifier = modifier,
        blindSpotPassed = blindSpotPassed,
        blindSpotNotice = blindSpotNotice,
        guaranteesUpheld = guaranteesUpheld,
        onRestoreGuarantees = onRestoreGuarantees
    )
}

@OptIn(ExperimentalMaterial3Api::class)
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
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("situation_input_form")
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // 1. OutlinedTextField for SituationInput.description (SituationSummary)
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "STEP 1 · SITUATION SUMMARY",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "What situation are you navigating?",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(50),
                        color = if (blindSpotPassed) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            SafetyPauseContainerLight
                        },
                        modifier = Modifier.testTag("blind_spot_status_badge")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Icon(
                                imageVector = if (blindSpotPassed) {
                                    Icons.Default.CheckCircle
                                } else {
                                    Icons.Default.WarningAmber
                                },
                                contentDescription = null,
                                tint = if (blindSpotPassed) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    SafetyPauseCrimsonLight
                                },
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = if (blindSpotPassed) "BlindSpotCheck Clear" else "BlindSpotCheck Flagged",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (blindSpotPassed) {
                                    MaterialTheme.colorScheme.onPrimaryContainer
                                } else {
                                    SafetyPauseCrimsonLight
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = onDescriptionChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("situation_summary_input"),
                    minLines = 4,
                    maxLines = 7,
                    label = { Text("Plain-language description of the dilemma") },
                    placeholder = {
                        Text("Describe who is involved, what tension has emerged, and what outcome you hope to reach…")
                    },
                    trailingIcon = {
                        if (description.isNotEmpty()) {
                            IconButton(
                                onClick = { onDescriptionChange("") },
                                modifier = Modifier.testTag("clear_situation_description_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear situation description"
                                )
                            }
                        }
                    },
                    shape = RoundedCornerShape(14.dp)
                )

                if (blindSpotNotice != null) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = blindSpotNotice,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.weight(1f)
                            )
                            if (guaranteesUpheld < 9 && onRestoreGuarantees != null) {
                                OutlinedButton(
                                    onClick = onRestoreGuarantees,
                                    modifier = Modifier.testTag("restore_guarantees_inline_button"),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text("Restore 9/9", style = MaterialTheme.typography.labelMedium)
                                }
                            }
                        }
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            // 2. Three Socratic Clarifying Questions (GoalMode, ImpactScale, ConflictPreference)
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "STEP 2 · CLARIFYING QUESTIONS (SESSION SCOPE)",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Three Brief Scoping Questions",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Select GoalMode, ImpactScale, and ConflictPreference to frame your guidance.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Control 1: GoalMode (SingleChoiceSegmentedButtonRow + SegmentedButton)
            Column(
                modifier = Modifier.testTag("goal_mode_selector_group"),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CompassCalibration,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "1. Goal Type (GoalMode) — Stabilize or create a new path?",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    GoalMode.entries.forEachIndexed { index, mode ->
                        val isSelected = mode == selectedGoal
                        SegmentedButton(
                            selected = isSelected,
                            onClick = { onSelectGoal(mode) },
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = GoalMode.entries.size
                            ),
                            modifier = Modifier.testTag("goal_option_${mode.name}")
                        ) {
                            Text(
                                text = mode.cleanLabel,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }

                Text(
                    text = "${selectedGoal.name}: ${selectedGoal.subtitle}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Control 2: ImpactScale (RadioGroup using Modifier.selectableGroup() + RadioButton)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectableGroup()
                    .testTag("impact_scale_selector_group"),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Groups,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "2. Decision Scale (ImpactScale) — Who is affected?",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                ImpactScale.entries.forEach { scale ->
                    val isSelected = scale == selectedScale
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.65f)
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                        },
                        border = BorderStroke(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.outlineVariant
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .selectable(
                                selected = isSelected,
                                onClick = { onSelectScale(scale) },
                                role = Role.RadioButton
                            )
                            .testTag("scale_option_${scale.name}")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = null
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = scale.cleanLabel,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "(${scale.name})",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                Text(
                                    text = scale.subtitle,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // Control 3: ConflictPreference (SingleChoiceSegmentedButtonRow + SegmentedButton)
            Column(
                modifier = Modifier.testTag("conflict_preference_selector_group"),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Handshake,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "3. Conflict Handling (ConflictPreference) — If goals clash?",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ConflictPreference.entries.forEachIndexed { index, pref ->
                        val isSelected = pref == selectedConflictPreference
                        SegmentedButton(
                            selected = isSelected,
                            onClick = { onSelectConflictPreference(pref) },
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = ConflictPreference.entries.size
                            ),
                            modifier = Modifier.testTag("conflict_option_${pref.name}")
                        ) {
                            Text(
                                text = pref.cleanLabel,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }

                Text(
                    text = "${selectedConflictPreference.name}: ${selectedConflictPreference.subtitle}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Active SessionScope Summary Footer
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.45f),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("session_scope_summary_bar")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = "ACTIVE SESSION SCOPE (SituationInput)",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = "${selectedGoal.cleanLabel} · ${selectedScale.cleanLabel} · ${selectedConflictPreference.cleanLabel}",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    }
}
