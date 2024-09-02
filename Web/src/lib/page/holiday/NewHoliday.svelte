<script lang="ts">
  import { Form, FormGroup, Input, Button } from "sveltestrap";
  import type { Group } from "../../../model/Group";
  import { createGroup } from "../../../service/GroupService";
  import LocationPicker from "./../../molecule/LocationPicker.svelte";
  import { format } from "date-fns";
  import { useNavigate } from "svelte-navigator";
  import { currentGidStore } from "../../../stores/currentGroup";

  const navigate = useNavigate();

  let group: Group = {
    gid: "",
    groupName: "",
    description: "",
    startDate: null,
    endDate: null,
    place: null,
    owner: "",
  };

  function handleLocationPicker(event: CustomEvent) {
    group.place = event.detail.selectedPlace;
  }

  function onSubmit(e: any) {
    e.preventDefault();

    if(group.startDate != null && group.endDate != null) {
      group.startDate = format(
        new Date(group.startDate),
        "yyyy-MM-dd'T'HH:mm:ss.SSS"
      );
      group.endDate = format(
        new Date(group.endDate),
        "yyyy-MM-dd'T'HH:mm:ss.SSS"
      );

      createGroup(group).then((gid: string | null) => {
        if (gid != null) {
          currentGidStore.set(gid);
          
          navigate("/holidays");
        } else {
          console.error("Erreur lors de la création du groupe");
        }
      });
    }
  }
</script>

<h1>Ajout d'une période de vacances</h1>
<section id="addHolidayForm">
  <Form>
    <!--on:submit={(e) => e.preventDefault()}>-->
    <FormGroup floating label="Titre des vacances">
      <Input
        id="holidayTitle"
        bind:value={group.groupName}
        name="holidayTitle"
      />
    </FormGroup>
    <FormGroup floating label="Date de début">
      <Input
        id="startHolidayDate"
        bind:value={group.startDate}
        type="date"
        name="startHolidayDate"
        style="margin-right:0.2rem;"
      />
    </FormGroup>
    <FormGroup floating label="Date de fin">
      <Input
        id="endHolidayDate"
        bind:value={group.endDate}
        type="date"
        name="endHolidayDate"
        style="margin-right:0.2rem;"
      />
    </FormGroup>
    <LocationPicker on:place={handleLocationPicker} />
    <FormGroup floating label="Ecrivez une description ici...">
      <Input
        id="holidayDescription"
        bind:value={group.description}
        type="textarea"
        name="holidayDescription"
      />
    </FormGroup>
    <Button color="primary" class="w-75 mb-3 mt-3" on:click={onSubmit}
      >Ajouter</Button
    >
  </Form>
</section>

<style>
  #addHolidayForm {
    margin: 2rem auto;
    width: 35rem;
  }
</style>
