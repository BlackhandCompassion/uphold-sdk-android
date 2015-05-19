package org.bitreserve.bitreserve_android_sdk.test.integration.client;

import com.darylteo.rx.promises.java.Promise;
import com.darylteo.rx.promises.java.functions.RepromiseFunction;

import junit.framework.Assert;

import org.bitreserve.bitreserve_android_sdk.client.BitreserveClient;
import org.bitreserve.bitreserve_android_sdk.client.restadapter.BitreserveRestAdapter;
import org.bitreserve.bitreserve_android_sdk.exception.BitreserveClientException;
import org.bitreserve.bitreserve_android_sdk.exception.StateMatchException;
import org.bitreserve.bitreserve_android_sdk.model.AuthenticationRequest;
import org.bitreserve.bitreserve_android_sdk.model.AuthenticationResponse;
import org.bitreserve.bitreserve_android_sdk.model.Rate;
import org.bitreserve.bitreserve_android_sdk.model.Token;
import org.bitreserve.bitreserve_android_sdk.model.User;
import org.bitreserve.bitreserve_android_sdk.test.util.MockRestAdapter;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;
import android.test.mock.MockContext;
import android.test.suitebuilder.annotation.SmallTest;

import java.io.ByteArrayOutputStream;
import java.util.List;

import retrofit.client.Header;
import retrofit.client.Request;

/**
 * BitreserveClient integration tests.
 */

@RunWith(AndroidJUnit4.class)
@SmallTest
public class BitreserveClientTest {

    @Test
    public void bitreserveClientWithTokenShouldSetTheTokenAndRestAdapter() {
        BitreserveClient bitreserveClient = new BitreserveClient(null);

        Assert.assertNull(bitreserveClient.getToken().getBearerToken());
        Assert.assertNotNull(bitreserveClient.getToken().getBitreserveRestAdapter());
    }

    @Test
    public void bitreserveClientWithTokenShouldReturnTheBearerTokenAndRestAdapter() {
        BitreserveClient bitreserveClient = new BitreserveClient("foobar");

        Assert.assertEquals(bitreserveClient.getToken().getBearerToken(), "foobar");
        Assert.assertNotNull(bitreserveClient.getToken().getBitreserveRestAdapter());
    }

    @Test
    public void completeAuthorizationShouldReturnBitreserveClientExceptionStateMatchError() throws Exception {
        String responseString = "{ \"access_token\": \"foo\", \"description\": \"bar\", \"expires\": null }";
        MockRestAdapter<AuthenticationResponse> adapter = new MockRestAdapter<>("foobar", responseString, null);

        adapter.request(new RepromiseFunction<BitreserveRestAdapter, AuthenticationResponse>() {
            @Override
            public Promise<AuthenticationResponse> call(BitreserveRestAdapter adapter) {
                BitreserveClient client = new BitreserveClient("foobar");
                Uri uri = Uri.parse("bitreserve://foobar.com?state=foobar");

                client.getToken().setBitreserveRestAdapter(adapter);

                return client.completeAuthorization(uri, "foo", "bar", "foobar", "foobuz");
            }
        });

        Exception exception = adapter.getException();

        Assert.assertEquals(exception.getClass().getName(), StateMatchException.class.getName());
        Assert.assertEquals(exception.getMessage(), "State does not match.");
    }

    @Test
    public void completeAuthorizationShouldReturnTheAuthenticationResponse() throws Exception {
        ByteArrayOutputStream bodyOutput = new ByteArrayOutputStream();
        String responseString = "{ \"access_token\": \"foo\", \"description\": \"bar\", \"expires\": null }";
        MockRestAdapter<AuthenticationResponse> adapter = new MockRestAdapter<>("foobar", responseString, null);

        adapter.request(new RepromiseFunction<BitreserveRestAdapter, AuthenticationResponse>() {
            @Override
            public Promise<AuthenticationResponse> call(BitreserveRestAdapter adapter) {
                BitreserveClient client = new BitreserveClient("foobar");
                Uri uri = Uri.parse("bitreserve://foobar.com?code=foo&state=foobar");

                client.getToken().setBitreserveRestAdapter(adapter);

                return client.completeAuthorization(uri, "foo", "bar", "foobiz", "foobar");
            }
        });

        AuthenticationResponse authenticationResponse = adapter.getResult();
        Request request = adapter.getRequest();

        request.getBody().writeTo(bodyOutput);

        Assert.assertEquals(authenticationResponse.getAccessToken(), "foo");
        Assert.assertEquals(authenticationResponse.getDescription(), "bar");
        Assert.assertEquals(bodyOutput.toString(), "code=foo&grant_type=foobiz");
        Assert.assertEquals(request.getMethod(), "POST");
        Assert.assertEquals(request.getUrl(), "https://api.bitreserve.org/oauth2/token");
        Assert.assertNull(authenticationResponse.getExpiresIn());
        Assert.assertTrue(request.getHeaders().contains(new Header("Authorization", "Basic Zm9vOmJhcg==")));
    }

    @Test
    public void getReserveShouldReturnTheReserveWithRestAdapter() {
        BitreserveClient bitreserveClient = new BitreserveClient("foobar");

        Assert.assertEquals(bitreserveClient.getReserve().getBitreserveRestAdapter(), bitreserveClient.getToken().getBitreserveRestAdapter());
    }

    @Test
    public void getTickersShouldReturnTheListOfRates() throws Exception {
        String responseString = "[" +
            "{" +
                "\"ask\": \"foo\"," +
                "\"bid\": \"bar\"," +
                "\"currency\": \"foobar\"," +
                "\"pair\": \"foobiz\"" +
            "}, {" +
                "\"ask\": \"fiz\"," +
                "\"bid\": \"biz\"," +
                "\"currency\": \"foobiz\"," +
                "\"pair\": \"bar\"" +
            "}, {" +
                "\"ask\": \"foobar\"," +
                "\"bid\": \"foobaz\"," +
                "\"currency\": \"bar\"," +
                "\"pair\": \"foo\"" +
            "}" +
        "]";
        MockRestAdapter<List<Rate>> adapter = new MockRestAdapter<>("foobar", responseString, null);

        adapter.request(new RepromiseFunction<BitreserveRestAdapter, List<Rate>>() {
            @Override
            public Promise<List<Rate>> call(BitreserveRestAdapter adapter) {
                BitreserveClient client = new BitreserveClient("foobar");

                client.getToken().setBitreserveRestAdapter(adapter);

                return client.getTicker();
            }
        });

        List<Rate> rates = adapter.getResult();
        Request request = adapter.getRequest();

        Assert.assertEquals(request.getMethod(), "GET");
        Assert.assertEquals(request.getUrl(), "https://api.bitreserve.org/v0/ticker");
        Assert.assertEquals(rates.size(), 3);
        Assert.assertEquals(rates.get(0).getAsk(), "foo");
        Assert.assertEquals(rates.get(0).getBid(), "bar");
        Assert.assertEquals(rates.get(0).getCurrency(), "foobar");
        Assert.assertEquals(rates.get(0).getPair(), "foobiz");
        Assert.assertEquals(rates.get(1).getAsk(), "fiz");
        Assert.assertEquals(rates.get(1).getBid(), "biz");
        Assert.assertEquals(rates.get(1).getCurrency(), "foobiz");
        Assert.assertEquals(rates.get(1).getPair(), "bar");
        Assert.assertEquals(rates.get(2).getAsk(), "foobar");
        Assert.assertEquals(rates.get(2).getBid(), "foobaz");
        Assert.assertEquals(rates.get(2).getCurrency(), "bar");
        Assert.assertEquals(rates.get(2).getPair(), "foo");
    }

    @Test
    public void getTickersByCurrencyShouldReturnTheListOfRates() throws Exception {
        String responseString = "[" +
          "{" +
              "\"ask\": \"foo\"," +
              "\"bid\": \"bar\"," +
              "\"currency\": \"foobar\"," +
              "\"pair\": \"foobiz\"" +
          "}, {"  +
              "\"ask\": \"fuz\"," +
              "\"bid\": \"buz\"," +
              "\"currency\": \"foobuz\"," +
              "\"pair\": \"bar\"" +
          "}, {"  +
              "\"ask\": \"foobar\"," +
              "\"bid\": \"foobaz\"," +
              "\"currency\": \"bar\"," +
              "\"pair\": \"foo\"" +
          "}" +
        "]";
        MockRestAdapter<List<Rate>> adapter = new MockRestAdapter<>("foobar", responseString, null);

        adapter.request(new RepromiseFunction<BitreserveRestAdapter, List<Rate>>() {
            @Override
            public Promise<List<Rate>> call(BitreserveRestAdapter adapter) {
                BitreserveClient client = new BitreserveClient("foobar");

                client.getToken().setBitreserveRestAdapter(adapter);

                return client.getTickersByCurrency("USD");
            }
        });

        List<Rate> rates = adapter.getResult();

        Assert.assertEquals(adapter.getRequest().getMethod(), "GET");
        Assert.assertEquals(adapter.getRequest().getUrl(), "https://api.bitreserve.org/v0/ticker/USD");
        Assert.assertEquals(rates.size(), 3);
        Assert.assertEquals(rates.get(0).getAsk(), "foo");
        Assert.assertEquals(rates.get(1).getAsk(), "fuz");
        Assert.assertEquals(rates.get(2).getAsk(), "foobar");
    }

    @Test
    public void getTokenShouldReturnTheToken() {
        BitreserveClient bitreserveClient = new BitreserveClient("foobar");

        Assert.assertEquals(bitreserveClient.getToken().getBearerToken(), "foobar");
    }

    @Test
    public void setTokenShouldSetTheToken() {
        BitreserveClient bitreserveClient = new BitreserveClient("foobar");
        bitreserveClient.setToken(new Token("new foobar"));

        Assert.assertEquals(bitreserveClient.getToken().getBearerToken(), "new foobar");
    }

    @Test
    public void getUserShouldReturnTheUser() throws Exception {
        String responseString = "{" +
            "\"username\": \"foobar\"," +
            "\"email\": \"foobar@bfoobar.org\"," +
            "\"firstName\": \"foo\"," +
            "\"lastName\": \"bar\"," +
            "\"name\": \"Foo Bar\"," +
            "\"country\": \"BAR\"," +
            "\"state\": \"FOO\"," +
            "\"currencies\": [" +
              "\"BTC\"," +
            "]," +
            "\"status\": {" +
            "\"email\": \"ok\"," +
            "\"phone\": \"pending\"," +
            "\"review\": \"pending\"," +
            "\"volume\": \"ok\"," +
            "\"identity\": \"pending\"," +
            "\"overview\": \"pending\"," +
            "\"screening\": \"pending\"," +
            "\"registration\": \"running\"" +
            "}," +
            "\"settings\": {" +
              "\"theme\": \"minimalistic\"," +
              "\"currency\": \"USD\"," +
              "\"hasNewsSubscription\": \"true\"," +
              "\"intl\": {" +
                  "\"language\": {" +
                      "\"locale\": \"en-US\"" +
                  "}," +
                  "\"dateTimeFormat\": {" +
                      "\"locale\": \"en-US\"" +
                  "}," +
                  "\"numberFormat\": {" +
                      "\"locale\": \"en-US\"" +
                  "}" +
              "}," +
              "\"hasOtpEnabled\": \"false\"" +
            "}" +
        "}";
        MockRestAdapter<User> adapter = new MockRestAdapter<>("foobar", responseString, null);

        adapter.request(new RepromiseFunction<BitreserveRestAdapter, User>() {
            @Override
            public Promise<User> call(BitreserveRestAdapter adapter) {
                BitreserveClient client = new BitreserveClient("foobar");

                client.getToken().setBitreserveRestAdapter(adapter);

                return client.getUser();
            }
        });

        Request request = adapter.getRequest();
        User user = adapter.getResult();

        Assert.assertEquals(request.getMethod(), "GET");
        Assert.assertEquals(request.getUrl(), "https://api.bitreserve.org/v0/me");
        Assert.assertEquals(user.getCountry(), "BAR");
        Assert.assertEquals(user.getEmail(), "foobar@bfoobar.org");
        Assert.assertEquals(user.getFirstName(), "foo");
        Assert.assertEquals(user.getLastName(), "bar");
        Assert.assertEquals(user.getName(), "Foo Bar");
        Assert.assertEquals(user.getSettings().getCurrency(), "USD");
        Assert.assertEquals(user.getSettings().getIntl().getDateTimeFormat().getLocale(), "en-US");
        Assert.assertEquals(user.getSettings().getIntl().getLanguage().getLocale(), "en-US");
        Assert.assertEquals(user.getSettings().getIntl().getNumberFormat().getLocale(), "en-US");
        Assert.assertEquals(user.getSettings().getTheme(), "minimalistic");
        Assert.assertEquals(user.getState(), "FOO");
        Assert.assertEquals(user.getStatus().getEmail(), "ok");
        Assert.assertEquals(user.getStatus().getIdentity(), "pending");
        Assert.assertEquals(user.getStatus().getOverview(), "pending");
        Assert.assertEquals(user.getStatus().getPhone(), "pending");
        Assert.assertEquals(user.getStatus().getRegistration(), "running");
        Assert.assertEquals(user.getStatus().getReview(), "pending");
        Assert.assertEquals(user.getStatus().getScreening(), "pending");
        Assert.assertEquals(user.getStatus().getVolume(), "ok");
        Assert.assertEquals(user.getUsername(), "foobar");
        Assert.assertFalse(user.getSettings().getHasOtpEnabled());
        Assert.assertTrue(user.getSettings().getHasNewsSubscription());
    }

}
