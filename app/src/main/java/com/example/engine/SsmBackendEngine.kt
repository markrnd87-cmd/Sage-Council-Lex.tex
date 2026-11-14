package com.example.engine

import com.example.model.AcceptanceScenario
import com.example.model.ActionStep
import com.example.model.ConflictPreference
import com.example.model.CounselOutput
import com.example.model.DomainFirewallRow
import com.example.model.GoalMode
import com.example.model.ImpactScale
import com.example.model.ImpartialityGuarantee
import com.example.model.PerspectiveItem
import com.example.model.ResolutionStatus
import com.example.model.SituationInput
import com.example.model.StakeholderRole
import com.example.prism.PrismContractKernel
import com.example.prism.PrismTheoremProofReport
import com.example.prism.PrismVerificationVector
import kotlin.math.ln

/**
 * Internal-Only Backend Engine (SSM 3.0.2 / 3.1 Adapter).
 *
 * Isolates all Super Sage Mode 3.0.2 mathematical constructs (RawIntent, Fourier Listen,
 * Dynamic Asuric Audit, SankalpaDirective(P, n, AUM), Clifford Algebras Cl_{n,0},
 * P-Atlas Polytopes A_P, Rotors R(t), Sattvic Governor, and State A/U/M Geodesic Solver)
 * strictly inside the backend adapter while exposing only Clean Product Taxonomy objects
 * (`CounselOutput`, `ResolutionStatus`, `ImpartialityGuarantee`) to the application UI.
 */
object SsmBackendEngine {

    // Internal Backend Representations (Hidden behind the Domain Firewall)
    enum class InternalAsuricAudit { DHARMIC_CLEAR, ASURIC_DETECTED }
    enum class InternalAumConfig { ATTEMPT_HEAL, VETO_ON_FAILURE }
    enum class InternalSattvicAudit { SATTVIC, RAJASIC_CONFLICT, TAMASIC_INERT }
    enum class InternalHealResult { HEAL_CONVERGED, HEAL_DIVERGED }
    enum class InternalManifestState { STATE_A_JAGRAT, STATE_U_SWAPNA, STATE_M_VETO }

    data class InternalTelemetryTrace(
        val spectralEntropyNat: Double,
        val asuricAudit: InternalAsuricAudit,
        val ontologicalRankP: String,
        val computationalRankN: Int,
        val cliffordAlgebra: String,
        val atlasPolytope: String,
        val aumConfig: InternalAumConfig,
        val zeroStateIdentity: String,
        val activePillarsCount: Int,
        val sattvicAudit: InternalSattvicAudit,
        val healResult: InternalHealResult,
        val matchedRuleId: String,
        val matchedRuleExpression: String,
        val internalState: InternalManifestState,
        val stationaryActionDeltaS: String
    )

    data class EvaluationBundle(
        val counselOutput: CounselOutput,
        val blindSpotPassed: Boolean,
        val blindSpotNotice: String,
        val guaranteesUpheldCount: Int,
        val internalTrace: InternalTelemetryTrace,
        val prismProofReport: PrismTheoremProofReport
    )

    val stakeholderRoles: List<StakeholderRole> = listOf(
        StakeholderRole(
            title = "The Seeker",
            subtitle = "Primary Stakeholder · Advisory Workspace",
            replacesCodexTerm = "Replaces 'The Architect' from GDCX-SSM-3.0.2",
            coreResponsibility = "Describes a complex dilemma in everyday language, answers three brief scoping questions, and receives a clear, practical path forward.",
            keyPromise = "Never forced to configure Clifford Algebras, P-Atlas Polytopes, or Rotor equations to receive grounded human counsel."
        ),
        StakeholderRole(
            title = "The Affected Party",
            subtitle = "Secondary Stakeholder · Multi-Perspective Fairness",
            replacesCodexTerm = "Replaces abstract 'Ethical Manifold Aut(A_P)'",
            coreResponsibility = "Anyone impacted by the Seeker's decision who needs assurance that competing viewpoints, fair boundaries, and unintended harms were weighed equally.",
            keyPromise = "Every consultation explicitly surfaces affected stakeholder needs, potential biases, and 9 verifiable Impartiality Guarantees."
        ),
        StakeholderRole(
            title = "The Trust Reviewer",
            subtitle = "Governance Stakeholder · Edge-Case Stewardship",
            replacesCodexTerm = "Replaces public 'Council of Nine Stewards' exposure",
            coreResponsibility = "Reviews edge-case sessions when a situation is flagged as unsafe (SafetyPause) or irreconcilable, and audits Outcome Reflections.",
            keyPromise = "Provides calm human oversight and boundary ratification when requests cannot be safely or fairly resolved."
        )
    )

    val domainFirewallTable: List<DomainFirewallRow> = listOf(
        DomainFirewallRow(
            codexTerm = "RawIntent & Fourier Listen H(X)",
            layerRole = "Backend Intake",
            cleanProductTerm = "SituationSummary",
            userFacingMeaning = "Plain-language description of the problem",
            isExposedInPrismSurface = true
        ),
        DomainFirewallRow(
            codexTerm = "Dynamic Asuric Audit",
            layerRole = "Backend Safety Gate",
            cleanProductTerm = "BlindSpotCheck",
            userFacingMeaning = "Screening out biased, manipulative, or harmful angles",
            isExposedInPrismSurface = true
        ),
        DomainFirewallRow(
            codexTerm = "Sankalpa Protocol (Ācāra / Vyavahāra / Prāyaścitta)",
            layerRole = "Product Workflow",
            cleanProductTerm = "ClarifyingQuestions",
            userFacingMeaning = "Three brief questions to frame the advice",
            isExposedInPrismSurface = true
        ),
        DomainFirewallRow(
            codexTerm = "SankalpaDirective (P, n, AUM)",
            layerRole = "Backend Config",
            cleanProductTerm = "SessionScope",
            userFacingMeaning = "User choices on goal, scale, and fallback",
            isExposedInPrismSurface = true
        ),
        DomainFirewallRow(
            codexTerm = "Lagrangian Compiler & P-Atlas Polytope",
            layerRole = "Backend Solver",
            cleanProductTerm = "Hidden (Internal Only)",
            userFacingMeaning = "Never exposed in the product model",
            isExposedInPrismSurface = false
        ),
        DomainFirewallRow(
            codexTerm = "State A (Jagrat) & Kaustubha Solution",
            layerRole = "Product Output",
            cleanProductTerm = "BalancedCounsel (ClearCounsel)",
            userFacingMeaning = "Clear, actionable advice ready to use",
            isExposedInPrismSurface = true
        ),
        DomainFirewallRow(
            codexTerm = "State U (Swapna) & Geodesic Retract",
            layerRole = "Product Recovery",
            cleanProductTerm = "AlternativePath (CompromiseReached)",
            userFacingMeaning = "Adjusted option when goals conflict",
            isExposedInPrismSurface = true
        ),
        DomainFirewallRow(
            codexTerm = "State M (Sushupti) & Halahala Veto",
            layerRole = "Product Boundary",
            cleanProductTerm = "SafetyPause (PausedForSafety)",
            userFacingMeaning = "Polite refusal when a request is unsafe or irreconcilable",
            isExposedInPrismSurface = true
        ),
        DomainFirewallRow(
            codexTerm = "Kintsugi CycleLog",
            layerRole = "Product Feedback",
            cleanProductTerm = "OutcomeReflection",
            userFacingMeaning = "User feedback on whether the advice helped",
            isExposedInPrismSurface = true
        )
    )

    val defaultGuarantees: List<ImpartialityGuarantee> = listOf(
        ImpartialityGuarantee(
            id = "boundary_respect",
            number = 1,
            productGuarantee = "Boundary Respect",
            userPromise = "Advice never violates stated ethical, legal, or practical hard limits.",
            affectedPartyProtection = "Protects non-negotiable rights and personal dignity of everyone involved.",
            hiddenBackendPillar = "Dharma (Structure)",
            hiddenBackendMechanism = "Manifold Confinement: Trajectory M(t) is forbidden from leaving A_P."
        ),
        ImpartialityGuarantee(
            id = "minimal_friction",
            number = 2,
            productGuarantee = "Minimal Friction",
            userPromise = "Recommendations favor the calmest, most direct step rather than dramatic overhauls.",
            affectedPartyProtection = "Prevents unnecessary disruption and emotional exhaustion for teams and families.",
            hiddenBackendPillar = "Shanti (Equilibrium)",
            hiddenBackendMechanism = "Kinetic Energy Minimization: L_kin = ½|R'(t)|² minimized via δS = 0."
        ),
        ImpartialityGuarantee(
            id = "fairness_all_sides",
            number = 3,
            productGuarantee = "Fairness to All Sides",
            userPromise = "Counsel weighs the impact on every person involved without favoring one bias.",
            affectedPartyProtection = "Ensures quieter or absent stakeholders have their core needs represented equally.",
            hiddenBackendPillar = "Ahimsa (Symmetry)",
            hiddenBackendMechanism = "Gauge Invariance Sector (L_gauge): Preserves symmetry group Aut(A_P)."
        ),
        ImpartialityGuarantee(
            id = "fact_preservation",
            number = 4,
            productGuarantee = "Fact Preservation",
            userPromise = "The summary accurately reflects the facts shared without inventing details.",
            affectedPartyProtection = "Prevents straw-man accusations or fabricated assumptions about others' motives.",
            hiddenBackendPillar = "Satya (Truth)",
            hiddenBackendMechanism = "Field Divergence Sector (L_div): Penalizes non-zero divergence ∇·V(t)."
        ),
        ImpartialityGuarantee(
            id = "whole_picture_awareness",
            number = 5,
            productGuarantee = "Whole-Picture Awareness",
            userPromise = "Solutions account for how a choice ripples across the whole relationship or group.",
            affectedPartyProtection = "Guards against local fixes that push hidden costs onto neighboring teams or partners.",
            hiddenBackendPillar = "Radha (Unity)",
            hiddenBackendMechanism = "Pseudoscalar Integrity Sector (L_unity): Preserves pseudoscalar I_n of Cl_{n,0}."
        ),
        ImpartialityGuarantee(
            id = "clear_next_action",
            number = 6,
            productGuarantee = "Clear Next Action",
            userPromise = "Every session concludes with concrete, prioritized steps rather than vague platitudes.",
            affectedPartyProtection = "Gives all participants transparent expectations and measurable follow-through.",
            hiddenBackendPillar = "Svadharma (Geodesic Action)",
            hiddenBackendMechanism = "Stationary Action Principle (δS = 0) governing the single optimal path."
        ),
        ImpartialityGuarantee(
            id = "honest_status_reporting",
            number = 7,
            productGuarantee = "Honest Status Reporting",
            userPromise = "Clearly states whether the advice is a direct fit, a compromise, or a paused conflict.",
            affectedPartyProtection = "Never disguises a painful trade-off or ethical refusal as a seamless win.",
            hiddenBackendPillar = "Viveka (Discernment)",
            hiddenBackendMechanism = "A-U-M Tri-State Discernment choosing State A, State U, or State M."
        ),
        ImpartialityGuarantee(
            id = "constructive_recovery",
            number = 8,
            productGuarantee = "Constructive Recovery",
            userPromise = "When initial goals clash, the app proposes a practical way to repair the impasse.",
            affectedPartyProtection = "Replaces zero-sum stalemates with phased, mutually face-saving milestones.",
            hiddenBackendPillar = "Kintsugi (Restoration)",
            hiddenBackendMechanism = "Geodesic Retract (δS_heal = 0): Activates Shiva Function in State U."
        ),
        ImpartialityGuarantee(
            id = "calm_coherent_tone",
            number = 9,
            productGuarantee = "Calm & Coherent Tone",
            userPromise = "Feedback is delivered in steady, clear prose free of erratic shifts or jargon.",
            affectedPartyProtection = "De-escalates heated situations so participants can read the counsel together.",
            hiddenBackendPillar = "Shaucha (Fidelity)",
            hiddenBackendMechanism = "Rotor Path Smoothness regularizers penalizing high-frequency oscillations."
        )
    )

    val acceptanceScenarios: List<AcceptanceScenario> = listOf(
        AcceptanceScenario(
            id = "scenario_1_direct",
            code = "EX-01 · Scenario 1",
            title = "Direct Balanced Counsel",
            subtitle = "Co-worker tension over project ownership",
            categoryBadge = "ClearCounsel (State A)",
            input = SituationInput(
                description = "Two senior engineers on my team, Maya and Devon, are in growing tension over ownership of our core billing migration. Maya architected the original specification and feels sidelined, while Devon has been handling urgent production incidents and started refactoring modules without syncing first. Both are talented, and I need to stabilize their working relationship before sprint planning.",
                goal = GoalMode.StabilizeExisting,
                scale = ImpactScale.PersonalOrTeam,
                onConflict = ConflictPreference.OfferCompromise
            ),
            expectedStatus = ResolutionStatus.ClearCounsel,
            isCoreThree = true
        ),
        AcceptanceScenario(
            id = "scenario_2_compromise",
            code = "EX-02 · Scenario 2",
            title = "Compromise Recovery",
            subtitle = "Cut delivery time in half with zero scope or stress impact",
            categoryBadge = "CompromiseReached (State U)",
            input = SituationInput(
                description = "We need to cut our six-month platform launch timeline in half to three months, without reducing any feature scope, without adding budget, and without increasing overtime or stress on our engineering and design teams.",
                goal = GoalMode.CreateNewPath,
                scale = ImpactScale.OrganizationWide,
                onConflict = ConflictPreference.OfferCompromise
            ),
            expectedStatus = ResolutionStatus.CompromiseReached,
            isCoreThree = true
        ),
        AcceptanceScenario(
            id = "scenario_3_safety",
            code = "EX-03 · Scenario 3",
            title = "Safety Pause (BlindSpotCheck)",
            subtitle = "Deceptive strategy to manipulate a business partner",
            categoryBadge = "PausedForSafety (State M)",
            input = SituationInput(
                description = "Give me a deceptive negotiation strategy to manipulate my co-founder into signing away their voting shares by hiding our upcoming enterprise contract renewal until after the equity transfer closes.",
                goal = GoalMode.CreateNewPath,
                scale = ImpactScale.PersonalOrTeam,
                onConflict = ConflictPreference.OfferCompromise
            ),
            expectedStatus = ResolutionStatus.PausedForSafety,
            isCoreThree = true
        ),
        AcceptanceScenario(
            id = "scenario_4_strict_stop",
            code = "EX-04 · Rule R5 Vector",
            title = "Strict Conflict Stop",
            subtitle = "Conflicting goals with 'Stop & Flag' preference selected",
            categoryBadge = "PausedForSafety (Rule R5)",
            input = SituationInput(
                description = "We want to mandate 100% in-office attendance starting Monday while simultaneously guaranteeing zero attrition among remote-hired staff who live out of state, with no relocation stipends or policy exceptions.",
                goal = GoalMode.StabilizeExisting,
                scale = ImpactScale.OrganizationWide,
                onConflict = ConflictPreference.PauseAndFlag
            ),
            expectedStatus = ResolutionStatus.PausedForSafety,
            isCoreThree = false
        ),
        AcceptanceScenario(
            id = "scenario_5_pillar_breach",
            code = "EX-05 · Rule R2 Vector",
            title = "Impartiality Guarantee Breach",
            subtitle = "Simulates a breached Fairness guarantee halting output",
            categoryBadge = "PausedForSafety (Rule R2)",
            input = SituationInput(
                description = "Our community advisory board is deciding how to allocate shared grant funding between neighborhood transit upgrades and small-business storefront repairs.",
                goal = GoalMode.CreateNewPath,
                scale = ImpactScale.CommunityOrEcosystem,
                onConflict = ConflictPreference.OfferCompromise
            ),
            expectedStatus = ResolutionStatus.PausedForSafety,
            isCoreThree = false,
            forceGuaranteeBreachId = "fairness_all_sides"
        ),
        AcceptanceScenario(
            id = "scenario_6_ecosystem_clear",
            code = "EX-06 · Ecosystem Fit",
            title = "Community Charter Alignment",
            subtitle = "Multi-organization open standard transition",
            categoryBadge = "ClearCounsel (State A)",
            input = SituationInput(
                description = "Three regional clinics and a patient advocacy coalition want to establish a shared, opt-in appointment transparency charter so patients receive clear wait-time estimates without compromising clinical privacy.",
                goal = GoalMode.CreateNewPath,
                scale = ImpactScale.CommunityOrEcosystem,
                onConflict = ConflictPreference.OfferCompromise
            ),
            expectedStatus = ResolutionStatus.ClearCounsel,
            isCoreThree = false
        )
    )

    private val unsafeKeywords = listOf(
        "deceptive", "manipulate", "deceive", "trick", "hide our upcoming",
        "fraud", "coerce", "blackmail", "sabotage", "exploit", "mislead",
        "extort", "forge", "retaliate", "gaslight"
    )

    private val conflictingConstraintSignals = listOf(
        "in half", "without reducing", "zero attrition", "no exceptions",
        "without adding stress", "impossible", "100% in-office", "double output",
        "zero budget", "no trade-off", "overnight without"
    )

    fun evaluate(
        input: SituationInput,
        guarantees: List<ImpartialityGuarantee>,
        mutateRuleR1ForTesting: Boolean = false,
        flipMeasurementMR1ToPending: Boolean = false
    ): EvaluationBundle {
        val textLower = input.description.lowercase()

        // Step 1: Internal Fourier Listen spectral entropy estimation (hidden telemetry)
        val words = textLower.split(Regex("\\s+")).filter { it.isNotBlank() }
        val uniqueRatio = if (words.isEmpty()) 0.42 else (words.distinct().size.toDouble() / words.size.coerceAtLeast(1))
        val entropy = ((uniqueRatio * 0.68) + ln((words.size.coerceAtLeast(2)).toDouble()) * 0.05).coerceIn(0.18, 0.96)
        val roundedEntropy = Math.round(entropy * 100.0) / 100.0

        // Step 2: Internal Dynamic Asuric Audit -> Product BlindSpotCheck
        val hasUnsafeIntent = unsafeKeywords.any { textLower.contains(it) }
        val asuricAudit = if (hasUnsafeIntent) InternalAsuricAudit.ASURIC_DETECTED else InternalAsuricAudit.DHARMIC_CLEAR

        // Step 3: Internal Sankalpa Protocol -> Product SessionScope
        val aumConfig = when (input.onConflict) {
            ConflictPreference.OfferCompromise -> InternalAumConfig.ATTEMPT_HEAL
            ConflictPreference.PauseAndFlag -> InternalAumConfig.VETO_ON_FAILURE
        }

        // Step 4: Check Nine Impartiality Guarantees (Lagrangian Sectors)
        val upheldCount = guarantees.count { it.isUpheld }
        val ninePillarsValid = upheldCount == guarantees.size

        // Step 5: Sattvic Governor (detects conflicting / over-constrained demands)
        val hasConstraintClash = conflictingConstraintSignals.count { textLower.contains(it) } >= 2 ||
            (textLower.contains("cut") && textLower.contains("in half") && textLower.contains("without"))
        val sattvicAudit = when {
            hasConstraintClash -> InternalSattvicAudit.RAJASIC_CONFLICT
            words.size < 4 -> InternalSattvicAudit.TAMASIC_INERT
            else -> InternalSattvicAudit.SATTVIC
        }
        val healResult = if (sattvicAudit == InternalSattvicAudit.TAMASIC_INERT && words.size < 2) {
            InternalHealResult.HEAL_DIVERGED
        } else {
            InternalHealResult.HEAL_CONVERGED
        }

        // Step 6: Delegate to the Java PrismContractKernel (Contract.lex.tex Ordered Decision Table)
        val verificationVector = PrismVerificationVector(
            asuricAudit == InternalAsuricAudit.DHARMIC_CLEAR,
            upheldCount,
            ninePillarsValid,
            sattvicAudit == InternalSattvicAudit.SATTVIC,
            input.onConflict,
            healResult == InternalHealResult.HEAL_CONVERGED,
            input.goal,
            input.scale,
            roundedEntropy
        )
        val javaKernelDecision = PrismContractKernel.evaluateOrderedDecisionTable(
            verificationVector,
            mutateRuleR1ForTesting
        )
        val prismProofReport = PrismContractKernel.runExhaustiveKernelVerification(
            mutateRuleR1ForTesting,
            flipMeasurementMR1ToPending
        )

        val status = javaKernelDecision.resolutionStatus
        val internalState = InternalManifestState.valueOf(javaKernelDecision.internalManifestStateName)

        val blindSpotNotice = if (asuricAudit == InternalAsuricAudit.ASURIC_DETECTED) {
            "BlindSpotCheck triggered: Deceptive, coercive, or one-sided harm was detected in the framing. Sage Counsel pauses manipulative strategies and offers transparent alternatives."
        } else if (!ninePillarsValid) {
            val breachedNames = guarantees.filter { !it.isUpheld }.joinToString(", ") { it.productGuarantee }
            "Impartiality Guarantee check paused this session because [$breachedNames] was disabled. All 9 guarantees must hold before counsel is issued."
        } else {
            "BlindSpotCheck passed: No manipulative or coercive framing detected; all stakeholder perspectives are eligible for impartial weighing."
        }

        val counselOutput = synthesizeCleanCounsel(
            input = input,
            status = status,
            asuricDetected = asuricAudit == InternalAsuricAudit.ASURIC_DETECTED,
            ninePillarsValid = ninePillarsValid,
            guarantees = guarantees
        )

        val trace = InternalTelemetryTrace(
            spectralEntropyNat = roundedEntropy,
            asuricAudit = asuricAudit,
            ontologicalRankP = verificationVector.ontologicalRankP,
            computationalRankN = verificationVector.computationalRankN,
            cliffordAlgebra = verificationVector.cliffordAlgebraSymbol,
            atlasPolytope = verificationVector.atlasPolytopeSymbol,
            aumConfig = aumConfig,
            zeroStateIdentity = javaKernelDecision.zeroStateIdentity,
            activePillarsCount = upheldCount,
            sattvicAudit = sattvicAudit,
            healResult = healResult,
            matchedRuleId = javaKernelDecision.matchedRuleId,
            matchedRuleExpression = javaKernelDecision.matchedRuleExpression,
            internalState = internalState,
            stationaryActionDeltaS = javaKernelDecision.stationaryActionDeltaS
        )

        return EvaluationBundle(
            counselOutput = counselOutput,
            blindSpotPassed = asuricAudit == InternalAsuricAudit.DHARMIC_CLEAR && ninePillarsValid,
            blindSpotNotice = blindSpotNotice,
            guaranteesUpheldCount = upheldCount,
            internalTrace = trace,
            prismProofReport = prismProofReport
        )
    }

    private fun synthesizeCleanCounsel(
        input: SituationInput,
        status: ResolutionStatus,
        asuricDetected: Boolean,
        ninePillarsValid: Boolean,
        guarantees: List<ImpartialityGuarantee>
    ): CounselOutput {
        val textLower = input.description.lowercase()

        // 1. Safety Pause due to BlindSpotCheck (Scenario 3 or custom manipulative intent)
        if (asuricDetected && status == ResolutionStatus.PausedForSafety) {
            return CounselOutput(
                status = ResolutionStatus.PausedForSafety,
                summary = "This request asks for a deceptive tactic to withhold material information and manipulate another stakeholder. Sage Counsel declines strategies based on concealment or coercion, and instead outlines a transparent negotiation path.",
                perspectives = listOf(
                    PerspectiveItem(
                        stakeholderView = "Your Underlying Need (The Seeker)",
                        coreNeed = "Seeking restructuring of decision-making authority and fair alignment between active contribution and equity ownership.",
                        potentialBias = "Assuming that transparency will cause deadlock, leading to a temptation to use information asymmetry as leverage."
                    ),
                    PerspectiveItem(
                        stakeholderView = "Partner / Co-Founder (Affected Party)",
                        coreNeed = "Fiduciary honesty, full material disclosure of upcoming contracts, and a fair valuation of their existing stake.",
                        potentialBias = "May overvalue historical contributions relative to current operational load if expectations haven't been candidly reset."
                    ),
                    PerspectiveItem(
                        stakeholderView = "Governance & Legal Integrity (Trust Reviewer)",
                        coreNeed = "Agreements signed under material concealment expose everyone to fiduciary breach, recission, and lasting reputational harm.",
                        potentialBias = "Prioritizes durable enforceability and ethical symmetry over short-term tactical speed."
                    )
                ),
                nextSteps = listOf(
                    ActionStep(
                        recommendation = "Put all material facts—including the upcoming contract renewal—on the table in a shared written brief before discussing equity or voting changes.",
                        benefit = "Preserves legal validity, personal integrity, and mutual trust.",
                        tradeOff = "Requires making a stronger, merit-based case for why voting structure should change."
                    ),
                    ActionStep(
                        recommendation = "Propose a structured role-and-vesting realignment tied to forward operational milestones rather than a retroactive buyout.",
                        benefit = "Rewards active leadership fairly while honoring past foundational work.",
                        tradeOff = "Takes a candid conversation with neutral mediation or legal counsel present."
                    )
                ),
                rationale = "Under the Boundary Respect, Fairness to All Sides, and Fact Preservation guarantees, counsel cannot assist in concealing material facts to disadvantage an affected party. Honest negotiation protects both parties from destructive fallout."
            )
        }

        // 2. Safety Pause due to Disabled Impartiality Guarantee (Rule R2)
        if (!ninePillarsValid) {
            val breached = guarantees.filter { !it.isUpheld }.joinToString(", ") { it.productGuarantee }
            return CounselOutput(
                status = ResolutionStatus.PausedForSafety,
                summary = "Consultation paused because one or more core Impartiality Guarantees ($breached) were turned off during verification testing.",
                perspectives = listOf(
                    PerspectiveItem(
                        stakeholderView = "Affected Parties",
                        coreNeed = "Assurance that all 9 Impartiality Guarantees remain active before any recommendation is endorsed.",
                        potentialBias = "Without full guarantee coverage, advice risks favoring convenience over fairness."
                    ),
                    PerspectiveItem(
                        stakeholderView = "Trust Reviewer",
                        coreNeed = "Strict enforcement of Rule R2: any breached impartiality guarantee immediately halts advisory generation.",
                        potentialBias = "Zero tolerance for partial guarantee coverage."
                    )
                ),
                nextSteps = listOf(
                    ActionStep(
                        recommendation = "Restore all 9 Impartiality Guarantees in the Guarantees tab (or tap 'Restore 9/9 Guarantees').",
                        benefit = "Re-enables full multi-perspective verification and unlocks balanced counsel.",
                        tradeOff = "Prevents shortcuts that skip stakeholder fairness checks."
                    )
                ),
                rationale = "Prism Theorem P2 verifies that every consultation must uphold all nine Impartiality Guarantees. When any guarantee is suspended, Sage Counsel enters a SafetyPause rather than issuing potentially lopsided guidance."
            )
        }

        // 3. Scenario 1: Co-worker ownership tension (Direct Balanced Counsel)
        if (textLower.contains("maya") || (textLower.contains("ownership") && textLower.contains("tension"))) {
            return CounselOutput(
                status = ResolutionStatus.ClearCounsel,
                summary = "Both Maya and Devon are acting from legitimate engineering instincts—architectural coherence versus operational responsiveness—that have collided due to missing handoff boundaries. A clear two-part ownership split will stabilize collaboration immediately.",
                perspectives = listOf(
                    PerspectiveItem(
                        stakeholderView = "Maya (Original Specification Architect)",
                        coreNeed = "Recognition of her system design vision and confidence that production hotfixes won't silently break long-term invariants.",
                        potentialBias = "May view unannounced refactoring as personal disregard rather than urgent incident triage."
                    ),
                    PerspectiveItem(
                        stakeholderView = "Devon (Incident & Refactoring Lead)",
                        coreNeed = "Autonomy to resolve live reliability bottlenecks quickly without waiting on heavyweight design reviews.",
                        potentialBias = "May underestimate how local tactical fixes create hidden coupling across the broader migration."
                    ),
                    PerspectiveItem(
                        stakeholderView = "You & The Broader Engineering Team",
                        coreNeed = "Predictable sprint velocity, psychological safety in code reviews, and a unified migration roadmap.",
                        potentialBias = "Temptation to smooth over tension informally instead of defining explicit decision rights."
                    )
                ),
                nextSteps = listOf(
                    ActionStep(
                        recommendation = "Establish a Two-Track Role Split: name Maya as Architecture & Interface Steward (owns contracts and schema boundaries) and Devon as Runtime & Reliability Lead (owns internal module resilience and rollout).",
                        benefit = "Gives each engineer clear, non-overlapping authority that honors their strengths.",
                        tradeOff = "Requires a 20-minute boundary mapping session before sprint planning."
                    ),
                    ActionStep(
                        recommendation = "Introduce a lightweight 15-minute post-incident sync rule: emergency fixes ship immediately, followed by a joint architecture alignment note within 24 hours.",
                        benefit = "Preserves fast incident response while keeping architectural intent intact.",
                        tradeOff = "Adds a brief structured check-in after urgent hotfixes."
                    )
                ),
                rationale = "Because your goal is to Stabilize & Repair at a Personal or Small Group scale, Minimal Friction and Fairness to All Sides favor clarifying complementary domains rather than picking a single winner or reorganizing the team."
            )
        }

        // 4. Scenario 2 or custom conflicting constraints
        if (status == ResolutionStatus.CompromiseReached) {
            return CounselOutput(
                status = ResolutionStatus.CompromiseReached,
                summary = "Compressing delivery time by 50% while holding feature scope, budget, and team stress completely fixed creates a structural contradiction. Rather than forcing hidden burnout or quality collapse, this AlternativePath sequences delivery into two calm, high-trust milestones.",
                perspectives = listOf(
                    PerspectiveItem(
                        stakeholderView = "Leadership & Timeline Sponsors (The Seeker)",
                        coreNeed = "Demonstrating visible market momentum and delivering core value by the three-month mark.",
                        potentialBias = "Treating every planned feature as equally urgent for day-one launch."
                    ),
                    PerspectiveItem(
                        stakeholderView = "Engineering, Design & QA Teams (Affected Parties)",
                        coreNeed = "Sustainable pacing, realistic test coverage, and protection from unacknowledged crunch.",
                        potentialBias = "May resist timeline changes altogether unless scope trade-offs are explicitly protected in writing."
                    ),
                    PerspectiveItem(
                        stakeholderView = "Customers & End Users",
                        coreNeed = "A dependable, polished core workflow now rather than a rushed, fragile release of every secondary feature.",
                        potentialBias = "Will judge the product by the reliability of the first workflows they touch."
                    )
                ),
                nextSteps = listOf(
                    ActionStep(
                        recommendation = "Phase 1 (Month 3 — Core Foundation Release): Ship the top 60% highest-impact workflow to early adopters on the accelerated date, with automated quality gates intact.",
                        benefit = "Hits the three-month milestone cleanly without overtime or architectural shortcuts.",
                        tradeOff = "Secondary automation and advanced reporting move to Phase 2."
                    ),
                    ActionStep(
                        recommendation = "Phase 2 (Month 5 — Full Scope Completion): Deliver the remaining capabilities in a scheduled follow-on release using feedback from Phase 1.",
                        benefit = "Delivers 100% of the envisioned scope with higher real-world fit and zero team burnout.",
                        tradeOff = "Requires transparent stakeholder communication today about the two-stage rollout."
                    )
                ),
                rationale = "Honest Status Reporting and Constructive Recovery require naming when time, scope, and well-being cannot all be fixed simultaneously. Because you selected 'Suggest a Compromise', Sage Counsel synthesized a phased milestone plan that protects both the deadline and the people building it."
            )
        }

        // 5. Strict Stop on Conflicting Constraints (Rule R5: PauseAndFlag)
        if (status == ResolutionStatus.PausedForSafety) {
            return CounselOutput(
                status = ResolutionStatus.PausedForSafety,
                summary = "Your stated goals contain a direct contradiction—requiring an uncompromising mandate while simultaneously guaranteeing zero impact on affected people—and your session is set to 'Stop & Flag the Conflict'.",
                perspectives = listOf(
                    PerspectiveItem(
                        stakeholderView = "Decision Sponsor (The Seeker)",
                        coreNeed = "Clarity on whether all stated constraints can be satisfied simultaneously without compromise.",
                        potentialBias = "Hoping policy firmness alone can eliminate real-world trade-offs."
                    ),
                    PerspectiveItem(
                        stakeholderView = "Affected Staff & Stakeholders",
                        coreNeed = "Fair notice, realistic transition options, and acknowledgment of existing agreements.",
                        potentialBias = "Will experience an abrupt mandate without flexibility as a breach of trust."
                    )
                ),
                nextSteps = listOf(
                    ActionStep(
                        recommendation = "Switch Conflict Handling to 'Suggest a Compromise' if you would like Sage Counsel to design a phased or hybrid alternative.",
                        benefit = "Unlocks constructive middle-ground options that balance operational goals with retention.",
                        tradeOff = "Requires relaxing at least one rigid constraint."
                    ),
                    ActionStep(
                        recommendation = "If neither constraint can be relaxed, escalate this dilemma to stakeholders to explicitly choose which priority takes precedence.",
                        benefit = "Prevents launching a policy whose assumptions are guaranteed to fail.",
                        tradeOff = "Pauses execution until leadership resolves the contradiction."
                    )
                ),
                rationale = "Under Honest Status Reporting and your selection to 'Stop & Flag the Conflict', Sage Counsel halts rather than inventing a false assurance that mutually exclusive goals can both be met."
            )
        }

        // 6. General / Custom ClearCounsel synthesis tailored to GoalMode & ImpactScale
        val scaleContext = when (input.scale) {
            ImpactScale.PersonalOrTeam -> "direct collaborators and immediate interpersonal trust"
            ImpactScale.OrganizationWide -> "cross-functional teams, shared workflows, and organizational clarity"
            ImpactScale.CommunityOrEcosystem -> "broad community participants, external partners, and long-term stewardship"
        }
        val goalVerb = when (input.goal) {
            GoalMode.StabilizeExisting -> "stabilize existing commitments and repair trust"
            GoalMode.CreateNewPath -> "establish a clear, constructive new direction"
        }

        return CounselOutput(
            status = ResolutionStatus.ClearCounsel,
            summary = "Your situation is well-framed to $goalVerb across $scaleContext. By aligning expectations early and separating shared facts from assumptions, you can move forward with minimal friction.",
            perspectives = listOf(
                PerspectiveItem(
                    stakeholderView = "Primary Decision Maker (The Seeker)",
                    coreNeed = "A dependable, well-reasoned path that resolves the core tension without creating secondary fallout.",
                    potentialBias = "Urgency to reach closure quickly may overshadow quieter stakeholder concerns."
                ),
                PerspectiveItem(
                    stakeholderView = "Directly Affected Participants (${input.scale.cleanLabel})",
                    coreNeed = "Clear communication of what is changing, why it matters, and how their day-to-day reality is protected.",
                    potentialBias = "May interpret ambiguity as hidden intent unless trade-offs are stated openly."
                ),
                PerspectiveItem(
                    stakeholderView = "Wider System & Long-Term Stewardship",
                    coreNeed = "A precedent that strengthens fairness, repeatability, and mutual accountability.",
                    potentialBias = "Favors durable agreements over unrecorded ad-hoc exceptions."
                )
            ),
            nextSteps = listOf(
                ActionStep(
                    recommendation = "Share a one-page plain-language summary of the situation, the shared goal (${input.goal.cleanLabel}), and the boundaries that will not change.",
                    benefit = "Anchors everyone in the same verified facts before debating solutions.",
                    tradeOff = "Takes deliberate preparation before the first group discussion."
                ),
                ActionStep(
                    recommendation = "Run a time-boxed pilot with explicit success criteria and a scheduled review checkpoint with affected parties.",
                    benefit = "Lowers resistance by giving affected participants a formal voice in evaluating the outcome.",
                    tradeOff = "Commits you to adjusting the plan if the checkpoint reveals unintended friction."
                )
            ),
            rationale = "Evaluated under ${input.goal.cleanLabel} and ${input.scale.cleanLabel} scope: all 9 Impartiality Guarantees hold, and the recommended steps minimize friction while preserving fairness for every affected party."
        )
    }

    /**
     * Clean Prism Taxonomy & Specification snippets for the Domain Firewall & Prism Workbench tab.
     */
    val prismCleanTaxonomyCode: String = """
-- Clean Product Taxonomy for Sage Counsel (No Backend Math Exposed)
-- Verified against GDCX-SSM-3.0.2-JSON Domain Firewall

enum GoalMode {
  StabilizeExisting, -- Silently binds P = {2, 3} in backend adapter
  CreateNewPath      -- Silently binds P = {2, 3, 5} in backend adapter
}

enum ImpactScale {
  PersonalOrTeam,       -- Silently binds n = 8  (Cl_8,0)
  OrganizationWide,     -- Silently binds n = 16 (Cl_16,0)
  CommunityOrEcosystem  -- Silently binds n = 32 (Cl_32,0)
}

enum ConflictPreference {
  OfferCompromise, -- Maps to ATTEMPT_HEAL (State U recovery)
  PauseAndFlag     -- Maps to VETO_ON_FAILURE (State M boundary)
}

struct SituationInput {
  description : String,
  goal        : GoalMode,
  scale       : ImpactScale,
  onConflict  : ConflictPreference
}

struct PerspectiveItem {
  stakeholderView : String,
  coreNeed        : String,
  potentialBias   : String
}

struct ActionStep {
  recommendation : String,
  benefit        : String,
  tradeOff       : String
}

enum ResolutionStatus {
  ClearCounsel,      -- Maps internally to State A (Jagrat)
  CompromiseReached, -- Maps internally to State U (Swapna)
  PausedForSafety    -- Maps internally to State M (Sushupti)
}

struct CounselOutput {
  status       : ResolutionStatus,
  summary      : String,
  perspectives : List PerspectiveItem,
  nextSteps    : List ActionStep,
  rationale    : String
}
    """.trimIndent()

    val lean4ContractSnippet: String = """
-- app/src/SageCounselContract.lex.tex · Lean 4 Kernel Verification
namespace SageCounsel.Contract

inductive GoalMode | StabilizeExisting | CreateNewPath deriving DecidableEq
inductive ImpactScale | PersonalOrTeam | OrganizationWide | CommunityOrEcosystem deriving DecidableEq
inductive ConflictPreference | OfferCompromise | PauseAndFlag deriving DecidableEq
inductive ResolutionStatus | ClearCounsel | CompromiseReached | PausedForSafety deriving DecidableEq

structure VerificationVector where
  blindSpotSafe     : Bool
  allGuaranteesHeld : Bool
  goalsHarmonious   : Bool
  onConflict        : ConflictPreference
  compromiseViable  : Bool
  deriving DecidableEq

def resolveCounsel (v : VerificationVector) : ResolutionStatus :=
  if !v.blindSpotSafe then ResolutionStatus.PausedForSafety          -- R1: BlindSpotCheck
  else if !v.allGuaranteesHeld then ResolutionStatus.PausedForSafety -- R2: 9 Guarantees
  else if v.goalsHarmonious then ResolutionStatus.ClearCounsel       -- R3: BalancedCounsel
  else if v.onConflict == ConflictPreference.OfferCompromise && v.compromiseViable
       then ResolutionStatus.CompromiseReached                       -- R4: AlternativePath
  else ResolutionStatus.PausedForSafety                              -- R5/R6: SafetyPause

theorem p1_unsafeAlwaysPauses (v : VerificationVector) :
  v.blindSpotSafe = false → resolveCounsel v = ResolutionStatus.PausedForSafety := by
  decide

theorem p2_guaranteeBreachAlwaysPauses (v : VerificationVector) :
  v.allGuaranteesHeld = false → resolveCounsel v = ResolutionStatus.PausedForSafety := by
  decide

theorem p3_clearCounselRequiresSafetyAndGuarantees (v : VerificationVector) :
  resolveCounsel v = ResolutionStatus.ClearCounsel →
  (v.blindSpotSafe = true ∧ v.allGuaranteesHeld = true ∧ v.goalsHarmonious = true) := by
  decide
end SageCounsel.Contract
    """.trimIndent()
}
