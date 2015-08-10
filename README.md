# Bitreserve SDK for Android

Bitreserve is a next generation platform that allows anyone to transfer and exchange value for free, instantly and securely.

The Bitreserve SDK for Android provides an easy way for developers to integrate Android applications with the [Bitreserve API](https://developers.bitreserve.org).

## Requirements

	* Android Studio
	* Minimum Android SDK Version - 10 (2.3.3)

## Installation

Using _gradle_:

```
repositories {
	// Add the jitpack maven repository url.
	maven {
		url "https://jitpack.io"
	}
}

dependencies {
	compile 'com.github.bitreserve:bitreserve-sdk-android:0.0.1'
	// Change to:
	// compile ('com.github.bitreserve:bitreserve-sdk-android:0.0.1:sandboxRelease@aar') {
	//     transitive = true
	// }
	// to use the sandbox environment.
}
```

## Basic usage

In order to learn more about the Bitreserve API, please visit the [developer website](https://developer.bitreserve.org).

To use the SDK you must first register an Application and obtain a unique `client_id` and `client_secret` combination. We recommend your first app be [registered in the Sandbox environment](https://sandbox.bitreserve.org/dashboard/profile/applications/developer/new), so you can safely play around during development.

From the application page in your account you can get the Client ID, Client Secret and configure the redirect URI and the desired Scopes.

### Authenticate User

Before instantiating the Bitreserve client to start the OAuth authentication flow, you must first initialize it:

```java
BitreserveClient.initialize(MainActivity.this);
```

Now we can start the authentication process by calling the `beginAuthorization` method:

```java
BitreserveClient bitreserveClient = new BitreserveClient();
bitreserveClient.beginAuthorization(MainActivity.this, CLIENT_ID, scopes, state);
```

To receive an intent for the callback URL it is necessary to register an intent filter for one of your Android activities in order for users to be redirected to your app after the authorization process:

```xml
<intent-filter>
    <action android:name="android.intent.action.VIEW" />
    <category android:name="android.intent.category.DEFAULT" />
    <category android:name="android.intent.category.BROWSABLE" />
    <data
        android:pathPrefix="/connect/bitreserve"
        android:scheme="bitreserve-demo" />
</intent-filter>
```

In the Android activity with the intent filter override the `onNewIntent` method to receive the redirect code:

```java
@Override
protected void onNewIntent(final Intent intent) {
	if (intent == null || intent.getAction() == null || !intent.getAction().equals("android.intent.action.VIEW")) {
	    return;
	}

	bitreserveClient.completeAuthorization(intent.getData(), CLIENT_ID, CLIENT_SECRET, "authorization_code", state).then(new PromiseAction<AuthenticationResponse>() {
        @Override
        public void call(AuthenticationResponse authenticationResponse) {
            // Get the user bearer token from the authenticationResponse.
        }
    }).fail(new PromiseAction<Exception>() {
        @Override
        public void call(Exception e) {
            // Handle the Error.
        }
    });
}
```

To get the current user information, just instantiate the Bitreserve client with the user bearer token:

```java
BitreserveClient bitreserveClient = new BitreserveClient(bearerToken);
bitreserveClient.getUser().then(new PromiseAction<User>() {
    @Override
    public void call(User user) {
        // The user information is available at the user object.
    }
});
```

### Get user cards with chaining

```java
BitreserveClient bitreserveClient = new BitreserveClient(bearerToken);
bitreserveClient.getUser().then(new RepromiseFunction<User, List<Card>>() {
    @Override
    public Promise<List<Card>> call(User user) {
        // Do something with the user.
        return user.getCards();
    }
}).then(new PromiseAction<List<Card>>() {
    @Override
    public void call(List<Card> cards) {
        // Do something with the list of cards.
    }
}).fail(new PromiseAction<Exception>() {
    @Override
    public void call(Exception e) {
        // Do something with the error.
    }
});
```

### Get user cards

```java
user.getCards().then(new PromiseAction<List<Card>>() {
    @Override
    public void call(List<Card> cards) {
        // Do something with the list of the cards.
    }
});
```

### Create new card

```java
CardRequest cardRequest = new CardRequest("label", "USD");
user.createCard(cardRequest);
```

Handling the success and error flow:

```java
CardRequest cardRequest = new CardRequest("label", "USD");
user.createCard(cardRequest).then(new PromiseAction<Card>() {
    @Override
    public void call(Card card) {
	    // Do something with the card created.
    }
}).fail(new PromiseAction<Exception>() {
    @Override
    public void call(Exception e) {
        // Handle the error.
    }
});
```

### Get ticker

```java
// Instantiate the client. In this case, we don't need an
// AUTHORIZATION_TOKEN because the Ticker endpoint is public.
BitreserveClient bitreserveClient = new BitreserveClient();

// Get tickers.
bitreserveClient.getTicker().then(new PromiseAction<List<Rate>>() {
    @Override
    public void call(List<Rate> rates) {
        // Do something with the rates list.
    }
});
```

Or you could get a ticker for a specific currency:

```java
// Get tickers for BTC.
bitreserveClient.getTickersByCurrency("BTC").then(new PromiseAction<List<Rate>>() {
    @Override
    public void call(List<Rate> rates) {
        // Do something with the rates list.
    }
});
```

### Create and commit a new transaction

```java
TransactionDenominationRequest transactionDenominationRequest = new TransactionDenominationRequest("1.0", "BTC");
TransactionRequest transactionRequest = new TransactionRequest(transactionDenominationRequest, "foo@bar.com");

card.createTransaction(transactionRequest).then(new PromiseAction<Transaction>() {
    @Override
    public void call(Transaction transaction) {
        // Commit the transaction.
        transaction.commit();
    }
});
```

### Get all public transactions

```java
// Instantiate the client. In this case, we don't need an
// AUTHORIZATION_TOKEN because the Ticker endpoint is public.
BitreserveClient bitreserveClient = new BitreserveClient();

Paginator<Transaction> paginator = bitreserveClient.getReserve().getTransactions();

// Get the list of transactions.
paginator.getElements().then(new PromiseAction<List<Transaction>>() {
    @Override
    public void call(List<Transaction> transactions) {
        // Do something with the list of transactions.
    }
});

// Get the next page of transactions.
paginator.getNext().then(new PromiseAction<List<Transaction>>() {
    @Override
    public void call(List<Transaction> transactions) {
        // Do something with the list of transactions.
    }
});
```

Or you could get a specific public transaction:

```java
// Get one public transaction.
bitreserveClient.getReserve().getTransactionById("a97bb994-6e24-4a89-b653-e0a6d0bcf634").then(new PromiseAction<Transaction>() {
    @Override
    public void call(Transaction transaction) {
        // Do something with the transaction.
    }
});
```

### Get reserve status

```java
// Instantiate the client. In this case, we don't need an
// AUTHORIZATION_TOKEN because the Ticker endpoint is public.
BitreserveClient bitreserveClient = new BitreserveClient();

// Get the reserve summary of all the obligations and assets within it.
bitreserveClient.getReserve().getStatistics().then(new PromiseAction<List<ReserveStatistics>>() {
    @Override
    public void call(List<ReserveStatistics> reserveStatisticses) {
        // Do something with the reserve statistics.
    }
});
```

### Pagination

Some endpoints will return a paginator. Here are some examples on how to handle it:

```java
// Get public transactions paginator.
Paginator<Transaction> paginator = bitreserveClient.getReserve().getTransactions();

// Get the first page of transactions.
paginator.getElements().then(new PromiseAction<List<Transaction>>() {
    @Override
    public void call(List<Transaction> transactions) {
	    // Do something with the list of transactions.
    }
});

// Check if the paginator has a valid next page.
paginator.hasNext().then(new PromiseAction<Boolean>() {
    @Override
    public void call(Boolean hasNext) {
		// Do something with the hasNext.
    }
});

// Get the number of paginator elements.
paginator.count().then(new PromiseAction<Integer>() {
    @Override
    public void call(Integer count) {
        // Do something with the count.
    }
});

// Get the next page.
paginator.getNext().then(new PromiseAction<List<Transaction>>() {
    @Override
    public void call(List<Transaction> transactions) {
        // Do something with the list of transactions.
    }
});

```

## Bitreserve SDK sample

Check the sample application to explore a application using the Bitreserve Android SDK.

#### Building

To build the sample application you need the [Android Studio](http://developer.android.com/sdk/installing/studio.html). Steps to build:

1. Clone the repository.
2. Open Android Studio.
3. Click 'Import project...'.
4. Open the `sample/Bitreserve-android-sdk-demo` directory in the cloned repository.
5. Build and run the app from inside Android Studio.

The sample application is configured to use the [sandbox environment](https://sandbox.bitreserve.org), make sure you use a sandbox account to perform the login.

## Contributing & Development

#### Contributing

Have you found a bug or want to suggest something? Please search the [issues](https://github.com/bitreserve/bitreserve-sdk-android/issues) first and, if it is new, go ahead and [submit it](https://github.com/bitreserve/bitreserve-sdk-android/issues/new).

#### Develop

It will be awesome if you can help us evolve `bitreserve-sdk-android`. Want to help?

1. [Fork it](https://github.com/bitreserve/bitreserve-sdk-android).
2. Hack away.
3. Run the tests.
5. Create a [Pull Request](https://github.com/bitreserve/bitreserve-sdk-android/compare).
