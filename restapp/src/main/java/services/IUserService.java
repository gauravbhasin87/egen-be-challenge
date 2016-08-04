package services;

import java.util.UUID;

import models.User;
/*
 * Interface for user service
 */
public interface IUserService {
	public String DBcreateUser(String user);
	public String DBgetAllUsers();
	public String DBupdateUser(String User);
}
