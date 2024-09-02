import { instance } from "./ApiClient";
import { downloadICSFile } from "../utils/CalendarDownloader";
import { currentGidStore } from "../stores/currentGroup";
import { groupListStore } from "../stores/groups";
import type { GroupMap } from "../model/GroupMap";

let gid: string;
let groups: GroupMap|null;


// Subscribe to changes in the currentGroupId store
currentGidStore.subscribe(value => {
  gid = value;
});

groupListStore.subscribe(value => {
    groups = value;
});


export async function exportCalendar() {
    if(groups == null) {
        return
    }
    
    const response = await instance.get<string>(`/activity/calendar/${gid}`);

    const calendar: string = response.data;

    if (calendar != null) {
        downloadICSFile(calendar, `Calendrier-${groups[gid].groupName}`);
    } else {
        console.error("Erreur lors de la récupération du calendrier");
    }
}