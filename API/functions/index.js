const functions = require("firebase-functions");
const admin = require("firebase-admin");

var serviceAccount = require("./permissions.json");
admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
});

const express = require("express");
const cors = require("cors");
const app = express();
const db = admin.firestore();

app.use(cors({origin:true}));
app.use(express.json());

// Test API
app.get('/api-test', (req, res) => {
    return res.status(200).send("Fibr API Test");
});

// Sign Up, Login, Logout Route --------------------------------------------------------------
// Sign Up as User
app.post('/api/signup', (req,res) => {
    (async() => {
        try{
            const document = db.collection('Users').doc('/' + req.body.id_user + '/');
            let product = await document.get();
            let response = product.data();

            if(response != undefined){
                throw console.error();
            }else{
                await db.collection('Users').doc('/' + req.body.id_user + '/')
                .create({
                    name: req.body.name,
                    password: req.body.password,
                    address: req.body.address,
                    credit: 0,
                    isLoggedIn: false,
                    transaction: 0,
                    thumbnail: req.body.thumbnail
                })
            }

            return res.status(200).send();
        }catch(error){
            console.log(error);
            return res.status(500).send(error);
        }
    })();
});
    
// Login as User
app.get('/api/login', (req, res) => {
    (async() => {
        try{
            const document = db.collection('Users').doc('/' + req.body.id_user + '/');
            let product = await document.get();
            let response = product.data();

            if((response == undefined) || (response.password != req.body.password)){
                throw console.error();
            }

            document.update({
                isLoggedIn: true
            });
            return res.status(200).send();
        }catch(error){
            console.log(error);
            return res.status(500).send(error);
        }
    })();
});

// Logout User
app.put('/api/logout', (req, res) => {
    (async() => {
        try{
            const document = db.collection('Users').doc('/' + req.body.id_user + '/');

            await document.update({
                isLoggedIn: false
            });
            
            return res.status(200).send();
        }catch(error){
            console.log(error);
            return res.status(500).send(error);
        }
    })();
});

// Sign Up as Merchant
app.post('/api/signup-merchant', (req,res) => {
    (async() => {
        try{
            const document = db.collection('Merchants').doc('/' + req.body.id_merchant + '/');
            let product = await document.get();
            let response = product.data();

            if(response != undefined){
                throw console.error();
            }else{
                await db.collection('Merchants').doc('/' + req.body.id_merchant + '/')
            .create({
                name: req.body.name,
                password: req.body.password,
                credit: 0,
                isLoggedIn: false,
                thumbnail: req.body.thumbnail
            })
            }

            return res.status(200).send();
        }catch(error){
            console.log(error);
            return res.status(500).send(error);
        }
    })();
});
    
// Login as Merchant
app.get('/api/login-merchant', (req, res) => {
    (async() => {
        try{
            const document = db.collection('Merchants').doc('/' + req.body.id_merchant + '/');
            let product = await document.get();
            let response = product.data();

            if((response == undefined) || (response.password != req.body.password)){
                throw console.error();
            }

            document.update({
                isLoggedIn: true
            });
            return res.status(200).send();
        }catch(error){
            console.log(error);
            return res.status(500).send(error);
        }
    })();
});

// Logout Merchant
app.put('/api/logout-merchant', (req, res) => {
    (async() => {
        try{
            const document = db.collection('Merchants').doc('/' + req.body.id_merchant + '/');

            await document.update({
                isLoggedIn: false
            });
            
            return res.status(200).send();
        }catch(error){
            console.log(error);
            return res.status(500).send(error);
        }
    })();
});

// Merchants/Product ---------------------------------------------------------------------
// Create/Add product
app.post('/api/create', (req,res) => {
    (async() => {
        try{
            await db.collection('Merchants/' + req.body.id_merchant + '/Products').doc('/' + req.body.id_product + '/')
            .create({
                name: req.body.name,
                price: req.body.price,
                unit: req.body.unit,
                quantity: req.body.quantity,
                thumbnail: req.body.thumbnail,
                description: req.body.description
            })
            return res.status(200).send();
        }catch(error){
            console.log(error);
            return res.status(500).send(error);
        }
    })();
});

// Read/Retreive specific product
app.get('/api/read', (req, res) => {
    (async() => {
        try{
            const document = db.collection('Merchants/' + req.body.id_merchant + '/Products').doc('/' + req.body.id_product + '/');
            let product = await document.get();
            let response = product.data();

            if(response == undefined){
                throw console.error();
            }

            return res.status(200).send(response);
        }catch(error){
            console.log(error);
            return res.status(500).send(error);
        }
    })();
});

// Read/Retreive all product from 1 merchant
app.get('/api/read-all', (req, res) => {
    (async() => {
        try{
            let query = db.collection('Merchants/' + req.body.id_merchant + '/Products');
            let response = [];
            
            await query.get().then(querySnapshot => {
                let docs = querySnapshot.docs;
                for(let doc of docs){
                    const selectedItem = {
                        id: doc.id,
                        name: doc.data().name,
                        price: doc.data().price,
                        unit: doc.data().unit,
                        quantity: doc.data().quantity,
                        thumbnail: doc.data().thumbnail,
                        description: doc.data().description
                    };
                    response.push(selectedItem);
                }
            })
            return res.status(200).send(response);
        }catch(error){
            console.log(error);
            return res.status(500).send(error);
        }
    })();
});

// Update a product
app.put('/api/update', (req,res) => {
    (async() => {
        try{
            const document = db.collection('Merchants/' + req.body.id_merchant + '/Products').doc('/' + req.body.id_product + '/');

            await document.update({
                name: req.body.name,
                price: req.body.price,
                unit: req.body.unit,
                quantity: req.body.quantity,
                thumbnail: req.body.thumbnail,
                description: req.body.description
            });
            
            return res.status(200).send();
        }catch(error){
            console.log(error);
            return res.status(500).send(error);
        }
    })();
});

// Delete a product
app.delete('/api/delete', (req,res) => {
    (async() => {
        try{
            const document = db.collection('Merchants/' + req.body.id_merchant + '/Products').doc('/' + req.body.id_product + '/');

            await document.delete();
            
            return res.status(200).send();
        }catch(error){
            console.log(error);
            return res.status(500).send(error);
        }
    })();
});


// Users -------------------------------------------------------------------------------
// Add to Cart
app.post('/api/add-cart', (req,res) => {
    (async() => {
        try{
            await db.collection('Users/' + req.body.id_user + '/Cart').doc('/' + req.body.id_item + '/')
            .create({
                id_merchant: req.body.id_merchant,
                id_product: req.body.id_product,
                quantity: req.body.quantity
            })
            return res.status(200).send();
        }catch(error){
            console.log(error);
            return res.status(500).send(error);
        }
    })();
});

// Get All Items within a Cart
app.get('/api/all-cart-item', (req, res) => {
    (async() => {
        try{
            let query = db.collection('Users/' + req.body.id_user + '/Cart');
            let response = [];

            await query.get().then(querySnapshot => {
                let docs = querySnapshot.docs;
                for(let doc of docs){
                    const selectedItem = {
                        id_item: doc.id,
                        id_merchant: doc.id_merchant,
                        id_product: doc.id_product,
                        quantity: doc.quantity
                    };
                    response.push(selectedItem);
                }
            })
            return res.status(200).send(response);
        }catch(error){
            console.log(error);
            return res.status(500).send(error);
        }
    })();
});

// Checkout Cart
app.get('/api/checkout', (req, res) => {
    (async() => {
        try{
            let query = db.collection('Users/' + req.body.id_user + '/Cart');
            let response = [];

            const documentUser = db.collection('Users/').doc(req.body.id_user + '/');
            let user = await documentUser.get();
            let responseUser = user.data();
            
            let numTransaction = responseUser.transaction + 1;
            let totalPrice = 0;
            let totalPriceMerchant = 0;
            
            await query.get().then(async querySnapshot => {
                let docs = querySnapshot.docs;
                for(let doc of docs){
                    const selectedItem = {
                        id: doc.id,
                        id_merchant: doc.data().id_merchant,
                        id_product: doc.data().id_product,
                        quantity: doc.data().quantity
                    };
                    response.push(selectedItem);
                    
                    const documentMerchant = db.collection('Merchants/').doc(doc.data().id_merchant + '/');
                    let merchant = await documentMerchant.get();
                    let responseMerchant = merchant.data();

                    const documentProduct = db.collection('Merchants/' + doc.data().id_merchant + '/Products').doc('/' + doc.data().id_product + '/');
                    let product = await documentProduct.get();
                    let responseProduct = product.data();
                    await documentProduct.update({
                        quantity: responseProduct.quantity - doc.data().quantity
                    });
                    totalPrice = totalPrice + (responseProduct.price * doc.data().quantity);
                    totalPriceMerchant = totalPriceMerchant + (responseProduct.price * doc.data().quantity);
                    await documentMerchant.update({
                        credit: responseMerchant.credit + totalPriceMerchant
                    });
                    totalPriceMerchant = 0;

                    
                    await db.collection('Users/' + req.body.id_user + '/Transaction' + numTransaction).doc('/' + doc.id + '/')
                    .create({
                        id_merchant: doc.data().id_merchant,
                        id_product: doc.data().id_product,
                        quantity: doc.data().quantity
                    })
                }
            })
            await documentUser.update({
                credit: responseUser.credit - totalPrice,
                transaction: responseUser.transaction + 1,
            });

            return res.status(200).send(response);
        }catch(error){
            console.log(error);
            return res.status(500).send(error);
        }
    })();
});

// Delete Cart
app.delete('/api/delete-cart', (req,res) => {
    (async() => {
        try{
            let query = db.collection('Users/' + req.body.id_user + '/Cart');
            
            await query.get().then(async querySnapshot => {
                let docs = querySnapshot.docs;
                for(let doc of docs){
                    const selectedItem = {
                        id: doc.id,
                        name: doc.data().name,
                        price: doc.data().price,
                        unit: doc.data().unit,
                        quantity: doc.data().quantity,
                        thumbnail: doc.data().thumbnail,
                        description: doc.data().description
                    };
                    const document = db.collection('Users/' + req.body.id_user + '/Cart').doc('/' + doc.id + '/');       
                    await document.delete();
                }
            })
            
            return res.status(200).send();
        }catch(error){
            console.log(error);
            return res.status(500).send(error);
        }
    })();
});

// Get All Transaction Items
app.get('/api/transaction', (req, res) => {
    (async() => {
        try{
            let query = db.collection('Users/' + req.body.id_user + '/Transaction' + req.body.id_transaction);
            let response = [];
            
            await query.get().then(querySnapshot => {
                let docs = querySnapshot.docs;
                for(let doc of docs){
                    const selectedItem = {
                        id: doc.id,
                        id_merchant: doc.data().id_merchant,
                        id_product: doc.data().id_product,
                        quantity: doc.data().quantity
                    };
                    response.push(selectedItem);
                }
            })
            return res.status(200).send(response);
        }catch(error){
            console.log(error);
            return res.status(500).send(error);
        }
    })();
});

exports.app = functions.https.onRequest(app);