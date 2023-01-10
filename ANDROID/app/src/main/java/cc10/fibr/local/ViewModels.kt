package cc10.fibr.local

import android.util.Log
import androidx.lifecycle.*
import cc10.fibr.network.*
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.DELETE

class MainViewModel (private val pref: UserPreferences) : ViewModel(){
    private var _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    private var _loginMerchantResponse = MutableLiveData<LoginMerchantResponse>()
    val loginMerchantResponse: LiveData<LoginMerchantResponse> = _loginMerchantResponse

    private var _allMerchantResponse = MutableLiveData<AllMerchantResponse>()
    val allMerchantResponse : MutableLiveData<AllMerchantResponse> = _allMerchantResponse

    private var _allProductsResponse = MutableLiveData<AllProductResponse>()
    val allProductsResponse : MutableLiveData<AllProductResponse> = _allProductsResponse

    private var _userResponse = MutableLiveData<UserResponse>()
    val userResponse: LiveData<UserResponse> = _userResponse

    private var _merchantResponse = MutableLiveData<MerchantResponse>()
    val merchantResponse: LiveData<MerchantResponse> = _merchantResponse

    private var _productResponse = MutableLiveData<ProductResponse>()
    val productResponse: LiveData<ProductResponse> = _productResponse

    private var _addCartResponse = MutableLiveData<AddCartResponse>()
    val addCartResponse: LiveData<AddCartResponse> = _addCartResponse

    private var _readCartResponse = MutableLiveData<ReadCartResponse>()
    val readCartResponse: LiveData<ReadCartResponse> = _readCartResponse

    private var _checkoutCartResponse = MutableLiveData<CheckoutCartResponse>()
    val checkoutCartResponse: LiveData<CheckoutCartResponse> = _checkoutCartResponse

    private var _logoutResponse = MutableLiveData<LogoutResponse>()
    val logoutResponse: LiveData<LogoutResponse> = _logoutResponse

    private var _deleteCartResponse = MutableLiveData<DeleteCartResponse>()
    val deleteCartResponse: LiveData<DeleteCartResponse> = _deleteCartResponse

    fun getTokenKey(): LiveData<String> {
        return pref.getTokenKey().asLiveData()
    }
    fun getAccTypeKey(): LiveData<Boolean>{
        return pref.getAccTypeKey().asLiveData()
    }

    fun saveTokenKey(token:String){
        viewModelScope.launch {
            pref.saveTokenKey(token)
        }
    }
    fun saveAccTypeKey(accType:Boolean){
        viewModelScope.launch {
            pref.saveAccTypeKey(accType)
        }
    }

    fun deleteTokenKey(){
        viewModelScope.launch{
            pref.deleteTokenKey()
        }
    }
    fun deleteAccTypeKey(){
        viewModelScope.launch{
            pref.deleteAccTypeKey()
        }
    }

    fun loginUser(id_user: String, password: String){
        val clientLogin = ApiConfig().getApiService().loginUser(id_user, password)
        clientLogin.enqueue(object: Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if(response.isSuccessful){
                    _loginResponse.value = response.body()
                    Log.d("MainViewModel", "onResponseSuccess: ${response.body()?.status} ${response.code()}")
                }else{
                    Log.d("MainViewModel", "onResponseFailure: ${response.body()?.status} ${response.code()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("MainViewModel", "onFailure: ${t.message}")
            }

        })
    }
    fun loginMerchant(id_merchant: String, password: String){
        val clientLogin = ApiConfig().getApiService().loginMerchant(id_merchant, password)
        clientLogin.enqueue(object: Callback<LoginMerchantResponse>{
            override fun onResponse(call: Call<LoginMerchantResponse>, response: Response<LoginMerchantResponse>) {
                if(response.isSuccessful){
                    _loginMerchantResponse.value = response.body()
                    Log.d("MainViewModel", "onResponseSuccess: ${response.body()?.status} ${response.code()}")
                }else{
                    Log.d("MainViewModel", "onResponseFailure: ${response.body()?.status} ${response.code()}")
                }
            }

            override fun onFailure(call: Call<LoginMerchantResponse>, t: Throwable) {
                Log.e("MainViewModel", "onFailure: ${t.message}")
            }

        })
    }

    fun getAllMerchant(){
        val clientMerchant = ApiConfig().getApiService().getAllMerchant()
        clientMerchant.enqueue(object: Callback<AllMerchantResponse>{
            override fun onResponse(
                call: Call<AllMerchantResponse>,
                response: Response<AllMerchantResponse>,
            ) {
                if(response.isSuccessful){
                    _allMerchantResponse.value = response.body()
                    Log.d("MainViewModel", "onResponseSuccess: ${response.body()?.status} ${response.code()}")
                }else{
                    Log.d("MainViewModel", "onResponseFailure: ${response.body()?.status} ${response.code()}")
                }
            }

            override fun onFailure(call: Call<AllMerchantResponse>, t: Throwable) {
                Log.e("MainViewModel", "onFailure: ${t.message}")
            }

        })
    }

    fun getAllProducts(id_merchant: String){
        val clientProducts = ApiConfig().getApiService().getAllProducts(id_merchant)
        clientProducts.enqueue(object: Callback<AllProductResponse>{
            override fun onResponse(
                call: Call<AllProductResponse>,
                response: Response<AllProductResponse>,
            ) {
                if(response.isSuccessful){
                    _allProductsResponse.value = response.body()
                    Log.d("MainViewModel", "onResponseSuccess: ${response.body()?.status} ${response.code()}")
                }else{
                    Log.d("MainViewModel", "onResponseFailure: ${response.body()?.status} ${response.code()}")
                }
            }

            override fun onFailure(call: Call<AllProductResponse>, t: Throwable) {
                Log.e("MainViewModel", "onFailure: ${t.message}")
            }
        })
    }

    fun getUser(id_user: String){
        val clientUser = ApiConfig().getApiService().getUser(id_user)
        clientUser.enqueue(object: Callback<UserResponse>{
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if(response.isSuccessful){
                    _userResponse.value = response.body()
                    Log.d("MainViewModel", "onResponseSuccess: ${response.body()?.status} ${response.code()}")
                }else{
                    Log.d("MainViewModel", "onResponseFailure: ${response.body()?.status} ${response.code()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e("MainViewModel", "onFailure: ${t.message}")
            }

        })
    }

    fun getMerchant(id_merchant: String){
        val clientMerchant = ApiConfig().getApiService().getMerchant(id_merchant)
        clientMerchant.enqueue(object: Callback<MerchantResponse>{
            override fun onResponse(call: Call<MerchantResponse>, response: Response<MerchantResponse>) {
                if(response.isSuccessful){
                    _merchantResponse.value = response.body()
                    Log.d("MainViewModel", "onResponseSuccess: ${response.body()?.status} ${response.code()}")
                }else{
                    Log.d("MainViewModel", "onResponseFailure: ${response.body()?.status} ${response.code()}")
                }
            }

            override fun onFailure(call: Call<MerchantResponse>, t: Throwable) {
                Log.e("MainViewModel", "onFailure: ${t.message}")
            }

        })
    }

    fun getProduct(id_merchant: String, id_user: String){
        val clientProduct = ApiConfig().getApiService().getProduct(id_merchant, id_user)
        clientProduct.enqueue(object: Callback<ProductResponse>{
            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                if(response.isSuccessful){
                    _productResponse.value = response.body()
                    Log.d("MainViewModel", "onResponseSuccess: ${response.body()?.status} ${response.code()}")
                }else{
                    Log.d("MainViewModel", "onResponseFailure: ${response.body()?.status} ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                Log.e("MainViewModel", "onFailure: ${t.message}")
            }

        })
    }

    fun addCart(id_user: String, id_item: String, id_merchant: String, id_product: String, name: String, thumbnail: String, price: Int, unit: String, quantity: Int){
        val clientCart = ApiConfig().getApiService().addCart(id_user, id_item,  id_merchant, id_product, name, thumbnail, price, unit, quantity)
        clientCart.enqueue(object: Callback<AddCartResponse>{
            override fun onResponse(call: Call<AddCartResponse>, response: Response<AddCartResponse>) {
                if(response.isSuccessful){
                    _addCartResponse.value = response.body()
                    Log.d("MainViewModel", "onResponseSuccess: ${response.body()?.status} ${response.code()}")
                }else{
                    Log.d("MainViewModel", "onResponseFailure: ${response.body()?.status} ${response.code()}")
                }
            }

            override fun onFailure(call: Call<AddCartResponse>, t: Throwable) {
                Log.e("MainViewModel", "onFailure: ${t.message}")
            }

        })
    }

    fun readCart(id_user: String){
        val clientCart = ApiConfig().getApiService().readCart(id_user)
        clientCart.enqueue(object: Callback<ReadCartResponse>{
            override fun onResponse(call: Call<ReadCartResponse>, response: Response<ReadCartResponse>) {
                if(response.isSuccessful){
                    _readCartResponse.value = response.body()
                    Log.d("MainViewModel", "onResponseSuccess: ${response.body()?.status} ${response.code()}")
                }else{
                    Log.d("MainViewModel", "onResponseFailure: ${response.body()?.status} ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ReadCartResponse>, t: Throwable) {
                Log.e("MainViewModel", "onFailure: ${t.message}")
            }

        })
    }

    fun checkoutCart(id_user: String){
        val clientCart = ApiConfig().getApiService().checkoutCart(id_user)
        clientCart.enqueue(object: Callback<CheckoutCartResponse>{
            override fun onResponse(
                call: Call<CheckoutCartResponse>,
                response: Response<CheckoutCartResponse>,
            ) {
                if(response.isSuccessful){
                    _checkoutCartResponse.value = response.body()
                    Log.d("MainViewModel", "onResponseSuccess: ${response.body()?.status} ${response.code()}")
                }else{
                    Log.d("MainViewModel", "onResponseFailure: ${response.body()?.status} ${response.code()}")
                }
            }

            override fun onFailure(call: Call<CheckoutCartResponse>, t: Throwable) {
                Log.e("MainViewModel", "onFailure: ${t.message}")
            }

        })
    }

    fun logout(id_user: String){
        val clientUser = ApiConfig().getApiService().logout(id_user)
        clientUser.enqueue(object: Callback<LogoutResponse>{
            override fun onResponse(
                call: Call<LogoutResponse>,
                response: Response<LogoutResponse>,
            ) {
                if(response.isSuccessful){
                    _logoutResponse.value = response.body()
                    deleteTokenKey()
                    Log.d("MainViewModel", "onResponseSuccess: ${response.body()?.status} ${response.code()}")
                }else{
                    Log.d("MainViewModel", "onResponseFailure: ${response.body()?.status} ${response.code()}")
                }
            }

            override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
                Log.e("MainViewModel", "onFailure: ${t.message}")
            }

        })
    }

    fun deleteCart(id_user: String){
        val clientUser = ApiConfig().getApiService().deleteCart(id_user)
        clientUser.enqueue(object: Callback<DeleteCartResponse>{
            override fun onResponse(
                call: Call<DeleteCartResponse>,
                response: Response<DeleteCartResponse>,
            ) {
                if(response.isSuccessful){
                    _deleteCartResponse.value = response.body()
                    Log.d("MainViewModel", "onResponseSuccess: ${response.body()?.status} ${response.code()}")
                }else{
                    Log.d("MainViewModel", "onResponseFailure: ${response.body()?.status} ${response.code()}")
                }
            }

            override fun onFailure(call: Call<DeleteCartResponse>, t: Throwable) {
                Log.e("MainViewModel", "onFailure: ${t.message}")
            }

        })
    }
}

class SignUpViewModel : ViewModel(){
    private var _signUpResponse = MutableLiveData<SignUpResponse>()
    val signUpResponse: LiveData<SignUpResponse> = _signUpResponse

    fun signUpUser(id_user: String, name: String, password: String, address: String, thumbnail: String){

        val clientSignUp = ApiConfig().getApiService().signUpUser(id_user, name, password, address, thumbnail)
        clientSignUp.enqueue(object : Callback<SignUpResponse>{
            override fun onResponse(
                call: Call<SignUpResponse>,
                response: Response<SignUpResponse>,
            ) {
                if(response.isSuccessful){
                    _signUpResponse.value = response.body()
                    Log.d("RegisterViewModel", "onResponseSuccess: ${response.body()?.status} ${response.code()}")
                }else{
                    Log.d("RegisterViewModel", "onResponseFailure: ${response.body()?.status} ${response.code()}")
                }
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                Log.e("RegisterViewModel", "onFailure: ${t.message}")
            }

        })
    }
}

class ViewModelFactory(private val pref: UserPreferences): ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class: "+ modelClass.name)
    }
}