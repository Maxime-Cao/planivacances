<script lang="ts">
  import { FormGroup, Form, Input } from "sveltestrap";
  import "@fortawesome/fontawesome-free/css/all.min.css";
  import EmojiPicker from "../molecule/EmojiPicker.svelte";
  import { createEventDispatcher } from "svelte";

  const dispatch = createEventDispatcher();

  let messageText: string;

  function handleKeyPress(event: KeyboardEvent) {
    if (event.key === "Enter") {
      onSubmit(event);
    }
  }

  function onSubmit(e: any) {
    e.preventDefault();

    messageText.trim();

    if (messageText) {
      dispatch("send", { message: messageText });
      messageText = "";
    }
  }

  function appendEmoji(event: CustomEvent) {
    const inputMessage = document.getElementById("inputMessage");
    const cursorPosition = inputMessage!.selectionStart;

    messageText =
      messageText.substring(0, cursorPosition) +
      event.detail.emoji +
      messageText.substring(cursorPosition);
  }
</script>

<Form>
  <FormGroup>
    <div id="message-container">
      <EmojiPicker on:change={appendEmoji} />

      <Input
        id="inputMessage"
        bind:value={messageText}
        type="text"
        name="message"
        placeholder="Message"
        required
        on:keydown={handleKeyPress}
      />
      <i
        class="fa-regular fa-paper-plane"
        on:click={onSubmit}
        on:keypress={onSubmit}
      />
    </div>
  </FormGroup>
</Form>

<style>
  #message-container {
    display: flex;
    align-items: center;
  }

  #message-container > i {
    margin-left: -30px;
  }
</style>
