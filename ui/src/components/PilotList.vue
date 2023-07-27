<template>
  <div>
    <h2>Pilot List</h2>

    <!-- Display existing pilots -->
    <ul>
      <li v-for="pilot in pilots" :key="pilot.account">
        {{ pilot.firstName }} {{ pilot.lastName }}
      </li>
    </ul>

    <!-- Form to add new pilot -->
    <div>
      <h3>Add New Pilot</h3>
      <form @submit.prevent="addPilot">
        <label for="account">Account:</label>
        <input type="text" id="account" v-model="newPilot.account" required>

        <label for="first-name">First Name:</label>
        <input type="text" id="first-name" v-model="newPilot.firstName" required>

        <label for="last-name">Last Name:</label>
        <input type="text" id="last-name" v-model="newPilot.lastName" required>

        <label for="phone">Phone:</label>
        <input type="text" id="phone" v-model="newPilot.phone" required>

        <label for="tax-id">Tax ID:</label>
        <input type="text" id="tax-id" v-model="newPilot.taxId" required>

        <label for="license">License:</label>
        <input type="text" id="license" v-model="newPilot.license" required>

        <label for="experience">Experience:</label>
        <input type="number" id="experience" v-model="newPilot.experience" required>

        <button type="submit">Add Pilot</button>
      </form>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      pilots: [], // Initialize pilots as an empty array
      newPilot: {
        account: '',
        firstName: '',
        lastName: '',
        phone: '',
        taxId: '',
        license: '',
        experience: null,
      },
    };
  },
  created() {
    // Fetch pilot data from the API when the component is created
    this.fetchPilots();
  },
  methods: {
    fetchPilots() {
      // Replace 'YOUR_API_ENDPOINT' with the actual URL of your backend API endpoint to fetch pilot data
      axios
        .get('http://localhost:8080/pilots')
        .then((response) => {
          // Assuming your API response returns an array of pilot objects
          this.pilots = response.data;
        })
        .catch((error) => {
          console.error('Error fetching pilot data:', error);
        });
    },

    addPilot() {
      axios
        .post('http://localhost:8080/pilots', this.newPilot)
        .then((response) => {
          // Assuming the API responds with the newly added pilot object
          const newPilot = response.data;

          // Update the local pilots array with the new pilot data
          this.pilots.push(newPilot);

          // Reset the form inputs
          this.newPilot.account = '';
          this.newPilot.firstName = '';
          this.newPilot.lastName = '';
          this.newPilot.phone = '';
          this.newPilot.taxId = '';
          this.newPilot.license = '';
          this.newPilot.experience = null;
        })
        .catch((error) => {
          console.error('Error adding new pilot:', error);
        });
    },
  },
};
</script>
