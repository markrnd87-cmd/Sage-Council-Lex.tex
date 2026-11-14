package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.CounselRepository
import com.example.data.CounselSessionEntity
import com.example.engine.SsmBackendEngine
import com.example.model.AcceptanceScenario
import com.example.model.ConflictPreference
import com.example.model.GoalMode
import com.example.model.ImpactScale
import com.example.model.ImpartialityGuarantee
import com.example.model.ReflectionOutcome
import com.example.model.ResolutionStatus
import com.example.model.SituationInput
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AppTab(val route: String, val label: String) {
    Seeker("seeker", "Counsel"),
    NewSituation("new_situation", "New Situation"),
    Guarantees("guarantees", "Guarantees"),
    Firewall("firewall", "Firewall"),
    Governance("governance", "Governance")
}

data class SageCounselUiState(
    val currentTab: AppTab = AppTab.Seeker,
    val isDarkTheme: Boolean = false,
    val selectedScenarioId: String = "scenario_1_direct",
    val situationDescription: String = SsmBackendEngine.acceptanceScenarios.first().input.description,
    val selectedGoal: GoalMode = SsmBackendEngine.acceptanceScenarios.first().input.goal,
    val selectedScale: ImpactScale = SsmBackendEngine.acceptanceScenarios.first().input.scale,
    val selectedConflictPreference: ConflictPreference = SsmBackendEngine.acceptanceScenarios.first().input.onConflict,
    val newSituationDraft: SituationInput = SituationInput(
        description = "",
        goal = GoalMode.StabilizeExisting,
        scale = ImpactScale.PersonalOrTeam,
        onConflict = ConflictPreference.OfferCompromise
    ),
    val hasSubmittedNewSituationDraft: Boolean = false,
    val newSituationEvaluation: SsmBackendEngine.EvaluationBundle = SsmBackendEngine.evaluate(
        input = SituationInput(
            description = "",
            goal = GoalMode.StabilizeExisting,
            scale = ImpactScale.PersonalOrTeam,
            onConflict = ConflictPreference.OfferCompromise
        ),
        guarantees = SsmBackendEngine.defaultGuarantees
    ),
    val guarantees: List<ImpartialityGuarantee> = SsmBackendEngine.defaultGuarantees,
    val mutateRuleR1ForKernelTest: Boolean = false,
    val flipMeasurementMR1ToPending: Boolean = false,
    val evaluation: SsmBackendEngine.EvaluationBundle = SsmBackendEngine.evaluate(
        input = SsmBackendEngine.acceptanceScenarios.first().input,
        guarantees = SsmBackendEngine.defaultGuarantees
    ),
    val selectedReflectionOutcome: ReflectionOutcome = ReflectionOutcome.HelpedClarify,
    val reflectionDraftNote: String = "",
    val statusBannerMessage: String? = null
)

class SageCounselViewModel(
    private val repository: CounselRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SageCounselUiState())
    val uiState: StateFlow<SageCounselUiState> = _uiState.asStateFlow()

    val savedSessions: StateFlow<List<CounselSessionEntity>> = repository.allSessions
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            repository.ensureSeeded()
        }
    }

    fun selectTab(tab: AppTab) {
        _uiState.update { it.copy(currentTab = tab, statusBannerMessage = null) }
    }

    fun toggleDarkTheme() {
        _uiState.update { it.copy(isDarkTheme = !it.isDarkTheme) }
    }

    fun dismissBanner() {
        _uiState.update { it.copy(statusBannerMessage = null) }
    }

    fun updateNewSituationDraft(input: SituationInput) {
        _uiState.update { state ->
            val draftEval = SsmBackendEngine.evaluate(
                input = input,
                guarantees = state.guarantees,
                mutateRuleR1ForTesting = state.mutateRuleR1ForKernelTest
            )
            state.copy(
                newSituationDraft = input,
                newSituationEvaluation = draftEval
            )
        }
    }

    fun resetNewSituationDraft() {
        val blank = SituationInput(
            description = "",
            goal = GoalMode.StabilizeExisting,
            scale = ImpactScale.PersonalOrTeam,
            onConflict = ConflictPreference.OfferCompromise
        )
        _uiState.update { state ->
            state.copy(
                newSituationDraft = blank,
                hasSubmittedNewSituationDraft = false,
                newSituationEvaluation = SsmBackendEngine.evaluate(
                    input = blank,
                    guarantees = state.guarantees,
                    mutateRuleR1ForTesting = state.mutateRuleR1ForKernelTest
                ),
                statusBannerMessage = "Cleared New Situation form."
            )
        }
    }

    fun submitNewSituationDraft(navigateToSeeker: Boolean = false) {
        val state = _uiState.value
        val draft = state.newSituationDraft
        if (draft.description.isBlank()) return

        val eval = SsmBackendEngine.evaluate(
            input = draft,
            guarantees = state.guarantees,
            mutateRuleR1ForTesting = state.mutateRuleR1ForKernelTest
        )
        val defaultReflection = when (eval.counselOutput.status) {
            ResolutionStatus.ClearCounsel -> ReflectionOutcome.HelpedClarify
            ResolutionStatus.CompromiseReached -> ReflectionOutcome.NeededAdjustment
            ResolutionStatus.PausedForSafety -> ReflectionOutcome.EscalatedToReviewer
        }

        _uiState.update {
            it.copy(
                selectedScenarioId = "custom",
                situationDescription = draft.description,
                selectedGoal = draft.goal,
                selectedScale = draft.scale,
                selectedConflictPreference = draft.onConflict,
                evaluation = eval,
                newSituationEvaluation = eval,
                hasSubmittedNewSituationDraft = true,
                selectedReflectionOutcome = defaultReflection,
                currentTab = if (navigateToSeeker) AppTab.Seeker else it.currentTab,
                statusBannerMessage = "Generated ${eval.counselOutput.status.cleanTitle} for your new situation."
            )
        }

        if (navigateToSeeker) {
            val isSafety = eval.counselOutput.status == ResolutionStatus.PausedForSafety
            viewModelScope.launch {
                repository.insert(
                    CounselSessionEntity(
                        title = "New Situation (${eval.counselOutput.status.cleanTitle})",
                        situationSummary = draft.description,
                        goalModeName = draft.goal.cleanLabel,
                        impactScaleName = draft.scale.cleanLabel,
                        conflictPreferenceName = draft.onConflict.cleanLabel,
                        resolutionStatusName = eval.counselOutput.status.name,
                        counselSummary = eval.counselOutput.summary,
                        rationale = eval.counselOutput.rationale,
                        blindSpotPassed = eval.blindSpotPassed,
                        guaranteesUpheldCount = eval.guaranteesUpheldCount,
                        outcomeReflectionLabel = defaultReflection.label,
                        outcomeReflectionNote = defaultReflection.description,
                        trustReviewerNote = if (isSafety) {
                            "Queued for Trust Reviewer inspection: session triggered SafetyPause."
                        } else {
                            "Verified consultation logged with ${eval.guaranteesUpheldCount}/9 Impartiality Guarantees."
                        },
                        isReviewedByTrust = false,
                        timestampMillis = System.currentTimeMillis()
                    )
                )
            }
        }
    }

    fun loadScenario(scenario: AcceptanceScenario) {
        val updatedGuarantees = SsmBackendEngine.defaultGuarantees.map { g ->
            if (scenario.forceGuaranteeBreachId != null && g.id == scenario.forceGuaranteeBreachId) {
                g.copy(isUpheld = false)
            } else {
                g.copy(isUpheld = true)
            }
        }
        val eval = SsmBackendEngine.evaluate(
            input = scenario.input,
            guarantees = updatedGuarantees,
            mutateRuleR1ForTesting = _uiState.value.mutateRuleR1ForKernelTest
        )
        val defaultReflection = when (eval.counselOutput.status) {
            ResolutionStatus.ClearCounsel -> ReflectionOutcome.HelpedClarify
            ResolutionStatus.CompromiseReached -> ReflectionOutcome.NeededAdjustment
            ResolutionStatus.PausedForSafety -> ReflectionOutcome.EscalatedToReviewer
        }
        _uiState.update {
            it.copy(
                selectedScenarioId = scenario.id,
                situationDescription = scenario.input.description,
                selectedGoal = scenario.input.goal,
                selectedScale = scenario.input.scale,
                selectedConflictPreference = scenario.input.onConflict,
                guarantees = updatedGuarantees,
                evaluation = eval,
                selectedReflectionOutcome = defaultReflection,
                statusBannerMessage = "Loaded ${scenario.code}: ${scenario.title}"
            )
        }
    }

    fun updateSituationDescription(text: String) {
        _uiState.update { state ->
            val nextInput = SituationInput(
                description = text,
                goal = state.selectedGoal,
                scale = state.selectedScale,
                onConflict = state.selectedConflictPreference
            )
            val eval = SsmBackendEngine.evaluate(
                input = nextInput,
                guarantees = state.guarantees,
                mutateRuleR1ForTesting = state.mutateRuleR1ForKernelTest
            )
            state.copy(
                selectedScenarioId = "custom",
                situationDescription = text,
                evaluation = eval
            )
        }
    }

    fun selectGoalMode(goal: GoalMode) {
        _uiState.update { state ->
            val nextInput = SituationInput(
                description = state.situationDescription,
                goal = goal,
                scale = state.selectedScale,
                onConflict = state.selectedConflictPreference
            )
            val eval = SsmBackendEngine.evaluate(
                input = nextInput,
                guarantees = state.guarantees,
                mutateRuleR1ForTesting = state.mutateRuleR1ForKernelTest
            )
            state.copy(
                selectedScenarioId = "custom",
                selectedGoal = goal,
                evaluation = eval
            )
        }
    }

    fun selectImpactScale(scale: ImpactScale) {
        _uiState.update { state ->
            val nextInput = SituationInput(
                description = state.situationDescription,
                goal = state.selectedGoal,
                scale = scale,
                onConflict = state.selectedConflictPreference
            )
            val eval = SsmBackendEngine.evaluate(
                input = nextInput,
                guarantees = state.guarantees,
                mutateRuleR1ForTesting = state.mutateRuleR1ForKernelTest
            )
            state.copy(
                selectedScenarioId = "custom",
                selectedScale = scale,
                evaluation = eval
            )
        }
    }

    fun selectConflictPreference(preference: ConflictPreference) {
        _uiState.update { state ->
            val nextInput = SituationInput(
                description = state.situationDescription,
                goal = state.selectedGoal,
                scale = state.selectedScale,
                onConflict = preference
            )
            val eval = SsmBackendEngine.evaluate(
                input = nextInput,
                guarantees = state.guarantees,
                mutateRuleR1ForTesting = state.mutateRuleR1ForKernelTest
            )
            state.copy(
                selectedScenarioId = "custom",
                selectedConflictPreference = preference,
                evaluation = eval
            )
        }
    }

    fun toggleGuarantee(guaranteeId: String) {
        _uiState.update { state ->
            val updated = state.guarantees.map { g ->
                if (g.id == guaranteeId) g.copy(isUpheld = !g.isUpheld) else g
            }
            val nextInput = SituationInput(
                description = state.situationDescription,
                goal = state.selectedGoal,
                scale = state.selectedScale,
                onConflict = state.selectedConflictPreference
            )
            val eval = SsmBackendEngine.evaluate(
                input = nextInput,
                guarantees = updated,
                mutateRuleR1ForTesting = state.mutateRuleR1ForKernelTest
            )
            val draftEval = SsmBackendEngine.evaluate(
                input = state.newSituationDraft,
                guarantees = updated,
                mutateRuleR1ForTesting = state.mutateRuleR1ForKernelTest
            )
            state.copy(
                guarantees = updated,
                evaluation = eval,
                newSituationEvaluation = draftEval
            )
        }
    }

    fun restoreAllGuarantees() {
        _uiState.update { state ->
            val restored = SsmBackendEngine.defaultGuarantees
            val nextInput = SituationInput(
                description = state.situationDescription,
                goal = state.selectedGoal,
                scale = state.selectedScale,
                onConflict = state.selectedConflictPreference
            )
            val eval = SsmBackendEngine.evaluate(
                input = nextInput,
                guarantees = restored,
                mutateRuleR1ForTesting = state.mutateRuleR1ForKernelTest
            )
            val draftEval = SsmBackendEngine.evaluate(
                input = state.newSituationDraft,
                guarantees = restored,
                mutateRuleR1ForTesting = state.mutateRuleR1ForKernelTest
            )
            state.copy(
                guarantees = restored,
                evaluation = eval,
                newSituationEvaluation = draftEval,
                statusBannerMessage = "All 9 Impartiality Guarantees restored."
            )
        }
    }

    fun setMutateRuleR1ForKernelTest(mutate: Boolean) {
        _uiState.update { state ->
            val nextInput = SituationInput(
                description = state.situationDescription,
                goal = state.selectedGoal,
                scale = state.selectedScale,
                onConflict = state.selectedConflictPreference
            )
            val eval = SsmBackendEngine.evaluate(
                input = nextInput,
                guarantees = state.guarantees,
                mutateRuleR1ForTesting = mutate,
                flipMeasurementMR1ToPending = false
            )
            val draftEval = SsmBackendEngine.evaluate(
                input = state.newSituationDraft,
                guarantees = state.guarantees,
                mutateRuleR1ForTesting = mutate,
                flipMeasurementMR1ToPending = false
            )
            state.copy(
                mutateRuleR1ForKernelTest = mutate,
                flipMeasurementMR1ToPending = false,
                evaluation = eval,
                newSituationEvaluation = draftEval,
                statusBannerMessage = if (mutate) {
                    "Lean 4 Anti-Vacuity Gate: Rule R1 mutated! Theorem P1 (p1_asuricAlwaysVetoes) & EX-03 now FAIL."
                } else {
                    "Canonical Prism Spec restored: 6/6 Examples & 5/5 Theorems PASS."
                }
            )
        }
    }

    fun setFlipMeasurementMR1ToPending(pending: Boolean) {
        _uiState.update { state ->
            val nextInput = SituationInput(
                description = state.situationDescription,
                goal = state.selectedGoal,
                scale = state.selectedScale,
                onConflict = state.selectedConflictPreference
            )
            val eval = SsmBackendEngine.evaluate(
                input = nextInput,
                guarantees = state.guarantees,
                mutateRuleR1ForTesting = false,
                flipMeasurementMR1ToPending = pending
            )
            val draftEval = SsmBackendEngine.evaluate(
                input = state.newSituationDraft,
                guarantees = state.guarantees,
                mutateRuleR1ForTesting = false,
                flipMeasurementMR1ToPending = pending
            )
            state.copy(
                mutateRuleR1ForKernelTest = false,
                flipMeasurementMR1ToPending = pending,
                evaluation = eval,
                newSituationEvaluation = draftEval,
                statusBannerMessage = if (pending) {
                    "OSCAL Security Gate: Measurement M-R1 flipped to Pending! Security.lex.tex & .holo attestation blocked."
                } else {
                    "Security Measurement M-R1 verified: all 6 .lex.tex facets and .holo attestation PASS."
                }
            )
        }
    }

    fun selectReflectionOutcome(outcome: ReflectionOutcome) {
        _uiState.update { it.copy(selectedReflectionOutcome = outcome) }
    }

    fun updateReflectionDraftNote(note: String) {
        _uiState.update { it.copy(reflectionDraftNote = note) }
    }

    fun saveSessionAndReflection() {
        val state = _uiState.value
        val eval = state.evaluation
        val isSafety = eval.counselOutput.status == ResolutionStatus.PausedForSafety
        val noteText = state.reflectionDraftNote.ifBlank {
            state.selectedReflectionOutcome.description
        }
        val titleText = SsmBackendEngine.acceptanceScenarios
            .find { it.id == state.selectedScenarioId }
            ?.let { "${it.code}: ${it.title}" }
            ?: "Custom Consultation (${eval.counselOutput.status.cleanTitle})"

        viewModelScope.launch {
            repository.insert(
                CounselSessionEntity(
                    title = titleText,
                    situationSummary = state.situationDescription,
                    goalModeName = state.selectedGoal.cleanLabel,
                    impactScaleName = state.selectedScale.cleanLabel,
                    conflictPreferenceName = state.selectedConflictPreference.cleanLabel,
                    resolutionStatusName = eval.counselOutput.status.name,
                    counselSummary = eval.counselOutput.summary,
                    rationale = eval.counselOutput.rationale,
                    blindSpotPassed = eval.blindSpotPassed,
                    guaranteesUpheldCount = eval.guaranteesUpheldCount,
                    outcomeReflectionLabel = state.selectedReflectionOutcome.label,
                    outcomeReflectionNote = noteText,
                    trustReviewerNote = if (isSafety) {
                        "Queued for Trust Reviewer inspection: session triggered SafetyPause."
                    } else {
                        "Verified consultation logged with ${eval.guaranteesUpheldCount}/9 Impartiality Guarantees."
                    },
                    isReviewedByTrust = false,
                    timestampMillis = System.currentTimeMillis()
                )
            )
            _uiState.update {
                it.copy(
                    reflectionDraftNote = "",
                    statusBannerMessage = "Saved OutcomeReflection to Governance log."
                )
            }
        }
    }

    fun markSessionReviewedByTrust(session: CounselSessionEntity, reviewerResolution: String) {
        viewModelScope.launch {
            repository.update(
                session.copy(
                    isReviewedByTrust = true,
                    trustReviewerNote = reviewerResolution
                )
            )
            _uiState.update {
                it.copy(statusBannerMessage = "Trust Reviewer ratification recorded for '${session.title}'.")
            }
        }
    }

    fun deleteSession(id: Long) {
        viewModelScope.launch {
            repository.deleteById(id)
        }
    }

    class Factory(private val repository: CounselRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SageCounselViewModel(repository) as T
        }
    }
}
