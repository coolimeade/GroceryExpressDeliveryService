export STORENAME=STORE$RANDOM
export DRONEID=DRONE$RANDOM
export PILOTID=ACCT$RANDOM
export LICENSE=LICENSE$RANDOM
export ORDER=ORDER$RANDOM
export CUSTOMER=CUSTOMER$RANDOM
export ITEMNAME=ITEM$RANDOM
export STATIONID=STATION$RANDOM
export OTHERID=STATION$RANDOM
export LASTID=STATION$RANDOM

echo DRONE = $DRONEID
echo STORE = $STORENAME
echo PILOT = $PILOTID
echo LICENSE = $LICENSE
echo ITEM = $ITEMNAME
echo STATION = $STATIONID
echo OTHER = $OTHERID
echo LAST = $LASTID

curl -X POST -H "Content-Type: application/json" -d "{\"name\":\"${STORENAME}\", \"revenue\":100, \"lat\": 4565778, \"lon\": -7214098}" http://localhost:8080/stores
curl http://localhost:8080/stores
curl -X POST -H "Content-Type: application/json" -d "{\"itemName\":\"${ITEMNAME}\", \"weight\":100}" http://localhost:8080/stores/${STORENAME}/items
curl http://localhost:8080/stores/${STORENAME}/items
echo
curl -X POST -H "Content-Type: application/json" -d "{\"droneId\":\"${DRONEID}\", \"capacity\": 25000, \"fuel\": 900, \"maxFuel\": 1000, \"speed\": 7}" http://localhost:8080/stores/${STORENAME}/drones
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
curl -X POST -H "Content-Type: application/json" -d "{\"account\":\"${CUSTOMER}\", \"firstName\":\"Edwina\", \"lastName\": \"Courant\", \"phoneNumber\": \"212-222-2225\", \"rating\": 3, \"credit\": 500, \"lat\": 4564339, \"lon\": -7213933}" http://localhost:8080/customers
curl http://localhost:8080/customers
echo
curl http://localhost:8080/stores/${STORENAME}/distance/${CUSTOMER}
echo
curl -X POST -H "Content-Type: application/json" -d "{\"id\":\"${STATIONID}\", \"lat\": 4565146, \"lon\": -7213977}" http://localhost:8080/stations
curl -X POST -H "Content-Type: application/json" -d "{\"id\":\"${OTHERID}\", \"lat\": 4564543, \"lon\": -7214167}" http://localhost:8080/stations
curl -X POST -H "Content-Type: application/json" -d "{\"id\":\"${LASTID}\", \"lat\": 4564371,  \"lon\": -7213962}" http://localhost:8080/stations
curl http://localhost:8080/stations
while output=$(curl -s http://localhost:8080/stores/${STORENAME}/drones | grep Refuelling); do
    echo $output
    sleep 1
done
curl -X POST -H "Content-Type: application/json" -d "{\"account\":\"${CUSTOMER}\", \"droneId\": \"${DRONEID}\", \"orderId\": \"${ORDER}\"}" http://localhost:8080/stores/${STORENAME}/orders
curl http://localhost:8080/stores/${STORENAME}/orders
echo
curl -X POST -H "Content-Type: application/json" -d "{\"itemName\":\"${ITEMNAME}\", \"quantity\": 1, \"unitPrice\": 10}" http://localhost:8080/stores/${STORENAME}/orders/${ORDER}/items
curl http://localhost:8080/stores/${STORENAME}/orders/${ORDER}/items
echo
curl -X POST http://localhost:8080/stores/${STORENAME}/orders/${ORDER}/purchase
echo
export DRONES=`curl -s http://localhost:8080/stores/${STORENAME}/drones`
until output=$(echo $DRONES | grep AcceptingOrders); do
    echo $DRONES
    sleep 1
    export DRONES=`curl -s http://localhost:8080/stores/${STORENAME}/drones`
done