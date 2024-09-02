<script lang="ts">
  export let sender: string;
  export let content: string;
  export let time: string;
  export let isCurrentUser = false;
</script>

<div class={isCurrentUser ? "message-item-right" : "message-item-left"}>
  {#if !isCurrentUser}
    <div id="sender">{sender}</div>
  {/if}
  <div
    class="message-bubble {isCurrentUser
      ? 'bg-primary text-white'
      : 'bg-light'}"
  >
    <p class="mb-0">{content}</p>
    <p id="time" class="mb-0">{time}</p>
  </div>
</div>

<style>
  .message-bubble {
    max-width: 30%;
    width: 30%;
    padding: 10px;
    border-radius: 10px;
    margin: 5px;
  }

  .message-item-left,
  .message-item-right {
    display: flex;
    flex-direction: column;
    margin-bottom: 1rem;
  }

  .message-item-left {
    text-align: left;
    align-items: start;
  }

  .message-item-right {
    text-align: right;
    align-items: end;
  }

  #sender {
    margin-left: 1rem;
  }

  #time,
  #sender {
    opacity: 0.5;
  }
</style>
