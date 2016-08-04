package controllers;

import java.io.IOException;
import java.io.StringWriter;

import com.egen.restapp.ResponseObject;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import models.User;
import services.IUserService;
import spark.Request;
import spark.Response;
import spark.Route;

/*
 * UserController acts as an intermediate between the user request and back-end service. 
 */
public class UserController {
	
	
	IUserService userService;

	//inject desired user service
	public UserController(IUserService userService){
		this.userService = userService;
	}
	
	//Route field for creating user in the database using user service
	public Route DBcreateNewUser = (Request request, Response response)->{
		String id = userService.DBcreateUser(request.body());
		response.type("application/json");
		if(id != null)
			return id;
		else{
			response.status(409);
			return "User already exists with given id";
		}
	};
	//Route field for fetching all users in the database using user service
	public Route DBgetAllUser = (Request request, Response response)->{
		String jsonUserArray = userService.DBgetAllUsers();
		response.type("application/json");
		return jsonUserArray;
	};
	////Route field for updating user in the database using user service
	public Route DBupdateUser = (Request request, Response response)->{
		String jsonUpdatedUser = userService.DBupdateUser(request.body());
		response.type("application/json");
		if(jsonUpdatedUser != null){
			return jsonUpdatedUser;
		}
		else{
			response.status(404);
			return "User not found";
		}
		
	};
}
