# Home-Prediction-Bot
A bot based on microservices architecture featuring batch processing, data streaming, load balancing, API key and IP based security to handle large datasets, developed using Spring Boot

1. There are 2 microservices majorly that are "home-price-prediction-bot-data" and "home-price-prediction-bot-query". The former service does a job of inserting CSV dataset into a SQL database and providing required endpoints for API usecases restricted to the latter service and the system it is working on, and retreival of data. 

2. The "home-price-prediction-bot-query" is second major service which provides API key secured public endpoints tied to the usecases defined above.

3. There are two supporting services that are "home-price-prediction-bot-service-registry" and "home-price-prediction-bot-gateway". The former service is a trafic detector and provides comprehensive information about the status of services connected to it, generally for load balancing.

4. The "home-price-prediction-bot-gateway" is a gateway/global routing service which encapsulates the route details for the services mentioned in point 1 and 2.

5. To put CSV dataset into databse, Spring Batch module has been used. Both synchronous and asynchronous methods have been provided for inserting scalably.

6. The endpoints stream the data to an output source(Web Browser, screen etc.) to ensure resource consumption is optimised and avoid fetching entire data into memory.

7. From the security POV, the service described in point 1 is IP restricted to access from the service mentioned in point 2 and the system it has been deployed on, using Spring Security module. The service mentioned in point 4 is API key restricted by using Global Filters so that no session is created between any of the major services before authorization.

8. To validate API keys recieved from user, a method has been defined which loads registered API keys in memory after intiallization of the service mentioned in point 4.

9. "home-price-prediction-bot-data" service uses Spring JDBC and Java 8 Streams for scalable approach towards databse operations. "home-price-prediction-bot-query" service uses Web-Flux and asynchronous & load balanced REST tempelate for non-blocking approach towards utilization of endpoints of the former service.

10. To consume the services, the application should be initiallized in the order of, "home-price-prediction-bot-service-registry" (Port:8761) -> "home-price-prediction-bot-data" (Port:8082) -> "home-price-prediction-bot-query" (Port:8081) -> "home-price-prediction-bot-gateway" (Port:8083).

11. At current due to monetory/physical limitations this microservices is hosted on a local server. Thus latency and downtimes are inevitable. Please let me know if the service/s is/are down before testing.

12. Enpoints defined in "home-price-prediction-bot-data",  
	a. "/job", GET, private, to batch insert CSV into DB, please define DB and file location when the service is deployed on a new system, response time: 1.5-2secs/5000 records  
	b. "/all", GET, partner("home-price-prediction-bot-query" service), fetch all the records from DB, modify a field and stream the data to output source, response time: 1-2.5secs  
	c. "/budgetHomes?min=xx&max=xx", GET, partner("home-price-prediction-bot-query" service), fetch all the records from DB in the range of float min & float max for a particular field, response time: 250ms-550ms  
	d. "/sqftHomes?minSqft=xx", GET, partner("home-price-prediction-bot-query" service), fetch all the records from DB greater than float minSqft for a particular field, response time:  250ms-550ms  
	e. "/ageHomes?year=xx", GET, partner("home-price-prediction-bot-query" service), fetch all the records from DB greater than int year for a particular field, response time:
	   250ms-550ms

13. Enpoints defined in "home-price-prediction-bot-query",  
	a. "/all", GET, secured public, fetch all the records from DB, modify a field and stream the data to output source, response time: 1-2.5secs  
	b. "/budgetHomes", POST, secured public, fetch all the records from DB in the range of float min & float max for a particular field, response time: 250ms-550ms  
		POST format: {
        				"min":float_number,
					"max":float_number
				 }  
	c. "/minSqft", POST, secured publice, fetch all the records from DB greater than float minSqft for a particular field, response time: 250ms-550ms  
		POST format: {
        				"minSqft":float_number
				 }  
	d. "/ageHomes", POST, secured public, fetch all the records from DB greater than int year for a particular field, response time: 250ms-550ms  
		POST format: {
        				"year":int_number
				 }  

14. All the responses for endpoints excluding "/job" will be in JSON and they will contain all the fields and its data from DB, mapped 1 to 1 to CSV.

15. These microservices are accesible via http://localhost:8083

16. To pass the authentication, add this API key, "7a8a2508-04fd-49c2-bbf8-74fb33abee5b" in the request header and give the header name as "apiKey".
