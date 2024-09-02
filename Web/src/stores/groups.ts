import { writable, type Writable } from 'svelte/store';
import type { GroupMap } from '../model/GroupMap';
import { createEncryptionStorage, createSessionStorage, GCMEncryption, persist } from '@macfja/svelte-persistent-store';
import { STORE_ENCRYPTION_KEY } from '../utils/config';

const storage = createEncryptionStorage(createSessionStorage(), new GCMEncryption(STORE_ENCRYPTION_KEY))
export const groupListStore: Writable<GroupMap> = persist(writable({}), storage, "groups") as Writable<GroupMap>
