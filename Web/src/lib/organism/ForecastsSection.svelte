<script lang="ts">
  import { onMount } from "svelte";
  import Forecast from "../molecule/Forecast.svelte";
  import { getWeatherData } from "../../service/WeatherService";
  import type { Place } from "../../model/Place";

  let forecasts: any = [];
  let errorMessage: string = "";

  export let place: Place;

  const getWeather = async () => {
    forecasts = await getWeatherData(place.lat, place.lon);

    if(forecasts == null) {
      errorMessage = "Erreur lors du chargement de la météo"
    }
  }

 onMount(() => {
    getWeather();
  });
</script>

{#if errorMessage.length > 0}
  <p>{errorMessage}</p>
{:else}
  <section id="forecastsSection container">
    {#each forecasts as forecast}
      <Forecast
        image={forecast.imageCondition}
        tempMin={forecast.tempMin}
        tempMax={forecast.tempMax}
        date={forecast.date}
        rain={forecast.rain}
        snow={forecast.snow}
        wind={forecast.wind}
        humidity={forecast.humidity}
      />
    {/each}
  </section>
{/if}
