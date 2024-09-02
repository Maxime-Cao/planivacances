import { writable, type Writable } from 'svelte/store';
import type { User } from '../model/User';
import { createEncryptionStorage, createSessionStorage, GCMEncryption, persist } from '@macfja/svelte-persistent-store';
import { STORE_ENCRYPTION_KEY } from '../utils/config';

const storage = createEncryptionStorage(createSessionStorage(), new GCMEncryption(STORE_ENCRYPTION_KEY))
export const userStore: Writable<User | null> = persist(writable(null), storage, "user") as Writable<User | null>