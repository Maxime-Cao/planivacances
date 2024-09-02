import { writable, type Writable } from "svelte/store";
import type { GroupInvite } from "../model/GroupInvite";
import { createEncryptionStorage, createSessionStorage, GCMEncryption, persist } from "@macfja/svelte-persistent-store";
import { STORE_ENCRYPTION_KEY } from "../utils/config";

const storage = createEncryptionStorage(createSessionStorage(), new GCMEncryption(STORE_ENCRYPTION_KEY))
export const groupInviteStore: Writable<Array<GroupInvite> | null> = persist(writable(null), storage, "groupInvites") as Writable<Array<GroupInvite> | null>;