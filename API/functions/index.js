const functions = require("firebase-functions");
const admin = require("firebase-admin");

var serviceAccount = require("./permissions.json");
admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
});

const express = require("express");
const cors = require("cors");
const { region } = require("firebase-functions");
const app = express();
const db = admin.firestore();

app.use(cors({origin:true}));
app.use(express.json());

const responseTemplate = {
    status: false
}

// Test API
app.get('/api-test', (req, res) => {
    return res.status(200).send("Fibr API Test");
});

// Sign Up, Login, Logout Route --------------------------------------------------------------
// Sign Up as User
app.post('/api/signup/:id_user/:name/:password/:address/:thumbnail', (req,res) => {
    (async() => {
        try{
            await db.collection('Users').doc('/' + req.params.id_user + '/')
                .create({
                    name: req.params.name,
                    password: req.params.password,
                    address: req.params.address,
                    credit: 1000000,
                    isLoggedIn: false,
                    transaction: 0,
                    thumbnail: req.params.thumbnail
                })
            return res.status(200).send({status: true});
        }catch(error){
            console.log(error);
            return res.status(500).send({status: false, error});
        }
    })();
});
    
// Login as User
app.get('/api/login/:id_user/:password', (req, res) => {
    (async() => {
        try{
            const document = db.collection('Users').doc('/' + req.params.id_user + '/');
            let response = await document.get();
            let data = response.data();

            if((data == undefined) || (data.password != req.params.password)){
                throw console.error();
            }
            await document.update({
                isLoggedIn: true
            });

            let responseReturn = await document.get();
            let dataReturn = responseReturn.data();
            

            return res.status(200).send({status: true , "data": dataReturn});
        }catch(error){
            console.log(error);
            return res.status(500).send({status: false, error});
        }
    })();
});

// Logout User
app.put('/api/logout/:id_user', (req, res) => {
    (async() => {
        try{
            const document = db.collection('Users').doc('/' + req.params.id_user + '/');

            await document.update({
                isLoggedIn: false
            });
            
            return res.status(200).send({status: true});
        }catch(error){
            console.log(error);
            return res.status(500).send({status: false, error});
        }
    })();
});

// Sign Up as Merchant
app.post('/api/signup-merchant/:id_merchant/:name/:password/:thumbnail', (req,res) => {
    (async() => {
        try{
            const document = db.collection('Merchants').doc('/' + req.params.id_merchant + '/');
            let response = await document.get();
            let data = response.data();

            if(data != undefined){
                throw console.error();
            }else{
                await db.collection('Merchants').doc('/' + req.params.id_merchant + '/')
                .create({
                    name: req.params.name,
                    password: req.params.password,
                    credit: 0,
                    isLoggedIn: false,
                    thumbnail: req.params.thumbnail
                })
            }
            
            return res.status(200).send({status: true});
        }catch(error){
            console.log(error);
            return res.status(500).send({status: false, error});
        }
    })();
});
    
// Login as Merchant
app.get('/api/login-merchant/:id_merchant/:password', (req, res) => {
    (async() => {
        try{
            const document = db.collection('Merchants').doc('/' + req.params.id_merchant + '/');
            let response = await document.get();
            let data = response.data();

            if((data == undefined) || (data.password != req.params.password)){
                throw console.error();
            }
            await document.update({
                isLoggedIn: true
            });

            let responseReturn = await document.get();
            let dataReturn = responseReturn.data();
            return res.status(200).send({status: true, "data": dataReturn});
        }catch(error){
            console.log(error);
            return res.status(500).send({status: false, error});
        }
    })();
});

// Logout Merchant
app.put('/api/logout-merchant/:id_merchant', (req, res) => {
    (async() => {
        try{
            const document = db.collection('Merchants').doc('/' + req.params.id_merchant + '/');

            await document.update({
                isLoggedIn: false
            });
            
            return res.status(200).send({status: true});
        }catch(error){
            console.log(error);
            return res.status(500).send({status: false, error});
        }
    })();
});

// Merchants/Product ---------------------------------------------------------------------
// Create/Add product
app.post('/api/create/:id_merchant/:id_product/:name/:price/:unit/:quantity/:thumbnail/:description', (req,res) => {
    (async() => {
        try{
            await db.collection('Merchants/' + req.params.id_merchant + '/Products').doc('/' + req.params.id_product + '/')
            .create({
                name: req.params.name,
                price: Number(req.params.price),
                unit: req.params.unit,
                quantity: Number(req.params.quantity),
                thumbnail: req.params.thumbnail,
                description: req.params.description
            })
            return res.status(200).send({status: true});
        }catch(error){
            console.log(error);
            return res.status(500).send({status: false, error});
        }
    })();
});

// Read/Retreive specific product
app.get('/api/read/:id_merchant/:id_product', (req, res) => {
    (async() => {
        try{
            const document = db.collection('Merchants/' + req.params.id_merchant + '/Products').doc('/' + req.params.id_product + '/');
            let response = await document.get();
            let data = response.data();

            if(data == undefined){
                throw console.error();
            }
            
            return res.status(200).send({status: true, data});
        }catch(error){
            console.log(error);
            return res.status(500).send({status : false, error});
        }
    })();
});

// Read/Retreive all product from 1 merchant
app.get('/api/read-all/:id_merchant', (req, res) => {
    (async() => {
        try{
            let query = db.collection('Merchants/' + req.params.id_merchant + '/Products');
            let data = [];
            
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
                    data.push(selectedItem);
                }
            })
            return res.status(200).send({status: true, data});
        }catch(error){
            console.log(error);
            return res.status(500).send({status: false, error});
        }
    })();
});

// Read/Retreive all Merchant
app.get('/api/read-all-merchant', (req, res) => {
    (async() => {
        try{
            let query = db.collection('Merchants/');
            let data = [];
            
            await query.get().then(querySnapshot => {
                let docs = querySnapshot.docs;
                for(let doc of docs){
                    const selectedItem = {
                        id: doc.id,
                        name: doc.data().name,
                        credit: doc.data().credit,
                        isLoggedIn: doc.data().isLoggedIn,
                        thumbnail: doc.data().thumbnail,
                    };
                    data.push(selectedItem);
                }
            })
            return res.status(200).send({status: true, data});
        }catch(error){
            console.log(error);
            return res.status(500).send({status: false, error});
        }
    })();
});

// Update a product
app.put('/api/update/:id_merchant/:id_product/:name/:price/:unit/:quantity/:thumbnail/:description', (req,res) => {
    (async() => {
        try{
            const document = db.collection('Merchants/' + req.params.id_merchant + '/Products').doc('/' + req.params.id_product + '/');

            await document.update({
                name: req.params.name,
                price: Number(req.params.price),
                unit: req.params.unit,
                quantity: Number(req.params.quantity),
                thumbnail: req.params.thumbnail,
                description: req.params.description
            });
            
            return res.status(200).send({status: true});
        }catch(error){
            console.log(error);
            return res.status(500).send({status: false, error});
        }
    })();
});

// Delete a product
app.delete('/api/delete/:id_merchant/:id_product', (req,res) => {
    (async() => {
        try{
            const document = db.collection('Merchants/' + req.params.id_merchant + '/Products').doc('/' + req.params.id_product + '/');

            await document.delete();
            
            return res.status(200).send({status: true});
        }catch(error){
            console.log(error);
            return res.status(500).send({status: false, error});
        }
    })();
});


// Users -------------------------------------------------------------------------------
// Add to Cart
app.post('/api/add-cart/:id_user/:id_item/:id_merchant/:id_product/:name/:thumbnail/:price/:unit/:quantity', (req,res) => {
    (async() => {
        try{
            await db.collection('Users/' + req.params.id_user + '/Cart').doc('/' + req.params.id_item + '/')
            .create({
                id_merchant: req.params.id_merchant,
                id_product: req.params.id_product,
                name: req.params.name,
                thumbnail: req.params.thumbnail,
                price: Number(req.params.price),
                unit: req.params.unit,
                quantity: Number(req.params.quantity)
            })
            return res.status(200).send({status: true});
        }catch(error){
            console.log(error);
            return res.status(500).send({status: false, error});
        }
    })();
});

// Get All Items within a Cart
app.get('/api/all-cart-item/:id_user', (req, res) => {
    (async() => {
        try{
            let query = db.collection('Users/' + req.params.id_user + '/Cart');
            let data = [];

            await query.get().then(querySnapshot => {
                let docs = querySnapshot.docs;
                for(let doc of docs){
                    const selectedItem = {
                        id_item: doc.id,
                        id_merchant: doc.data().id_merchant,
                        id_product: doc.data().id_product,
                        name: doc.data().name,
                        thumbnail: doc.data().thumbnail,
                        price: doc.data().price,
                        unit: doc.data().unit,
                        quantity: doc.data().quantity
                    };
                    data.push(selectedItem);
                }
            })
            let data_length = data.length

            return res.status(200).send({status: true, data_length, data});
        }catch(error){
            console.log(error);
            return res.status(500).send({status: false, error});
        }
    })();
});

// Checkout Cart
app.post('/api/checkout/:id_user', (req, res) => {
    (async() => {
        try{
            let query = db.collection('Users/' + req.params.id_user + '/Cart');
            let response = [];

            const documentUser = db.collection('Users/').doc(req.params.id_user + '/');
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
                        name: doc.data().name,
                        thumbnail: doc.data().thumbnail,
                        price: doc.data().price,
                        unit: doc.data().unit,
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

                    await db.collection('Users/' + req.params.id_user + '/Transaction' + numTransaction).doc('/' + doc.id + '/')
                    .create({
                        id_merchant: doc.data().id_merchant,
                        id_product: doc.data().id_product,
                        quantity: Number(doc.data().quantity)
                    });
                }
            });

            await documentUser.update({
                credit: responseUser.credit - totalPrice,
                transaction: responseUser.transaction + 1,
            });

            return res.status(200).send({status: true});
        }catch(error){
            console.log(error);
            return res.status(500).send({status: true, error});
        }
    })();
});

// Get All Transaction Items
app.get('/api/transaction/:id_user/:id_transaction', (req, res) => {
    (async() => {
        try{
            let query = db.collection('Users/' + req.params.id_user + '/Transaction' + req.params.id_transaction);
            let data = [];
            
            await query.get().then(querySnapshot => {
                let docs = querySnapshot.docs;
                for(let doc of docs){
                    const selectedItem = {
                        id: doc.id,
                        id_merchant: doc.data().id_merchant,
                        id_product: doc.data().id_product,
                        name: doc.data().name,
                        thumbnail: doc.data().thumbnail,
                        price: doc.data().price,
                        unit: doc.data().unit,
                        quantity: doc.data().quantity
                    };
                    data.push(selectedItem);
                }
            })
            return res.status(200).send({status: true, data});
        }catch(error){
            console.log(error);
            return res.status(500).send({status: true, error});
        }
    })();
});

// Delete Cart
app.delete('/api/delete-cart/:id_user', (req,res) => {
    (async() => {
        try{
            let query = db.collection('Users/' + req.params.id_user + '/Cart');
            
            await query.get().then(async querySnapshot => {
                let docs = querySnapshot.docs;
                for(let doc of docs){
                    await db.collection('Users/' + req.params.id_user + '/Cart').doc('/' + doc.id + '/').delete();
                }
            })
            
            return res.status(200).send({status: true});
        }catch(error){
            console.log(error);
            return res.status(500).send({status: false, error});
        }
    })();
});

// Read User
app.get('/api/user/:id_user', async (req, res) => {
    (async() => {
        try{
            const document = db.doc('Users/' + req.params.id_user);
            let response = await document.get();
            let data = response.data();

            if(data == undefined){
                throw console.error();
            }
            
            return res.status(200).send({status: true, data});
        }catch(error){
            console.log(error);
            return res.status(500).send({status : false, error});
        }
    })();
});

// Read Merchant
app.get('/api/merchant/:id_mechant', async (req, res) => {
    (async() => {
        try{
            const document = db.doc('Merchants/' + req.params.id_mechant);
            let response = await document.get();
            let data = response.data();

            if(data == undefined){
                throw console.error();
            }
            
            return res.status(200).send({status: true, data});
        }catch(error){
            console.log(error);
            return res.status(500).send({status : false, error});
        }
    })();
});

exports.app = functions.region('asia-southeast2').https.onRequest(app);