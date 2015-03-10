package org.bitreserve.bitreserve_android_sdk.model.transaction;

/**
 * This class represents the source model.
 */

public class Source {

    private final String id;
    private final String amount;

    /**
     * Constructor.
     *
     * @param id The id of the source.
     * @param amount The amount of the source.
     */

    public Source(String id, String amount) {
        this.id = id;
        this.amount = amount;
    }

    /**
     * Gets the id of the source.
     *
     * @return the id of the source.
     */

    public String getId() {
        return id;
    }

    /**
     * Gets the amount of the source.
     *
     * @return the amount of the source.
     */

    public String getAmount() {
        return amount;
    }
}
