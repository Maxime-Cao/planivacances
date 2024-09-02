<script lang="ts">
  import { EmojiButton } from "@joeattardi/emoji-button";
  import { createEventDispatcher, onMount } from "svelte";

  const dispatch = createEventDispatcher();

  const picker = new EmojiButton();
  let trigger: HTMLElement | null = null;

  onMount(() => {
    trigger = document.getElementById("emoji-button");
  });

  picker.on("emoji", (selection) => {
    dispatch("change", selection);
  });

  function togglePicker() {
    console.log(trigger);
    picker.togglePicker(trigger!);
  }
</script>

<button id="emoji-button" type="button" on:click={togglePicker}>
  <i class="fa-regular fa-face-smile" />
</button>

<style>
  #emoji-button {
    padding: 0.3rem 0.2rem;
  }
</style>
