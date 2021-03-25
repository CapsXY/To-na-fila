const firebase = require("firebase");

// Initialize Firebase
var config = {
  apiKey: "AIzaSyA_9kJeXso4RoOIyRk692P1Sck5Ji2fTpU",
  authDomain: "tonafilavf-2019.firebaseapp.com",
  databaseURL: "https://tonafilavf-2019.firebaseio.com",
  projectId: "tonafilavf-2019",
  storageBucket: "tonafilavf-2019.appspot.com",
  messagingSenderId: "406704195795",
  appId: "1:406704195795:web:2c0cabcdd5a8cbca"
  };
  
  const db = firebase.initializeApp(config);

  module.exports = db;


