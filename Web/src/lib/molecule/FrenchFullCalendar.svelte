<script lang="ts">
  import { createEventDispatcher, onMount } from "svelte";
  import { Calendar } from "@fullcalendar/core";
  import dayGridPlugin from "@fullcalendar/daygrid";

  let calendar: Calendar;
  let dispatch = createEventDispatcher();
  
  export function addEventToCalendar(
    aid: string,
    title: string,
    startDate: string,
    endDate: string,
    description: string
  ) {
    if (calendar) {
      calendar.addEvent({
        title: title,
        start: startDate,
        end: endDate,
        description: description,
        extendedProps: {
          aid: aid
        }
      });
    }
  }

  export function removeAllEvents() {
    calendar.removeAllEvents();
  }

  onMount(() => {
    calendar = new Calendar(document.getElementById("calendar")!, {
      plugins: [dayGridPlugin],
      initialView: "dayGridMonth",
      locale: "frLocale",
      buttonText: {
        today: "Aujourd'hui",
      },
      eventBorderColor: "black",
      eventClick: function (info) {
        const activity = info.event;

        dispatch("navToDetails", {
          aid: activity.extendedProps.aid
        });
      },
    });

    calendar.render();
  });
</script>

<div id="calendar" />
