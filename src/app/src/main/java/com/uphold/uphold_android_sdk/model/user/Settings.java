package com.uphold.uphold_android_sdk.model.user;

import java.io.Serializable;

/**
 * Settings model.
 */

public class Settings implements Serializable {

    private final String currency;
    private final Boolean hasNewsSubscription;
    private final Boolean hasOtpEnabled;
    private final InternationalizationUserSettings intl;
    private final String theme;

    /**
     * Constructor.
     *
     * @param currency The currency selected at the user settings.
     * @param hasNewsSubscription A boolean indicating if the user has the news subscription enable.
     * @param hasOtpEnabled A boolean indicating if the user has the OTP enable.
     * @param intl The user internationalization settings.
     * @param theme The user theme.
     */

    public Settings(String currency, Boolean hasNewsSubscription, Boolean hasOtpEnabled, InternationalizationUserSettings intl, String theme) {
        this.currency = currency;
        this.hasNewsSubscription = hasNewsSubscription;
        this.hasOtpEnabled = hasOtpEnabled;
        this.intl = intl;
        this.theme = theme;
    }

    /**
     * Gets the user currency.
     *
     * @return the user currency
     */

    public String getCurrency() {
        return currency;
    }

    /**
     * Gets a boolean indicating if the user has the news subscription enable.
     *
     * @return the a boolean indicating if the user has the news subscription enable
     */

    public Boolean getHasNewsSubscription() {
        return hasNewsSubscription;
    }

    /**
     * Gets a boolean indicating if the user has the otp enable.
     *
     * @return the a boolean indicating if the user has the otp enable
     */

    public Boolean getHasOtpEnabled() {
        return hasOtpEnabled;
    }

    /**
     * Gets the user internationalization settings.
     *
     * @return the {@link InternationalizationUserSettings}
     */

    public InternationalizationUserSettings getIntl() {
        return intl;
    }

    /**
     * Gets the user theme.
     *
     * @return the user theme
     */

    public String getTheme() {
        return theme;
    }

}
