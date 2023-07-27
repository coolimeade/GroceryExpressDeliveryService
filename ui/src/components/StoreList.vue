<template>
  <div>
    <h1>Store List</h1>
    <div v-for="store in stores" :key="store.name" class="store-card" @click="selectStore(store)">
      <router-link :to="{ name: 'store-details', params: { name: store.name } }">
        <h2>{{ store.name }}</h2>
      </router-link>
      <p>Revenue: {{ store.revenue }}</p>
      <!-- Add other store details here -->
    </div>

    <!-- Form to add new store -->
    <div>
      <h3>Add New Store</h3>
      <form @submit.prevent="addStore">
        <label for="new-store-name">Store Name:</label>
        <input type="text" id="new-store-name" v-model="newStore.name" required>

        <label for="new-store-revenue">Revenue:</label>
        <input type="number" id="new-store-revenue" v-model="newStore.revenue" required>

        <label for="new-store-lat">Latitude:</label>
        <input type="number" id="new-store-lat" v-model="newStore.lat" required>

        <label for="new-store-lon">Longitude:</label>
        <input type="number" id="new-store-lon" v-model="newStore.lon" required>

        <!-- Add other input fields for additional store properties if needed -->

        <button type="submit">Add Store</button>
      </form>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      stores: [],
      newStore: {
        name: '',
        revenue: null,
        lat: null,
        lon: null
        // Add other properties for additional store properties if needed
      },
      selectedStoreName: null, // Initialize selectedStoreName to null
    };
  },
  async created() {
    try {
      await this.fetchStores();
    } catch (error) {
      console.error('Error fetching stores:', error);
    }
  },
  methods: {
    async fetchStores() {
      const response = await axios.get('http://localhost:8080/stores');
      this.stores = response.data;
    },
    async addStore() {
      try {
        // Make a POST request to add the new store to the backend
        const response = await axios.post('http://localhost:8080/stores', this.newStore);
        const newStore = response.data;

        // Update the frontend stores list with the newly added store
        this.stores.push(newStore);

        // Reset the form inputs
        this.newStore.name = '';
        this.newStore.revenue = null;
        this.newStore.lat = null;
        this.newStore.lon = null;
        // Reset other form inputs for additional store properties if needed
      } catch (error) {
        console.error('Error adding store:', error);
      }
    },
    selectStore(store) {
      this.selectedStoreName = store.name; // Store the clicked store's name
    },
  },
};
</script>

<style>
.store-card {
  border: 1px solid #ccc;
  padding: 10px;
  margin: 10px;
}
</style>