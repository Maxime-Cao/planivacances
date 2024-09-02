<script lang="ts">
  import { onMount } from "svelte";
  import type { GroupInvite } from "../../model/GroupInvite";
  import Notification from "../molecule/Notification.svelte";
  import { groupInviteStore } from "../../stores/groupInvite";
  import { acceptGroupInvite, declineGroupInvite, loadUserGroupInvites } from "../../service/GroupService";

  let groupInvites: Array<GroupInvite> = [];

  onMount(() => {
    const unsubscribe = groupInviteStore.subscribe((value) => {
      groupInvites = value || [];
    });

    return unsubscribe;
  });

  loadUserGroupInvites();

  function handleOnAccept(event: CustomEvent) {
    acceptGroupInvite(event.detail.gid);
  }

  function handleOnDecline(event: CustomEvent) {
    declineGroupInvite(event.detail.gid);
  }

</script>

<section id="notificationsSection" class="container">
  {#each groupInvites as notification}
    <Notification title={notification.groupName} value={notification.gid} action="Rejoindre le groupe" on:accept={handleOnAccept} on:decline={handleOnDecline} />
  {/each}
</section>
