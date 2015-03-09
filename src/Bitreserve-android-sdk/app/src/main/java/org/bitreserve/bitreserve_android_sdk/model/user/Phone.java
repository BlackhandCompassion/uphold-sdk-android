package org.bitreserve.bitreserve_android_sdk.model.user;

/**
 * Phone represents the phone information.
 */

public class Phone {

    private final String id;
    private final String e164Masked;
    private final String internationalMasked;
    private final String nationalMasked;
    private final Boolean primary;
    private final Boolean verified;

    /**
     * Constructor.
     *
     * @param id The contact id.
     * @param e164Masked The E.164 phone mask.
     * @param internationalMasked The international phone mask.
     * @param nationalMasked The national phone mask.
     * @param primary A boolean indicating if the phone is the primary user phone.
     * @param verified A boolean indicating if the phone is verified.
     */

    public Phone(String id, String e164Masked, String internationalMasked, String nationalMasked, Boolean primary, Boolean verified) {
        this.id = id;
        this.e164Masked = e164Masked;
        this.internationalMasked = internationalMasked;
        this.nationalMasked = nationalMasked;
        this.primary = primary;
        this.verified = verified;
    }

    /**
     * Gets the phone id.
     *
     * @return the phone id
     */

    public String getId() {
        return id;
    }

    /**
     * Gets the phone E.164 mask.
     *
     * @return the phone E.164 mask
     */

    public String getE164Masked() {
        return e164Masked;
    }

    /**
     * Gets the phone international mask.
     *
     * @return the phone international mask
     */

    public String getInternationalMasked() {
        return internationalMasked;
    }

    /**
     * Gets the phone national mask.
     *
     * @return the phone national mask
     */

    public String getNationalMasked() {
        return nationalMasked;
    }

    /**
     * Gets a boolean indicating if the phone is the primary user phone.
     *
     * @return a boolean indicating if the phone is the primary user phone
     */

    public Boolean getPrimary() {
        return primary;
    }

    /**
     * Gets a boolean indicating if the phone is verified.
     *
     * @return a boolean indicating if the phone is verified
     */

    public Boolean getVerified() {
        return verified;
    }
}
