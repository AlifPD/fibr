package cc10.fibr.network

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

// Login User
data class LoginResponse(

    @field:SerializedName("data")
    val data: LoginResponseItem? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)

data class LoginResponseItem(

    @field:SerializedName("thumbnail")
    val thumbnail: String? = null,

    @field:SerializedName("password")
    val password: String? = null,

    @field:SerializedName("address")
    val address: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("isLoggedIn")
    val isLoggedIn: Boolean? = null,

    @field:SerializedName("credit")
    val credit: Int? = null,

    @field:SerializedName("transaction")
    val transaction: Int? = null
)

// Login Merchant
data class LoginMerchantResponse(

    @field:SerializedName("data")
    val data: LoginMerchantResponseItem? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)
@Parcelize
data class LoginMerchantResponseItem(

    @field:SerializedName("thumbnail")
    val thumbnail: String? = null,

    @field:SerializedName("password")
    val password: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("isLoggedIn")
    val isLoggedIn: Boolean? = null,

    @field:SerializedName("credit")
    val credit: Int? = null
): Parcelable

// Sign Up User
data class SignUpResponse(
    @field:SerializedName("status")
    val status: Boolean? = null
)

// Get All Merchant
data class AllMerchantResponse(

    @field:SerializedName("data")
    val data: List<AllMerchantResponseItem?>? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)
@Parcelize
data class AllMerchantResponseItem(

    @field:SerializedName("thumbnail")
    val thumbnail: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("isLoggedIn")
    val isLoggedIn: Boolean? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("credit")
    val credit: Int? = null
): Parcelable

// Get All Product
data class AllProductResponse(

    @field:SerializedName("data")
    val data: List<AllProductResponseItem?>? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)
@Parcelize
data class AllProductResponseItem(

    @field:SerializedName("unit")
    val unit: String? = null,

    @field:SerializedName("thumbnail")
    val thumbnail: String? = null,

    @field:SerializedName("quantity")
    val quantity: Int? = null,

    @field:SerializedName("price")
    val price: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("id")
    val id: String? = null
): Parcelable

// Get User
data class UserResponse(

    @field:SerializedName("data")
    val data: UserResponseItem? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)
@Parcelize
data class UserResponseItem(

    @field:SerializedName("password")
    val password: String? = null,

    @field:SerializedName("thumbnail")
    val thumbnail: String? = null,

    @field:SerializedName("address")
    val address: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("isLoggedIn")
    val isLoggedIn: Boolean? = null,

    @field:SerializedName("credit")
    val credit: Int? = null,

    @field:SerializedName("transaction")
    val transaction: Int? = null
): Parcelable

// Get Merchant
data class MerchantResponse(

    @field:SerializedName("data")
    val data: MerchantResponseItem? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)
@Parcelize
data class MerchantResponseItem(

    @field:SerializedName("thumbnail")
    val thumbnail: String? = null,

    @field:SerializedName("password")
    val password: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("isLoggedIn")
    val isLoggedIn: Boolean? = null,

    @field:SerializedName("credit")
    val credit: Int? = null
): Parcelable

// Get Product
data class ProductResponse(

    @field:SerializedName("data")
    val data: ProductResponseItem? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)
@Parcelize
data class ProductResponseItem(

    @field:SerializedName("thumbnail")
    val thumbnail: String? = null,

    @field:SerializedName("unit")
    val unit: String? = null,

    @field:SerializedName("quantity")
    val quantity: Int? = null,

    @field:SerializedName("price")
    val price: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("description")
    val description: String? = null
): Parcelable

// Add Cart
data class AddCartResponse(
    @field:SerializedName("status")
    val status: Boolean? = null
)

// Read Cart
data class ReadCartResponse(

    @field:SerializedName("data")
    val data: List<ReadCartResponseItem?>? = null,

    @field:SerializedName("data_length")
    val dataLength: Int? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)
@Parcelize
data class ReadCartResponseItem(

    @field:SerializedName("id_product")
    val idProduct: String? = null,

    @field:SerializedName("quantity")
    val quantity: Int? = null,

    @field:SerializedName("id_item")
    val idItem: String? = null,

    @field:SerializedName("id_merchant")
    val idMerchant: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("price")
    val price: Int? = null,

    @field:SerializedName("thumbnail")
    val thumbnail: String? = null,

    @field:SerializedName("unit")
    val unit: String? = null
): Parcelable

// Checkout
data class CheckoutCartResponse(
    @field:SerializedName("status")
    val status: Boolean? = null
)

// Logout User
data class LogoutResponse(
    @field:SerializedName("status")
    val status: Boolean? = null
)

// Delete Cart
data class DeleteCartResponse(
    @field:SerializedName("status")
    val status: Boolean? = null
)
