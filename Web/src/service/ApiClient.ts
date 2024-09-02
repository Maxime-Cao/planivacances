import axios from "axios";
import { getIdToken } from "./AuthService";
import { customTokenStore } from "../stores/authToken";

let customToken: string;

customTokenStore.subscribe(value => {
    customToken = value;
});

export let weatherInstance = axios.create({
    baseURL: `https://api.weatherapi.com/v1`,
});


export const instance = axios.create({
  baseURL: 'https://studapps.cg.helmo.be:5011/REST_CAO_BART/api',
  headers: {
    'Content-Type': 'application/json'
    }
});

export const setToken = (token: string) => {
    instance.defaults.headers.common['Authorization'] = `Bearer ${token}`;
}

const refreshToken = async () => {
    // Logic to fetch a new token
    const newToken = await getIdToken();
  
    // Update the Axios instance headers with the new token
    instance.defaults.headers.common['Authorization'] = `Bearer ${newToken}`;
  
    // Return the new token
    return newToken;
};

// Add response interceptor
instance.interceptors.response.use(
  (response) => {
    // Return a successful response
    return response;
  },
  async (error) => {
    // Check if the error is due to an expired token
    if (error.response && error.response.status === 401) {
      // Token is expired, refresh it
      const newToken = await refreshToken();

      // Retry the original request with the new token
      error.config.headers['Authorization'] = `Bearer ${newToken}`;
      return instance.request(error.config);
    }

    // If not an error due to an expired token, return the error
    return Promise.reject(error);
  }
);