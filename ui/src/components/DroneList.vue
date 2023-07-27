<template>
  <div>
    <h2>Drone Details</h2>

    <div v-if="drone">
      <p><strong>ID:</strong> {{ drone.id }}</p>
      <p><strong>Capacity:</strong> {{ drone.capacity }}</p>
      <p><strong>Fuel:</strong> {{ drone.fuel }}</p>
      <p><strong>Max Fuel:</strong> {{ drone.maxFuel }}</p>
      <p><strong>Speed:</strong> {{ drone.speed }}</p>
      <p><strong>Status:</strong> {{ droneStateToString(drone.state) }}</p>
      <p><strong>Location:</strong> {{ drone.location.lat }}, {{ drone.location.lon }}</p>
    </div>
    <div v-else>
      <p>No drone selected.</p>
    </div>

    <!-- Allow user to input drone information -->
    <div>
      <label for="droneId">Drone ID:</label>
      <input type="text" v-model="droneId" id="droneId">
    </div>
    <div>
      <label for="capacity">Capacity:</label>
      <input type="number" v-model="capacity" id="capacity">
    </div>
    <div>
      <label for="fuel">Fuel:</label>
      <input type="number" v-model="fuel" id="fuel">
    </div>
    <div>
      <label for="maxFuel">Max Fuel:</label>
      <input type="number" v-model="maxFuel" id="maxFuel">
    </div>
    <div>
      <label for="speed">Speed:</label>
      <input type="number" v-model="speed" id="speed">
    </div>
    <button @click="createDrone">Create Drone</button>

    <!-- Add a button/link to navigate back to the store details page -->
    <router-link :to="{ path: '/stores' }">Back to Store Details</router-link>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  props: {
    storeName: {
      type: String,
      default: null,
    },
    drones: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      droneId: '', // Input for drone ID
      capacity: 0, // Input for capacity
      fuel: 0, // Input for fuel
      maxFuel: 0, // Input for max fuel
      speed: 0, // Input for speed
    };
  },
  computed: {
    drone() {
      // Find the selected drone from the drones array based on the storeName
      return this.drones.find(drone => drone.storeName === this.storeName);
    },
  },
  methods: {
    async createDrone() {
      try {
        // Make an API call to create the drone
        await axios.post(`/api/${this.storeName}/drones`, {
          droneId: this.droneId,
          capacity: this.capacity,
          fuel: this.fuel,
          maxFuel: this.maxFuel,
          speed: this.speed
        });

        // Optionally, you can show a success message or perform any other actions
        console.log('Drone created successfully');
      } catch (error) {
        // Handle errors, such as displaying an error message
        console.error('Error creating drone:', error.message);
      }
    },
    droneStateToString(state) {
      // Helper function to convert drone state enum to a string for display purposes.
      return state === 0 ? "Refuelling At Home" : "Accepting Orders";
    },
  },
};
</script>
