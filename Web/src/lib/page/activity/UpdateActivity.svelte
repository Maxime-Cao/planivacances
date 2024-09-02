<script lang="ts">
  import { Form, FormGroup, Input, Button } from "sveltestrap";
  import type { Activity } from "../../../model/Activity";
  import LocationPicker from "../../molecule/LocationPicker.svelte";
  import { differenceInSeconds, format } from "date-fns";
  import { updateActivity } from "../../../service/ActivityService";
  import { useNavigate } from "svelte-navigator";
  import { activityListStore } from "../../../stores/activities";
  import type { ActivityMap } from "../../../model/ActivityMap";
  import { currentAidStore } from "../../../stores/currentActivity";

  const navigate = useNavigate();

  let activities: ActivityMap = $activityListStore || {};

  let activity: Activity;
  let endDate: string;

  currentAidStore.subscribe(value => {
    activity = activities[value];
    formatEndDate();
  });

  function formatEndDate() {
    if(activity.startDate != null) {
      endDate = format(
        new Date(new Date(activity.startDate).getTime() + activity.duration * 1000), 
        "yyyy-MM-dd'T'HH:mm:ss.SSS");
    }
  }

  function handleLocationPicker(event: CustomEvent) {
    activity.place = event.detail.selectedPlace;
  }

  function onSubmit(e: any) {
    e.preventDefault();

    if(activity.startDate != null && endDate != null) {

      activity.startDate = format(
        new Date(activity.startDate), 
        "yyyy-MM-dd'T'HH:mm:ss.SSS");

      activity.duration = Math.abs(differenceInSeconds(new Date(endDate), new Date(activity.startDate)));
      //console.log(activity.duration); TODO check
      updateActivity(activity).then((isDone: boolean | null) => {
        if(isDone) {
          navigate("/planning");
        } else {
          console.error("Erreur lors de la modification de l'activité");
        }
      });
    }

  }
</script>

<h1>Modification d'une activité</h1>
<section id="updateActivityForm">
  {#if activity != null}
    <Form>
      <FormGroup floating label="Titre de l'activité">
        <Input id="activityTitle" name="activityTitle" bind:value={activity.title} />
      </FormGroup>
      <FormGroup floating label="Date de début">
        <Input
          id="startActivityDate"
          type="datetime-local"
          name="startActivityDate"
          style="margin-right:0.2rem;"
          bind:value={activity.startDate}
        />
      </FormGroup>
      <FormGroup floating label="Date de fin">
        <Input
          id="endActivityDate"
          type="datetime-local"
          name="endActivityDate"
          style="margin-right:0.2rem;"
          bind:value={endDate}
        />
      </FormGroup>
      <LocationPicker place={activity.place} on:place={handleLocationPicker} />
      <FormGroup floating label="Ecrivez une description ici...">
        <Input
          id="activityDescription"
          type="textarea"
          name="activityDescription"
          bind:value={activity.description}
        />
      </FormGroup>
      <Button color="primary" class="w-75 mb-3 mt-3" on:click={onSubmit}>Modifier</Button>
    </Form>
  {:else}
    <h1>Modification de l'activité impossible</h1>
  {/if}
</section>

<style>
  #updateActivityForm {
    margin: 2rem auto;
    width: 35rem;
  }
</style>
