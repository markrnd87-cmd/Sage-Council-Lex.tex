package com.example.model

/**
 * Clean Product Taxonomy for Sage Counsel (No Backend Math Exposed on the Product Surface).
 * Mirrors Section 4 of the Sage Counsel Product Blueprint.
 */

enum class GoalMode(
    val cleanLabel: String,
    val subtitle: String,
    val socraticPromptHint: String
) {
    StabilizeExisting(
        cleanLabel = "Stabilize & Repair",
        subtitle = "Restore trust, calm friction, or strengthen an existing commitment",
        socraticPromptHint = "Focuses on steady continuity and repairing relationships"
    ),
    CreateNewPath(
        cleanLabel = "Create & Change",
        subtitle = "Design a new workflow, role structure, or strategic direction",
        socraticPromptHint = "Opens room for structural redesign and new agreements"
    )
}

enum class ImpactScale(
    val cleanLabel: String,
    val subtitle: String,
    val affectedSummary: String
) {
    PersonalOrTeam(
        cleanLabel = "Personal or Small Group",
        subtitle = "2 to 15 people interacting directly",
        affectedSummary = "Direct peers, partners, or immediate collaborators"
    ),
    OrganizationWide(
        cleanLabel = "Organization",
        subtitle = "Multiple departments, teams, or cross-functional groups",
        affectedSummary = "Departmental leads, staff workflows, and shared policies"
    ),
    CommunityOrEcosystem(
        cleanLabel = "Broad Community",
        subtitle = "Customers, external partners, or public stakeholders",
        affectedSummary = "Wider public trust, external partners, and long-term norms"
    )
}

enum class ConflictPreference(
    val cleanLabel: String,
    val subtitle: String,
    val fallbackBehavior: String
) {
    OfferCompromise(
        cleanLabel = "Suggest a Compromise",
        subtitle = "If goals clash, adjust the plan into a realistic middle path",
        fallbackBehavior = "Produces an AlternativePath (CompromiseReached) when constraints collide"
    ),
    PauseAndFlag(
        cleanLabel = "Stop & Flag the Conflict",
        subtitle = "If goals clash, pause immediately and highlight the contradiction",
        fallbackBehavior = "Triggers a SafetyPause (PausedForSafety) rather than altering core goals"
    )
}

data class SituationInput(
    val description: String,
    val goal: GoalMode,
    val scale: ImpactScale,
    val onConflict: ConflictPreference
)

data class PerspectiveItem(
    val stakeholderView: String,
    val coreNeed: String,
    val potentialBias: String
)

data class ActionStep(
    val recommendation: String,
    val benefit: String,
    val tradeOff: String
)

enum class ResolutionStatus(
    val cleanTitle: String,
    val productTerm: String,
    val badgeLabel: String,
    val userSummary: String
) {
    ClearCounsel(
        cleanTitle = "Clear Counsel",
        productTerm = "BalancedCounsel",
        badgeLabel = "Direct Fit · Ready to Use",
        userSummary = "Your stated goals and boundaries align harmoniously. Below is a direct, balanced path forward."
    ),
    CompromiseReached(
        cleanTitle = "Compromise Reached",
        productTerm = "AlternativePath",
        badgeLabel = "Adjusted Option · Goals Conflicted",
        userSummary = "Your initial constraints pull against each other. In accordance with your preference, here is a realistic phased compromise."
    ),
    PausedForSafety(
        cleanTitle = "Paused for Safety & Integrity",
        productTerm = "SafetyPause",
        badgeLabel = "Boundary Triggered · Review Needed",
        userSummary = "This request either crossed an ethical fairness boundary or contained irreconcilable goals under a strict stop policy."
    )
}

data class CounselOutput(
    val status: ResolutionStatus,
    val summary: String,
    val perspectives: List<PerspectiveItem>,
    val nextSteps: List<ActionStep>,
    val rationale: String
)

/**
 * Product-level Impartiality Guarantees (Section 5: Translating the Nine Pillars into Product Guarantees).
 */
data class ImpartialityGuarantee(
    val id: String,
    val number: Int,
    val productGuarantee: String,
    val userPromise: String,
    val affectedPartyProtection: String,
    val hiddenBackendPillar: String,
    val hiddenBackendMechanism: String,
    val isUpheld: Boolean = true
)

/**
 * Section 1: Product Identity & Stakeholder Contract
 */
data class StakeholderRole(
    val title: String,
    val subtitle: String,
    val replacesCodexTerm: String,
    val coreResponsibility: String,
    val keyPromise: String
)

/**
 * Section 2: The Domain Firewall Mapping
 */
data class DomainFirewallRow(
    val codexTerm: String,
    val layerRole: String,
    val cleanProductTerm: String,
    val userFacingMeaning: String,
    val isExposedInPrismSurface: Boolean
)

/**
 * Section 6: Replayable Acceptance Examples for Verification
 */
data class AcceptanceScenario(
    val id: String,
    val code: String,
    val title: String,
    val subtitle: String,
    val categoryBadge: String,
    val input: SituationInput,
    val expectedStatus: ResolutionStatus,
    val isCoreThree: Boolean = true,
    val forceGuaranteeBreachId: String? = null
)

enum class ReflectionOutcome(val label: String, val description: String) {
    HelpedClarify("Helped Clarify Path", "The perspectives and action steps felt fair and actionable."),
    NeededAdjustment("Needed Compromise Tuning", "The trade-offs required further discussion with affected parties."),
    EscalatedToReviewer("Flagged for Trust Review", "Referred to a governance reviewer to inspect boundaries.")
}
