<template>
  <div>
    <h1>Store Details</h1>

    <div v-if="store">
      <p><strong>Name:</strong> {{ store.name }}</p>
      <p><strong>Revenue:</strong> {{ store.revenue }}</p>
      <p><strong>Location:</strong> {{ store.location.lat / 100000.0 }}, {{ store.location.lon / 100000.0 }}</p>
    </div>
    <div v-else>
      <p>No store selected.</p>
    </div>

    <h2>Items</h2>

    <div v-if="items">
      <table>
        <thead>
          <tr>
            <th>Name</th>
            <th>Weight</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in items" :key="item.name" class="item-card">
            <td class="name">{{ item.name }}</td>
            <td class="weight">{{ item.weight }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <div v-else>
      <p>No items yet.</p>
    </div>

    <h2>Drones</h2>

    <div v-if="drones">
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Capacity</th>
            <th>Fuel</th>
            <th>Max Fuel</th>
            <th>Speed</th>
            <th>State</th>
            <th>Location</th>
            <th>Route</th>
            <th>Distance to Next</th>
            <th>Range</th>
            <th>Max Range</th>
            <th>Bearing to Next</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="drone in drones" :key="drone.id" class="drone-card">
            <td class="id">{{ drone.droneId }}</td>
            <td class="capacity">{{ drone.capacity }}</td>
            <td class="fuel">{{  drone.fuel  }}</td>
            <td class="maxFuel">{{ drone.maxFuel }}</td>
            <td class="speed">{{ drone.speed }}</td>
            <td class="state">{{ drone.state }}</td>
            <td class="location">{{ drone.lat / 100000.0 }}, {{ drone.lon / 100000.0 }}</td>
            <td class="route">{{ drone.route  }}</td>
            <td class="distanceToNext">{{ drone.distanceToNext }}</td>
            <td class="range">{{  drone.range  }}</td>
            <td class="maxRange">{{ drone.maxRange }}</td>
            <td class="bearingToNext">{{ drone.bearingToNext }}</td>
          </tr>
        </tbody>
      </table>
    </div>
    <div v-else>
      <p>No drones yet.</p>
    </div>

    <!-- Add buttons/links to navigate to other pages -->
    <router-link v-if="storeName" :to="{ name: 'drone-list', params: { name: storeName } }">Go to Drones Page</router-link>
    <!-- Add other buttons/links for other pages if needed -->
  </div>
</template>

<script>
export default {
  async mounted() {
    this.name = this.$route.params.name;
    this.store = await (await fetch('http://localhost:8080/stores/'+this.name)).json()
    this.items = await (await fetch('http://localhost:8080/stores/'+this.name+'/items')).json()
    this.drones = await (await fetch('http://localhost:8080/stores/'+this.name+'/drones')).json()
  },
  data() {
    return {
      name: null,
      store: null,
      items: null,
      drones: null
    };
  }
};
</script>
