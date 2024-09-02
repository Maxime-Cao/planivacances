<script lang="ts">
  import { Form, FormGroup, Input, Button } from "sveltestrap";
  import LocationPicker from "../../molecule/LocationPicker.svelte";
  import type { Activity } from "../../../model/Activity";
  import { differenceInSeconds, format } from "date-fns";
  import { createActivity } from "../../../service/ActivityService";
  import { useNavigate } from "svelte-navigator";
  
  const navigate = useNavigate();

  let activity: Activity = {
    title: "",
    description: "",
    startDate: null,
    duration: 0,
    place: null
  }

  let endDate: string;

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
      createActivity(activity).then((aid: string | null) => {
        if(aid != null) {
          navigate("/planning");
        } else {
          console.error("Erreur lors de la création de l'activité");
        }
      });
    }

  }
  
</script>

<h1>Ajout d'une activité</h1>
<section id="addActivityForm">
  <Form>
    <FormGroup floating label="Titre de l'activité">
      <Input 
      id="activityTitle" 
      bind:value={activity.title}
      name="activityTitle" />
    </FormGroup>
    <FormGroup floating label="Date de début">
      <Input
        id="startActivityDate"
        bind:value={activity.startDate}
        type="datetime-local"
        name="startActivityDate"
        style="margin-right:0.2rem;"
      />
    </FormGroup>
    <FormGroup floating label="Date de fin">
      <Input
        id="endActivityDate"
        bind:value={endDate}
        type="datetime-local"
        name="endActivityDate"
        style="margin-right:0.2rem;"
      />
    </FormGroup>
    <LocationPicker on:place={handleLocationPicker} />
    <FormGroup floating label="Ecrivez une description ici...">
      <Input
        id="activityDescription"
        bind:value={activity.description}
        type="textarea"
        name="activityDescription"
      />
    </FormGroup>
    <Button color="primary" class="w-75 mb-3 mt-3" on:click={onSubmit}>Ajouter</Button>
  </Form>
</section>

<style>
  #addActivityForm {
    margin: 2rem auto;
    width: 35rem;
  }
</style>
