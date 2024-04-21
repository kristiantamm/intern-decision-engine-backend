## Adjusted parameter types:
 - The loanAmount parameter in the calculateApprovedLoan method was changed from Long to int to ensure consistency with the type used in the rest of the code.
 - Similarly, the calculateLoanAmount method now returns a Decision object instead of Long, aligning with its purpose of calculating the approved loan amount and period.
## Enhanced exception handling:
- Previously, exceptions were caught as Exception e, which could potentially catch more exceptions than intended. Now, each specific exception is caught separately, improving the error handling.
- This change allows for more precise error messages to be returned to the caller, improving debugging and troubleshooting.
## Refactoring of loan amount calculation:
- The logic for calculating the approved loan amount has been moved to a separate method calculateLoanAmount. This enhances code readability and maintainability by separating concerns.
- The method calculateLoanAmount now throws NoValidLoanException directly if no valid loan is found, simplifying the control flow.
## Added Personal Code Parsing:
- An instance of EstonianPersonalCodeParser is introduced to extract age information from the personal code. 
- This parsing ensures that age-related checks can be performed accurately, contributing to the reliability of age validation.
## Adjusted Loan Amount Validation:
- In the calculateLoanAmount method, the condition to check if the loan period is within the valid range (loanPeriod <= DecisionEngineConstants.MAXIMUM_LOAN_PERIOD && loanPeriod >= DecisionEngineConstants.MINIMUM_LOAN_PERIOD) is added. This ensures that only valid loan periods are considered, enhancing the reliability of the loan calculation.