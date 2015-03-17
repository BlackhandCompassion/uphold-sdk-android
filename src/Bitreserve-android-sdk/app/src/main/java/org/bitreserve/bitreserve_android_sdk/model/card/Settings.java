package org.bitreserve.bitreserve_android_sdk.model.card;

/**
 * Card settings model.
 */

public class Settings {

    private final String position;
    private final Boolean starred;

    /**
     * Constructor.
     *
     * @param position The position of the card.
     * @param starred A boolean indicating if the card is starred.
     */

    public Settings(String position, Boolean starred) {
        this.position = position;
        this.starred = starred;
    }

    /**
     * Gets the position of the card.
     *
     * @return the position of the card
     */

    public String getPosition() {
        return position;
    }

    /**
     * Gets a boolean indicating if the card is starred.
     *
     * @return a boolean indicating if the card is starred
     */

    public Boolean getStarred() {
        return starred;
    }

}
