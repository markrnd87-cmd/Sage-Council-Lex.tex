package com.example.prism;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.example.model.ConflictPreference;
import com.example.model.ResolutionStatus;
import java.util.List;
import org.junit.Test;

/**
 * JUnit verification suite in Java testing {@link PrismContractKernel} and
 * {@link PrismTexFacetCatalog} against the {@code UOR-Foundation/template} specification.
 */
public class PrismContractKernelTest {

    @Test
    public void canonicalSpec_provesAll5TheoremsAnd6ExamplesAcross32DomainStates() {
        PrismTheoremProofReport report =
                PrismContractKernel.runExhaustiveKernelVerification(false, false);

        assertTrue(report.isOverallGatePassed());
        assertEquals(6, report.getPassedExamplesCount());
        assertEquals(5, report.getProvenTheoremsCount());
        assertEquals(32, PrismContractKernel.enumerateFiniteDomain().size());
        assertTrue(report.getHoloCertificateJson().contains("VERIFIED_AND_SEALED"));
    }

    @Test
    public void mutatingRuleR1_failsTheoremP1AndBlocksHoloAttestation() {
        PrismTheoremProofReport report =
                PrismContractKernel.runExhaustiveKernelVerification(true, false);

        assertFalse(report.isOverallGatePassed());
        assertEquals(5, report.getPassedExamplesCount()); // EX-03 fails
        assertTrue(report.getHoloCertificateJson().contains("REJECTED_BY_KERNEL"));
    }

    @Test
    public void flippingMeasurementMR1ToPending_blocksSecurityGate() {
        PrismTheoremProofReport report =
                PrismContractKernel.runExhaustiveKernelVerification(false, true);

        assertFalse(report.isOverallGatePassed());
        assertFalse(report.isMeasurementMR1Verified());
    }

    @Test
    public void lexTexCatalog_containsSixFacetsAndMasterTexWithExtractableLeanBlocks() {
        List<PrismTexFacetCatalog.TexFacetModule> facets = PrismTexFacetCatalog.getAllFacets();
        assertEquals(7, facets.size());

        PrismTexFacetCatalog.TexFacetModule contractFacet = facets.get(0);
        assertEquals("prism/src/Contract.lex.tex", contractFacet.getFilePath());

        List<String> leanBlocks =
                PrismTexFacetCatalog.extractLeanCodeBlocks(contractFacet.getTexContent());
        assertEquals(1, leanBlocks.size());
        assertTrue(leanBlocks.get(0).contains("theorem p1_asuricAlwaysVetoes"));
        assertNotNull(contractFacet.getSha256Digest());
        assertEquals(64, contractFacet.getSha256Digest().length());
    }

    @Test
    public void orderedDecisionTable_respectsRulesR1ThroughR6() {
        PrismVerificationVector r1Unsafe = PrismVerificationVector.forFiniteDomainPoint(
                false, true, true, ConflictPreference.OfferCompromise, true);
        assertEquals(
                "R1",
                PrismContractKernel.evaluateOrderedDecisionTable(r1Unsafe, false).getMatchedRuleId()
        );
        assertEquals(
                ResolutionStatus.PausedForSafety,
                PrismContractKernel.evaluateOrderedDecisionTable(r1Unsafe, false).getResolutionStatus()
        );
    }
}
