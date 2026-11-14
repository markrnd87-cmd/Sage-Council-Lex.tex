package com.example.prism;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Immutable report produced by the Java Prism Kernel verifier ({@code just vv} pipeline).
 * Encapsulates theorem proof statuses (P1..P5), acceptance vector conformance (EX-01..EX-06),
 * anti-vacuity rule coverage (R1..R6), and the cryptographic {@code .holo} attestation digest.
 */
public final class PrismTheoremProofReport {

    public static final class TheoremStatus {
        private final String id;
        private final String leanName;
        private final String statement;
        private final boolean proven;
        private final int checkedDomainStates;
        private final String counterexampleSummary;

        public TheoremStatus(
                String id,
                String leanName,
                String statement,
                boolean proven,
                int checkedDomainStates,
                String counterexampleSummary) {
            this.id = Objects.requireNonNull(id);
            this.leanName = Objects.requireNonNull(leanName);
            this.statement = Objects.requireNonNull(statement);
            this.proven = proven;
            this.checkedDomainStates = checkedDomainStates;
            this.counterexampleSummary = counterexampleSummary;
        }

        public String getId() {
            return id;
        }

        public String getLeanName() {
            return leanName;
        }

        public String getStatement() {
            return statement;
        }

        public boolean isProven() {
            return proven;
        }

        public int getCheckedDomainStates() {
            return checkedDomainStates;
        }

        public String getCounterexampleSummary() {
            return counterexampleSummary;
        }
    }

    public static final class GateStageStatus {
        private final int stepNumber;
        private final String command;
        private final String description;
        private final boolean passed;
        private final String detailBadge;

        public GateStageStatus(
                int stepNumber,
                String command,
                String description,
                boolean passed,
                String detailBadge) {
            this.stepNumber = stepNumber;
            this.command = Objects.requireNonNull(command);
            this.description = Objects.requireNonNull(description);
            this.passed = passed;
            this.detailBadge = Objects.requireNonNull(detailBadge);
        }

        public int getStepNumber() {
            return stepNumber;
        }

        public String getCommand() {
            return command;
        }

        public String getDescription() {
            return description;
        }

        public boolean isPassed() {
            return passed;
        }

        public String getDetailBadge() {
            return detailBadge;
        }
    }

    private final String sdkSetupCommand;
    private final String templateRepoUrl;
    private final String templateContractId;
    private final boolean overallGatePassed;
    private final int passedExamplesCount;
    private final int totalExamplesCount;
    private final int provenTheoremsCount;
    private final int totalTheoremsCount;
    private final boolean measurementMR1Verified;
    private final List<TheoremStatus> theoremStatuses;
    private final List<GateStageStatus> gateStages;
    private final String holoDigestSha256;
    private final String holoCertificateJson;

    public PrismTheoremProofReport(
            String sdkSetupCommand,
            String templateRepoUrl,
            String templateContractId,
            boolean overallGatePassed,
            int passedExamplesCount,
            int totalExamplesCount,
            int provenTheoremsCount,
            int totalTheoremsCount,
            boolean measurementMR1Verified,
            List<TheoremStatus> theoremStatuses,
            List<GateStageStatus> gateStages,
            String holoDigestSha256,
            String holoCertificateJson) {
        this.sdkSetupCommand = Objects.requireNonNull(sdkSetupCommand);
        this.templateRepoUrl = Objects.requireNonNull(templateRepoUrl);
        this.templateContractId = Objects.requireNonNull(templateContractId);
        this.overallGatePassed = overallGatePassed;
        this.passedExamplesCount = passedExamplesCount;
        this.totalExamplesCount = totalExamplesCount;
        this.provenTheoremsCount = provenTheoremsCount;
        this.totalTheoremsCount = totalTheoremsCount;
        this.measurementMR1Verified = measurementMR1Verified;
        this.theoremStatuses = Collections.unmodifiableList(theoremStatuses);
        this.gateStages = Collections.unmodifiableList(gateStages);
        this.holoDigestSha256 = Objects.requireNonNull(holoDigestSha256);
        this.holoCertificateJson = Objects.requireNonNull(holoCertificateJson);
    }

    public String getSdkSetupCommand() {
        return sdkSetupCommand;
    }

    public String getTemplateRepoUrl() {
        return templateRepoUrl;
    }

    public String getTemplateContractId() {
        return templateContractId;
    }

    public boolean isOverallGatePassed() {
        return overallGatePassed;
    }

    public int getPassedExamplesCount() {
        return passedExamplesCount;
    }

    public int getTotalExamplesCount() {
        return totalExamplesCount;
    }

    public int getProvenTheoremsCount() {
        return provenTheoremsCount;
    }

    public int getTotalTheoremsCount() {
        return totalTheoremsCount;
    }

    public boolean isMeasurementMR1Verified() {
        return measurementMR1Verified;
    }

    public List<TheoremStatus> getTheoremStatuses() {
        return theoremStatuses;
    }

    public List<GateStageStatus> getGateStages() {
        return gateStages;
    }

    public String getHoloDigestSha256() {
        return holoDigestSha256;
    }

    public String getHoloCertificateJson() {
        return holoCertificateJson;
    }
}
