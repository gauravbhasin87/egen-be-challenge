package services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.UUID;

import com.egen.restapp.ResponseObject;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import models.User;

import repositories.IUserRepository;
/*
 * UserMongoService is a concrete class to interact with the backing MongoDB.
 * It holds the reference to UserRepository for performing CRUD on user collection in MongoDB.
 * The main purpose of this class it convert json to-and-from User POJO and pass back-and-forth to
 * UserRepository 
 */
public class UserMongoService implements IUserService {

	private static final int HTTP_BAD_REQUEST = 400;
	
	IUserRepository userRepository;
	ObjectMapper mapper;
	
	//inject user repository
	public UserMongoService(IUserRepository userRepository){
		this.userRepository = userRepository;
		mapper = new ObjectMapper();
	}

	
/***************************************************************************************************
*Service to create user in backing data store
*@param String
*@return id of create user in backing datastore
****************************************************************************************************/	
	@Override
	public String DBcreateUser(String userJson) {
		User newUser = (User) jsonToUserPOJO(userJson);
		String id = userRepository.create(newUser);
		return id;
	}

	
/***************************************************************************************************
*Get all the users from data store
*@param void
*@return Json array of users
****************************************************************************************************/	
	@Override
	public String DBgetAllUsers() {
		// TODO Auto-generated method stub
		List<User> list = userRepository.getAll();
		return dataToJson(list);
	}
	
/***************************************************************************************************
*Service to update the user in backing data store
*@param String 
*@return response in json format
****************************************************************************************************/	

	@Override
	public String DBupdateUser(String userJson) {
		User user = (User) jsonToUserPOJO(userJson);
		User updatedUser = userRepository.update(user);
		if(updatedUser != null){
			return dataToJson(updatedUser);
		}
		else{
			return createError("404","User not found and not updated");
		}
		
	}

	
/***************************************************************************************************
*Convert json to User POJO
*@param String
*@return Object
****************************************************************************************************/	
	public static Object jsonToUserPOJO(String body){
		try{
			ObjectMapper mapper = new ObjectMapper();
			User user = mapper.readValue(body, User.class);
			return user;
		}
		catch (JsonParseException jpe) {
            return createError("400","Error in creating user");
        }
		catch (NullPointerException jpe) {
            return createError("400","Error in JSON");
        }
		catch (Exception jpe) {
            return createError(Integer.toString(HTTP_BAD_REQUEST),"Error :" + jpe.getMessage());
        }
	}
	
	
	
/***************************************************************************************************
*Convert serializable object of any type to json format
*@param Object
*@return String in json format
****************************************************************************************************/	
	public static String dataToJson(Object data) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            //for pretty printing JSON
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
            return mapper.writeValueAsString(data);
        } catch (IOException e){
        	System.out.println("Error");
            return createError("404","Jackson: Error converting object into json format");
        }
    }
	
/***************************************************************************************************
*Create erro in json format
*@param error code
*@param error message
*@return Error String in json format
****************************************************************************************************/	
	public static String createError(String errorCode, String error){
		ResponseObject obj = new ResponseObject();
		obj.errorCode = errorCode;
		obj.errorData = error;
		return dataToJson(obj);
	}

}
