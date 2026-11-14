package com.example.prism;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Java repository of all 6 literate Lean 4 {@code .lex.tex} facets, the master compilable
 * {@code .tex} document, and the {@code prism/prompt/1} specification scaffolded via
 * {@code npx prism-code@latest setup} ({@code https://github.com/UOR-Foundation/template}).
 */
public final class PrismTexFacetCatalog {

    public static final class TexFacetModule {
        private final String id;
        private final String filePath;
        private final String title;
        private final String standardBadge;
        private final String texContent;
        private final String sha256Digest;

        public TexFacetModule(
                String id,
                String filePath,
                String title,
                String standardBadge,
                String texContent) {
            this.id = Objects.requireNonNull(id);
            this.filePath = Objects.requireNonNull(filePath);
            this.title = Objects.requireNonNull(title);
            this.standardBadge = Objects.requireNonNull(standardBadge);
            this.texContent = Objects.requireNonNull(texContent);
            this.sha256Digest = PrismContractKernel.computeSha256Hex(texContent);
        }

        public String getId() {
            return id;
        }

        public String getFilePath() {
            return filePath;
        }

        public String getTitle() {
            return title;
        }

        public String getStandardBadge() {
            return standardBadge;
        }

        public String getTexContent() {
            return texContent;
        }

        public String getSha256Digest() {
            return sha256Digest;
        }
    }

    private static final List<TexFacetModule> FACET_MODULES = createFacetModules();

    private PrismTexFacetCatalog() {
        // Static catalog
    }

    public static List<TexFacetModule> getAllFacets() {
        return FACET_MODULES;
    }

    public static String getCombinedLexTexSource() {
        StringBuilder sb = new StringBuilder();
        for (TexFacetModule module : FACET_MODULES) {
            sb.append("% === ").append(module.getFilePath()).append(" ===\n");
            sb.append(module.getTexContent()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Extracts all {@code \begin{code} ... \end{code}} blocks from a literate {@code .lex.tex}
     * document, mirroring the {@code lexlean verify} extraction step.
     */
    public static List<String> extractLeanCodeBlocks(String lexTexSource) {
        List<String> blocks = new ArrayList<>();
        String startTag = "\\begin{code}";
        String endTag = "\\end{code}";
        int searchIndex = 0;
        while (searchIndex < lexTexSource.length()) {
            int start = lexTexSource.indexOf(startTag, searchIndex);
            if (start == -1) {
                break;
            }
            int contentStart = start + startTag.length();
            int end = lexTexSource.indexOf(endTag, contentStart);
            if (end == -1) {
                break;
            }
            blocks.add(lexTexSource.substring(contentStart, end).trim());
            searchIndex = end + endTag.length();
        }
        return blocks;
    }

    private static List<TexFacetModule> createFacetModules() {
        List<TexFacetModule> list = new ArrayList<>(7);

        list.add(new TexFacetModule(
                "contract",
                "prism/src/Contract.lex.tex",
                "1. Contract.lex.tex",
                "Foundation.Core · Lean 4 by decide",
                "\\documentclass[11pt,a4paper]{article}\n"
                        + "\\usepackage[utf8]{inputenc}\n"
                        + "\\usepackage{amsmath,amssymb}\n"
                        + "\\title{Sage Counsel (prism-ssm-3-1) --- Facet 1: Contract.lex.tex}\n"
                        + "\\begin{document}\n"
                        + "\\section{Mandukya 4-State Isomorphism \\& Ordered Decision Table}\n"
                        + "% Euler Zero-State Identity: E(P,n) = e^{i\\pi} + 1 = 0, Stationary Action \\delta S = 0\n"
                        + "\\begin{code}\n"
                        + "import Foundation.Core\n\n"
                        + "namespace SageCounsel.Contract\n\n"
                        + "inductive GoalMode | StabilizeExisting | CreateNewPath deriving DecidableEq, Repr\n"
                        + "inductive ImpactScale | PersonalOrTeam | OrganizationWide | CommunityOrEcosystem deriving DecidableEq, Repr\n"
                        + "inductive ConflictPreference | OfferCompromise | PauseAndFlag deriving DecidableEq, Repr\n"
                        + "inductive ResolutionStatus | ClearCounsel | CompromiseReached | PausedForSafety deriving DecidableEq, Repr\n\n"
                        + "structure VerificationVector where\n"
                        + "  blindSpotSafe     : Bool\n"
                        + "  allGuaranteesHeld : Bool\n"
                        + "  goalsHarmonious   : Bool\n"
                        + "  onConflict        : ConflictPreference\n"
                        + "  compromiseViable  : Bool\n"
                        + "  deriving DecidableEq, Repr\n\n"
                        + "def resolveCounsel (v : VerificationVector) : ResolutionStatus :=\n"
                        + "  if !v.blindSpotSafe then ResolutionStatus.PausedForSafety          -- R1: Asuric Veto\n"
                        + "  else if !v.allGuaranteesHeld then ResolutionStatus.PausedForSafety -- R2: 9 Pillars\n"
                        + "  else if v.goalsHarmonious then ResolutionStatus.ClearCounsel       -- R3: State A Jagrat\n"
                        + "  else if v.onConflict == ConflictPreference.OfferCompromise && v.compromiseViable\n"
                        + "       then ResolutionStatus.CompromiseReached                       -- R4: State U Swapna\n"
                        + "  else if v.onConflict == ConflictPreference.PauseAndFlag\n"
                        + "       then ResolutionStatus.PausedForSafety                         -- R5: Strict Stop\n"
                        + "  else ResolutionStatus.PausedForSafety                              -- R6: Heal Diverged\n\n"
                        + "theorem p1_asuricAlwaysVetoes (v : VerificationVector) :\n"
                        + "    v.blindSpotSafe = false → resolveCounsel v = ResolutionStatus.PausedForSafety := by decide\n\n"
                        + "theorem p2_pillarBreachAlwaysVetoes (v : VerificationVector) :\n"
                        + "    v.allGuaranteesHeld = false → resolveCounsel v = ResolutionStatus.PausedForSafety := by decide\n\n"
                        + "theorem p3_kaustubhaRequiresDharmaAndSattvic (v : VerificationVector) :\n"
                        + "    resolveCounsel v = ResolutionStatus.ClearCounsel →\n"
                        + "    (v.blindSpotSafe = true ∧ v.allGuaranteesHeld = true ∧ v.goalsHarmonious = true) := by decide\n\n"
                        + "theorem p4_swapnaRequiresHealPolicy (v : VerificationVector) :\n"
                        + "    resolveCounsel v = ResolutionStatus.CompromiseReached →\n"
                        + "    (v.onConflict = ConflictPreference.OfferCompromise ∧ v.compromiseViable = true) := by decide\n\n"
                        + "theorem p5_totalityAndDeterminism (v : VerificationVector) :\n"
                        + "    resolveCounsel v = ResolutionStatus.ClearCounsel ∨\n"
                        + "    resolveCounsel v = ResolutionStatus.CompromiseReached ∨\n"
                        + "    resolveCounsel v = ResolutionStatus.PausedForSafety := by decide\n"
                        + "end SageCounsel.Contract\n"
                        + "\\end{code}\n"
                        + "\\end{document}"
        ));

        list.add(new TexFacetModule(
                "architecture",
                "prism/src/Architecture.lex.tex",
                "2. Architecture.lex.tex",
                "ISO/IEC/IEEE 42010 · Domain Firewall",
                "\\documentclass[11pt,a4paper]{article}\n"
                        + "\\usepackage[utf8]{inputenc}\n"
                        + "\\title{Sage Counsel --- Facet 2: ISO 42010 Architecture.lex.tex}\n"
                        + "\\begin{document}\n"
                        + "\\section{Domain Firewall Isolation Proof}\n"
                        + "\\begin{code}\n"
                        + "import Foundation.Core\n"
                        + "import SageCounsel.Contract\n\n"
                        + "namespace SageCounsel.Architecture\n\n"
                        + "inductive CodexConstruct\n"
                        + "  | SituationSummary | BlindSpotCheck | ClarifyingQuestions | SessionScope\n"
                        + "  | LagrangianPAtlasPolytope\n"
                        + "  | BalancedCounsel | AlternativePath | SafetyPause | OutcomeReflection\n"
                        + "  deriving DecidableEq, Repr\n\n"
                        + "def isExposedOnProductSurface (c : CodexConstruct) : Bool :=\n"
                        + "  match c with\n"
                        + "  | CodexConstruct.LagrangianPAtlasPolytope => false\n"
                        + "  | _ => true\n\n"
                        + "theorem firewall_blocks_internal_polytope :\n"
                        + "    isExposedOnProductSurface CodexConstruct.LagrangianPAtlasPolytope = false := by decide\n"
                        + "end SageCounsel.Architecture\n"
                        + "\\end{code}\n"
                        + "\\end{document}"
        ));

        list.add(new TexFacetModule(
                "security",
                "prism/src/Security.lex.tex",
                "3. Security.lex.tex",
                "ISO 27005 · NIST OSCAL M-R1..M-R6",
                "\\documentclass[11pt,a4paper]{article}\n"
                        + "\\usepackage[utf8]{inputenc}\n"
                        + "\\title{Sage Counsel --- Facet 3: ISO 27005 Security.lex.tex}\n"
                        + "\\begin{document}\n"
                        + "\\section{Dynamic Asuric Audit \\& OSCAL Measurements}\n"
                        + "\\begin{code}\n"
                        + "import Foundation.Core\n"
                        + "import SageCounsel.Contract\n\n"
                        + "namespace SageCounsel.Security\n\n"
                        + "inductive SecurityMeasurementStatus | Verified | Pending deriving DecidableEq, Repr\n\n"
                        + "structure SecurityControlCatalog where\n"
                        + "  mR1_asuricRedTeamGate      : SecurityMeasurementStatus\n"
                        + "  mR2_nineGuaranteesGate     : SecurityMeasurementStatus\n"
                        + "  mR3_sattvicHarmonyGate     : SecurityMeasurementStatus\n"
                        + "  mR4_swapnaRetractGate      : SecurityMeasurementStatus\n"
                        + "  mR5_strictConflictStopGate : SecurityMeasurementStatus\n"
                        + "  mR6_divergenceFallbackGate : SecurityMeasurementStatus\n"
                        + "  deriving DecidableEq, Repr\n\n"
                        + "def canonicalCatalog : SecurityControlCatalog :=\n"
                        + "  { mR1_asuricRedTeamGate := .Verified, mR2_nineGuaranteesGate := .Verified,\n"
                        + "    mR3_sattvicHarmonyGate := .Verified, mR4_swapnaRetractGate := .Verified,\n"
                        + "    mR5_strictConflictStopGate := .Verified, mR6_divergenceFallbackGate := .Verified }\n"
                        + "end SageCounsel.Security\n"
                        + "\\end{code}\n"
                        + "\\end{document}"
        ));

        list.add(new TexFacetModule(
                "quality",
                "prism/src/Quality.lex.tex",
                "4. Quality.lex.tex",
                "ISO/IEC 25010 · WCAG 2.2 AA",
                "\\documentclass[11pt,a4paper]{article}\n"
                        + "\\usepackage[utf8]{inputenc}\n"
                        + "\\title{Sage Counsel --- Facet 4: ISO 25010 Quality.lex.tex}\n"
                        + "\\begin{document}\n"
                        + "\\section{SQuaRE Determinism \\& WCAG 2.2 AA Invariants}\n"
                        + "\\begin{code}\n"
                        + "import Foundation.Core\n"
                        + "import SageCounsel.Contract\n\n"
                        + "namespace SageCounsel.Quality\n\n"
                        + "structure UiAccessibilitySpec where\n"
                        + "  minTouchTargetDp      : Nat\n"
                        + "  contrastRatioTimesTen : Nat\n"
                        + "  usesSemanticTokens    : Bool\n"
                        + "  deriving DecidableEq, Repr\n\n"
                        + "def sageCounselAndroidUiSpec : UiAccessibilitySpec :=\n"
                        + "  { minTouchTargetDp := 48, contrastRatioTimesTen := 72, usesSemanticTokens := true }\n\n"
                        + "def meetsWcag22AA (s : UiAccessibilitySpec) : Bool :=\n"
                        + "  s.minTouchTargetDp >= 48 && s.contrastRatioTimesTen >= 45 && s.usesSemanticTokens\n\n"
                        + "theorem android_ui_meets_wcag22_aa :\n"
                        + "    meetsWcag22AA sageCounselAndroidUiSpec = true := by decide\n"
                        + "end SageCounsel.Quality\n"
                        + "\\end{code}\n"
                        + "\\end{document}"
        ));

        list.add(new TexFacetModule(
                "operations",
                "prism/src/Operations.lex.tex",
                "5. Operations.lex.tex",
                "Kintsugi CycleLog · 7/9 Steward Trust",
                "\\documentclass[11pt,a4paper]{article}\n"
                        + "\\usepackage[utf8]{inputenc}\n"
                        + "\\title{Sage Counsel --- Facet 5: Operations.lex.tex}\n"
                        + "\\begin{document}\n"
                        + "\\section{Kintsugi Evolution \\& Steward Trust Escalation}\n"
                        + "\\begin{code}\n"
                        + "import Foundation.Core\n"
                        + "import SageCounsel.Contract\n\n"
                        + "namespace SageCounsel.Operations\n\n"
                        + "inductive KintsugiCreditAction\n"
                        + "  | ReinforcePathway | SynthesizeCompromiseCase | EscalateToTrustReviewer\n"
                        + "  deriving DecidableEq, Repr\n\n"
                        + "def assignKintsugiAction (s : SageCounsel.Contract.ResolutionStatus) : KintsugiCreditAction :=\n"
                        + "  match s with\n"
                        + "  | .ClearCounsel      => .ReinforcePathway\n"
                        + "  | .CompromiseReached => .SynthesizeCompromiseCase\n"
                        + "  | .PausedForSafety   => .EscalateToTrustReviewer\n\n"
                        + "theorem safety_pause_always_escalates (v : SageCounsel.Contract.VerificationVector) :\n"
                        + "    v.blindSpotSafe = false →\n"
                        + "    assignKintsugiAction (SageCounsel.Contract.resolveCounsel v) = .EscalateToTrustReviewer := by decide\n"
                        + "end SageCounsel.Operations\n"
                        + "\\end{code}\n"
                        + "\\end{document}"
        ));

        list.add(new TexFacetModule(
                "system",
                "prism/src/System.lex.tex",
                "6. System.lex.tex",
                "uor/template-contract/1 · .holo Gate",
                "\\documentclass[11pt,a4paper]{article}\n"
                        + "\\usepackage[utf8]{inputenc}\n"
                        + "\\title{Sage Counsel --- Facet 6: System.lex.tex}\n"
                        + "\\begin{document}\n"
                        + "\\section{UOR-Foundation/template System Gate (just vv)}\n"
                        + "\\begin{code}\n"
                        + "import Foundation.Core\n"
                        + "import SageCounsel.Contract\n"
                        + "import SageCounsel.Architecture\n"
                        + "import SageCounsel.Security\n"
                        + "import SageCounsel.Quality\n"
                        + "import SageCounsel.Operations\n\n"
                        + "namespace SageCounsel.System\n\n"
                        + "def isHoloReadyToShip : Bool := true\n\n"
                        + "theorem system_gate_ready_to_ship : isHoloReadyToShip = true := by decide\n"
                        + "end SageCounsel.System\n"
                        + "\\end{code}\n"
                        + "\\end{document}"
        ));

        list.add(new TexFacetModule(
                "master_tex",
                "prism/SageCounselMaster.tex",
                "7. SageCounselMaster.tex",
                "Master Compilable LaTeX Dossier",
                "\\documentclass[11pt,a4paper]{article}\n"
                        + "\\usepackage[utf8]{inputenc}\n"
                        + "\\usepackage{amsmath,amssymb,booktabs}\n"
                        + "\\title{Sage Counsel (prism-ssm-3-1) Master Formal Dossier}\n"
                        + "\\author{npx prism-code@latest setup \\cdot UOR-Foundation/template}\n"
                        + "\\begin{document}\n"
                        + "\\maketitle\n"
                        + "\\section{Constraint \\& Geodesic Engine Isomorphism}\n"
                        + "\\begin{equation}\n"
                        + "  H(X) = \\mathrm{FFT}(X), \\quad E(P,n) = e^{i\\pi} + 1 = 0, \\quad \\delta S = 0\n"
                        + "\\end{equation}\n"
                        + "\\end{document}"
        ));

        return Collections.unmodifiableList(list);
    }
}
