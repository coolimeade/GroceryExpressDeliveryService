#!/bin/bash

curl -X POST -H "Content-Type: application/json" -d '{"name":"maxi", "revenue":100, "lat": 4565853, "lon": -7214114}' http://localhost:8080/stores
curl http://localhost:8080/stores
echo
curl -X POST -H "Content-Type: application/json" -d '{"itemName":"cheese", "weight":200}' http://localhost:8080/stores/maxi/items
curl http://localhost:8080/stores/maxi/items
echo
curl -X POST -H "Content-Type: application/json" -d '{"droneId":"42", "capacity": 25000, "fuel": 1000, "maxFuel": 1000, "speed": 7}' http://localhost:8080/stores/maxi/drones
curl -X POST -H "Content-Type: application/json" -d '{"droneId":"23", "capacity": 15000, "fuel": 800, "maxFuel": 1000, "speed": 15}' http://localhost:8080/stores/maxi/drones
curl http://localhost:8080/stores/maxi/drones
echo
curl -X POST -H "Content-Type: application/json" -d '{"account":"0001", "firstName":"John", "lastName": "Smith", "phoneNumber": "212-222-2222", "rating": 3, "credit": 1000,    "lat": 4564339, "lon": -7213933}' http://localhost:8080/customers
curl -X POST -H "Content-Type: application/json" -d '{"account":"0002", "firstName":"Marie", "lastName": "Laforge", "phoneNumber": "212-222-2224", "rating": 3, "credit": 2000, "lat": 4566859, "lon": -7214927}' http://localhost:8080/customers
curl -X POST -H "Content-Type: application/json" -d '{"account":"0003", "firstName":"Edwina", "lastName": "Courant", "phoneNumber": "212-222-2225", "rating": 3, "credit": 500, "lat": 4565370, "lon": -7213854}' http://localhost:8080/customers
curl http://localhost:8080/customers
echo
curl -X POST -H "Content-Type: application/json" -d '{"id":"3", "lat": 4563637, "lon": -7214445}' http://localhost:8080/stations
curl http://localhost:8080/stations
echo
curl -X POST -H "Content-Type: application/json" -d '{"lat": 4566524, "lon": -7215926}' http://localhost:8080/stations/3/move
curl http://localhost:8080/stations
echo
curl -X POST -H "Content-Type: application/json" -d '{"account":"0002", "firstName": "Jennifer", "lastName": "Wu", "phoneNumber": "212-222-2223", "taxId": "333-33-3333", "license": "11111111", "experience": 500}' http://localhost:8080/pilots
curl http://localhost:8080/pilots
echo
curl -X POST -H "Content-Type: application/json" -d '{"droneId":"42", "storeName": "maxi"}' http://localhost:8080/pilots/0002/fly
curl http://localhost:8080/stores/maxi/drones
echo
curl http://localhost:8080/pilots
echo
curl -X POST -H "Content-Type: application/json" -d '{"account":"0001", "droneId": "42", "orderId": "0001-42-maxi-1"}' http://localhost:8080/stores/maxi/orders
curl http://localhost:8080/stores/maxi/orders
echo
curl -X POST -H "Content-Type: application/json" -d '{"itemName": "cheese", "quantity": 2, "unitPrice": 3}' http://localhost:8080/stores/maxi/orders/0001-42-maxi-1/items
curl http://localhost:8080/stores/maxi/orders/0001-3-maxi-1/items
echo
curl http://localhost:8080/stores/maxi/orders
echo
curl -X POST -H "Content-Type: application/json" -d '{"account":"0002", "droneId": "23", "orderId": "0002-23-maxi-1"}' http://localhost:8080/stores/maxi/orders
curl http://localhost:8080/stores/maxi/orders
echo
curl -X POST -H "Content-Type: application/json" -d '{}' http://localhost:8080/stores/maxi/orders/0002-23-maxi-1/cancel
curl http://localhost:8080/stores/maxi/orders
echo

# Make a new order, make a new drone, transfer order to new drone

curl -X POST -H "Content-Type: application/json" -d '{"droneId":"4", "capacity": 30000, "fuel": 600, "maxFuel": 1000, "speed": 12}' http://localhost:8080/stores/maxi/drones
curl http://localhost:8080/stores/maxi/drones
echo
curl -X POST -H "Content-Type: application/json" -d '{"account":"0002", "droneId": "23", "orderId": "0002-23-maxi-2"}' http://localhost:8080/stores/maxi/orders
curl http://localhost:8080/stores/maxi/orders
echo
curl -X POST -H "Content-Type: application/json" -d '{"droneId": "4"}' http://localhost:8080/stores/maxi/orders/0002-23-maxi-2/transfer
curl http://localhost:8080/stores/maxi/orders
echo

# Get distance to some customers

curl http://localhost:8080/stores/maxi/distance/0001
echo
curl http://localhost:8080/stores/maxi/distance/0002
echo
curl http://localhost:8080/stores/maxi/distance/0003
echo

# Purchase an order

curl -X POST http://localhost:8080/stores/maxi/orders/0001-42-maxi-1/purchase
curl http://localhost:8080/stores/maxi/orders
echo
curl http://localhost:8080/stores
echo
curl http://localhost:8080/customers
echo
curl http://localhost:8080/pilots
echo

# Fuel efficiency

curl http://localhost:8080/settings/drones/fuelEfficiency
echo
curl -X PUT  -H "Content-Type: application/json" -d '20' http://localhost:8080/settings/drones/fuelEfficiency
curl http://localhost:8080/settings/drones/fuelEfficiency
echo

# Store efficiency

curl http://localhost:8080/efficiencies
echo
curl http://localhost:8080/efficiencies/maxi
echo

# Refuelling rate

curl http://localhost:8080/settings/drones/refuellingRate
echo
curl -X PUT  -H "Content-Type: application/json" -d '12' http://localhost:8080/settings/drones/refuellingRate
curl http://localhost:8080/settings/drones/refuellingRate
echo