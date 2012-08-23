package de.hliebau.tracktivity.persistence;

import java.util.List;

import de.hliebau.tracktivity.domain.User;

public interface UserDao extends CommonDao<User> {

	public User findByUsername(String username);

	public List<User> getUserList();

}
