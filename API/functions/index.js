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

// Test API
app.get('/api-test', (req, res) => {
    return res.status(200).send("Fibr API Test");
});

// Post data to Database (Products)
app.post('/api/create', (req,res) => {
    (async() => {
        try{
            await db.collection('Merchants/' + req.body.id_merchant + '/Products').doc('/' + req.body.id_product + '/')
            .create({
                name: req.body.name,
                price: req.body.price,
                unit: req.body.unit,
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

// Get Database (Products)
app.get('/api/read', (req, res) => {
    (async() => {
        try{
            const document = db.collection('Merchants/' + req.body.id_merchant + '/Products').doc('/' + req.body.id_product + '/');
            let product = await document.get();
            let response = product.data();

            return res.status(200).send(response);
        }catch(error){
            console.log(error);
            return res.status(500).send(error);
        }
    })();
});

//Get Database (All Products)
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
                        thumbnail: doc.data().thumbnail,
                        description: doc.data().description
                    };
                    response.push(selectedItem);
                }
                return response;
            })
            return res.status(200).send(response);
        }catch(error){
            console.log(error);
            return res.status(500).send(error);
        }
    })();
});

exports.app = functions.https.onRequest(app);