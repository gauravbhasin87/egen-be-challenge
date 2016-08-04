package com.egen.restapp;

import com.mongodb.MongoClient;

import controllers.UserController;
import repositories.IUserRepository;
import repositories.UserMongoRepository;
import services.IUserService;
import services.UserMongoService;

import static spark.Spark.*;

//import org.apache.log4j.Logger;

/**
 * Application Bootstrap
 * Spark application start from here and deployed on Jetty server on port 4567.
 * This class initializes all the beans and sets the routes for each url.
 * This class imitates the behavior of Spring container and inject the required dependencies 
 *
 */
public class App 
{
	//static final Logger logger = Logger.getLogger(App.class);
    public static void main( String[] args )
    {
    	//initializing dependencies
        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
        IUserRepository userRepositoryBean = new UserMongoRepository(mongoClient, "userDB");
        IUserService userServiceBean = new UserMongoService(userRepositoryBean);
        UserController userControllerBean = new UserController(userServiceBean);
        
        //Routes
        post("/addUser", userControllerBean.DBcreateNewUser );
        get("/allUsers", userControllerBean.DBgetAllUser);
        put("/updateUser", userControllerBean.DBupdateUser);
        
    }
    
}
