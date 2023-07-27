<template>
  <div>
    <h2>Refueling Station List</h2>

    <!-- Display existing stations -->
    <ul>
      <li v-for="station in stations" :key="station.id">
        {{ station.id }}
      </li>
    </ul>

    <!-- Form to add new station -->
    <div>
      <h3>Add New Station</h3>
      <form @submit.prevent="addStation">
        <label for="station-id">Station ID:</label>
        <input type="text" id="station-id" v-model="newStation.id" required>

        <label for="latitude">Latitude:</label>
        <input type="number" id="latitude" v-model="newStation.lat" required>

        <label for="longitude">Longitude:</label>
        <input type="number" id="longitude" v-model="newStation.lon" required>

        <button type="submit">Add Station</button>
      </form>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      stations: [], // Initialize stations as an empty array
      newStation: {
        id: '',
        lat: null,
        lon: null,
      },
    };
  },
  created() {
    // Fetch station data from the API when the component is created
    this.fetchStations();
  },
  methods: {
    fetchStations() {
      // Replace 'YOUR_API_ENDPOINT' with the actual URL of your backend API endpoint to fetch station data
      axios
        .get('http://localhost:8080/stations')
        .then((response) => {
          // Assuming your API response returns an array of station objects
          this.stations = response.data;
        })
        .catch((error) => {
          console.error('Error fetching station data:', error);
        });
    },

    addStation() {
      axios
        .post('http://localhost:8080/stations', this.newStation)
        .then((response) => {
          // Assuming the API responds with the newly added station object
          const newStation = response.data;

          // Update the local stations array with the new station data
          this.stations.push(newStation);

          // Reset the form inputs
          this.newStation.id = '';
          this.newStation.lat = null;
          this.newStation.lon = null;
        })
        .catch((error) => {
          console.error('Error adding new station:', error);
        });
    },
  },
};
</script>
