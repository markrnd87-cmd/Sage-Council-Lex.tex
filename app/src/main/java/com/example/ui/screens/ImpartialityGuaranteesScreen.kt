package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.engine.SsmBackendEngine
import com.example.model.ImpartialityGuarantee
import com.example.model.StakeholderRole
import com.example.ui.theme.SafetyPauseContainerLight
import com.example.ui.theme.SafetyPauseCrimsonLight

@Composable
fun ImpartialityGuaranteesScreen(
    guarantees: List<ImpartialityGuarantee>,
    onToggleGuarantee: (String) -> Unit,
    onRestoreAllGuarantees: () -> Unit,
    onBackToSeeker: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBackToSeeker)

    var showBackendMapping by rememberSaveable { mutableStateOf(false) }
    val upheldCount = guarantees.count { it.isUpheld }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 840.dp)
                .testTag("guarantees_lazy_column"),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Section 1: Stakeholder Contract
            item {
                StakeholderContractSection(roles = SsmBackendEngine.stakeholderRoles)
            }

            // Section 5 Header: Nine Impartiality Guarantees
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (upheldCount == 9) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            SafetyPauseContainerLight
                        }
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
                                Icon(
                                    imageVector = if (upheldCount == 9) Icons.Default.VerifiedUser else Icons.Default.WarningAmber,
                                    contentDescription = null,
                                    tint = if (upheldCount == 9) MaterialTheme.colorScheme.primary else SafetyPauseCrimsonLight
                                )
                                Text(
                                    text = "Nine Impartiality Guarantees ($upheldCount/9 Active)",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = if (upheldCount == 9) {
                                        MaterialTheme.colorScheme.onPrimaryContainer
                                    } else {
                                        SafetyPauseCrimsonLight
                                    }
                                )
                            }
                        }

                        Text(
                            text = "Appendix A of the Codex defines Nine Pillars as Lagrangian sector constraints. In Sage Counsel, these become plain-language Impartiality Guarantees protecting the Seeker and Affected Parties. Toggle any guarantee off below to verify Theorem P2 (any breached guarantee immediately triggers a SafetyPause).",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (upheldCount == 9) {
                                MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.9f)
                            } else {
                                SafetyPauseCrimsonLight
                            }
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedButton(
                                onClick = { showBackendMapping = !showBackendMapping },
                                modifier = Modifier.testTag("toggle_backend_pillar_mapping_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Code,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = if (showBackendMapping) "Hide Backend Pillars" else "Inspect Hidden Pillars",
                                    modifier = Modifier.padding(start = 6.dp)
                                )
                            }

                            if (upheldCount < 9) {
                                Button(
                                    onClick = onRestoreAllGuarantees,
                                    modifier = Modifier.testTag("restore_all_guarantees_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "Restore 9/9",
                                        modifier = Modifier.padding(start = 6.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Guarantee Cards (1..9)
            items(guarantees, key = { it.id }) { guarantee ->
                GuaranteeItemCard(
                    guarantee = guarantee,
                    showBackendMapping = showBackendMapping,
                    onToggle = { onToggleGuarantee(guarantee.id) }
                )
            }
        }
    }
}

@Composable
private fun StakeholderContractSection(roles: List<StakeholderRole>) {
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Groups,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Column {
                    Text(
                        text = "PRODUCT IDENTITY & STAKEHOLDER CONTRACT",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Who Sage Counsel Serves",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            roles.forEachIndexed { idx, role ->
                if (idx > 0) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                }
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = role.title,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = MaterialTheme.colorScheme.secondaryContainer
                        ) {
                            Text(
                                text = role.replacesCodexTerm,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                    Text(
                        text = role.coreResponsibility,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Product Guarantee: ${role.keyPromise}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun GuaranteeItemCard(
    guarantee: ImpartialityGuarantee,
    showBackendMapping: Boolean,
    onToggle: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (guarantee.isUpheld) {
                MaterialTheme.colorScheme.surface
            } else {
                SafetyPauseContainerLight
            }
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (guarantee.isUpheld) {
                MaterialTheme.colorScheme.outlineVariant
            } else {
                SafetyPauseCrimsonLight
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onToggle)
            .testTag("guarantee_card_${guarantee.id}")
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = if (guarantee.isUpheld) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            SafetyPauseCrimsonLight
                        },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "${guarantee.number}",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                    Column {
                        Text(
                            text = guarantee.productGuarantee,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (guarantee.isUpheld) "Verified & Upheld" else "Breach Simulated (Halts Counsel)",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (guarantee.isUpheld) MaterialTheme.colorScheme.primary else SafetyPauseCrimsonLight
                        )
                    }
                }
                Switch(
                    checked = guarantee.isUpheld,
                    onCheckedChange = { onToggle() },
                    modifier = Modifier.testTag("guarantee_switch_${guarantee.id}")
                )
            }

            Text(
                text = guarantee.userPromise,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Affected Party Assurance: ${guarantee.affectedPartyProtection}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            AnimatedVisibility(visible = showBackendMapping) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = "Hidden Backend Pillar: ${guarantee.hiddenBackendPillar}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = guarantee.hiddenBackendMechanism,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
