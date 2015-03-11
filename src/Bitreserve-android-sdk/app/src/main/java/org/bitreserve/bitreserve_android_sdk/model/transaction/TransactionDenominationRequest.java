package org.bitreserve.bitreserve_android_sdk.model.transaction;

/**
 * This class represents the transaction denomination request model.
 */

public class TransactionDenominationRequest {

    private final String amount;
    private final String currency;

    /**
     * Constructor.
     *
     * @param amount The amount of the transaction request.
     * @param currency The currency of the transaction request.
     */

    public TransactionDenominationRequest(String amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    /**
     * Gets the amount of the transaction request.
     *
     * @return the amount of the transaction request
     */

    public String getAmount() {
        return amount;
    }

    /**
     * Gets the currency of the transaction request.
     *
     * @return the currency of the transaction request
     */

    public String getCurrency() {
        return currency;
    }

}
