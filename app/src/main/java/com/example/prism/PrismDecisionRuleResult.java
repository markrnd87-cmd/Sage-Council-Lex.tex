package com.example.prism;

import com.example.model.ResolutionStatus;
import java.util.Objects;

/**
 * Output of the Ordered Decision Table (Rules R1..R6) in {@code Contract.lex.tex}.
 */
public final class PrismDecisionRuleResult {

    private final String matchedRuleId;
    private final String matchedRuleExpression;
    private final ResolutionStatus resolutionStatus;
    private final String internalManifestStateName;
    private final String stationaryActionDeltaS;
    private final String zeroStateIdentity;

    public PrismDecisionRuleResult(
            String matchedRuleId,
            String matchedRuleExpression,
            ResolutionStatus resolutionStatus,
            String internalManifestStateName,
            String stationaryActionDeltaS,
            String zeroStateIdentity) {
        this.matchedRuleId = Objects.requireNonNull(matchedRuleId);
        this.matchedRuleExpression = Objects.requireNonNull(matchedRuleExpression);
        this.resolutionStatus = Objects.requireNonNull(resolutionStatus);
        this.internalManifestStateName = Objects.requireNonNull(internalManifestStateName);
        this.stationaryActionDeltaS = Objects.requireNonNull(stationaryActionDeltaS);
        this.zeroStateIdentity = Objects.requireNonNull(zeroStateIdentity);
    }

    public String getMatchedRuleId() {
        return matchedRuleId;
    }

    public String getMatchedRuleExpression() {
        return matchedRuleExpression;
    }

    public ResolutionStatus getResolutionStatus() {
        return resolutionStatus;
    }

    public String getInternalManifestStateName() {
        return internalManifestStateName;
    }

    public String getStationaryActionDeltaS() {
        return stationaryActionDeltaS;
    }

    public String getZeroStateIdentity() {
        return zeroStateIdentity;
    }
}
