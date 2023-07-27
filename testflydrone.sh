#!/bin/bash

export STORENAME=STORE$RANDOM
export DRONEID=DRONE$RANDOM
export PILOTID=ACCT$RANDOM
export LICENSE=LICENSE$RANDOM

echo DRONE = $DRONEID
echo STORE = $STORENAME
echo PILOT = $PILOTID
echo LICENSE = $LICENSE

curl -X POST -H "Content-Type: application/json" -d "{\"name\":\"${STORENAME}\", \"revenue\":100, \"lat\": 4565853, \"lon\": -7214114}" http://localhost:8080/stores
curl http://localhost:8080/stores
echo
curl -X POST -H "Content-Type: application/json" -d "{\"droneId\":\"${DRONEID}\", \"capacity\": 25000, \"fuel\": 500, \"maxFuel\": 1000, \"speed\": 7}" http://localhost:8080/stores/${STORENAME}/drones
curl http://localhost:8080/stores/${STORENAME}/drones
echo
curl -X POST -H "Content-Type: application/json" -d "{\"account\":\"${PILOTID}\", \"firstName\": \"Jennifer\", \"lastName\": \"Wu\", \"phoneNumber\": \"212-222-2223\", \"taxId\": \"333-33-3333\", \"license\": \"${LICENSE}\", \"experience\": 500}" http://localhost:8080/pilots
curl http://localhost:8080/pilots
echo
curl -X POST -H "Content-Type: application/json" -d "{\"droneId\":\"${DRONEID}\", \"storeName\": \"${STORENAME}\"}" http://localhost:8080/pilots/${PILOTID}/fly
curl http://localhost:8080/stores/${STORENAME}/drones
echo
curl http://localhost:8080/pilots
echo