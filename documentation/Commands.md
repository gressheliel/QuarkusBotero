# Utitliy commands and input schema for the CRUD operations.

## `ContactResource`:

1. **Create a new contact**:
    ```sh
    curl -X POST http://localhost:8080/api/contact \
    -H "Content-Type: application/json" \
    -d '{"name": "Agie", "email": "angie@angie.com","dataProtection": true}'
    ```
   - Another one:
    ```sh
    curl -X POST http://localhost:8080/api/contact \
    -H "Content-Type: application/json" \
     -d '{"name": "micol", "email": "micol@example.com","dataProtection": true}'
    ```

2. **Get all contacts**:
    ```sh
    curl -X GET http://localhost:8080/api/contact
    ```

3. **Get a contact by ID**:
    ```sh
    curl -X GET http://localhost:8080/api/contact/1
    ```

4. **Update a contact by ID**:
    ```sh
    curl -X PUT http://localhost:8080/api/contact/1 \
    -H "Content-Type: application/json" \
     -d '{"name": "Agie", "email": "angie@example.com","dataProtection": true}'
    ```

5. **Delete a contact by ID**:
    ```sh
    curl -X DELETE http://localhost:8080/api/contact/1
    ```
## `ProductApi`:

1. **Get all products**:
    ```sh
    curl -X GET http://localhost:8081/product
    ```

2. **Get a product by ID**:
    ```sh
    curl -X GET http://localhost:8081/product/1
    ```

3. **Create a new product**:
    ```sh
    curl -X POST http://localhost:8081/product \
    -H "Content-Type: application/json" \
    -d '{"name": "Tarjeta credito", "description": "Credito al portador de la tarjeta"}'
    ```
    ```sh
    curl -X POST http://localhost:8081/product \
    -H "Content-Type: application/json" \
    -d '{"code": "01", "name": "cuenta de ahorros", "description": "cuenta principal del cliente"}'
    ```
4. **Update a product**:
    ```sh
    curl -X PUT http://localhost:8081/product \
    -H "Content-Type: application/json" \
    -d '{"id": 1, "name": "Tarjeta de credito multiusos", "description": "Puede usarse en cualquier establecimiento"}'
    ```
5. **Delete a product by ID**:
    ```sh
    curl -X DELETE http://localhost:8081/product/1
    ```

### JSON Schemas

**JSON Schema for POST (Create a new product)**:

**Create products**
```json
{
   "code": "32548",
    "name": "Tarjeta credito",
    "description": "Description1"
}
```
```json
{
    "code": "123ABC",
    "name": "Cuenta Corriente",
    "description": "Cuenta bancaria para uso diario"
}
```
**JSON Schema for PUT (Update a product)**:
```json
{
    "code": "2154C",
    "name": "Puntos fidelización",
    "description": "Cuenta bancaria para uso diario"
}
```

