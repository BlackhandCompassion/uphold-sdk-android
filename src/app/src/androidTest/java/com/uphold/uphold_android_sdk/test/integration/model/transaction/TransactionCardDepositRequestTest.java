package com.uphold.uphold_android_sdk.test.integration.model.transaction;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import com.uphold.uphold_android_sdk.model.transaction.TransactionCardDepositRequest;
import com.uphold.uphold_android_sdk.test.util.Fixtures;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

/**
 * TransactionCardDepositRequestTest integration tests.
 */

@RunWith(AndroidJUnit4.class)
@SmallTest
public class TransactionCardDepositRequestTest {

    @Test
    public void getOriginShouldReturnTheOrigin() {
        TransactionCardDepositRequest transactionCardDepositRequest = Fixtures.loadTransactionCardDepositRequest(new HashMap<String, String>() {{
            put("origin", "foobar");
        }});

        Assert.assertEquals(transactionCardDepositRequest.getOrigin(), "foobar");
    }

    @Test
    public void getDenominationShouldReturnTheDenomination() {
        TransactionCardDepositRequest transactionCardDepositRequest = Fixtures.loadTransactionCardDepositRequest(new HashMap<String, String>() {{
            put("amount", "0.01");
            put("currency", "foobar");
        }});

        Assert.assertEquals(transactionCardDepositRequest.getDenomination().getAmount(), "0.01");
        Assert.assertEquals(transactionCardDepositRequest.getDenomination().getCurrency(), "foobar");
    }

    @Test
    public void getSecurityCodeShouldReturnTheSecurityCode() {
        TransactionCardDepositRequest transactionCardDepositRequest = Fixtures.loadTransactionCardDepositRequest(new HashMap<String, String>() {{
            put("securityCode", "1234");
        }});

        Assert.assertEquals(transactionCardDepositRequest.getSecurityCode(), "1234");
    }

}
