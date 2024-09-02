import type { Group } from "../model/Group";
import type { GroupInvite } from "../model/GroupInvite";
import type { GroupMap } from "../model/GroupMap";
import type { User } from "../model/User";
import { groupListStore } from "../stores/groups";
import { groupInviteStore } from "../stores/groupInvite";
import { userStore } from "../stores/user";
import { instance } from "./ApiClient";
import { sendFcmToken } from "./UserService";
import { isLoadingStore } from "../stores/loading";

let user: User|null;

userStore.subscribe(value => {
    user = value;
});

export async function createGroup(group: Group): Promise<string|null> {
    isLoadingStore.set(true);

    try {
        const response = await instance.post<string>(`/group`, group);

        if(response.status == 200) {

            await sendFcmToken(response.data);

            isLoadingStore.set(false);

            return response.data;
        }
        
    } catch (error) {
        console.error(error);
        isLoadingStore.set(false);
    }

    return null;
}

export async function updateGroup(gid: string, group: Group): Promise<boolean|null> {
    isLoadingStore.set(true);

    try {
        const response = await instance.put<string>(`/group/${gid}`, group);

        if(response.status == 200) {
            isLoadingStore.set(false);
            return response.data as unknown as boolean;
        }
        
    } catch (error) {
        console.error(error);
        isLoadingStore.set(false);
    }

    return false;
}

export async function deleteGroup(gid: string): Promise<boolean|null> {
    isLoadingStore.set(true);

    try {
        const response = await instance.delete<string>(`/group/${gid}`);

        if(response.status == 200) {
            isLoadingStore.set(false);
            return true;
        }
        
    } catch (error) {
        console.error(error);
    }

    isLoadingStore.set(false);
    return false;
}

export async function loadUserGroups() {
    isLoadingStore.set(true);
    
    const groups: GroupMap = {};

    try {
        const response = await instance.get<string>(`/group/list`);

        if(response.status == 200) {
            const groupList = response.data as unknown as Array<Group>;

            groupList.forEach(group => {
                groups[group.gid] = group;
            });

            groupListStore.set(groups);
            isLoadingStore.set(false);
            
            console.log("Groupes chargés");
            
        }
    } catch (error) {
        console.error(error);
        isLoadingStore.set(false);
    }

}

export async function loadUserGroupInvites() {
    isLoadingStore.set(true);

    try {
        const response = await instance.get<string>(`/group/invitation`);

        if(response.status == 200) {

            groupInviteStore.set(response.data as unknown as Array<GroupInvite>);
            isLoadingStore.set(false);
            
            console.log("Invitations aux groupes chargées");
        }
    } catch (error) {
        console.error(error);
        isLoadingStore.set(false);
    }

}

export async function sendGroupInvite(gid: string, mail: string) {
    isLoadingStore.set(true);

    if(user != null && mail == user.email) {
        console.log("Impossible de s'inviter soit même");
        alert("Impossible de s'inviter soit même");
        isLoadingStore.set(false);
        return;
    }

    try {
        const response = await instance.post<string>(`/group/invitation/${gid}/${mail}`);

        if(response.status == 200 && response.data) {     
            console.log("Invitation envoyée");
            isLoadingStore.set(false);
        }
    } catch (error) {
        console.error(error);
        alert("Erreur lors de l'envoi de l'invitation");
        isLoadingStore.set(false);
    }

}

export async function acceptGroupInvite(gid: string) {
    try {
        const response = await instance.post<boolean>(`/group/invitation/${gid}`);

        if(response.status == 200 && response.data) {
            loadUserGroupInvites()

            await sendFcmToken(gid);

            console.log("Invitation acceptée");
        }
    } catch (error) {
        console.error(error);
    }
}

export async function declineGroupInvite(gid: string) {
    try {
        const response = await instance.delete<string>(`/group/invitation/${gid}`);

        if(response.status == 200 && response.data) {  
            loadUserGroupInvites()

            console.log("Invitation supprimée");
        }
    } catch (error) {
        console.error(error);
    }
}