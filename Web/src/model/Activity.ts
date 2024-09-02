import type { Place } from "./Place"

export type Activity = {
    title: string;
    description: string;
    startDate: string|null;
    duration: number;
    place: Place|null;
}