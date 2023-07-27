<template>
  <div>
    <h1 class="title">Delivery Service App</h1>
    <div class="nav-links">
      <router-link class="nav-button" to="/stores">Store List</router-link>
      <router-link class="nav-button" to="/customers">Customer List</router-link>
      <router-link class="nav-button" to="/pilots">Pilot List</router-link>
      <router-link class="nav-button" to="/stations">Station List</router-link>
    </div>

    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: 'App',
};
</script>

<style>
/* Add space between each nav link */
.nav-links {
  display: flex;
  gap: 10px; /* Adjust the gap value to control the spacing between links */
  justify-content: center; /* Center the buttons horizontally */
  margin-bottom: 20px; /* Add some space between the buttons and the content below */
}

/* Style the links as buttons */
.nav-button {
  display: inline-block;
  padding: 10px 20px;
  background-color: #007BFF;
  color: white;
  border: none;
  border-radius: 4px;
  text-decoration: none;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.nav-button:hover {
  background-color: #0056b3;
}

/* Style the title with a background color */
.title {
  background-color: #f2f2f2;
  padding: 10px;
  margin-bottom: 20px; /* Add some space between the title and the buttons */
}
</style>

