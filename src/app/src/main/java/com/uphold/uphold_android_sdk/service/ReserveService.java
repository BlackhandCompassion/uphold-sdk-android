package com.uphold.uphold_android_sdk.service;

import com.uphold.uphold_android_sdk.model.Transaction;
import com.uphold.uphold_android_sdk.model.reserve.Deposit;
import com.uphold.uphold_android_sdk.model.reserve.ReserveStatistics;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Path;

/**
 * Reserve service.
 */

public interface ReserveService {

    /**
     * Performs a request to get the reserve ledger.
     *
     * @param range The range of the request.
     * @param callback A callback to receive the request information.
     */

    @GET("/v0/reserve/ledger")
    void getLedger(@Header("Range") String range, Callback<List<Deposit>> callback);

    /**
     * Performs a request to get a reserve transaction.
     *
     * @param transactionId The id of the transaction.
     * @param callback A callback to receive the request information.
     */

    @GET("/v0/reserve/transactions/{transactionId}")
    void getReserveTransactionById(@Path("transactionId") String transactionId, Callback<Transaction> callback);

    /**
     * Performs a request to get the reserve transactions.
     *
     * @param range The range of the request.
     * @param callback A callback to receive the request information.
     */

    @GET("/v0/reserve/transactions")
    void getReserveTransactions(@Header("Range") String range, Callback<List<Transaction>> callback);

    /**
     * Performs a request to get the reserve statistics.
     *
     * @param callback A callback to receive the request information.
     */

    @GET("/v0/reserve/statistics")
    void getStatistics(Callback<List<ReserveStatistics>> callback);

}
