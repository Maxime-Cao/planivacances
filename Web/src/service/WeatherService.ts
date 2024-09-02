import { isLoadingStore } from "../stores/loading";
import { API_KEY_WEATHER } from "../utils/config";
import { weatherInstance } from "./ApiClient";

export const getWeatherData = async (lat: number, lon: number) => {
  isLoadingStore.set(true);

  const days = 14;

  try {
    const response = await weatherInstance.get<string>(`forecast.json?key=${API_KEY_WEATHER}&q=${lat},${lon}&days=${days}&aqi=no&alerts=no`);

    if (response.status == 200) {
      const forecastsResult = await response.data as unknown;

      isLoadingStore.set(false);

      return forecastsResult?.forecast?.forecastday?.map((forecast: any) => ({
        date: forecast.date,
        imageCondition: forecast.day.condition.icon,
        tempMin: forecast.day.mintemp_c,
        tempMax: forecast.day.maxtemp_c,
        rain: forecast.day.totalprecip_mm,
        snow: forecast.day.totalsnow_cm,
        wind: forecast.day.maxwind_kph,
        humidity: forecast.day.avghumidity,
      }));
    }
        
  } catch (error) {
    console.error(error);
    isLoadingStore.set(false);

    return null;
  }

};