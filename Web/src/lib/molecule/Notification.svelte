<script lang="ts">
  import { createEventDispatcher } from "svelte";

  export let title: string;
  export let value: String;
  export let action: string;

  const dispatch = createEventDispatcher();

  function handleOnAccept(e: any) {
    e.preventDefault();
    dispatch("accept", { gid : value });
  }

  function handlOnDecline(e: any) {
    e.preventDefault();
    dispatch("decline", { gid : value });
  }

</script>

<div class="row" id="notification">
  <div class="col" id="title">{title}</div>
  <div class="col">{action}</div>
  <div class="col" id="buttons">
    <i class="fa-solid fa-square-check fa-2xl" style="color: #5d9f28;" on:click={handleOnAccept} on:keypress={handleOnAccept} />
    <i class="fa-solid fa-square-xmark fa-2xl" style="color: #cc192b;" on:click={handlOnDecline} on:keypress={handlOnDecline} />
  </div>
</div>

<style>
  #notification {
    margin-bottom: 1rem;
    background-color: #f2f2f2;
    padding: 1rem;
  }

  #title {
    font-weight: bold;
    text-align: left;
  }

  #buttons {
    text-align: right;
  }

  #buttons * {
    cursor: pointer;
  }
</style>
