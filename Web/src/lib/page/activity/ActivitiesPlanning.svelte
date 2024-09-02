<script lang="ts">
  import { Button } from "sveltestrap";
  import { useNavigate } from "svelte-navigator";
  import FrenchFullCalendar from "../../molecule/FrenchFullCalendar.svelte";
  import { exportCalendar } from "../../../service/CalendarService";
  import { loadActivities } from "../../../service/ActivityService";
  import { activityListStore } from "../../../stores/activities";
  import { onMount } from "svelte";
  import type { ActivityMap } from "../../../model/ActivityMap";
  import { currentAidStore } from "../../../stores/currentActivity";

  let navigate = useNavigate();

  let activities: ActivityMap = {};

  let calendarComponent: FrenchFullCalendar;

  const addEventsToCalendar = () => {
    const keys = Object.keys(activities ?? {}) || [];

    if(calendarComponent == null) {
      return;
    }

    calendarComponent.removeAllEvents();

    keys.forEach((key) => {
      const activity = activities[key];

      if(activity!= null && activity.place != null) {
        //const address = `${activity.place.street}, ${activity.place.number} ${activity.place.city} ${activity.place.country}`;
        const endDate = new Date(new Date(activity.startDate ?? "").getTime() + activity.duration * 1000);
        
        calendarComponent.addEventToCalendar(
          key,
          activity.title,
          activity.startDate ?? "",
          endDate.toDateString(),
          //address,
          activity.description
        );
      }

    });

  };

  function onNavToActivityDetails(event: CustomEvent) {
    const activity = event.detail;

    currentAidStore.set(activity.aid);

    navigate("/activityDetails");
  }

  function goToAddActivity() {
    navigate("/newActivity");
  }

  onMount(() => {
    activityListStore.subscribe((value) => {
      activities = value;
      addEventsToCalendar();
    });

    loadActivities();
  });

</script>

<h1>Planning des activités</h1>

<section id="activitiesPlanning">
  <div>
    <Button color="primary" outline on:click={goToAddActivity}
      >Ajouter une activité</Button
    >
    <Button color="primary" outline on:click={exportCalendar}>Exporter le calendrier</Button>
  </div>
  <FrenchFullCalendar
    bind:this={calendarComponent}
    on:navToDetails={onNavToActivityDetails}
  />
</section>

<style></style>
