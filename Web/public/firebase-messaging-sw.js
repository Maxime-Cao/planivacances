// firebase-messaging-sw.js
importScripts('https://www.gstatic.com/firebasejs/9.0.0/firebase-app-compat.js');
importScripts('https://www.gstatic.com/firebasejs/9.0.0/firebase-messaging-compat.js');

const firebaseConfig = {
    apiKey: "...",
    authDomain: "...",
    databaseURL: "...",
    projectId: "...",
    storageBucket: "...",
    messagingSenderId: "...",
    appId: "..."
};
  
const app = firebase.initializeApp(firebaseConfig);
const messaging = firebase.messaging();

self.addEventListener('push', (event) => {
    const payload = event.data.json();
    console.log('Message received in background:', payload);
});
