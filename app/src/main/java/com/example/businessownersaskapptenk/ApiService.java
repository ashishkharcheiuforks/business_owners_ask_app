package com.example.businessownersaskapptenk;

import com.example.businessownersaskapptenk.JsonModelObject.Drink;
import com.example.businessownersaskapptenk.JsonModelObject.Example;
import com.example.businessownersaskapptenk.JsonModelObject.Meal;
import com.example.businessownersaskapptenk.JsonModelObject.Registration;
import com.example.businessownersaskapptenk.JsonModelObject.ResponseBasicLogin;
import com.example.businessownersaskapptenk.JsonModelObject.ResponseBodyLogin;
import com.example.businessownersaskapptenk.JsonModelObject.revenue.ExampleRevenue;
import com.example.businessownersaskapptenk.JsonModelObject.revenue.ResponseAvatar;
import com.example.businessownersaskapptenk.Objects.OrderModel;
import com.example.businessownersaskapptenk.Objects.TransactionStatus;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    /**
     * USED Call<Void> here but Call<String> Works as well in this case
     *
     * @param token
     * @param clientid
     * @param clientsecret
     * @return
     */
    @FormUrlEncoded
    @POST("api/social/revoke-token")
    Call<Void> getToken(@Field("token") String token,
                        @Field("client_id") String clientid,
                        @Field("client_secret") String clientsecret);

    @FormUrlEncoded
    @POST("api/customer/order/add/")
    Call<ChecksumPaytm> getCustomerOrderAddPayment(  //StatusSuccessOrError
                                                     @Field("access_token") String accessToken,
                                                     @Field("registration_id") String registrationId,
                                                     @Field("address") String address,
                                                     @Field("order_details") String orderDetails);



    @FormUrlEncoded
    @POST("api/paytm/response/")
    Call<TransactionStatus> checkTransactionStatus(
            @Field("ORDER_ID") String orderId,
            @Field("access_token") String accessToken,
            @Field("registration_id") String registrationId,
            @Field("address") String address,
            @Field("order_details") String orderDetails
    );



    @POST("/api/social/convert-token")
    Call<ResponseBodyLogin> facebookLogin(@Body RequestBody request);


    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("api/customer/order/latest/")
    Call<Example> getResponseQuery(
            @Query("access_token") String responseAccessToken
    );

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("api/driver/revenue/")
    Call<ExampleRevenue> getResponseDriverRevenue(
            @Query("access_token") String responseAccessToken
    );


    /**
     * What are we expecting from the response, using the GET request,
     * We need to specify where endpoint is (using our API)
     * Using @Path for "/" and you would use @Query for "&" "?"
     * If we don't specify Call method, the request will be executed synchronously:
     * Network connection,Wait for response,Send the response
     * Only seconds later it will show up in our app, which would crash it.
     * <p>
     * We are using an asynchronous method which Retrofit hides from us
     */
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("api/customer/meals/{id}/")
    Call<Meal> getMeals(@Path("id") int id);


    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("api/customer/drinks/{id}/")
    Call<Drink> getDrinks(@Path("id") int id);



    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("api/customer/registrations")
    Call<Registration> getResponseRestuarnatList();

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("api/driver/orders/ready")
    Call<OrderModel> getReponseDriverOrderReady();


//    PAYTM

    @FormUrlEncoded
    @POST("generateChecksum.php")
    Call<ChecksumPaytm> getChecksum(
            @Field("MID") String mId,
            @Field("ORDER_ID") String orderId,
            @Field("CUST_ID") String custId,
            @Field("CHANNEL_ID") String channelId,
            @Field("TXN_AMOUNT") String txnAmount,
            @Field("WEBSITE") String website,
            @Field("CALLBACK_URL") String callbackUrl,
            @Field("INDUSTRY_TYPE_ID") String industryTypeId
    );


    ///////////Basic Login

    @POST("rest-auth/registration/")
    Call<ResponseBasicLogin> basicRegister(@Body RequestBody request);
    @POST("rest-auth/login/")
    Call<ResponseBasicLogin> basicLogin(@Body RequestBody request);
    @POST("/rest-auth/password/reset/")
    Call<ResponseBasicLogin> basicResetPassword(@Body RequestBody request);


    ///////////image upload
    @Multipart
    @POST("/image/upload/")
    Call<ResponseAvatar> uploadImage(@Part MultipartBody.Part file, @Part("token") RequestBody token);

}