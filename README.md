# Using

* Configure H2
```
jdbc.properties
```
The H2 used as server (not embedded)

use your configuration instead this: 
```
spring.datasource.url=jdbc:h2:tcp://localhost/C:\\Users\\ilyai\\H2\\courseSC37\\spring.datasource;

spring.datasource.username=
spring.datasource.password=
spring.datasource.driverClassName=org.h2.Driver
```

* Build project
```
$ mvn clean package
```

* Tests
```
$ mvn test
```

* Run with Spring-Shell
```
$ mvn clean package
$ java -jar ./target/course-SC-37-1.0-SNAPSHOT-jar-with-dependencies.jar
```
use tab for show available commands

### Shell interface. 

```$xslt

getAuditoriums

getAuditorium --auditorium_id

assignAuditorium --auditorium_id  --date_time  --event_id

getAvailableSeats

getVipSeats

getEvents

getEventById --id

getVipSeatsInAuditorium --auditorium_id

getAvailableSeats --date_time  --event_id

getUserList

getUserByEmail --email

addUser --email --firstName --lastName

buyTicket --date_time  --email --event_id  --seats

getPurchasedTicketsForEvent --date_time --email --event_id

getCounterMap

getCounter --key

```

### Examples
#### 1
```
> getAuditoriums
```
Output:
```
Auditorium 'Blue Hall' with 90 seats (ID: 2)
-------
Auditorium 'Red Hall' with 50 seats (ID: 1)
-------
Auditorium 'Diamond Hall' with 25 seats (ID: 3)

```
#### 2
```$xslt
> buyTicket --event_id 1 --date_time "2018-04-29 12:30:00" --seats "28,29" --email "j.smith@test.com"
```
Output:
```$xslt
Requested seats: '28, 29' for event: Event 'Harry Potter and The Philosopher's Stone' with HIGH rating; Price: $29.99 (ID: 1)
    Schedule:
          2018-04-29 10:00:00. Auditorium: Red Hall
          2018-04-29 12:30:00. Auditorium: Blue Hall
          2018-04-29 15:30:00. Auditorium: Blue Hall
To auditorium: Auditorium 'Blue Hall' with 90 seats (ID: 2)
Tickets count: 2

======================
Ticket for data: 2018-04-29, at time: 12:30:00
John Smith (j.smith@test.com, ID: 1)
Event 'Harry Potter and The Philosopher's Stone' with HIGH rating; Price: $29.99 (ID: 1)
Seat: 28
-------

Ticket for data: 2018-04-29, at time: 12:30:00
John Smith (j.smith@test.com, ID: 1)
Event 'Harry Potter and The Philosopher's Stone' with HIGH rating; Price: $29.99 (ID: 1)
Seat: 29
```

#### 2

```
> getCounterMap
```

Output:
```
Counter Map:
BookingService.bookTickets(1): 11
DiscountService.getDiscountByBirthday: 26
DiscountService.getDiscountByCount: 26
EventService.getByName: 18
```

* NOTE!
  * the Counter count all access to method. If you bought 2 tickets then in counter it will be 3 access, because 1 addition for validate. 