<script lang="ts">
  import { Button } from "sveltestrap";
  import { useNavigate } from "svelte-navigator";
  import type { ActivityMap } from "../../../model/ActivityMap";
  import { activityListStore } from "../../../stores/activities";
  import type { Activity } from "../../../model/Activity";
  import { currentAidStore } from "../../../stores/currentActivity";
  import { format } from "date-fns";
  import { deleteActivity } from "../../../service/ActivityService";

  const navigate = useNavigate();

  let activities: ActivityMap = $activityListStore || {};

  let activity: Activity;

  let address: string;
  let formattedStartDate: string;
  let formattedEndDate: string;

  currentAidStore.subscribe(value => {
    activity = activities[value];

    if(activity != null && activity.place != null) {
      address = `${activity.place.street}, ${activity.place.number} ${activity.place.city} ${activity.place.country}`;
      formattedStartDate = format(new Date(activity.startDate ?? ""), "dd/MM/yyyy");
      formattedEndDate = format(new Date(new Date(activity.startDate ?? "").getTime() + activity.duration * 1000), "dd/MM/yyyy");
    }
  });

  function onUpdateActivity() {
    navigate("/updateActivity");
  }

  function onDeleteActivity() {
    if(deleteActivity() != null) {
      navigate("/planning");
    }
  }

</script>

<section id="activityDetails">
  {#if activity != null}
    <h1>{activity.title}</h1>
    <h2>Du {formattedStartDate} au {formattedEndDate}</h2>
    <h3>{address}</h3>
    <p>{activity.description}</p>
    <div>
      <Button color="primary" on:click={onUpdateActivity}>Modifier</Button>
      <Button color="danger" on:click={onDeleteActivity}>Supprimer</Button>
    </div>
  {:else}
    <h1>Activité non trouvée</h1>
  {/if}
</section>

<style>
  #activityDetails {
    width: 60%;
    margin: auto;
  }
</style>
