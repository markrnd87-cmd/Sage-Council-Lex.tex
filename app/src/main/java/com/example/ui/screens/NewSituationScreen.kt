package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.engine.SsmBackendEngine
import com.example.model.ConflictPreference
import com.example.model.GoalMode
import com.example.model.ImpactScale
import com.example.model.ResolutionStatus
import com.example.model.SituationInput
import com.example.ui.components.SituationInputForm
import com.example.ui.theme.ClearCounselContainerLight
import com.example.ui.theme.ClearCounselEmeraldLight
import com.example.ui.theme.CompromiseAmberLight
import com.example.ui.theme.CompromiseContainerLight
import com.example.ui.theme.SafetyPauseContainerLight
import com.example.ui.theme.SafetyPauseCrimsonLight

private data class PromptStarter(
    val id: String,
    val label: String,
    val input: SituationInput
)

private val quickStarters = listOf(
    PromptStarter(
        id = "role_boundary",
        label = "Role & Ownership Split",
        input = SituationInput(
            description = "Two leads on our product launch disagree on who owns final sign-off for customer-facing release notes versus technical readiness criteria, causing last-minute delays.",
            goal = GoalMode.StabilizeExisting,
            scale = ImpactScale.PersonalOrTeam,
            onConflict = ConflictPreference.OfferCompromise
        )
    ),
    PromptStarter(
        id = "timeline_crunch",
        label = "Aggressive Deadline vs. Scope",
        input = SituationInput(
            description = "Executive sponsors asked us to cut our quarterly delivery timeline in half without reducing any planned features and without adding stress or overtime for the team.",
            goal = GoalMode.CreateNewPath,
            scale = ImpactScale.OrganizationWide,
            onConflict = ConflictPreference.OfferCompromise
        )
    ),
    PromptStarter(
        id = "community_policy",
        label = "Community Resource Charter",
        input = SituationInput(
            description = "Our neighborhood coalition and local business owners need a fair charter for shared street-space hours that balances quiet residential evenings with storefront foot traffic.",
            goal = GoalMode.CreateNewPath,
            scale = ImpactScale.CommunityOrEcosystem,
            onConflict = ConflictPreference.OfferCompromise
        )
    )
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NewSituationScreen(
    draftInput: SituationInput,
    hasSubmittedDraft: Boolean,
    draftEvaluation: SsmBackendEngine.EvaluationBundle,
    onDraftInputChange: (SituationInput) -> Unit,
    onResetDraft: () -> Unit,
    onSubmitNewSituation: () -> Unit,
    onSubmitAndOpenCounselTab: () -> Unit,
    onRestoreGuarantees: () -> Unit,
    onBackToCounsel: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBackToCounsel)

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 840.dp)
                .testTag("new_situation_lazy_column"),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Header Card for Inputting a New Situation
            item {
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.AddCircleOutline,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onPrimary,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                                Column {
                                    Text(
                                        text = "NEW SITUATION INTAKE · SEEKER FORM",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = "Input a New Situation",
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }

                            OutlinedButton(
                                onClick = onResetDraft,
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                modifier = Modifier.testTag("reset_new_situation_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.RestartAlt,
                                    contentDescription = "Reset blank form",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Reset Blank", style = MaterialTheme.typography.labelMedium)
                            }
                        }

                        Text(
                            text = "Describe your own dilemma from scratch or pick a quick template below, set your GoalMode, ImpactScale, and ConflictPreference, and generate balanced multi-perspective counsel.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.9f)
                        )

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            quickStarters.forEach { starter ->
                                AssistChip(
                                    onClick = { onDraftInputChange(starter.input) },
                                    label = { Text(starter.label) },
                                    modifier = Modifier.testTag("starter_chip_${starter.id}")
                                )
                            }
                        }
                    }
                }
            }

            // 2. The SituationInputForm Composable
            item {
                SituationInputForm(
                    input = draftInput,
                    onInputChange = onDraftInputChange,
                    blindSpotPassed = draftEvaluation.blindSpotPassed,
                    blindSpotNotice = if (draftInput.description.isBlank()) {
                        "Enter a plain-language description of your situation above to run BlindSpotCheck and frame your guidance."
                    } else {
                        draftEvaluation.blindSpotNotice
                    },
                    guaranteesUpheld = draftEvaluation.guaranteesUpheldCount,
                    onRestoreGuarantees = onRestoreGuarantees
                )
            }

            // 3. Primary Action Bar (Generate Counsel & Save / View in Counsel Workspace)
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = onSubmitNewSituation,
                                enabled = draftInput.description.isNotBlank(),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("generate_new_situation_counsel_button"),
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
                                Text("Generate Balanced Counsel")
                            }

                            OutlinedButton(
                                onClick = onSubmitAndOpenCounselTab,
                                enabled = draftInput.description.isNotBlank(),
                                modifier = Modifier.testTag("open_in_counsel_workspace_button")
                            ) {
                                Text("Save & Open Workspace")
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        if (draftInput.description.isBlank()) {
                            Text(
                                text = "Tip: Type at least one sentence describing the situation (or tap a starter chip above) to enable counsel generation.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // 4. Live Counsel Output Preview once generated
            item {
                AnimatedVisibility(visible = hasSubmittedDraft && draftInput.description.isNotBlank()) {
                    val output = draftEvaluation.counselOutput
                    val (bannerBg, bannerAccent) = when (output.status) {
                        ResolutionStatus.ClearCounsel -> ClearCounselContainerLight to ClearCounselEmeraldLight
                        ResolutionStatus.CompromiseReached -> CompromiseContainerLight to CompromiseAmberLight
                        ResolutionStatus.PausedForSafety -> SafetyPauseContainerLight to SafetyPauseCrimsonLight
                    }

                    Card(
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.5.dp, bannerAccent.copy(alpha = 0.6f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("new_situation_result_card")
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(bannerBg)
                                    .padding(16.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Surface(
                                        shape = RoundedCornerShape(50),
                                        color = bannerAccent
                                    ) {
                                        Text(
                                            text = "${output.status.productTerm} · ${output.status.badgeLabel}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                        )
                                    }
                                    Text(
                                        text = output.status.cleanTitle,
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = bannerAccent
                                    )
                                }
                            }

                            Column(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = output.summary,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                                Text(
                                    text = "AFFECTED PARTY PERSPECTIVES (${output.perspectives.size})",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                output.perspectives.forEach { p ->
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(12.dp),
                                            verticalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Text(
                                                text = p.stakeholderView,
                                                style = MaterialTheme.typography.titleSmall,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = "Need: ${p.coreNeed}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = "Potential Bias: ${p.potentialBias}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }

                                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                                Text(
                                    text = "RECOMMENDED NEXT STEPS (${output.nextSteps.size})",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                output.nextSteps.forEachIndexed { idx, step ->
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(12.dp),
                                            verticalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Text(
                                                text = "${idx + 1}. ${step.recommendation}",
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.SemiBold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Text(
                                                text = "Benefit: ${step.benefit} · Trade-Off: ${step.tradeOff}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }

                                Button(
                                    onClick = onSubmitAndOpenCounselTab,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 16.dp)
                                        .testTag("save_and_view_full_counsel_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.BookmarkAdded,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Log Consultation & Open in Counsel Workspace")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
