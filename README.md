# Assignment 4

This is our working dir for Assignment 4. A video overview of our project can be found at https://youtu.be/ygxqp1Z_u3c 

## Prerequisites

- Docker

To build outside of Docker, you need Java 20 and Maven 3.

Also, you need to have NodeJS 20 to build the UI.

## How to run

From the command line, run:

```
docker compose up --build
```

Then open http://localhost:3000 in your browser.

This will build the server and UI, and run them in Docker containers. It also starts a Redis server, and stores its data in a Docker volume.

The client is on port 3000, and the API is on port 8080.

## Database

You can clear the database with this command:

```
docker compose exec db /usr/local/bin/redis-cli FLUSHDB
```

## Directories

- `api`: The API server
- `ui`: The client
- `archive`: Assignment 3 code for reference

## API

This is the API provided by the API server. All the input and output is JSON.

A note about locations: lat and lon are represented by integers so we
can do equality comparisons on them. The actual value is the integer
divided by 100000. So 123456 is 1.23456. This level of granularity gets
us ~0.3m resolution, which is probably fine for delivery drones. The UI should handle conversion.

### `GET /stores`

Returns an array of stores. Each store has a `name` and a `revenue`,
plus a `location` object with a `lat` and `lon` property. See above
for lat/lon format.

### `POST /stores`

Creates a new store. The body should be a JSON object with these properties:

- `name`: The store name, must be unique
- `revenue`: The store's revenue, in dollars
- `lat`: The latitude of the store's location x 100000 and rounded (see note above)
- `lon`: The longitude of the store's location x 100000 and rounded (see note above)

### `POST /stores/{storeName}/items`

Add an item to the store catalog. The body should be a JSON object with an `itemName` and a `weight`.

### `GET /stores/{storeName}/items`

Returns an array of items in the store catalog. Each item has an `itemName` and a `weight`.

### `POST /stores/{storeName}/drones`

Add a drone to the store. The body should be a JSON object with:

- `droneId`: The drone ID
- `speed`: The drone's speed, in km/h
- `fuel`: The drone's current fuel level, in fuel units
- `maxFuel`: The drone's maximum fuel level, in fuel units
- `capacity`: The drone's capacity, in weight units

### `GET /stores/{storeName}/drones`

Returns the drones at the store. Each drone has a `droneId`, `speed`, `fuel`, `maxFuel`, and `capacity`, plus a `pilotAccount` id of the
pilot they are assigned to, or `null` if they are not assigned to a pilot.

### `POST /customers`

Creates a new customer. The body should be a JSON object with these properties:

- `account`: The account number
- `firstName`: The first name
- `lastName`: The last name
- `phoneNumber`: The phone number
- `rating`: customer rating, from 1-5
- `credit`: The credit limit, in dollars
- `lat`: The latitude of the customer's location x 100000 and rounded (see note above)
- `lon`: The longitude of the customer's location x 100000 and rounded (see note above)

### `GET /customers`

Gets all customers. Returns a JSON array of customers, with the above properties, except `lat` and `lon` are in a `location` object.

### `POST /stations`

Creates a new refuelling station. The body should be a JSON object with these properties:

- `id`: The station ID
- `lat`: The latitude of the station's location x 100000 and rounded (see note above)
- `lon`: The longitude of the station's location x 100000 and rounded (see note above)

### `GET /stations`

Gets all stations. Returns a JSON array of stations, with the above properties, except `lat` and `lon` are in a `location` object.

### `POST /stations/{stationId}/move`

Moves the refuelling station with id `stationId`. The body should be a JSON object with these properties:

- `lat`: The latitude of the station's location x 100000 and rounded (see note above)
- `lon`: The longitude of the station's location x 100000 and rounded (see note above)

### `POST /pilots`

Creates a new pilot. The body should be a JSON object with these properties:

- `account`: The account number
- `firstName`: The first name
- `lastName`: The last name
- `phoneNumber`: The phone number
- `taxId`: The tax ID
- `license`: The pilot's license number
- `experience`: The pilot's experience, in number of flights

### `GET /pilots`

Gets all pilots. Returns a JSON array of pilots, with the above properties, plus a `droneId` of the drone they are assigned to, or `null` if they are not assigned to a drone.

### `POST /pilots/{pilotId}/fly`

Makes the pilot with id `pilotId` fly a drone. The body should be a JSON object with these properties:

- `droneId`: The drone ID
- `storeName`: The store name

## `POST /stores/{storeName}/orders/{orderId}/cancel`

Cancels the order with id `orderId` at store `storeName`.

## `POST /stores/{storeName}/orders/{orderId}/transfer`

Transfers the order with id `orderId` at store `storeName` to another drone. The body should be a JSON object with these properties:

- `droneId`: The drone ID to transfer to

## `GET /stores/{storeName}/distance/{account}`

Gets the distance between the store `storeName` and the customer with account number `account`. Returns a JSON long integer, representing
the meter distance between the two locations.

## `GET /settings/drones/fuelEfficiency`

Get the current fuel efficiency of all drones. This is measured in the
number of meters a drone can travel on one fuel unit. Returns a JSON
integer.

## `POST /settings/drones/fuelEfficiency`

Set the current fuel efficiency of all drones. This is measured in the
number of meters a drone can travel on one fuel unit. The body should
be a JSON integer.

## `GET /settings/drones/refuellingRate`

Get the current refuelling rate of drones, in fuel units per second.

## `POST /settings/drones/refuellingRate`

Set the current refuelling rate of drones, in fuel units per second.

## `GET /efficiencies`

Get the list of store efficiencies. A JSON array of objects with these
properties:

- `storeName`: The store name
- `purchases`: The number of purchases made at the store
- `overloads`: The number of drone flights with extra orders
- `transfers`: The number of transfers of orders between drones

## `GET /efficiencies/{storeName}`

Get the efficiency of one store. A JSON object with above properties.
