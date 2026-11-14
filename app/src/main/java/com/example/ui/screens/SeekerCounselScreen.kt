package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.engine.SsmBackendEngine
import com.example.model.AcceptanceScenario
import com.example.model.ActionStep
import com.example.model.ConflictPreference
import com.example.model.GoalMode
import com.example.model.ImpactScale
import com.example.model.PerspectiveItem
import com.example.model.ReflectionOutcome
import com.example.model.ResolutionStatus
import com.example.ui.AppTab
import com.example.ui.SageCounselUiState
import com.example.ui.components.SituationInputForm
import com.example.ui.theme.ClearCounselContainerLight
import com.example.ui.theme.ClearCounselEmeraldLight
import com.example.ui.theme.CompromiseAmberLight
import com.example.ui.theme.CompromiseContainerLight
import com.example.ui.theme.SafetyPauseContainerLight
import com.example.ui.theme.SafetyPauseCrimsonLight

@Composable
fun SeekerCounselScreen(
    uiState: SageCounselUiState,
    onLoadScenario: (AcceptanceScenario) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onSelectGoal: (GoalMode) -> Unit,
    onSelectScale: (ImpactScale) -> Unit,
    onSelectConflict: (ConflictPreference) -> Unit,
    onRestoreGuarantees: () -> Unit,
    onSelectReflectionOutcome: (ReflectionOutcome) -> Unit,
    onReflectionNoteChange: (String) -> Unit,
    onSaveReflection: () -> Unit,
    onNavigateToTab: (AppTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val eval = uiState.evaluation
    val output = eval.counselOutput

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 840.dp)
                .testTag("seeker_lazy_column"),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // 1. Editorial Hero Card
            item {
                SeekerHeroBanner(
                    guaranteesUpheld = eval.guaranteesUpheldCount,
                    onOpenNewSituation = { onNavigateToTab(AppTab.NewSituation) },
                    onInspectGuarantees = { onNavigateToTab(AppTab.Guarantees) },
                    onInspectFirewall = { onNavigateToTab(AppTab.Firewall) }
                )
            }

            // 2. Replayable Acceptance Scenarios (Section 6)
            item {
                ScenarioPickerSection(
                    scenarios = SsmBackendEngine.acceptanceScenarios,
                    selectedScenarioId = uiState.selectedScenarioId,
                    onSelectScenario = onLoadScenario
                )
            }

            // 3. Unified SituationInputForm (SituationSummary + GoalMode, ImpactScale, ConflictPreference)
            item {
                SituationInputForm(
                    description = uiState.situationDescription,
                    selectedGoal = uiState.selectedGoal,
                    selectedScale = uiState.selectedScale,
                    selectedConflictPreference = uiState.selectedConflictPreference,
                    onDescriptionChange = onDescriptionChange,
                    onSelectGoal = onSelectGoal,
                    onSelectScale = onSelectScale,
                    onSelectConflictPreference = onSelectConflict,
                    blindSpotPassed = eval.blindSpotPassed,
                    blindSpotNotice = eval.blindSpotNotice,
                    guaranteesUpheld = eval.guaranteesUpheldCount,
                    onRestoreGuarantees = onRestoreGuarantees
                )
            }

            // 4. Counsel Output Surface (BalancedCounsel / AlternativePath / SafetyPause)
            item {
                CounselOutputCard(
                    status = output.status,
                    summary = output.summary,
                    rationale = output.rationale,
                    perspectives = output.perspectives,
                    nextSteps = output.nextSteps,
                    guaranteesUpheld = eval.guaranteesUpheldCount,
                    onNavigateToGuarantees = { onNavigateToTab(AppTab.Guarantees) }
                )
            }

            // 5. Outcome Reflection (Replaces Kintsugi CycleLog)
            item {
                OutcomeReflectionCard(
                    selectedOutcome = uiState.selectedReflectionOutcome,
                    draftNote = uiState.reflectionDraftNote,
                    onSelectOutcome = onSelectReflectionOutcome,
                    onDraftNoteChange = onReflectionNoteChange,
                    onSaveReflection = onSaveReflection,
                    onOpenGovernance = { onNavigateToTab(AppTab.Governance) }
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SeekerHeroBanner(
    guaranteesUpheld: Int,
    onOpenNewSituation: () -> Unit,
    onInspectGuarantees: () -> Unit,
    onInspectFirewall: () -> Unit
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                secondaryColor.copy(alpha = 0.18f),
                                Color.Transparent
                            ),
                            center = Offset(size.width * 0.9f, size.height * 0.2f),
                            radius = size.width * 0.55f
                        )
                    )
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                primaryColor.copy(alpha = 0.12f),
                                Color.Transparent
                            ),
                            center = Offset(size.width * 0.1f, size.height * 0.85f),
                            radius = size.width * 0.45f
                        )
                    )
                }
                .padding(20.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(34.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Balance,
                                contentDescription = "Sage Counsel Emblem",
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    Column {
                        Text(
                            text = "THE SEEKER WORKSPACE · PRISM VERIFIED",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Grounded Socratic Counsel",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                Text(
                    text = "Share a complex dilemma in everyday language, answer three clarifying questions about your goal, scale, and conflict preference, and receive impartial multi-perspective guidance—with zero backend math exposed.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.9f)
                )

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .clickable(onClick = onOpenNewSituation)
                            .testTag("hero_new_situation_chip")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "+ Input New Situation",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(50),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .clickable(onClick = onInspectGuarantees)
                            .testTag("hero_guarantees_chip")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.VerifiedUser,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "$guaranteesUpheld/9 Guarantees Active",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(50),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .clickable(onClick = onInspectFirewall)
                            .testTag("hero_firewall_chip")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Domain Firewall Active",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ScenarioPickerSection(
    scenarios: List<AcceptanceScenario>,
    selectedScenarioId: String,
    onSelectScenario: (AcceptanceScenario) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Column {
            Text(
                text = "Verified Acceptance Journeys",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Tap any replayable scenario to verify ClearCounsel, CompromiseReached, or PausedForSafety",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            scenarios.forEach { scenario ->
                val isSelected = scenario.id == selectedScenarioId
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) {
                            MaterialTheme.colorScheme.secondaryContainer
                        } else {
                            MaterialTheme.colorScheme.surface
                        }
                    ),
                    border = BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.secondary
                        } else {
                            MaterialTheme.colorScheme.outlineVariant
                        }
                    ),
                    modifier = Modifier
                        .width(252.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .clickable { onSelectScenario(scenario) }
                        .testTag("scenario_card_${scenario.id}")
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = scenario.code,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Surface(
                                shape = RoundedCornerShape(50),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Text(
                                    text = scenario.expectedStatus.cleanTitle,
                                    style = MaterialTheme.typography.labelSmall,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Text(
                            text = scenario.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = scenario.subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CounselOutputCard(
    status: ResolutionStatus,
    summary: String,
    rationale: String,
    perspectives: List<PerspectiveItem>,
    nextSteps: List<ActionStep>,
    guaranteesUpheld: Int,
    onNavigateToGuarantees: () -> Unit
) {
    val (bannerBg, bannerAccent) = when (status) {
        ResolutionStatus.ClearCounsel -> ClearCounselContainerLight to ClearCounselEmeraldLight
        ResolutionStatus.CompromiseReached -> CompromiseContainerLight to CompromiseAmberLight
        ResolutionStatus.PausedForSafety -> SafetyPauseContainerLight to SafetyPauseCrimsonLight
    }

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.5.dp, bannerAccent.copy(alpha = 0.55f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("counsel_output_card")
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(bannerBg)
                    .padding(18.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = bannerAccent
                        ) {
                            Text(
                                text = "${status.productTerm} · ${status.badgeLabel}",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                        Text(
                            text = "$guaranteesUpheld/9 Guarantees",
                            style = MaterialTheme.typography.labelSmall,
                            color = bannerAccent,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable(onClick = onNavigateToGuarantees)
                                .padding(4.dp)
                        )
                    }
                    Text(
                        text = status.cleanTitle,
                        style = MaterialTheme.typography.headlineMedium,
                        color = bannerAccent
                    )
                    Text(
                        text = status.userSummary,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF232725)
                    )
                }
            }

            Column(
                modifier = Modifier.padding(horizontal = 18.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "COUNSEL SUMMARY",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = summary,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "MULTI-PERSPECTIVE WEIGHING (THE AFFECTED PARTIES)",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    perspectives.forEach { item ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = item.stakeholderView,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Core Need: ${item.coreNeed}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Watch Out For (Potential Bias): ${item.potentialBias}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "RECOMMENDED ACTION STEPS",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    nextSteps.forEachIndexed { idx, step ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.38f),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = "${idx + 1}",
                                                style = MaterialTheme.typography.labelMedium,
                                                color = MaterialTheme.colorScheme.onPrimary
                                            )
                                        }
                                    }
                                    Text(
                                        text = "Action Step ${idx + 1}",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                Text(
                                    text = step.recommendation,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Benefit: ${step.benefit}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Trade-Off: ${step.tradeOff}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 18.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "IMPARTIALITY RATIONALE",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = rationale,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun OutcomeReflectionCard(
    selectedOutcome: ReflectionOutcome,
    draftNote: String,
    onSelectOutcome: (ReflectionOutcome) -> Unit,
    onDraftNoteChange: (String) -> Unit,
    onSaveReflection: () -> Unit,
    onOpenGovernance: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "STEP 4 · OUTCOME REFLECTION (FEEDBACK LOOP)",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Did this counsel help clarify a fair path?",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Record your OutcomeReflection to save this consultation and, when needed, queue edge cases for the Trust Reviewer.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ReflectionOutcome.entries.forEach { outcome ->
                    FilterChip(
                        selected = outcome == selectedOutcome,
                        onClick = { onSelectOutcome(outcome) },
                        label = { Text(outcome.label) },
                        modifier = Modifier.testTag("reflection_chip_${outcome.name}")
                    )
                }
            }

            OutlinedTextField(
                value = draftNote,
                onValueChange = onDraftNoteChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("reflection_note_input"),
                label = { Text("Optional reflection or note for affected parties / reviewer") },
                placeholder = { Text(selectedOutcome.description) },
                minLines = 2,
                maxLines = 4,
                shape = RoundedCornerShape(12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onSaveReflection,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("save_reflection_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Save Outcome Reflection")
                }

                OutlinedButton(
                    onClick = onOpenGovernance,
                    modifier = Modifier.testTag("view_governance_log_button")
                ) {
                    Text("Reviewer Log")
                }
            }
        }
    }
}
