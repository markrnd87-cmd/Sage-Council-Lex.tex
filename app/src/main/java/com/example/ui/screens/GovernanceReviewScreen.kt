package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.CounselSessionEntity
import com.example.model.ResolutionStatus
import com.example.ui.theme.ClearCounselContainerLight
import com.example.ui.theme.ClearCounselEmeraldLight
import com.example.ui.theme.CompromiseAmberLight
import com.example.ui.theme.CompromiseContainerLight
import com.example.ui.theme.SafetyPauseContainerLight
import com.example.ui.theme.SafetyPauseCrimsonLight

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GovernanceReviewScreen(
    sessions: List<CounselSessionEntity>,
    onMarkReviewed: (CounselSessionEntity, String) -> Unit,
    onDeleteSession: (Long) -> Unit,
    onBackToSeeker: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBackToSeeker)

    var filterMode by rememberSaveable { mutableStateOf("ALL") }

    val filteredSessions = when (filterMode) {
        "SAFETY" -> sessions.filter { it.resolutionStatusName == ResolutionStatus.PausedForSafety.name }
        "COMPROMISE" -> sessions.filter { it.resolutionStatusName == ResolutionStatus.CompromiseReached.name }
        else -> sessions
    }

    val safetyCount = sessions.count { it.resolutionStatusName == ResolutionStatus.PausedForSafety.name }
    val compromiseCount = sessions.count { it.resolutionStatusName == ResolutionStatus.CompromiseReached.name }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 840.dp)
                .testTag("governance_lazy_column"),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Card: The Trust Reviewer
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AdminPanelSettings,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary
                            )
                            Text(
                                text = "GOVERNANCE STAKEHOLDER · THE TRUST REVIEWER",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                        Text(
                            text = "Edge-Case Review & Outcome Reflections",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Text(
                            text = "Replaces public-facing 'Council of Nine Stewards' and 'Kintsugi CycleLog' exposure with a clean governance log. Edge-case consultations that trigger a SafetyPause are queued here for human review.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.9f)
                        )

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                selected = filterMode == "ALL",
                                onClick = { filterMode = "ALL" },
                                label = { Text("All Sessions (${sessions.size})") },
                                modifier = Modifier.testTag("governance_filter_all")
                            )
                            FilterChip(
                                selected = filterMode == "SAFETY",
                                onClick = { filterMode = "SAFETY" },
                                label = { Text("SafetyPause Queue ($safetyCount)") },
                                modifier = Modifier.testTag("governance_filter_safety")
                            )
                            FilterChip(
                                selected = filterMode == "COMPROMISE",
                                onClick = { filterMode = "COMPROMISE" },
                                label = { Text("Compromises ($compromiseCount)") },
                                modifier = Modifier.testTag("governance_filter_compromise")
                            )
                        }
                    }
                }
            }

            if (filteredSessions.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(36.dp)
                            )
                            Text(
                                text = "No consultations match this filter yet",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Save an OutcomeReflection from the Seeker tab to populate the Trust Reviewer log.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(filteredSessions, key = { it.id }) { session ->
                    GovernanceSessionCard(
                        session = session,
                        onRatifyReview = {
                            onMarkReviewed(
                                session,
                                "Ratified by Trust Reviewer: Verified that boundary handling and stakeholder protections complied with all 9 Impartiality Guarantees."
                            )
                        },
                        onDelete = { onDeleteSession(session.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun GovernanceSessionCard(
    session: CounselSessionEntity,
    onRatifyReview: () -> Unit,
    onDelete: () -> Unit
) {
    val status = runCatching { ResolutionStatus.valueOf(session.resolutionStatusName) }
        .getOrDefault(ResolutionStatus.ClearCounsel)

    val (badgeBg, badgeColor) = when (status) {
        ResolutionStatus.ClearCounsel -> ClearCounselContainerLight to ClearCounselEmeraldLight
        ResolutionStatus.CompromiseReached -> CompromiseContainerLight to CompromiseAmberLight
        ResolutionStatus.PausedForSafety -> SafetyPauseContainerLight to SafetyPauseCrimsonLight
    }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("governance_session_card_${session.id}")
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = badgeBg
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(
                            imageVector = if (status == ResolutionStatus.PausedForSafety) {
                                Icons.Default.WarningAmber
                            } else {
                                Icons.Default.CheckCircle
                            },
                            contentDescription = null,
                            tint = badgeColor,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "${status.productTerm} (${status.cleanTitle})",
                            style = MaterialTheme.typography.labelSmall,
                            color = badgeColor
                        )
                    }
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.testTag("delete_session_${session.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Delete Session Log",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Text(
                text = session.title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "Scope: ${session.goalModeName} · ${session.impactScaleName} · ${session.conflictPreferenceName}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = session.situationSummary,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            Text(
                text = "Counsel Summary: ${session.counselSummary}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "OutcomeReflection: ${session.outcomeReflectionLabel}",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = session.outcomeReflectionNote,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Trust Reviewer Status: ${session.trustReviewerNote}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            if (!session.isReviewedByTrust) {
                Button(
                    onClick = onRatifyReview,
                    modifier = Modifier.testTag("ratify_session_${session.id}")
                ) {
                    Icon(
                        imageVector = Icons.Default.FactCheck,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Ratify as Trust Reviewer")
                }
            }
        }
    }
}
