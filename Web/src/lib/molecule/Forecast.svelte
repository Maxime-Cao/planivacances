<script lang="ts">
  import { formatDateForDisplay } from "../../utils/DateFormatter";
  export let image: string;
  export let tempMin: string;
  export let tempMax: string;
  export let date: string;
  export let rain: string;
  export let snow: string;
  export let wind: string;
  export let humidity: string;
</script>

<div class="row" id="forecast">
  <div class="col" id="imageWeather">
    <img src={image} alt="Icône météo" />
  </div>
  <div class="col" id="temperatures">
    Min : {tempMin} °C - Max : {tempMax} °C
  </div>
  <div class="col" id="moreWeatherInformations">
    <div id="weatherDate">{formatDateForDisplay(date)}</div>
    <div>Pluie: {rain} mm - Neige: {snow} cm</div>
    <div>Vent: {wind} km/h - Humidité : {humidity} %</div>
  </div>
</div>

<style>
  #forecast {
    display: flex;
    align-items: center;
    margin-bottom: 1rem;
    background-color: #f2f2f2;
  }

  #temperatures {
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 1.3rem;
    font-weight: bold;
  }

  #moreWeatherInformations {
    text-align: right;
  }

  #weatherDate {
    font-size: 1.2rem;
    font-weight: bold;
  }

  #imageWeather {
    text-align: left;
  }
</style>
