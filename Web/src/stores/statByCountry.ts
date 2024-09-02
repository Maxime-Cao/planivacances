import { writable, type Writable } from "svelte/store";
import type { CountryUserMap } from "../model/CountryUserMap";

export const userPerCountryStore: Writable<CountryUserMap | null> = writable(null)