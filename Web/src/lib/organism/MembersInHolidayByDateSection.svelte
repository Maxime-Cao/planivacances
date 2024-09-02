<script lang="ts">
  import { Button, Input } from "sveltestrap";
  import { getUsersPerCountry } from "../../service/UserService";
  import type { CountryUserMap } from "../../model/CountryUserMap";
  import { userPerCountryStore } from "../../stores/statByCountry";
  import { onMount } from "svelte";

  let date: string;

  let userPerCountryMap: CountryUserMap = {};

  onMount(() => {
    const unsubscribe = userPerCountryStore.subscribe(value => {
      userPerCountryMap = value ?? {};
    });

    return unsubscribe;
  });

  function query() {
    getUsersPerCountry(date);
  }

</script>

<section id="numberMembersInHolidayByDate">
  <p>Découvrez le nombre de personnes en vacances le jour de votre choix :</p>
  <div id="searchHolidayByDateBar">
    <Input type="date" name="dateToSearch" bind:value={date} style="margin-right:0.2rem;" />
    <Button color="primary" outline on:click={query}>Valider</Button>
  </div>
  <div id="listMembersInCountries">
    {#if Object.keys(userPerCountryMap).length > 0}
      {#each Object.entries(userPerCountryMap) as [country, userCount]}
        <p>{country}: {userCount} {userCount > 1 ? 'membres' : 'membre'}</p>
      {/each}
    {:else}
      <p>Pas de données disponibles</p>
    {/if}
  </div>
</section>

<style>
  #numberMembersInHolidayByDate {
    display: flex;
    flex-direction: column;
    justify-content: center;
    margin: auto;
    padding: 12px;
    border-radius: 5px;
    font-size: 1.2rem;
    font-weight: 600;
    width: 33rem;
    flex-shrink: 0;
    color: black;
    background-color: lightgrey;
  }

  #numberMembersInHolidayByDate {
    margin: 2rem auto;
    width: 33rem;
  }

  #searchHolidayByDateBar {
    display: flex;
    flex-direction: row;
  }
</style>
