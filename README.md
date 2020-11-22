# Flat viewing scheduler

## Run
```
1. Checkout
2. Open project in IDEA
3. Run java main in class BookingApplication
4. Server will be start on `localhost:8080`
5. Call for check `http://localhost:8080/health`
6. Profit
```

## Available API
- /health - check server/service status
- /bookings - add, get, delete, check booking status 
- /users - get and add users
- /properties - get and add properties

## Step by step example
### 1. Add test users/customers
Add first user (current tenant)
```
curl -ik -XPOST 'localhost:8080/users' -H 'Content-Type: application/json' -d '{"id":"b3b76f64-ce97-45aa-8b7b-486dc6cce4ce","name":"Ivan","surname":"Ivanov","email":"ivanov@gmail.com"}'
```
Add second user (current tenant)
```
curl -ik -XPOST 'localhost:8080/users' -H 'Content-Type: application/json' -d '{"id":"df6396be-bf7d-469c-a584-66f5abc8dd64","name":"Semen","surname":"Petrov","email":"petrov@gmail.com"}'
```
Add third user (new tenant)
```
curl -ik -XPOST 'localhost:8080/users' -H 'Content-Type: application/json' -d '{"id":"abc396be-bf7d-469c-a584-66f5abc8dd00","name":"Dmitriy","surname":"Afanasiev","email":"afanasiev@gmail.com"}'
```
Get list of users
```
curl -ik -XGET 'localhost:8080/users'
```
### 2. Add properties
Add first flat
```
curl -ik -XPOST 'localhost:8080/properties' -H 'Content-Type: application/json' -d '{"id":"af305f1f-c270-4da5-8a72-f4a6a31e0e3e","tenantId":"b3b76f64-ce97-45aa-8b7b-486dc6cce4ce", "address":{"city":"Moscow","street":"Vinokuva","building":"15b","flat":"1"}}'
```
Add second flat
```
curl -ik -XPOST 'localhost:8080/properties' -H 'Content-Type: application/json' -d '{"id":"df6396be-bf7d-469c-a584-66f5abc8dd64","tenantId":"df6396be-bf7d-469c-a584-66f5abc8dd64", "address":{"city":"Moscow","street":"Lenina","building":"23","flat":"2"}}'
```
Get list properties
```
curl -ik -XGET 'localhost:8080/properties'
```
### 3. Add booking
Use header `X-User-Id` = `new tenant id` for request
```
curl -ik -XPOST 'localhost:8080/bookings' -H 'X-User-Id: abc396be-bf7d-469c-a584-66f5abc8dd00' -H 'Content-Type: application/json' -d '{"propertyId":"af305f1f-c270-4da5-8a72-f4a6a31e0e3e", "bookStart":"2020-11-24T11:00:00Z"}'
```
Get list created bookings
```
curl -ik -XGET 'localhost:8080/bookings'
```
### 4. Change booking status approve/reject/delete
Change booking id `2b209ec8-5709-4e55-a87d-33ae4072141e` of booking (get from `curl -ik -XGET 'localhost:8080/bookings'` id field)
Use header `X-User-Id` = `current tenant id` for approve and reject request 
```
curl -ik -XPATCH -H 'X-User-Id: b3b76f64-ce97-45aa-8b7b-486dc6cce4ce' 'localhost:8080/bookings/2b209ec8-5709-4e55-a87d-33ae4072141e/approve'
curl -ik -XPATCH -H 'X-User-Id: b3b76f64-ce97-45aa-8b7b-486dc6cce4ce' 'localhost:8080/bookings/2b209ec8-5709-4e55-a87d-33ae4072141e/reject'
```
Use header `X-User-Id` = `new tenant id` for delete
```
curl -ik -XPATCH -H 'X-User-Id: abc396be-bf7d-469c-a584-66f5abc8dd00' 'localhost:8080/bookings/2b209ec8-5709-4e55-a87d-33ae4072141e/delete'
```
### 5. Check changes
```
curl -ik -XGET 'localhost:8080/bookings'
```

## Fast example
Single request for building users, properties and booking
```
curl -ik -XPOST 'localhost:8080/users' -H 'Content-Type: application/json' -d '{"id":"b3b76f64-ce97-45aa-8b7b-486dc6cce4ce","name":"Ivan","surname":"Ivanov","email":"ivanov@gmail.com"}' \
    && curl -ik -XPOST 'localhost:8080/users' -H 'Content-Type: application/json' -d '{"id":"df6396be-bf7d-469c-a584-66f5abc8dd64","name":"Semen","surname":"Petrov","email":"petrov@gmail.com"}' \
    && curl -ik -XPOST 'localhost:8080/users' -H 'Content-Type: application/json' -d '{"id":"abc396be-bf7d-469c-a584-66f5abc8dd00","name":"Dmitriy","surname":"Afanasiev","email":"afanasiev@gmail.com"}' \
    && curl -ik -XGET 'localhost:8080/users' \
    && curl -ik -XPOST 'localhost:8080/properties' -H 'Content-Type: application/json' -d '{"id":"af305f1f-c270-4da5-8a72-f4a6a31e0e3e","tenantId":"b3b76f64-ce97-45aa-8b7b-486dc6cce4ce", "address":{"city":"Moscow","street":"Vinokuva","building":"15b","flat":"1"}}' \
    && curl -ik -XPOST 'localhost:8080/properties' -H 'Content-Type: application/json' -d '{"id":"df6396be-bf7d-469c-a584-66f5abc8dd64","tenantId":"df6396be-bf7d-469c-a584-66f5abc8dd64", "address":{"city":"Moscow","street":"Lenina","building":"23","flat":"2"}}' \
    && curl -ik -XGET 'localhost:8080/properties' \
    && curl -ik -XPOST 'localhost:8080/bookings' -H 'X-User-Id: abc396be-bf7d-469c-a584-66f5abc8dd00' -H 'Content-Type: application/json' -d '{"propertyId":"af305f1f-c270-4da5-8a72-f4a6a31e0e3e", "bookStart":"2020-11-24T11:00:00Z"}' \
    && curl -ik -XGET 'localhost:8080/bookings'
```
Need get booking id for request (replace `4e1ba963-f75a-4a5e-8679-02cab7c12215` from `curl -ik -XGET 'localhost:8080/bookings'`)
```
curl -ik -XPATCH -H 'X-User-Id: b3b76f64-ce97-45aa-8b7b-486dc6cce4ce' 'localhost:8080/bookings/2b209ec8-5709-4e55-a87d-33ae4072141e/approve'
```
