# Refuelling stations

In order to add refuelling stations to the system, we will add two classes.

The first is a Location, representing a location on the surface of the Earth, with a latitude and longitude. The class will include a method to measure the distance between two locations using the Haversine formula, and an attribute to determine if the location can be used for refuelling.

We will also add a RefuellingStation class, with an ID property.

The GroceryStore, Customer, Drone and RefuellingStation classes will all have an association with the new Location class, showing the location they are currently at.

Instead of the simple representation of Drone fuel by trips taken used in assignment 3, we'll include a fuelUnits attribute that represents the amount of fuel (weight or volume) used by the drone. Each drone will also have a maxFuelUnits attribute, representing the maximum amount of fuel the drone can carry. The Drone class will have a static attribute, fuelRate, to represent current fuel efficiency in distance per fuel unit. Finally, the Drone class will have methods to calculate range (possible distance to travel with current fuel), to move a drone to a new location and debit the fuel used, and to refuel.

We'll also factor in time. Each drone will have a speed property when created. We can also set a refuelling time. Each order will have a max expected delivery time. If the total time for the route (travel between charging stations and refuelling time) is greater than the max expected time, we will give an error.

The DeliveryService class will change the make* and display* methods for GroceryStore, Customer, and Drone to include the location and other attributes. It will also add methods to make and move RefuellingStations, set drone fuel rate, and to dispatch a refuelling drone.

We will change the purchaseOrder() method of the DeliveryService class to refuel the Drone if needed, then use the moveTo() method of the Drone class to move the drone to the Customer, possibly stopping at one or more RefuellingStations or GroceryStores, if a route is possible. The Drone will then move back to its home GroceryStore, possibly stopping at one or more RefuellingStations or GroceryStores. If it doesn't have enough fuel to get to a RefuellingStation or GroceryStore, it will stop where it is and wait for a refuelling drone.

We will have a configurable multiplier for time, so that we can speed up or slow down the simulation.

We will use the simplifying assumption that refuelling drones are always available and have enough fuel to get to any drone that needs refuelling and back to their main base. We will assume that grocery stores and refuelling stations have unlimited fuel available. Finally, we will assume that the weight of the fuel need not be considered when calculating the carrying capacity of the drone.
