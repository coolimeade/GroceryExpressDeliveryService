<template>
  <div>
    <h2>Customer List</h2>

    <!-- Display existing customers -->
    <ul>
      <li v-for="customer in customers" :key="customer.account">
        {{ customer.firstName }} {{ customer.lastName }}
      </li>
    </ul>

    <!-- Form to add new customer -->
    <div>
      <h3>Add New Customer</h3>
      <form @submit.prevent="addCustomer">
        <label for="account">Account:</label>
        <input type="text" id="account" v-model="newCustomer.account" required>

        <label for="first-name">First Name:</label>
        <input type="text" id="first-name" v-model="newCustomer.firstName" required>

        <label for="last-name">Last Name:</label>
        <input type="text" id="last-name" v-model="newCustomer.lastName" required>

        <label for="phone-number">Phone Number:</label>
        <input type="text" id="phone-number" v-model="newCustomer.phoneNumber" required>

        <label for="rating">Rating:</label>
        <input type="number" id="rating" v-model="newCustomer.rating" required>

        <label for="credit">Credit:</label>
        <input type="number" id="credit" v-model="newCustomer.credit" required>

        <label for="latitude">Latitude:</label>
        <input type="number" id="latitude" v-model="newCustomer.lat" required>

        <label for="longitude">Longitude:</label>
        <input type="number" id="longitude" v-model="newCustomer.lon" required>

        <button type="submit">Add Customer</button>
      </form>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      customers: [], // Initialize customers as an empty array
      newCustomer: {
        account: '',
        firstName: '',
        lastName: '',
        phoneNumber: '',
        rating: null,
        credit: null,
        lat: null,
        lon: null,
      },
    };
  },
  created() {
    // Fetch customer data from the API when the component is created
    this.fetchCustomers();
  },
  methods: {
    fetchCustomers() {
      // Replace 'YOUR_API_ENDPOINT' with the actual URL of your backend API endpoint to fetch customer data
      axios
        .get('http://localhost:8080/customers')
        .then((response) => {
          // Assuming your API response returns an array of customer objects
          this.customers = response.data;
        })
        .catch((error) => {
          console.error('Error fetching customer data:', error);
        });
    },

    addCustomer() {
      axios
        .post('http://localhost:8080/customers', this.newCustomer)
        .then((response) => {
          // Assuming the API responds with the newly added customer object
          const newCustomer = response.data;

          // Update the local customers array with the new customer data
          this.customers.push(newCustomer);

          // Reset the form inputs
          this.newCustomer.account = '';
          this.newCustomer.firstName = '';
          this.newCustomer.lastName = '';
          this.newCustomer.phoneNumber = '';
          this.newCustomer.rating = null;
          this.newCustomer.credit = null;
          this.newCustomer.lat = null;
          this.newCustomer.lon = null;
        })
        .catch((error) => {
          console.error('Error adding new customer:', error);
        });
    },
  },
};
</script>

