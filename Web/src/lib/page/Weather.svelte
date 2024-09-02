<script lang="ts">
  import ForecastsSection from "../organism/ForecastsSection.svelte";
  import type { GroupMap } from "../../model/GroupMap";
  import { groupListStore } from "../../stores/groups";
  import { currentGidStore } from "../../stores/currentGroup";


  let groups: GroupMap = $groupListStore || {};
  
  let group = groups[$currentGidStore];

</script>

{#if group.place != null}
  <h1>Météo - {group.place.street}, {group.place.number} {group.place.city} {group.place.country}</h1>
  <ForecastsSection place={group.place}/>
{:else}
  <h1>Pas de météo à afficher pour ce voyage</h1>
{/if}
