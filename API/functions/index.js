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

// Post data to Database
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

exports.app = functions.https.onRequest(app);