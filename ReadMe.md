
User Management REST API

This is a REST api exposing Crud operations to backing MongoDB

Pre-requisited
1. Running MongoDb Instance
2. Java Spark
3. Jackson Liberary
4. MongoDb Java Driver


End Points:
1. Post  : http://localhost:4567/addUser  :  add user to the backing datastore
2. Get   : http://localhost:4567/allUsers :  fetch all the user from datastore in json array format
3. Put   : http://localhost:4567/updateUsers : update the existing user in the database


Design:

Rest Api is desinged with regards to MVC and Dependency injection design patterns.

App : Boot class
	Spark application start from here and deployed on Jetty server on port 4567. This class initializes all the beans and sets the routes for each url. It imitates the behavior of Spring container and inject the required dependencies 

User : Model Class

UserController :
	UserController acts as an intermediate between the user request and back-end service.
	
IUserService : interface for user service. This will help in testing with injecting mock user service object.

UserMongoService : UserMongoService is a concrete class to interact with the backing MongoDB.It holds the reference to UserRepository for performing CRUD on 
                   user collection in MongoDB.The main purpose of this class it convert json to-and-from User POJO and pass back-and-forth to UserRepository 
	
IUserRepository :  Interface for User Repository
					Different implementation of IUserReposiotry provide interaction with different databases. One such implementation is UserMongoRepository which provide CRUD operations on MongoDB. This will help in testing with injecting mock user repository object. 

UserMongoRepository : UserMongoRepository performs CRUD operation on MongoDB. It takes the User objects and save them 
						as documents in MongoDB. It handles the responsibility of converting MOngoDB documents to POJO and vice-versa.					

Test Cases:

1. newUserTest : takes a user in json format from a file and send HTTP POST request to url:  http://localhost:4567/addUser .
					Asserts the equality of passed and return UUID. Test File named "newUserTest.json" should be located in "src/test/resources/" relative to the project's root directory. 
				
2. userAlreadyExists : Sends an add request with already existing user in DB.

3. userNotFoundForUpdate : Test to update non-existing user in the database. Api returns 404 saying user not present.












Sample User json

{  
   "id":"1630215c-2608-44b9-aad4-9d56d8aafd4c",
   "firstName":"Dorris",
   "lastName":"Keeling",
   "email":"Darby_Leffler68@gmail.com",
   "address":{  
      "street":"193 Talon Valley",
      "city":"South Tate furt",
      "zip":"47069",
      "state":"IA",
      "country":"US"
   },
   "dateCreated":"2016-03-15T07:02:40.896Z",
   "company":{  
      "name":"Denesik Group",
      "website":"http://jodie.org"
   },
   "profilePic":"http://lorempixel.com/640/480/people"
}
