// firebase-messaging-sw.js
importScripts('https://www.gstatic.com/firebasejs/9.0.0/firebase-app-compat.js');
importScripts('https://www.gstatic.com/firebasejs/9.0.0/firebase-messaging-compat.js');

const firebaseConfig = {
    apiKey: "AIzaSyDEed69NWze9I39twdSl8a8SvZAOvO51QU",
    authDomain: "planivacances.firebaseapp.com",
    databaseURL: "https://planivacances-default-rtdb.europe-west1.firebasedatabase.app",
    projectId: "planivacances",
    storageBucket: "planivacances.appspot.com",
    messagingSenderId: "389100630019",
    appId: "1:389100630019:web:11cfe216ccc49552511e36"
};
  
const app = firebase.initializeApp(firebaseConfig);
const messaging = firebase.messaging();

self.addEventListener('push', (event) => {
    const payload = event.data.json();
    console.log('Message received in background:', payload);
});
