package ee.taltech.inbankbackend.service;

import ee.taltech.inbankbackend.config.DecisionEngineConstants;
import ee.taltech.inbankbackend.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class DecisionEngineTest {

    @InjectMocks
    private DecisionEngine decisionEngine;

    private String debtorPersonalCode;
    private String segment1PersonalCode;
    private String segment2PersonalCode;
    private String segment3PersonalCode;
    private String underAgePersonalCode;
    private String tooOldPersonalCode;

    @BeforeEach
    void setUp() {
        debtorPersonalCode = "37605030299";
        segment1PersonalCode = "50307172740";
        segment2PersonalCode = "38411266610";
        segment3PersonalCode = "35006069515";

        underAgePersonalCode = "50612017067";
        tooOldPersonalCode = "35006069515";
    }

    @Test
    void testDebtorPersonalCode() {
        assertThrows(NoValidLoanException.class,
                () -> decisionEngine.calculateLoanAmount(debtorPersonalCode, 12));
    }

    @Test
    void testSegment1PersonalCode() throws InvalidLoanPeriodException, NoValidLoanException,
            InvalidPersonalCodeException, InvalidLoanAmountException, InvalidCustomerAge {
        Decision decision = decisionEngine.calculateLoanAmount(segment1PersonalCode, 12);
        assertEquals(2000, decision.getLoanAmount());
        assertEquals(20, decision.getLoanPeriod());
    }

    @Test
    void testSegment2PersonalCode() throws InvalidLoanPeriodException, NoValidLoanException,
            InvalidPersonalCodeException, InvalidLoanAmountException, InvalidCustomerAge {
        Decision decision = decisionEngine.calculateLoanAmount(segment2PersonalCode, 12);
        assertEquals(3600, decision.getLoanAmount());
        assertEquals(12, decision.getLoanPeriod());
    }

    @Test
    void testSegment3PersonalCode() throws InvalidLoanPeriodException, NoValidLoanException,
            InvalidPersonalCodeException, InvalidLoanAmountException, InvalidCustomerAge {
        Decision decision = decisionEngine.calculateLoanAmount(segment3PersonalCode, 12);
        assertEquals(10000, decision.getLoanAmount());
        assertEquals(12, decision.getLoanPeriod());
    }

    @Test
    void testInvalidPersonalCode() {
        String invalidPersonalCode = "12345678901";
        assertThrows(InvalidPersonalCodeException.class,
                () -> decisionEngine.calculateApprovedLoan(invalidPersonalCode, 4000, 12));
    }

    @Test
    void testInvalidLoanAmount() {
        int tooLowLoanAmount = DecisionEngineConstants.MINIMUM_LOAN_AMOUNT - 1;
        int tooHighLoanAmount = DecisionEngineConstants.MAXIMUM_LOAN_AMOUNT + 1;

        assertThrows(InvalidLoanAmountException.class,
                () -> decisionEngine.calculateApprovedLoan(segment1PersonalCode, tooLowLoanAmount, 12));

        assertThrows(InvalidLoanAmountException.class,
                () -> decisionEngine.calculateApprovedLoan(segment1PersonalCode, tooHighLoanAmount, 12));
    }

    @Test
    void testInvalidLoanPeriod() {
        int tooShortLoanPeriod = DecisionEngineConstants.MINIMUM_LOAN_PERIOD - 1;
        int tooLongLoanPeriod = DecisionEngineConstants.MAXIMUM_LOAN_PERIOD + 1;

        assertThrows(InvalidLoanPeriodException.class,
                () -> decisionEngine.calculateApprovedLoan(segment1PersonalCode, 4000, tooShortLoanPeriod));

        assertThrows(InvalidLoanPeriodException.class,
                () -> decisionEngine.calculateApprovedLoan(segment1PersonalCode, 4000, tooLongLoanPeriod));
    }

    @Test
    void testFindSuitableLoanPeriod() throws InvalidLoanPeriodException, NoValidLoanException,
            InvalidPersonalCodeException, InvalidLoanAmountException, InvalidCustomerAge {
        Decision decision = decisionEngine.calculateLoanAmount(segment2PersonalCode, 12);
        assertEquals(3600, decision.getLoanAmount());
        assertEquals(12, decision.getLoanPeriod());
    }

    @Test
    void testNoValidLoanFound() {
        assertThrows(NoValidLoanException.class,
                () -> decisionEngine.calculateApprovedLoan(debtorPersonalCode, 10000, 60));
    }

    @Test
    void testUnderage() throws InvalidLoanPeriodException, InvalidCustomerAge, NoValidLoanException, InvalidPersonalCodeException, InvalidLoanAmountException {
        InvalidCustomerAge exception = assertThrows(InvalidCustomerAge.class,
                () -> decisionEngine.verifyCustomerAge(underAgePersonalCode));
        assertEquals("Person must be over 18 years of age!", exception.getMessage());
    }

    @Test
    void testTooOld() {
        InvalidCustomerAge exception = assertThrows(InvalidCustomerAge.class,
                () -> decisionEngine.verifyCustomerAge(tooOldPersonalCode));
        assertEquals("Person is too old to qualify for a loan!", exception.getMessage());
    }
}
