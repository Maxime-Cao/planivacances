import type { Place } from "./Place";

export type Group = {
    gid: string;
    groupName: string;
    description: string;
    startDate: string|null;
    endDate: string|null;
    place: Place|null;
    owner: string;
};