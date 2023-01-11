package cc10.fibr.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

class ApiConfig {
    fun getApiService(): ApiService {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://asia-southeast2-fibr-3ac5b.cloudfunctions.net/app/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }
}

interface ApiService {
    @GET("api/login/{id_user}/{password}")
    fun loginUser(
        @Path("id_user") id_user: String,
        @Path("password") password: String
    ): Call<LoginResponse>

    @POST("api/signup/{id_user}/{name}/{password}/{address}/{thumbnail}")
    fun signUpUser(
        @Path("id_user") id_user: String,
        @Path("name") name: String,
        @Path("password") password: String,
        @Path("address") address: String,
        @Path("thumbnail") thumbnail: String
    ): Call<SignUpResponse>

    @GET("api/read-all-merchant")
    fun getAllMerchant() : Call<AllMerchantResponse>

    @GET("api/read-all/{id_merchant}")
    fun getAllProducts(
        @Path("id_merchant") id_merchant: String
    ): Call<AllProductResponse>

    @GET("api/user/{id_user}")
    fun getUser(
        @Path("id_user") id_user: String
    ): Call<UserResponse>

    @GET("api/merchant/{id_merchant}")
    fun getMerchant(
        @Path("id_merchant") id_merchant: String
    ): Call<MerchantResponse>

    @GET("api/read/{id_merchant}/{id_product}")
    fun getProduct(
        @Path("id_merchant") id_merchant: String,
        @Path("id_product") id_user: String
    ): Call<ProductResponse>

    @POST("api/add-cart/{id_user}/{id_item}/{id_merchant}/{id_product}/{name}/{thumbnail}/{price}/{unit}/{quantity}")
    fun addCart(
        @Path("id_user") id_user: String,
        @Path("id_item") id_item: String,
        @Path("id_merchant") id_merchant: String,
        @Path("id_product") id_product: String,
        @Path("name") name: String,
        @Path("thumbnail") thumbnail: String,
        @Path("price") price: Int,
        @Path("unit") unit: String,
        @Path("quantity") quantity: Int
    ): Call<AddCartResponse>

    @GET("api/all-cart-item/{id_user}")
    fun readCart(
        @Path("id_user") id_user: String
    ): Call<ReadCartResponse>

    @POST("api/checkout/{id_user}")
    fun checkoutCart(
        @Path("id_user") id_user: String
    ): Call<CheckoutCartResponse>

    @PUT("api/logout/{id_user}")
    fun logout(
        @Path("id_user") id_user: String
    ): Call<LogoutResponse>

    @DELETE("api/delete-cart/{id_user}")
    fun deleteCart(
        @Path("id_user") id_user: String
    ): Call<DeleteCartResponse>

    @GET("api/transaction/{id_user}/{id_transaction}")
    fun readTransaction(
        @Path("id_user") id_user: String,
        @Path("id_transaction") id_transaction: String
    ): Call<ReadTransactionResponse>

    @GET("api/all-transaction/{id_user}")
    fun readAllTransaction(
        @Path("id_user") id_user: String
    ): Call<ReadAllTransactionResponse>
}
