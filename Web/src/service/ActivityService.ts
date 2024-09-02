import type { Activity } from "../model/Activity";
import type { ActivityMap } from "../model/ActivityMap";
import { activityListStore } from "../stores/activities";
import { currentAidStore } from "../stores/currentActivity";
import { currentGidStore } from "../stores/currentGroup";
import { isLoadingStore } from "../stores/loading";
import { instance } from "./ApiClient";

let gid: string;
let aid: string;


currentGidStore.subscribe(value => {
    gid = value;
});

currentAidStore.subscribe(value => {
    aid = value;
});

export async function loadActivities() {
    isLoadingStore.set(true);

    try {
        const response = await instance.get<string>(`/activity/${gid}`);

        if(response.status == 200) {
            activityListStore.set(response.data as unknown as ActivityMap);
            console.log("Activitées récupérées")
        }

        isLoadingStore.set(false);
        
    } catch (error) {
        console.error(error);
        isLoadingStore.set(false);
    }
}

export async function createActivity(activity: Activity): Promise<string|null> {
    isLoadingStore.set(true);

    try {
        const response = await instance.post<string>(`/activity/${gid}`, activity);

        if(response.status == 200) {
            return response.data;
        }

        isLoadingStore.set(false);
        
    } catch (error) {
        console.error(error);
        isLoadingStore.set(false);
    }

    return null;
}

export async function updateActivity(activity: Activity): Promise<boolean|null> {
    isLoadingStore.set(true);
    
    try {
        const response = await instance.put<string>(`/activity/${gid}/${aid}`, activity);

        if(response.status == 200) {
            return response.data as unknown as boolean;
        }

        isLoadingStore.set(false);
        
    } catch (error) {
        console.error(error);
        isLoadingStore.set(false);
    }

    return false;
}

export async function deleteActivity(): Promise<string|null> {
    try {
        const response = await instance.delete<string>(`/activity/${gid}/${aid}`);

        if(response.status == 200) {
            return response.data;
        }
        
    } catch (error) {
        console.error(error);
    }

    return null;
}