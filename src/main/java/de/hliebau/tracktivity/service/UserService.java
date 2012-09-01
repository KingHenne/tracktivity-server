package de.hliebau.tracktivity.service;

import java.util.List;

import de.hliebau.tracktivity.domain.User;

public interface UserService {

	public void createUser(User user);

	public void deleteUser(User user);

	public List<User> getAllUsers();

	public long getUserCount();

	public User retrieveUser(long id);

	public User retrieveUser(String username, boolean fetchActivities);

	public User updateUser(User user);

	public boolean userExists(String username);

}
