package repositories;

import java.util.List;
import java.util.UUID;

import models.User;

public interface IUserRepository {
	public String create(User user);
	public List<User> getAll();
	public User update(User user);
}
