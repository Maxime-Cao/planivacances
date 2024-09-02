import { instance, setToken as setTokenToInstance} from "./ApiClient";
import { 
    getAuth, 
    GoogleAuthProvider, 
    FacebookAuthProvider, 
    TwitterAuthProvider,
    signInWithPopup, 
    signInWithCustomToken,
    signOut
} from "firebase/auth";
import { userStore } from "../stores/user";
import type { User } from '../model/User';
import { userPerCountryStore } from "../stores/statByCountry";
import { groupListStore } from "../stores/groups";
import { currentGidStore } from "../stores/currentGroup";
import { activityListStore } from "../stores/activities";
import { currentAidStore } from "../stores/currentActivity";
import { groupInviteStore } from "../stores/groupInvite";
import { customTokenStore } from "../stores/authToken";
import { app } from "./FirebaseApp";
import { sendFcmToken } from "./UserService";
import { isLoadingStore } from "../stores/loading";

const auth = getAuth(app);

auth.useDeviceLanguage();

export async function verifyToken(token: string): Promise<boolean> {
    setTokenToInstance(token);

    try {
        const response = await instance.post<string>("/auth/token");
        const result: boolean = JSON.parse(response.data);

        console.log(result ? "Token valide" : "Token invalide");

        return result
    } catch (error) {
        console.error(error);

        return false;
    }
}

export async function authenticate(customToken: string): Promise<string|null> {
    if (customToken != null) {
        const credentials = await signInWithCustomToken(auth, customToken)
        const token = await credentials.user.getIdToken(false);

        console.log("Authentification réussie");

        customTokenStore.set(customToken);
        setTokenToInstance(token);
        await setCurrentUser();
        await sendFcmToken(null);

        return token;
    } else {
        console.error("Erreur lors de l'authentification'");

        return null;
    }
}

export async function getIdToken() {
    return auth?.currentUser?.getIdToken();
}

export async function login(email: string, password: string): Promise<boolean> {
    isLoadingStore.set(true);
    
    try {
        const response = await instance.post<string>("/auth/login", {
            mail: email,
            password: password
        });

        const customToken: string = response.data;

        const result = await authenticate(customToken);

        isLoadingStore.set(false);

        return result != null;

    } catch (error) {
        console.error(error);
        isLoadingStore.set(false);

        return false;
    }
}

export async function register(name: string, surname: string, email: string, password: string): Promise<boolean> {
    isLoadingStore.set(true);

    try {
        const response = await instance.post<string>("/auth/register", {
            username: `${surname} ${name}`,
            mail: email,
            password: password
        });

        const customToken: string = response.data;

        const result = await authenticate(customToken)

        isLoadingStore.set(false);

        return result != null;
    } catch (error) {
        console.error(error);
        isLoadingStore.set(false);
        
        return false;
    }
}

export const signInWithOtherProvider = async (provider: string) => {    
    try {
        const authProvider = (provider == "google" ? new GoogleAuthProvider() : 
        (provider == "facebook" ? new FacebookAuthProvider() : new TwitterAuthProvider()));
        authProvider.addScope("email");
        authProvider.setCustomParameters({
            'lang': auth.languageCode!
        });

        const result = await signInWithPopup(auth, authProvider);
        const token = await result.user.getIdToken(false);

        if(await verifyToken(token)) {
            setTokenToInstance(token);
            
            await setCurrentUser();
            return true;
        }
    } catch (error) {
      console.error(error);

      return false;
    }
};

async function setCurrentUser() {
    if(auth.currentUser != null) {
        const user: User = {
            uid: auth.currentUser.uid,
            email: auth.currentUser.email || '',
            displayName: auth.currentUser.displayName || ''
          }; 

          if (!user.email) {
            try {
               user.email = auth.currentUser?.providerData[0]?.email || ''
            } catch (error) {
                console.error("Erreur lors de la récupération de l'email de l'utilisateur :", error);
            }
        }
        userStore.set(user);
    }
}


function clearStores() {
    customTokenStore.set("");
    userStore.set(null);
    userPerCountryStore.set(null);
    groupListStore.set({});
    activityListStore.set({});
    currentGidStore.set("");
    currentAidStore.set("");
    groupInviteStore.set([]);
}


export function disconnect() {
    signOut(auth);
    clearStores();
}