<script lang="ts">
  import { formatDateForDisplay } from "../../utils/DateFormatter";
  import { createEventDispatcher } from "svelte";
  import {
    Card,
    CardBody,
    CardTitle,
    CardSubtitle,
    CardText,
  } from "sveltestrap";

  export let id: string;
  export let title: string;
  export let startDate: string;
  export let endDate: string;
  export let description: string;

  let displayedStartDate: string = formatDateForDisplay(startDate);
  let displayedEndDate: string = formatDateForDisplay(endDate);

  const dispatch = createEventDispatcher();

  function onNavToDetails() {
    dispatch("navToDetails", { id: id });
  }
</script>

<Card
  style="width: 22rem; height:16rem;margin-bottom: 20px; margin-right:1rem;"
>
  <CardBody>
    <CardTitle
      >{title.length > 20 ? `${title.substring(0, 20)}...` : title}</CardTitle
    >
    <CardSubtitle class="mb-2 text-muted"
      >Du {displayedStartDate} au {displayedEndDate}</CardSubtitle
    >
    <CardText>
      {description.length > 70
        ? `${description.substring(0, 70)}...`
        : description}
    </CardText>
    <button on:click={onNavToDetails}>Consulter</button>
  </CardBody>
</Card>
