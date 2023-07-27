import * as VueRouter from 'vue-router';
import StoreList from '../components/StoreList.vue';
import StoreDetails from '../components/StoreDetails.vue';
import CustomerList from '../components/CustomerList.vue';
import PilotList from '../components/PilotList.vue';
import StationList from '../components/StationList.vue';
import DroneList from '../components/DroneList.vue';

const routes = [
  { path: '/stores', component: StoreList },
  { path: '/stores/:name', name: 'store-details', component: StoreDetails }, // Removed props: true
  { path: '/:storename/drones', name: 'drone-list', component: DroneList }, // Removed props: true
  { path: '/customers', component: CustomerList },
  { path: '/pilots', component: PilotList },
  { path: '/stations',  name: 'StationList', component: StationList },
];

const router = VueRouter.createRouter({
  history: VueRouter.createWebHistory(),
  routes,
});

export default router;
