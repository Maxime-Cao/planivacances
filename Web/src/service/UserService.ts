import type { CountryUserMap } from "../model/CountryUserMap";
import { registrationTokenStore } from "../stores/fcmToken";
import { isLoadingStore } from "../stores/loading";
import { userPerCountryStore } from "../stores/statByCountry";
import { instance } from "./ApiClient";

let registrationToken: string;

registrationTokenStore.subscribe(value => {
    registrationToken = value;
});

export async function onAddUser() {
    try {
        await instance.get<string>(`/users/number`);
    } catch (error) {
        console.error(error);
    }
}

export async function getUsersPerCountry(givenDate: string) {
    isLoadingStore.set(true);
    
    try {
        const response = await instance.get<string>(`/users/country/${givenDate}`);

        if(response.status == 200) {
            const statList = response.data as unknown as CountryUserMap;

            userPerCountryStore.set(statList);
            isLoadingStore.set(false);
            
            console.log("Statistiques chargés");
            
        }
    } catch (error) {
        console.error(error);
        isLoadingStore.set(false);
    }
}

export async function sendFcmToken(gid:string|null) {
    try {
        const response = await instance.post<boolean>(`/users/${registrationToken}/${gid}`);

        if(response.status == 200) {
            const result = response.data as unknown as boolean;
            
            console.log("FCM: Registration Token envoyé");
            
            return result;
        }
    } catch (error) {
        console.error(error);
        return false;
    }
}