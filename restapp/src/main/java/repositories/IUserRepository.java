package repositories;

import java.util.List;

import java.util.UUID;

import models.User;

/***************************************************************************************************
*Interface for User Repository
*Different implementation of IUserReposiotry provide interaction with different databases
*One such implementation is UserMongoRepository which provide CRUD operations on MongoDB 
****************************************************************************************************/	
	
public interface IUserRepository {
	public String create(User user);
	public List<User> getAll();
	public User update(User user);
}
