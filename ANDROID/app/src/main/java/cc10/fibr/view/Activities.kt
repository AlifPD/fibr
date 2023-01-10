package cc10.fibr.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import cc10.fibr.R
import cc10.fibr.databinding.*
import cc10.fibr.helper.uriToFile
import cc10.fibr.local.MainViewModel
import cc10.fibr.local.SignUpViewModel
import cc10.fibr.local.UserPreferences
import cc10.fibr.local.ViewModelFactory
import cc10.fibr.network.AllMerchantResponseItem
import cc10.fibr.network.AllProductResponseItem
import cc10.fibr.network.LoginResponse
import cc10.fibr.network.ReadCartResponseItem
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.lang.Exception

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val viewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[MainViewModel::class.java]

        binding.loginButtonTologin.setOnClickListener {
            val inputId = binding.loginEdtextId.text.toString()
            val inputPassword = binding.loginEdtextPassword.text.toString()

            viewModel.loginUser(inputId, inputPassword)
            viewModel.loginResponse.observe(this){
                if(it.status == true){
                    Toast.makeText(
                        this,
                        "Login Success",
                        Toast.LENGTH_SHORT
                    ).show()

                    viewModel.saveTokenKey(inputId)

                    val intent = Intent(this, MainActivity::class.java)

                    intent.putExtra("DATA_NAME", it.data?.name)
                    intent.putExtra("DATA_ADDRESS", it.data?.address)
                    intent.putExtra("DATA_CREDIT", it.data?.credit)
                    intent.putExtra("DATA_THUMBNAIL", it.data?.thumbnail)
                    intent.putExtra("LOGINCHECK", true)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK

                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(
                        this,
                        "Please Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.loginEdtextId.text?.clear()
                    binding.loginEdtextPassword.text?.clear()
                }
            }
        }

        binding.loginTvTosignup.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}

class LoginMerchantActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityLoginMerchantBinding
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityLoginMerchantBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        val viewModel = ViewModelProvider(
//            this,
//            ViewModelFactory(UserPreferences.getInstance(dataStore))
//        )[MainViewModel::class.java]
//
//        binding.loginMerchantButton.setOnClickListener {
//            val inputId = binding.loginMerchantIdEdittext.text.toString()
//            val inputPassword = binding.loginMerchantPasswordEdittext.text.toString()
//
//            viewModel.loginMerchant(inputId, inputPassword)
//            viewModel.loginMerchantResponse.observe(this){
//                if(it.status == true){
//                    Toast.makeText(
//                        this,
//                        "Login Success",
//                        Toast.LENGTH_SHORT
//                    ).show()
//
//                    viewModel.saveTokenKey(true)
//                    viewModel.saveAccTypeKey(true)
//
//                    val intent = Intent(this, MainActivity::class.java)
//                    intent.flags =
//                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                    intent.putExtra("LOGINCHECK", true)
//                    startActivity(intent)
//                    finish()
//                }else{
//                    Toast.makeText(
//                        this,
//                        "Please Try Again",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    binding.loginMerchantIdEdittext.text?.clear()
//                    binding.loginMerchantPasswordEdittext.text?.clear()
//                }
//            }
//        }
//
//        binding.switchUser.setOnClickListener {
//            val intent = Intent(this, LoginActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//            startActivity(intent)
//            finish()
//        }
//
//        binding.switchSignupMerchant.setOnClickListener {
//            val intent = Intent(this, SignUpMerchantActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//            startActivity(intent)
//            finish()
//        }
//    }
}

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private var myFile: Uri? = null

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val selectedImg: Uri = it.data?.data as Uri
            myFile = selectedImg

            binding.signupImvThumbnail.setImageURI(selectedImg)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[SignUpViewModel::class.java]

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.signupButtonTogalerry.setOnClickListener{startGallery()}
        binding.signupButtonTosignup.setOnClickListener {
            val inputId = binding.signupEdtextId.text.toString()
            val inputPassword = binding.signupEdtextPassword.text.toString()
            val inputName = binding.signupEdtextName.text.toString()
            val inputAddress = binding.signupEdtextAddress.text.toString()
            val storageRef = FirebaseStorage.getInstance().getReference("Users/Profile/" + inputId)

            myFile?.let { it1 -> storageRef.putFile(it1) }?.addOnSuccessListener {
                Toast.makeText(this, "Succesfully Upload Image", Toast.LENGTH_SHORT).show()

                viewModel.signUpUser(inputId, inputName, inputPassword, inputAddress, inputId)
                viewModel.signUpResponse.observe(this){
                    if(it.status == true){
                        Toast.makeText(
                            this,
                            getString(R.string.signup_success),
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }else{
                        Toast.makeText(
                            this,
                            getString(R.string.signup_failed),
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.signupEdtextId.text?.clear()
                        binding.signupEdtextName.text?.clear()
                        binding.signupEdtextPassword.text?.clear()
                        binding.signupEdtextAddress.text?.clear()
                    }
                }
            }?.addOnFailureListener {
                Toast.makeText(this, "Failed Upload Image and Sign Up", Toast.LENGTH_SHORT).show()
                binding.signupEdtextId.text?.clear()
                binding.signupEdtextName.text?.clear()
                binding.signupEdtextPassword.text?.clear()
                binding.signupEdtextAddress.text?.clear()
            }

        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"

        val chooser = Intent.createChooser(intent, "Select a Picture")
        galleryLauncher.launch(chooser)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    getString(R.string.permission_denied),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }
}

class SignUpMerchantActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_sign_up_merchant)
//    }
}

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val viewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[MainViewModel::class.java]

        val isJustLogin = intent.getBooleanExtra("LOGINCHECK", false)

        if (!isJustLogin) {
            viewModel.getTokenKey().observe(this) { token ->
                if (token.isEmpty()) {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }else{
                    viewModel.getTokenKey().observe(this){token2 ->
                        viewModel.getUser(token2)
                        viewModel.userResponse.observe(this){
                            binding.mainTvName.text = it.data?.name ?: ""
                            binding.mainTvAddress.text = it.data?.address ?: ""
                            binding.mainTvCredit.text = it.data?.credit.toString()

                            val urlThumbnail = "https://storage.googleapis.com/fibr-3ac5b.appspot.com/Users/Profile/" + (it.data?.thumbnail?: "")
                            Log.d("TAG", urlThumbnail)
                            Glide.with(this)
                                .load(urlThumbnail)
                                .into(binding.mainImvThumbnail)
                        }
                    }
                    viewModel.getAllMerchant()
                    viewModel.allMerchantResponse.observe(this){
                        showMerchants(it.data)
                    }
                }
            }
        } else {
            viewModel.getTokenKey().observe(this){
                viewModel.getUser(it)
                viewModel.userResponse.observe(this){
                    binding.mainTvName.text = it.data?.name ?: ""
                    binding.mainTvAddress.text = it.data?.address ?: ""
                    binding.mainTvCredit.text = it.data?.credit.toString()

                    val urlThumbnail = "https://storage.googleapis.com/fibr-3ac5b.appspot.com/Users/Profile/" + (it.data?.thumbnail?: "")
                    Log.d("TAG", urlThumbnail)
                    Glide.with(this)
                        .load(urlThumbnail)
                        .into(binding.mainImvThumbnail)
                }
            }
            viewModel.getAllMerchant()
            viewModel.allMerchantResponse.observe(this){list ->
                showMerchants(list.data)
            }
        }

        binding.mainButtonTocart.setOnClickListener {
            val toCartIntent = Intent(this, CartActivity::class.java)
            startActivity(toCartIntent)
        }

        binding.mainButtonTologout.setOnClickListener {
            viewModel.getTokenKey().observe(this){ token ->
                viewModel.logout(token)
                viewModel.logoutResponse.observe(this){
                    if(it.status == true){
                        Toast.makeText(this, "Logout Success", Toast.LENGTH_SHORT).show()
                        val toLoginIntent = Intent(this, LoginActivity::class .java)
                        toLoginIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(toLoginIntent)
                        finish()
                    }else{
                        Toast.makeText(this, "Logout Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun showMerchants(list: List<AllMerchantResponseItem?>?) {
        binding.mainRvMerchants.layoutManager = GridLayoutManager(this, 2)

        val merchantAdapter = MerchantsAdapter(list)
        binding.mainRvMerchants.adapter = merchantAdapter

        merchantAdapter.setOnItemClickCallback(object : MerchantsAdapter.OnItemClickCallback {
            override fun onItemClicked(data: AllMerchantResponseItem?) {
                val toMerchantIntent = Intent(this@MainActivity, DetailMerchantActivity::class.java)
                toMerchantIntent.putExtra("DATA", data)
                startActivity(toMerchantIntent)
            }

        })
    }
}

class DetailMerchantActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailMerchantBinding
    private var idMerchant = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMerchantBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val viewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[MainViewModel::class.java]

        val intentData = intent.getParcelableExtra<AllMerchantResponseItem>("DATA")
        idMerchant = intentData?.id.toString()
        val urlThumbnail = "https://storage.googleapis.com/fibr-3ac5b.appspot.com/Merchants/Thumbnails/"

        viewModel.getMerchant(intentData?.id.toString())
        viewModel.merchantResponse.observe(this){
            binding.merchantTvName.text = it.data?.name ?: ""
            Glide.with(this).load(urlThumbnail+(it.data?.thumbnail)).into(binding.merchantImvThumbnail)

            viewModel.getAllProducts(intentData?.id.toString())
            viewModel.allProductsResponse.observe(this){ list ->
                showProducts(list.data)
            }
        }
    }

    private fun showProducts(list: List<AllProductResponseItem?>?) {
        binding.merchantRvProducts.layoutManager = LinearLayoutManager(this)

        val productAdapter = ProductAdapter(list)
        binding.merchantRvProducts.adapter = productAdapter

        productAdapter.setOnItemClickCallback(object : ProductAdapter.OnItemClickCallback {
            override fun onItemClicked(data: AllProductResponseItem?) {
                val toMerchantIntent = Intent(this@DetailMerchantActivity, DetailProductActivity::class.java)
                toMerchantIntent.putExtra("DATA", data)
                toMerchantIntent.putExtra("ID_MERCHANT", idMerchant)
                startActivity(toMerchantIntent)
            }
        })
    }
}

class DetailProductActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailProductBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val viewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[MainViewModel::class.java]

        val dataIntent = intent.getParcelableExtra<AllProductResponseItem>("DATA")
        val idProduct = dataIntent?.id
        val idMerchant = intent.getStringExtra("ID_MERCHANT")
        val urlThumbnail = "https://storage.googleapis.com/fibr-3ac5b.appspot.com/Merchants/Products/"

        var productName = ""
        var productPrice = 0
        var productThumbnail = ""
        var productUnit = ""

        viewModel.getProduct(idMerchant.toString(), idProduct.toString())
        viewModel.productResponse.observe(this) {
            productName = it.data?.name.toString()
            productPrice = it.data?.price?:0
            productThumbnail = it.data?.thumbnail.toString()
            productUnit = it.data?.unit.toString()

            Glide.with(this).load(urlThumbnail+ (it.data?.thumbnail)).into(binding.productImvThumbnail)
            binding.productTvName.text = it.data?.name ?: ""
            binding.productTvPrice.text = it.data?.price.toString() + "/" + it.data?.unit
            binding.productTvDescription.text = dataIntent?.description ?: ""
        }

        binding.productPickerQuantity.minValue = 1
        binding.productPickerQuantity.maxValue = 50

        binding.productButtonToadd.setOnClickListener {
            viewModel.getTokenKey().observe(this){token ->
                viewModel.readCart(token)
                viewModel.readCartResponse.observe(this){numOfItem ->
                    viewModel.addCart(token, ((numOfItem.dataLength?.plus(1)).toString()), idMerchant?:"",
                        idProduct.toString(), productName, productThumbnail, productPrice, productUnit, binding.productPickerQuantity.value)
                }
            }
        }

        viewModel.addCartResponse.observe(this){
            if(it.status == true){
                Toast.makeText(
                    this,
                    "Added to Cart",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else{
                Toast.makeText(
                    this,
                    "Failed to Add",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

class CartActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val viewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreferences.getInstance(dataStore))
        )[MainViewModel::class.java]

        viewModel.getTokenKey().observe(this){token ->
            viewModel.readCart(token)
            viewModel.readCartResponse.observe(this){
                showCart(it.data)
                binding.cartTvTotalvalue.text = calculateTotal(it.data)
            }
        }

        binding.cartButtonTocheckout.setOnClickListener{
            viewModel.getTokenKey().observe(this){ token ->
                viewModel.checkoutCart(token)
                viewModel.checkoutCartResponse.observe(this){ response ->
                    if(response.status == true){
                        Toast.makeText(this, "Checkout Success", Toast.LENGTH_SHORT).show()
                        viewModel.deleteCart(token)
                        finish()
                    }else{
                        Toast.makeText(this, "Checkout Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun showCart(list: List<ReadCartResponseItem?>?) {
        binding.cartRvCarts.layoutManager = LinearLayoutManager(this)

        val cartAdapter = CartAdapter(list)
        binding.cartRvCarts.adapter = cartAdapter

//        cartAdapter.setOnItemClickCallback(object : CartAdapter.OnItemClickCallback {
//            override fun onItemClicked(data: ReadCartResponseItem?) {
//                val toCartIntent = Intent(this@CartActivity, DetailMerchantActivity::class.java)
//                toCartIntent.putExtra("DATA", data)
//                startActivity(toCartIntent)
//            }
//        })
    }

    private fun calculateTotal(list: List<ReadCartResponseItem?>?): String{
        var cartTotal = 0
        var temp = 0

        if (list != null) {
            for(i in list){
                temp = i?.quantity?.let { i.price?.times(it) } ?: 0
                cartTotal += temp
            }
        }

        return cartTotal.toString()
    }
}

