<script lang="ts">
  import { onDestroy, onMount } from "svelte";
  import Pusher, { Channel } from "pusher-js";
  import { PUSHER } from "../../utils/config";
  import InputMessage from "../organism/InputMessage.svelte";
  import MessageItem from "../molecule/MessageItem.svelte";
  import { formatTimestampForDisplay } from "../../utils/DateFormatter";
  import { userStore } from "../../stores/user";
  import { getIdToken } from "../../service/AuthService";
  import { groupListStore } from "../../stores/groups";
  import { currentGidStore as currentGidStore } from "../../stores/currentGroup";
  import type { GroupMap } from "../../model/GroupMap";
  import { instance } from "../../service/ApiClient";

  let definedHoliday = false;
  let messages: any = [];
  let token: string | undefined;
  let uid: string;
  let title: string;
  let groupId: string;
  let displayName: string;
  let tchatWS: Pusher | null;
  let channel: Channel | null;
  let previousMessagesLoaded: boolean = false;

  let isArrowVisible = true;

  let groups: GroupMap = $groupListStore || {};
  let group = groups[$currentGidStore];

  function sendMessage(event: CustomEvent) {
    if (token && groupId) {
      instance.post<string>("/chat/message", {
        sender: uid,
        displayName: displayName,
        groupId: groupId,
        content: event.detail.message,
        time: Date.now(),
      });
    }
  }

  function addMessage(message: any) {
    if (messages.length == 100) {
      messages.shift();
    }
    messages = [...messages, message];

    scrollDown();
  }

  function scrollDown() {
    document.querySelector("footer")?.scrollIntoView();
  }

  const loadPreviousMessages = async (gid: string) => {
    const response = await instance.post<Array<any>>(
      "/chat/messages",
      {},
      {
        headers: {
          GID: gid,
        },
      }
    );

    if (response.status == 200) {
      const messages = response.data;
      messages.forEach((message: any) => {
        addMessage(message);
      });
      previousMessagesLoaded = true;
    }
  };

  if ($userStore) {
    uid = $userStore.uid;
    displayName = $userStore.displayName;

    if (group) {
      definedHoliday = true;
      groupId = group.gid;
      title = group.groupName;
    }

    onMount(async () => {
      const handleScroll = () => {
        const viewPort = document.getElementById("tchat");
        const scrollY = window.scrollY || window.pageYOffset;

        // Set the threshold as needed (e.g., 20 pixels from the bottom)
        const threshold = 350;

        // Calculate the distance from the bottom
        const distanceFromBottom =
          viewPort!.offsetHeight - (scrollY + window.innerHeight / 2);

        // Update visibility based on the distance from the bottom
        isArrowVisible = distanceFromBottom >= threshold;
      };

      window.addEventListener("scroll", handleScroll);

      token = await getIdToken();
      if (token != null) {
        tchatWS = new Pusher(PUSHER.key, {
          cluster: PUSHER.cluster,
          authEndpoint:
            "https://studapps.cg.helmo.be:5011/REST_CAO_BART/api/chat/",
          auth: {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          },
        });

        tchatWS.connect();

        channel = tchatWS.subscribe(`private-${groupId}`);

        channel.bind("pusher:subscription_succeeded", () => {
          loadPreviousMessages(groupId);
        });

        channel.bind("new_messages", (message: any) => {
          if (previousMessagesLoaded) {
            addMessage(message);
          }
        });
      }
    });

    onDestroy(() => {
      console.log("onDestroy called");
      channel?.unbind("pusher:subscription_succeeded");
      channel?.unbind("new_messages");
      tchatWS?.unsubscribe(`private-${groupId}`);
      tchatWS?.disconnect();
    });
  }
</script>

{#if definedHoliday}
  <h1>Tchat - {title}</h1>
  <section id="tchat">
    {#each messages as message (message)}
      <MessageItem
        sender={message.displayName}
        content={message.content}
        time={formatTimestampForDisplay(message.time)}
        isCurrentUser={message.sender === uid}
      />
    {/each}
  </section>
  <InputMessage on:send={sendMessage} />
  <div style="height:2rem" />
  {#if isArrowVisible}
    <button
      id="scroll-btn"
      class="scroll-button"
      class:hidden={!isArrowVisible}
      on:click={scrollDown}
    >
      <i class="fa-solid fa-arrow-down"></i>
    </button>
  {/if}
{:else}
  <h1>Impossible de charger le tchat pour ce voyage</h1>
{/if}

<style>
  #tchat {
    min-height: 50vh;
  }

  .scroll-button {
    position: fixed;
    bottom: 4rem;
    right: 2rem;
    transition: opacity 0.5s ease;
  }

  .hidden {
    opacity: 0;
  }
</style>
