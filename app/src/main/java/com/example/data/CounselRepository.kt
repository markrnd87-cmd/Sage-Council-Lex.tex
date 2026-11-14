package com.example.data

import com.example.engine.SsmBackendEngine
import com.example.model.ReflectionOutcome
import com.example.model.ResolutionStatus
import kotlinx.coroutines.flow.Flow

class CounselRepository(private val dao: CounselDao) {
    val allSessions: Flow<List<CounselSessionEntity>> = dao.getAllSessions()

    suspend fun insert(session: CounselSessionEntity): Long = dao.insertSession(session)

    suspend fun update(session: CounselSessionEntity) = dao.updateSession(session)

    suspend fun deleteById(id: Long) = dao.deleteSessionById(id)

    suspend fun ensureSeeded() {
        if (dao.getCount() == 0) {
            val now = System.currentTimeMillis()
            val coreExamples = SsmBackendEngine.acceptanceScenarios.filter { it.isCoreThree }
            coreExamples.forEachIndexed { idx, scenario ->
                val eval = SsmBackendEngine.evaluate(
                    input = scenario.input,
                    guarantees = SsmBackendEngine.defaultGuarantees
                )
                val isSafety = eval.counselOutput.status == ResolutionStatus.PausedForSafety
                dao.insertSession(
                    CounselSessionEntity(
                        title = "${scenario.code}: ${scenario.title}",
                        situationSummary = scenario.input.description,
                        goalModeName = scenario.input.goal.cleanLabel,
                        impactScaleName = scenario.input.scale.cleanLabel,
                        conflictPreferenceName = scenario.input.onConflict.cleanLabel,
                        resolutionStatusName = eval.counselOutput.status.name,
                        counselSummary = eval.counselOutput.summary,
                        rationale = eval.counselOutput.rationale,
                        blindSpotPassed = eval.blindSpotPassed,
                        guaranteesUpheldCount = eval.guaranteesUpheldCount,
                        outcomeReflectionLabel = when (eval.counselOutput.status) {
                            ResolutionStatus.ClearCounsel -> ReflectionOutcome.HelpedClarify.label
                            ResolutionStatus.CompromiseReached -> ReflectionOutcome.NeededAdjustment.label
                            ResolutionStatus.PausedForSafety -> ReflectionOutcome.EscalatedToReviewer.label
                        },
                        outcomeReflectionNote = when (eval.counselOutput.status) {
                            ResolutionStatus.ClearCounsel -> "Two-track role split resolved tension between Maya and Devon ahead of sprint planning."
                            ResolutionStatus.CompromiseReached -> "Leadership agreed to the two-phase Month 3 / Month 5 milestone compromise."
                            ResolutionStatus.PausedForSafety -> "BlindSpotCheck halted deceptive equity tactic and redirected to transparent disclosure."
                        },
                        trustReviewerNote = if (isSafety) {
                            "Flagged under BlindSpotCheck (Rule R1). Verified that manipulative tactic was declined and honest negotiation path was offered."
                        } else {
                            "Routine verification passed (9/9 Impartiality Guarantees intact)."
                        },
                        isReviewedByTrust = isSafety,
                        timestampMillis = now - (coreExamples.size - idx) * 3600_000L
                    )
                )
            }
        }
    }
}
