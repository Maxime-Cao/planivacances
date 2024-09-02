<!-- LocationPicker.svelte -->
<script lang="ts">
  import { createEventDispatcher, onMount } from 'svelte';
  import { API_KEY_MAPS } from '../../utils/config';
  import type { Place } from '../../model/Place';

  let map: any;
  let marker: any;
  let searchInput: HTMLInputElement;

  export let place: Place|null = {
      street: '',
      number: '',
      city: '',
      country: '',
      postalCode: '',
      lat: 0,
      lon: 0
  };

  const dispatch = createEventDispatcher();

  onMount(() => {
    const script = document.createElement('script');
    script.src = `https://maps.googleapis.com/maps/api/js?key=${API_KEY_MAPS}&libraries=places`;
    script.async = true;
    script.defer = true;

    script.onload = () => {
      if(place == null) {
        getUserLocation();
      }
      initMap();
    };

    document.head.appendChild(script);

    return () => {
      if (map) {
        google.maps.event.clearInstanceListeners(map);
      }
    };
  });

  function initMap() {
    map = new google.maps.Map(document.getElementById('map')!, {
      center: { lat: place?.lat ?? 0, lng: place?.lon ?? 0 },
      zoom: 5,
    });

    // Place first new marker at the clicked location
    marker = new google.maps.Marker({
        position: { lat: place?.lat ?? 0, lng: place?.lon ?? 0 },
        map: map,
        draggable: true,
      });

    // Add a click event listener to the map
    map.addListener('click', (event: any) => {
      // Remove existing marker if any
      if (marker) {
        marker.setMap(null);
      }

      // Place a new marker at the clicked location
      marker = new google.maps.Marker({
        position: event.latLng,
        map: map,
        draggable: true,
      });

      // Update place object with the clicked location
      const { lat, lng } = event.latLng.toJSON();
      if(place != null) {
        place.lat = lat;
        place.lon = lng;
      }

      getAddress(lat, lng);

      dispatch('place', {
        selectedPlace: place,
      });

    });

    // Add a Places Autocomplete search bar
    const autocomplete = new google.maps.places.Autocomplete(searchInput, { types: ['geocode'] });
    autocomplete.addListener('place_changed', () => {
      searchInput.textContent = autocomplete.getPlace.name;
      const selectedPlace = autocomplete.getPlace();

      if (!selectedPlace.geometry) {
        console.error("Place details not found for the input: ", searchInput.value);
        return;
      }

      // Update map and marker with the selected place
      map.setCenter(selectedPlace.geometry.location);
      marker.setPosition(selectedPlace.geometry.location);

      const { lat, lng } = selectedPlace.geometry.location.toJSON();
      if (place != null) {
        place.lat = lat;
        place.lon = lng;
      }

      getAddress(lat, lng);

      dispatch('place', {
        selectedPlace: place,
      });
    });
    
  }

  function getUserLocation() {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          const { latitude, longitude } = position.coords;
          map.setCenter({ lat: latitude, lng: longitude });
          marker.setPosition({ lat: latitude, lng: longitude });

          if(place != null) {
            place.lat = latitude;
            place.lon = longitude;
          }

          getAddress(latitude, longitude);

          dispatch("place", {
              selectedPlace: place,
          });
        },
        (error) => {
          console.error('Error getting user location:', error.message);
        }
      );
    } else {
      console.error('Geolocation is not supported by this browser.');
    }
  }

  function getAddress(lat: number, lng: number) {
      const geocoder = new google.maps.Geocoder();
      const latLng = new google.maps.LatLng(lat, lng);

      geocoder.geocode({ location: latLng }, (results, status) => {
      if (status === 'OK') {
          if (results[0]) {
            const components = results[0].address_components;

            cleanAddress();

            if(place != null) {
              components.forEach((component) => {
                  if (component.types.includes('route')) {
                      place!.street = component.long_name;
                  } else if (component.types.includes('street_number')) {
                      place!.number = component.long_name;
                  } else if (component.types.includes('locality')) {
                      place!.city = component.long_name;
                  } else if (component.types.includes('country')) {
                      place!.country = component.long_name;
                  } else if (component.types.includes('postal_code')) {
                      place!.postalCode = component.long_name;
                  }
              });
            }
          } else {
            console.error('No results found');
          }
      } else {
          console.error('Geocoder failed due to:', status);
      }
    });
  }

  function cleanAddress() {
    if(place != null) {
      place.street = "";
      place.number = "";
      place.city = "";
      place.country = "";
      place.postalCode = "";
    }
  }

</script>
  
<input type="text" bind:this={searchInput} placeholder="Rechercher un lieu">
<div id="map"></div>
<p>{place?.street}, {place?.number} {place?.city} {place?.country}</p>

<style>
    input {
      width: 100%;
    }

    #map {
      height: 400px;
    }

    p {
        color: black;
    }
</style>
  