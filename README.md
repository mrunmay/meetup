# Conference Room Booking API

This application exposes REST API  for booking conference rooms within the organization. The API allows users to book conference rooms for a specific time and number of people. It also provides a way to check the availability of rooms within a specified time range.

## API Endpoints

### Book a Conference Room

**Endpoint**: `POST /api/rooms/book`

**Request Body**:
```json
{
  "startTime": "HH:mm",
  "endTime": "HH:mm",
  "numberOfPeople": 5
}
```

- `startTime` and `endTime` should be in 24-hour format (e.g., "14:30").
- `numberOfPeople` is the number of people attending the meeting.

**Response**:
- If the booking is successful, it returns a 201 (Created) status with a JSON response containing the booking details.

- If there are no available rooms for the specified time and number of people, it returns a 400 (Bad Request) status with an error message.

- If the booking overlaps with maintenance time or the time intervals are not in 15-minute increments, it returns a 400 (Bad Request) status with an error message.

### Check Room Availability

**Endpoint**: `GET /api/rooms/availability`

**Query Parameters**:
- `startTime`: The start time of the time range in HH:mm format.
- `endTime`: The end time of the time range in HH:mm format.

**Response**:
- If rooms are available within the specified time range, it returns a 200 (OK) status with a JSON response containing the available rooms.

- If there are no available rooms for the specified time range, it returns a 404 (Not Found) status with an error message.

- If the time intervals are not in 15-minute increments or the time range overlaps with maintenance time, it returns a 400 (Bad Request) status with an error message.

## Error Handling

The API handles various error scenarios and provides descriptive error messages in the response.

## Tech Stack

Before running the application, make sure you have the following prerequisites installed:

- Java 11 or above
- SpringBoot 2.7.17
- Maven
- H2 in-memory database

## Running the Application

To run the Conference Room Booking REST API, follow these steps:

1. **Clone the Repository**: Start by cloning this repository to your local machine using the following command:
```   
git clone https://github.com/mrunmay/meetup.git
```
2. **Open the Project**: Open the project in your favorite Integrated Development Environment (IDE). We recommend using an IDE like IntelliJ IDEA or Eclipse for Java development.

3. **Configure the Database**: The API uses an in-memory database (e.g., H2) by default. You can configure the database connection by editing the `application.properties` file. Set the database URL, username, and password as needed.

```properties
spring.datasource.url=jdbc:h2:mem:conference-room-db
spring.datasource.username=your-username
spring.datasource.password=your-password
```
4. **Build the Project**:Use Maven to build the project by running the following command in the project's root directory:
```
mvn clean install
```
5. **Run the Application the Project**:You can run the application using the following command:
```
mvn spring-boot:run
```
The API will start, and you can access it at http://localhost:8080

## Testing

Unit tests are provided for the service class. You can run the tests to ensure the functionality of the application.

**POST**
```shell
curl --location 'localhost:8080/api/rooms/book' \
--header 'Content-Type: application/json' \
--data '{
   "numberOfPeople":20,
   "startTime": "09:30",
   "endTime" : "09:45"
}'
```
**GET**
```shell
curl --location 'localhost:8080/api/rooms/availability?startTime=11:00&endTime=12:30'
```

## Conclusion

The Conference Room Booking REST API is a convenient solution for managing and booking conference rooms within your organization. 
With the ability to check room availability and handle various error scenarios, this API simplifies the booking process.

By following the provided prerequisites and running the application, you can integrate this API into your organization's workflow, improving the efficiency of room booking and ensuring a smooth scheduling process.

