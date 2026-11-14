package com.example.prism;

import com.example.model.ConflictPreference;
import com.example.model.GoalMode;
import com.example.model.ImpactScale;
import java.util.Objects;

/**
 * Formal finite-domain VerificationVector modeled in {@code prism/src/Contract.lex.tex}.
 *
 * <p>Scaffolded according to {@code npx prism-code@latest setup} and the
 * {@code UOR-Foundation/template} ({@code uor/template-contract/1}) specification.
 */
public final class PrismVerificationVector {

    private final boolean blindSpotSafe;
    private final int upheldGuaranteesCount;
    private final boolean allGuaranteesHeld;
    private final boolean goalsHarmonious;
    private final ConflictPreference onConflict;
    private final boolean compromiseViable;
    private final GoalMode goalMode;
    private final ImpactScale impactScale;
    private final double spectralEntropyNat;

    public PrismVerificationVector(
            boolean blindSpotSafe,
            int upheldGuaranteesCount,
            boolean allGuaranteesHeld,
            boolean goalsHarmonious,
            ConflictPreference onConflict,
            boolean compromiseViable,
            GoalMode goalMode,
            ImpactScale impactScale,
            double spectralEntropyNat) {
        this.blindSpotSafe = blindSpotSafe;
        this.upheldGuaranteesCount = upheldGuaranteesCount;
        this.allGuaranteesHeld = allGuaranteesHeld;
        this.goalsHarmonious = goalsHarmonious;
        this.onConflict = Objects.requireNonNull(onConflict, "onConflict must not be null");
        this.compromiseViable = compromiseViable;
        this.goalMode = Objects.requireNonNull(goalMode, "goalMode must not be null");
        this.impactScale = Objects.requireNonNull(impactScale, "impactScale must not be null");
        this.spectralEntropyNat = spectralEntropyNat;
    }

    public static PrismVerificationVector forFiniteDomainPoint(
            boolean blindSpotSafe,
            boolean allGuaranteesHeld,
            boolean goalsHarmonious,
            ConflictPreference onConflict,
            boolean compromiseViable) {
        return new PrismVerificationVector(
                blindSpotSafe,
                allGuaranteesHeld ? 9 : 8,
                allGuaranteesHeld,
                goalsHarmonious,
                onConflict,
                compromiseViable,
                GoalMode.StabilizeExisting,
                ImpactScale.PersonalOrTeam,
                0.42
        );
    }

    public boolean isBlindSpotSafe() {
        return blindSpotSafe;
    }

    public int getUpheldGuaranteesCount() {
        return upheldGuaranteesCount;
    }

    public boolean isAllGuaranteesHeld() {
        return allGuaranteesHeld;
    }

    public boolean isGoalsHarmonious() {
        return goalsHarmonious;
    }

    public ConflictPreference getOnConflict() {
        return onConflict;
    }

    public boolean isCompromiseViable() {
        return compromiseViable;
    }

    public GoalMode getGoalMode() {
        return goalMode;
    }

    public ImpactScale getImpactScale() {
        return impactScale;
    }

    public double getSpectralEntropyNat() {
        return spectralEntropyNat;
    }

    public String getOntologicalRankP() {
        return goalMode == GoalMode.StabilizeExisting ? "{2, 3}" : "{2, 3, 5}";
    }

    public int getComputationalRankN() {
        switch (impactScale) {
            case PersonalOrTeam:
                return 8;
            case OrganizationWide:
                return 16;
            case CommunityOrEcosystem:
            default:
                return 32;
        }
    }

    public String getCliffordAlgebraSymbol() {
        return "Cl_" + getComputationalRankN() + ",0";
    }

    public String getAtlasPolytopeSymbol() {
        return "A_" + getOntologicalRankP().replace(" ", "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PrismVerificationVector)) return false;
        PrismVerificationVector that = (PrismVerificationVector) o;
        return blindSpotSafe == that.blindSpotSafe
                && upheldGuaranteesCount == that.upheldGuaranteesCount
                && allGuaranteesHeld == that.allGuaranteesHeld
                && goalsHarmonious == that.goalsHarmonious
                && compromiseViable == that.compromiseViable
                && Double.compare(that.spectralEntropyNat, spectralEntropyNat) == 0
                && onConflict == that.onConflict
                && goalMode == that.goalMode
                && impactScale == that.impactScale;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                blindSpotSafe,
                upheldGuaranteesCount,
                allGuaranteesHeld,
                goalsHarmonious,
                onConflict,
                compromiseViable,
                goalMode,
                impactScale,
                spectralEntropyNat
        );
    }
}
