package repositories;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import models.Address;
import models.Company;
import models.User;
/*
 * UserMongoRepository performs CRUD operation on MongoDB. It takes the User objects and save them 
 * as documents in MongoDB. It handles the responsibility of converting documents to POJO and vice-versa.
 */
public class UserMongoRepository implements IUserRepository{

	private MongoClient mongoClient;
	private MongoDatabase db;
	private MongoCollection<Document> userCollection;
	private static final String COLLECTION = "users";
	
	//inject databse details
	public UserMongoRepository(MongoClient mongoClient, String database){
		this.mongoClient = mongoClient;
		db = mongoClient.getDatabase(database);
		userCollection = db.getCollection(COLLECTION);
	}
	
/***************************************************************************************************
*Creates user record in mongoDB
*@param User object to be created
*@return id of create user in backing datastore or error message that user already exists
*			with the given id 
****************************************************************************************************/	
	@Override
	public String create(User user) {
		
		User checkUser = findById(user.getId());
		if(checkUser == null){
			Document dbuser = createUserDocument(user);
			userCollection.insertOne(dbuser);
			return dbuser.getString("id");
		}
		else{
			return null;
		}
	}
	
/***************************************************************************************************
*Updates user record in mongoDB
*@param User object needed to be Updated
*@return id of create user in mongodb or null if user id is not found 
****************************************************************************************************/		
	@Override
	public User update(User user) {
		User checkUser = findById(user.getId());
		if(checkUser == null){
			return null;
		}
		else{
			Bson filter = new Document("id", user.getId());
			userCollection.replaceOne(filter, createUserDocument(user));
			return user;
		}
	}
	
/***************************************************************************************************
*Fetches all the documents in user collection in the database.
*@param void
*@return List of User objects in backing datastore
****************************************************************************************************/	
	@Override
	public List<User> getAll() {
		// TODO Auto-generated method stub
		List<User> list = new ArrayList<>();
		FindIterable<Document> itr = userCollection.find();
		MongoCursor<Document> mongoItr = itr.iterator();
		while(mongoItr.hasNext()){
			Document doc = mongoItr.next();
			User newUser = createUserPOJOFromDocument(doc);
			list.add(newUser);
		}
		return list;
	}

/***************************************************************************************************
*Finds user document with the given id.
*@param String id of the user
*@return User object if found, otherwise null
****************************************************************************************************/	
	public User findById(String id){
		FindIterable<Document> itr = userCollection.find(new Document("id", id));
		if(itr !=  null){
			for(Document doc : itr){
				return createUserPOJOFromDocument(doc);
			}
		}
		
		return null;
	}
	
/***************************************************************************************************
*Create mongodb document from User object.
*@param User object to be serialized in to mongodb
*@return MongoDb Document
****************************************************************************************************/	
	public static Document createUserDocument(User user){
		Document dbuser = new Document();
		String id = UUID.randomUUID().toString();
		dbuser.append("id", user.getId());
		dbuser.append("firstName", user.getFirstName());
		dbuser.append("lastName", user.getLastName());
		dbuser.append("email", user.getEmail());
		
		BasicDBObject addr = createAddressDocument(user.getAddress());
		dbuser.append("address", addr);
		
	    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	    //get current date time with Date()
	    Date date = new Date();
	    System.out.println(dateFormat.format(date));
		dbuser.append("dateCreated", dateFormat.format(date));
		
		BasicDBObject cmpny = createCompanyDocument(user.getCompany());
		dbuser.append("company", cmpny);
		
		dbuser.append("profilePic", user.getProfilePic());
		return dbuser;
	}
	
/***************************************************************************************************
*Creates BasicDbObject from Address object.
*@param Address Model object
*@return BasicDBObject
****************************************************************************************************/	
	
	public static BasicDBObject createAddressDocument(Address address){
		BasicDBObject addr = new BasicDBObject();
		addr.append("street", address.getStreet());
		addr.append("city", address.getCity());
		addr.append("zip", address.getZip());
		addr.append("state", address.getState());
		addr.append("country", address.getCountry());
		return addr;
	}
/***************************************************************************************************
*Creates BasicDbObject from Company object.
*@param Company Model object
*@return BasicDBObject
****************************************************************************************************/	
			
	public static BasicDBObject createCompanyDocument(Company company){
		BasicDBObject cmpny = new BasicDBObject();
		cmpny.append("name", company.getName());
		cmpny.append("website", company.getWebsite());
		return cmpny;
	}

/***************************************************************************************************
*Creates User POJO from User Document object.
*@param MongoDb Document Object 
*@return User POJO
****************************************************************************************************/	
		
	public static User createUserPOJOFromDocument(Document userDocument){
		User newUser = new User();
		newUser.setId(userDocument.getString("id"));
		newUser.setFirstName(userDocument.getString("firstName"));
		newUser.setLastName(userDocument.getString("lastName"));
		newUser.setEmail(userDocument.getString("email"));
		newUser.setDateCreated(userDocument.getString("dateCreated"));
		Address addr = new Address();
		Document addrDoc = (Document) userDocument.get("address");
		addr.setStreet(addrDoc.getString("street"));
		addr.setCity(addrDoc.getString("city"));
		addr.setState(addrDoc.getString("state"));
		addr.setZip(addrDoc.getString("zip"));
		addr.setCountry(addrDoc.getString("country"));
		
		newUser.setAddress(addr);
		
		Company cmpny = new Company();
		Document cmpnyDoc = (Document) userDocument.get("company");
		cmpny.setName(cmpnyDoc.getString("name"));
		cmpny.setWebsite(cmpnyDoc.getString("website"));
		
		newUser.setCompany(cmpny);
		newUser.setProfilePic(userDocument.getString("profilePic"));
		return newUser;
	}
	
}
