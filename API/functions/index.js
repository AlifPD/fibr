const functions = require("firebase-functions");
const admin = require("firebase-admin");
const express = require("express");
const cors = require("cors");
const app = express();

app.get('/api-test', (req, res) => {
    return res.status(200).send("Fibr API Test");
});

exports.app = functions.https.onRequest(app);