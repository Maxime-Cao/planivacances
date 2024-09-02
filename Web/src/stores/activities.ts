import { writable, type Writable } from "svelte/store";
import type { ActivityMap } from "../model/ActivityMap";
import { createEncryptionStorage, createSessionStorage, GCMEncryption, persist } from "@macfja/svelte-persistent-store";
import { STORE_ENCRYPTION_KEY } from "../utils/config";

const storage = createEncryptionStorage(createSessionStorage(), new GCMEncryption(STORE_ENCRYPTION_KEY))
export const activityListStore: Writable<ActivityMap> = persist(writable({}), storage, "activities") as Writable<ActivityMap>;