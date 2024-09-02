import { type Writable } from "svelte/store";
import { GCMEncryption, createEncryptionStorage, createSessionStorage, persist, writable } from "@macfja/svelte-persistent-store"
import { STORE_ENCRYPTION_KEY } from "../utils/config";

const storage = createEncryptionStorage(createSessionStorage(), new GCMEncryption(STORE_ENCRYPTION_KEY))
export const currentAidStore: Writable<string> = persist(writable(""), storage, "currentAid") as Writable<string>;