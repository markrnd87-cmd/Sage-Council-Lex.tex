package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.engine.SsmBackendEngine
import com.example.model.DomainFirewallRow
import com.example.prism.PrismTexFacetCatalog
import com.example.prism.PrismTheoremProofReport
import com.example.ui.SageCounselUiState
import com.example.ui.theme.ClearCounselContainerLight
import com.example.ui.theme.ClearCounselEmeraldLight
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.SafetyPauseContainerLight
import com.example.ui.theme.SafetyPauseCrimsonLight

@Composable
fun DomainFirewallScreen(
    uiState: SageCounselUiState,
    onSetMutateRuleR1: (Boolean) -> Unit,
    onSetFlipMeasurementMR1: (Boolean) -> Unit = {},
    onBackToSeeker: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBackToSeeker)

    val clipboardManager = LocalClipboardManager.current
    var selectedSpecTab by rememberSaveable { mutableStateOf(0) }
    var selectedFacetIndex by rememberSaveable { mutableStateOf(0) }
    var copiedNotice by rememberSaveable { mutableStateOf<String?>(null) }

    val proofReport = uiState.evaluation.prismProofReport

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 840.dp)
                .testTag("firewall_lazy_column"),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // 1. Domain Firewall & Prism SDK Overview Card
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "PRISM SDK · UOR-FOUNDATION/TEMPLATE · DOMAIN FIREWALL",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Text(
                            text = "Formally Verified Prism App (Java + .lex.tex)",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Built with `${proofReport.sdkSetupCommand}` against `${proofReport.templateRepoUrl}` (${proofReport.templateContractId}). The Java `PrismContractKernel` evaluates the Ordered Decision Table (Rules R1–R6) and exhaustively verifies Theorems P1–P5 across all 32 finite-domain states while isolating SSM 3.0.2 differential geometry behind the Domain Firewall.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.9f)
                        )
                    }
                }
            }

            // 2. Lean 4 Kernel & Anti-Vacuity Gate (`just vv` + P1..P5 Theorems + 4 Gate Stages)
            item {
                AntiVacuityKernelGateCard(
                    proofReport = proofReport,
                    isMutated = uiState.mutateRuleR1ForKernelTest,
                    isMR1Pending = uiState.flipMeasurementMR1ToPending,
                    onSetMutate = onSetMutateRuleR1,
                    onSetFlipMR1 = onSetFlipMeasurementMR1
                )
            }

            // 3. Interactive .lex.tex / .tex / .holo Source & Facet Explorer
            item {
                PrismSpecificationViewerCard(
                    selectedTab = selectedSpecTab,
                    onSelectTab = { selectedSpecTab = it },
                    selectedFacetIndex = selectedFacetIndex,
                    onSelectFacetIndex = { selectedFacetIndex = it },
                    holoCertificateJson = proofReport.holoCertificateJson,
                    copiedNotice = copiedNotice,
                    onCopyCode = { label, code ->
                        clipboardManager.setText(AnnotatedString(code))
                        copiedNotice = "Copied $label to clipboard"
                    }
                )
            }

            // 4. Live Hidden Adapter Inspector (Shows how the current Seeker choices map to internal math)
            item {
                LiveBackendAdapterCard(uiState = uiState)
            }

            // 5. Complete 9-Row Domain Firewall Mapping Table
            item {
                DomainFirewallTableCard(rows = SsmBackendEngine.domainFirewallTable)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AntiVacuityKernelGateCard(
    proofReport: PrismTheoremProofReport,
    isMutated: Boolean,
    isMR1Pending: Boolean,
    onSetMutate: (Boolean) -> Unit,
    onSetFlipMR1: (Boolean) -> Unit
) {
    val gatePassed = proofReport.isOverallGatePassed
    val bannerBg = if (gatePassed) ClearCounselContainerLight else SafetyPauseContainerLight
    val bannerAccent = if (gatePassed) ClearCounselEmeraldLight else SafetyPauseCrimsonLight

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.5.dp, bannerAccent),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("prism_kernel_gate_card")
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
                    imageVector = if (gatePassed) Icons.Default.Lock else Icons.Default.BugReport,
                    contentDescription = null,
                    tint = bannerAccent
                )
                Column {
                    Text(
                        text = "PRISM VERIFICATION GATE (just vv · Java + Lean 4 by decide)",
                        style = MaterialTheme.typography.labelSmall,
                        color = bannerAccent
                    )
                    Text(
                        text = if (gatePassed) {
                            "PASS · ${proofReport.passedExamplesCount}/${proofReport.totalExamplesCount} Examples · ${proofReport.provenTheoremsCount}/${proofReport.totalTheoremsCount} Theorems Verified"
                        } else if (isMutated) {
                            "FAIL · Kernel Contradiction in Theorem P1, P3 & EX-03"
                        } else {
                            "FAIL · Security Measurement M-R1 Pending in Security.lex.tex"
                        },
                        style = MaterialTheme.typography.titleLarge,
                        color = bannerAccent
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = bannerBg,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (gatePassed) {
                        "Java PrismContractKernel verified all 32 finite-domain states across 6 .lex.tex facets (`Contract`, `Architecture`, `Security`, `Quality`, `Operations`, `System`). Signed app.holo digest: sha256:${proofReport.holoDigestSha256.take(16)}…"
                    } else if (isMutated) {
                        "Mutating Rule R1 to allow unsafe requests into ClearCounsel violates Theorem P1 (p1_asuricAlwaysVetoes), Theorem P3 (kaustubhaRequiresDharmaAndSattvic), and Acceptance Scenario EX-03. Lean 4 & Java Kernel reject the build."
                    } else {
                        "Flipping OSCAL Security Measurement M-R1 to Pending causes `lexlean verify` and `prism build-holo` to reject the build in `Security.lex.tex`."
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF1F2421),
                    modifier = Modifier.padding(12.dp)
                )
            }

            // 3-Button Anti-Vacuity Gate Bar (Canonical / Mutate R1 / Flip M-R1)
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onSetMutate(false) },
                    enabled = isMutated || isMR1Pending,
                    modifier = Modifier.testTag("canonical_spec_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Canonical Spec (PASS)")
                }

                OutlinedButton(
                    onClick = { onSetMutate(true) },
                    enabled = !isMutated,
                    modifier = Modifier.testTag("mutate_rule_r1_button")
                ) {
                    Text("Mutate Rule R1 (Test Gate)")
                }

                OutlinedButton(
                    onClick = { onSetFlipMR1(!isMR1Pending) },
                    modifier = Modifier.testTag("flip_measurement_mr1_button")
                ) {
                    Text(
                        if (isMR1Pending) "Restore M-R1 (Verified)" else "Flip Sec M-R1 to Pending"
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            // Formal Theorems P1..P5 Checked by Java Kernel
            Text(
                text = "MACHINE-CHECKED THEOREMS (32 FINITE-DOMAIN VECTORS · by decide)",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )

            proofReport.theoremStatuses.forEach { theorem ->
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    border = BorderStroke(
                        1.dp,
                        if (theorem.isProven) MaterialTheme.colorScheme.outlineVariant else SafetyPauseCrimsonLight
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${theorem.id} · ${theorem.leanName}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = theorem.statement,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            if (theorem.counterexampleSummary != null) {
                                Text(
                                    text = "Counterexample: ${theorem.counterexampleSummary}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = SafetyPauseCrimsonLight
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = if (theorem.isProven) ClearCounselEmeraldLight else SafetyPauseCrimsonLight
                        ) {
                            Text(
                                text = if (theorem.isProven) "PROVEN (${theorem.checkedDomainStates}/32)" else "FAILED",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            // 4-Stage `just vv` Verification Pipeline
            Text(
                text = "WHAT `just vv` VERIFIES IN CI & LOCAL PRISM GATE",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )

            proofReport.gateStages.forEach { stage ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${stage.stepNumber}. ${stage.command}",
                            fontFamily = JetBrainsMonoFontFamily,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = stage.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stage.detailBadge,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = if (stage.isPassed) ClearCounselEmeraldLight else SafetyPauseCrimsonLight
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PrismSpecificationViewerCard(
    selectedTab: Int,
    onSelectTab: (Int) -> Unit,
    selectedFacetIndex: Int,
    onSelectFacetIndex: (Int) -> Unit,
    holoCertificateJson: String,
    copiedNotice: String?,
    onCopyCode: (String, String) -> Unit
) {
    val allFacets = remember { PrismTexFacetCatalog.getAllFacets() }
    val activeFacet = allFacets[selectedFacetIndex.coerceIn(0, allFacets.lastIndex)]

    val (codeTitle, codeText) = when (selectedTab) {
        0 -> "SageCounselTaxonomy.prism" to SsmBackendEngine.prismCleanTaxonomyCode
        1 -> activeFacet.filePath to activeFacet.texContent
        else -> "app.holo (prism/proof/v1)" to holoCertificateJson
    }

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
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Terminal,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Column {
                        Text(
                            text = "STEP 2 & 4 · GENERATED PRISM .LEX.TEX FACETS & .HOLO PROOF",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Product Taxonomy, .lex.tex & .holo Spec",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                OutlinedButton(
                    onClick = { onCopyCode(codeTitle, codeText) },
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("copy_spec_code_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy Specification",
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Copy", style = MaterialTheme.typography.labelMedium)
                }
            }

            if (copiedNotice != null) {
                Text(
                    text = copiedNotice,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedTab == 0,
                    onClick = { onSelectTab(0) },
                    label = { Text("Clean Product Taxonomy") },
                    modifier = Modifier.testTag("spec_tab_taxonomy")
                )
                FilterChip(
                    selected = selectedTab == 1,
                    onClick = { onSelectTab(1) },
                    label = { Text("Lean 4 & LaTeX (.lex.tex / .tex)") },
                    modifier = Modifier.testTag("spec_tab_lean4")
                )
                FilterChip(
                    selected = selectedTab == 2,
                    onClick = { onSelectTab(2) },
                    label = { Text("Proof Certificate (app.holo)") },
                    modifier = Modifier.testTag("spec_tab_holo")
                )
            }

            if (selectedTab == 1) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    allFacets.forEachIndexed { index, facet ->
                        FilterChip(
                            selected = selectedFacetIndex == index,
                            onClick = { onSelectFacetIndex(index) },
                            label = { Text(facet.title) },
                            modifier = Modifier.testTag("facet_chip_${facet.id}")
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        text = "${activeFacet.filePath} · ${activeFacet.standardBadge} · sha256:${activeFacet.sha256Digest.take(12)}…",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFF121A17),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(14.dp)
                ) {
                    Text(
                        text = codeText,
                        fontFamily = JetBrainsMonoFontFamily,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFD8F3DC)
                    )
                }
            }
        }
    }
}

@Composable
private fun DomainFirewallTableCard(rows: List<DomainFirewallRow>) {
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
                text = "SECTION 2 · DOMAIN FIREWALL TRANSLATION TABLE",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Codex Physics → Clean Product Taxonomy",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )

            rows.forEachIndexed { index, row ->
                if (index > 0) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                }
                Column(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = if (row.isExposedInPrismSurface) {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                MaterialTheme.colorScheme.surfaceVariant
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Icon(
                                    imageVector = if (row.isExposedInPrismSurface) {
                                        Icons.Default.CheckCircle
                                    } else {
                                        Icons.Default.VisibilityOff
                                    },
                                    contentDescription = null,
                                    modifier = Modifier.size(13.dp),
                                    tint = if (row.isExposedInPrismSurface) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    }
                                )
                                Text(
                                    text = row.cleanProductTerm,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (row.isExposedInPrismSurface) {
                                        MaterialTheme.colorScheme.onPrimaryContainer
                                    } else {
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    }
                                )
                            }
                        }

                        Text(
                            text = row.layerRole,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }

                    Text(
                        text = row.userFacingMeaning,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Isolated SSM 3.0.2 Codex Term: ${row.codexTerm}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun LiveBackendAdapterCard(uiState: SageCounselUiState) {
    val trace = uiState.evaluation.internalTrace
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
                    imageVector = Icons.Default.Memory,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary
                )
                Column {
                    Text(
                        text = "SECTION 3 · INTERNAL ADAPTER TELEMETRY (JAVA PRISM KERNEL)",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = "Silent Socratic-to-Algebraic Binding",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Text(
                text = "While the Seeker only sees GoalMode, ImpactScale, and ConflictPreference, the Java PrismVerificationVector silently binds the algebraic parameters for the active session:",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AdapterBadge(
                    productLabel = "Goal: ${uiState.selectedGoal.cleanLabel}",
                    backendValue = "Rank P = ${trace.ontologicalRankP} · Manifold ${trace.atlasPolytope}"
                )
                AdapterBadge(
                    productLabel = "Scale: ${uiState.selectedScale.cleanLabel}",
                    backendValue = "Rank n = ${trace.computationalRankN} · Algebra ${trace.cliffordAlgebra}"
                )
                AdapterBadge(
                    productLabel = "Conflict: ${uiState.selectedConflictPreference.cleanLabel}",
                    backendValue = "AUM = ${trace.aumConfig.name}"
                )
                AdapterBadge(
                    productLabel = "BlindSpotCheck",
                    backendValue = "${trace.asuricAudit.name} (H(X) = ${trace.spectralEntropyNat} nat)"
                )
                AdapterBadge(
                    productLabel = "Matched Rule ${trace.matchedRuleId}",
                    backendValue = "${trace.internalState.name} · ${trace.stationaryActionDeltaS}"
                )
            }
        }
    }
}

@Composable
private fun AdapterBadge(productLabel: String, backendValue: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = productLabel,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = backendValue,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
