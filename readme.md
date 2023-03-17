# Software Licence Key Generator and Validaor API

## Brief
- endpoint 1: Creating a license key.  
  - This API endpoint must accept:
    - The full name of the end-user
    - The name of the software package
    - A secret to validate the API access is from an authenticated user
  - This API endpoint must return
    - A secure key based off of the given inputs and internal private key
    - A HTTP 401 Unauthorized response if the secret hasn't been provided or is incorrect
- endpoint2: Validating a license key
  - This API endpoint must accept:
    - The full name of the end-user
    - The license key they wish to validate
  - This API must return
    - A HTTP 204 response on successful license key validation
    - A HTTP 404 response if the license key validation fails

## Testing

### Licence generator endpoint successful requests:
- GET http://localhost:8080/licenceapi/getLicenceKey?userFirstName=Bob&userLastName=Smith&softwarePackageName=SOFTWARE_B 
  auth: xhGpQ7IuIP1fO1CJ8EbOcDRqXjUcDVBvvjKXWRzxwak=
- GET http://localhost:8080/licenceapi/getLicenceKey?userFirstName=Fred&userLastName=Doe&softwarePackageName=SOFTWARE_B
  Accept: application/json
  auth: +DHYHc7rdMXeW1rQ9yiwnNslVLoJveRO/lXKER2Yq+Q=


### Licence validator endpoint successful request:
- GET http://localhost:8080/licenceapi/validateLicenceKey?userFirstName=Fred&userLastName=Doe
  auth: CBG0TZyssuFhKarrm8xv8MRvwwjB7XOdtBvSZcdvCyc=B

- GET http://localhost:8080/licenceapi/validateLicenceKey?userFirstName=Bob&userLastName=Smith
  auth: Mz2iywrIGlmgAkFyq60a9DIg85zdNphF2IRQp2SDfns=B

