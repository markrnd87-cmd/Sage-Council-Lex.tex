package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.Balance
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.CounselDatabase
import com.example.data.CounselRepository
import com.example.ui.AppTab
import com.example.ui.SageCounselViewModel
import com.example.ui.screens.DomainFirewallScreen
import com.example.ui.screens.GovernanceReviewScreen
import com.example.ui.screens.ImpartialityGuaranteesScreen
import com.example.ui.screens.NewSituationScreen
import com.example.ui.screens.SeekerCounselScreen
import com.example.ui.theme.SageCounselTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val repository = remember(context) {
                val db = CounselDatabase.getInstance(context)
                CounselRepository(db.counselDao())
            }
            val viewModel: SageCounselViewModel = viewModel(
                factory = SageCounselViewModel.Factory(repository)
            )
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val savedSessions by viewModel.savedSessions.collectAsStateWithLifecycle()

            SageCounselTheme(darkTheme = uiState.isDarkTheme) {
                SageCounselApp(
                    uiState = uiState,
                    savedSessions = savedSessions,
                    viewModel = viewModel
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SageCounselApp(
    uiState: com.example.ui.SageCounselUiState,
    savedSessions: List<com.example.data.CounselSessionEntity>,
    viewModel: SageCounselViewModel
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val useNavigationRail = maxWidth >= 640.dp
        val upheldGuaranteesCount = uiState.evaluation.guaranteesUpheldCount

        Scaffold(
            contentWindowInsets = WindowInsets.safeDrawing,
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = stringResource(R.string.app_name),
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = stringResource(R.string.app_subtitle),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = { viewModel.selectTab(AppTab.NewSituation) },
                            modifier = Modifier.testTag("top_bar_new_situation_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddCircle,
                                contentDescription = "Input New Situation",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        IconButton(
                            onClick = { viewModel.toggleDarkTheme() },
                            modifier = Modifier.testTag("toggle_theme_button")
                        ) {
                            Icon(
                                imageVector = if (uiState.isDarkTheme) {
                                    Icons.Default.LightMode
                                } else {
                                    Icons.Default.DarkMode
                                },
                                contentDescription = "Toggle Dark or Light Theme",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
            },
            bottomBar = {
                if (!useNavigationRail) {
                    NavigationBar(
                        modifier = Modifier.testTag("bottom_navigation_bar")
                    ) {
                        AppTab.entries.forEach { tab ->
                            val selected = uiState.currentTab == tab
                            NavigationBarItem(
                                selected = selected,
                                onClick = { viewModel.selectTab(tab) },
                                icon = {
                                    TabIconWithBadge(
                                        tab = tab,
                                        selected = selected,
                                        upheldGuaranteesCount = upheldGuaranteesCount,
                                        sessionCount = savedSessions.size
                                    )
                                },
                                label = { Text(tab.label) },
                                modifier = Modifier.testTag("nav_tab_${tab.route}")
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (useNavigationRail) {
                    NavigationRail(
                        modifier = Modifier
                            .fillMaxHeight()
                            .testTag("side_navigation_rail")
                    ) {
                        AppTab.entries.forEach { tab ->
                            val selected = uiState.currentTab == tab
                            NavigationRailItem(
                                selected = selected,
                                onClick = { viewModel.selectTab(tab) },
                                icon = {
                                    TabIconWithBadge(
                                        tab = tab,
                                        selected = selected,
                                        upheldGuaranteesCount = upheldGuaranteesCount,
                                        sessionCount = savedSessions.size
                                    )
                                },
                                label = { Text(tab.label) },
                                modifier = Modifier.testTag("nav_rail_${tab.route}")
                            )
                        }
                    }
                }

                Column(modifier = Modifier.fillMaxSize()) {
                    AnimatedVisibility(visible = uiState.statusBannerMessage != null) {
                        uiState.statusBannerMessage?.let { msg ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 6.dp)
                                    .testTag("status_banner")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = msg,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                                        modifier = Modifier.weight(1f)
                                    )
                                    IconButton(
                                        onClick = { viewModel.dismissBanner() },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Dismiss notification",
                                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    when (uiState.currentTab) {
                        AppTab.Seeker -> SeekerCounselScreen(
                            uiState = uiState,
                            onLoadScenario = viewModel::loadScenario,
                            onDescriptionChange = viewModel::updateSituationDescription,
                            onSelectGoal = viewModel::selectGoalMode,
                            onSelectScale = viewModel::selectImpactScale,
                            onSelectConflict = viewModel::selectConflictPreference,
                            onRestoreGuarantees = viewModel::restoreAllGuarantees,
                            onSelectReflectionOutcome = viewModel::selectReflectionOutcome,
                            onReflectionNoteChange = viewModel::updateReflectionDraftNote,
                            onSaveReflection = viewModel::saveSessionAndReflection,
                            onNavigateToTab = viewModel::selectTab,
                            modifier = Modifier.weight(1f)
                        )
                        AppTab.NewSituation -> NewSituationScreen(
                            draftInput = uiState.newSituationDraft,
                            hasSubmittedDraft = uiState.hasSubmittedNewSituationDraft,
                            draftEvaluation = uiState.newSituationEvaluation,
                            onDraftInputChange = viewModel::updateNewSituationDraft,
                            onResetDraft = viewModel::resetNewSituationDraft,
                            onSubmitNewSituation = { viewModel.submitNewSituationDraft(navigateToSeeker = false) },
                            onSubmitAndOpenCounselTab = { viewModel.submitNewSituationDraft(navigateToSeeker = true) },
                            onRestoreGuarantees = viewModel::restoreAllGuarantees,
                            onBackToCounsel = { viewModel.selectTab(AppTab.Seeker) },
                            modifier = Modifier.weight(1f)
                        )
                        AppTab.Guarantees -> ImpartialityGuaranteesScreen(
                            guarantees = uiState.guarantees,
                            onToggleGuarantee = viewModel::toggleGuarantee,
                            onRestoreAllGuarantees = viewModel::restoreAllGuarantees,
                            onBackToSeeker = { viewModel.selectTab(AppTab.Seeker) },
                            modifier = Modifier.weight(1f)
                        )
                        AppTab.Firewall -> DomainFirewallScreen(
                            uiState = uiState,
                            onSetMutateRuleR1 = viewModel::setMutateRuleR1ForKernelTest,
                            onSetFlipMeasurementMR1 = viewModel::setFlipMeasurementMR1ToPending,
                            onBackToSeeker = { viewModel.selectTab(AppTab.Seeker) },
                            modifier = Modifier.weight(1f)
                        )
                        AppTab.Governance -> GovernanceReviewScreen(
                            sessions = savedSessions,
                            onMarkReviewed = viewModel::markSessionReviewedByTrust,
                            onDeleteSession = viewModel::deleteSession,
                            onBackToSeeker = { viewModel.selectTab(AppTab.Seeker) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TabIconWithBadge(
    tab: AppTab,
    selected: Boolean,
    upheldGuaranteesCount: Int,
    sessionCount: Int
) {
    val icon = when (tab) {
        AppTab.Seeker -> if (selected) Icons.Filled.Balance else Icons.Outlined.Balance
        AppTab.NewSituation -> if (selected) Icons.Filled.AddCircle else Icons.Outlined.AddCircleOutline
        AppTab.Guarantees -> if (selected) Icons.Filled.VerifiedUser else Icons.Outlined.VerifiedUser
        AppTab.Firewall -> if (selected) Icons.Filled.Shield else Icons.Outlined.Shield
        AppTab.Governance -> if (selected) Icons.Filled.AdminPanelSettings else Icons.Outlined.AdminPanelSettings
    }

    BadgedBox(
        badge = {
            if (tab == AppTab.Guarantees && upheldGuaranteesCount < 9) {
                Badge(containerColor = MaterialTheme.colorScheme.error) {
                    Text("$upheldGuaranteesCount/9")
                }
            } else if (tab == AppTab.Governance && sessionCount > 0) {
                Badge {
                    Text("$sessionCount")
                }
            }
        }
    ) {
        Icon(imageVector = icon, contentDescription = tab.label)
    }
}
