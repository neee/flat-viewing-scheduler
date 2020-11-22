# Flat viewing scheduler

# Example
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

curl -ik -XGET 'localhost:8080/bookings/4e1ba963-f75a-4a5e-8679-02cab7c12215/approve'
```