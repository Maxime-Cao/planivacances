<script lang="ts">
  import { useNavigate } from "svelte-navigator";
  import { Form, FormGroup, Input, Button } from "sveltestrap";
  import type { GroupMap } from "../../../model/GroupMap";
  import { groupListStore } from "../../../stores/groups";
  import type { Group } from "../../../model/Group";
  import { currentGidStore } from "../../../stores/currentGroup";
  import { format } from "date-fns";
  import LocationPicker from "../../molecule/LocationPicker.svelte";
  import { updateGroup } from "../../../service/GroupService";

  const navigate = useNavigate();

  let groups: GroupMap = $groupListStore || {};
  let currentGid: string;
  let group: Group;

  currentGidStore.subscribe(value => {
    currentGid = value;
    group = groups[value];

    const formattedStartDate = new Date(group!.startDate!).toISOString().split("T")[0];
    group.startDate = formattedStartDate;

    const formattedEndDate = new Date(group!.endDate!).toISOString().split("T")[0];
    group.endDate = formattedEndDate;
  });

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

      updateGroup(currentGid, group).then((isDone: boolean | null) => {
        if (isDone) {
          navigate("/holidays");
        } else {
          console.error("Erreur lors de la modification du groupe");
        }
      });
    }
  }
  
</script>

<h1>Modification d'une période de vacances</h1>
<section id="updateHolidayForm">
  {#if group != null}
    <Form>
      <FormGroup floating label="Titre des vacances">
        <Input id="holidayTitle" name="holidayTitle" bind:value={group.groupName} />
      </FormGroup>
      <FormGroup floating label="Date de début">
        <Input
          id="startHolidayDate"
          type="date"
          name="startHolidayDate"
          style="margin-right:0.2rem;"
          bind:value={group.startDate}
        />
      </FormGroup>
      <FormGroup floating label="Date de fin">
        <Input
          id="endHolidayDate"
          type="date"
          name="endHolidayDate"
          style="margin-right:0.2rem;"
          bind:value={group.endDate}
        />
      </FormGroup>
      <LocationPicker place={group.place} on:place={handleLocationPicker} />
      <FormGroup floating label="Ecrivez une description ici...">
        <Input
          id="holidayDescription"
          type="textarea"
          name="holidayDescription"
          bind:value={group.description}
        />
      </FormGroup>
      <Button color="primary" class="w-75 mb-3 mt-3" on:click={onSubmit}>Modifier</Button>
    </Form>
  {:else}
    <h1>Modification de la période de vacances impossible</h1>
  {/if}
</section>

<style>
  #updateHolidayForm {
    margin: 2rem auto;
    width: 35rem;
  }
</style>
