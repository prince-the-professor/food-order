# food-order

This project is a basic online food delivery system which involves basic features like order place, assign & update.

APIs are

1. Order place which returns orderId, bill Amount, discount applied, preparation time
2. Get order status 
3. Assign order to available delivery-person
4. Update order status (use by person to update order like: PICKED/DELIVERED)
5. Delivery agent status (if not available returns time remain to deliver current order)
6. Get status of each agent
7. Add and fetch items in inventory
8. Add a new agent

API flow:

1. Add item then add agent.
2. Order place -> order assign to delivery boy -> update order(PICKED/DELIVERED) -> agent available again


I coded in spring-boot framework 2.2.5 with gradle dependency management. 
For data management I used H2 database file based to persist data. I used caffeineCache with 10 minutes expiry time to access onging order faster instead of direct to DB. 


Except these I included JUnit test with mockito along with spring-boot API integration test. I have added actuator health point also.

Scope of improvement:

1. I can add queuing mechanism to assign order asynchronous.
2. I can use NoSql DB to manage order history or any other transactions for analysis purpose.
3. A feedback API should also be created.
4. There should be 10-20 second of time-out for each delivery agent to accept the order before request transferred to another person.

Along these I have added swagger file, ER diagram, Class diagram and APIs execution request-response snapshot.
