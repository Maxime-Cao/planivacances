<script lang="ts">
  import { onDestroy, onMount } from "svelte";
  import { Link } from "svelte-navigator";
  import { userStore } from "../../stores/User";
  let counterMembers: number = 0;
  let eventSource: EventSource;

  onMount(() => {
    eventSource = new EventSource(
      "https://studapps.cg.helmo.be:5011/REST_CAO_BART/api/users/number/flux"
    );

    eventSource.onmessage = (event) => {
      counterMembers = event.data;
    };

    eventSource.onerror = () => {
      eventSource.close();
    };
  });

  onDestroy(() => {
    if (eventSource) {
      eventSource.close();
    }
  });
</script>

<section id="numberRegistredMembers" class="bg-primary">
  <p>
    Nous comptons déjà <span id="counterMembers">{counterMembers}</span>
    {counterMembers > 1 ? "membres" : "membre"}
    !
  </p>
  {#if !$userStore}
    <Link class="text-white text-decoration-underline" to="/register"
      >Cliquez ici pour nous rejoindre</Link
    >
  {:else}
    <p>Et tout cela, c’est grâce à vous !</p>
  {/if}
</section>

<style>
  #numberRegistredMembers {
    display: flex;
    flex-direction: column;
    justify-content: center;
    margin: auto;
    border-radius: 5px;
    font-size: 1.2rem;
    font-weight: 600;
    width: 33rem;
    height: 11.0625rem;
    flex-shrink: 0;
    color: white;
  }

  #counterMembers {
    font-size: 1.5rem;
  }
</style>
