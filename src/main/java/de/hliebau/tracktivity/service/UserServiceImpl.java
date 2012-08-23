package de.hliebau.tracktivity.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.hliebau.tracktivity.domain.User;
import de.hliebau.tracktivity.persistence.UserDao;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	@Override
	@Transactional
	public void createUser(User user) {
		if (userExists(user.getUsername())) {
			throw new DataIntegrityViolationException("A user with this user name (case-insensitive!) exists.");
		}
		userDao.save(user);
	}

	@Override
	@Transactional
	public void deleteUser(User user) {
		userDao.delete(user);
	}

	@Override
	@Transactional(readOnly = true)
	public List<User> getAllUsers() {
		return userDao.getUserList();
	}

	@Override
	@Transactional(readOnly = true)
	public long getUserCount() {
		return userDao.getCount();
	}

	@Override
	@Transactional(readOnly = true)
	public User retrieveUser(long id) {
		return userDao.findById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public User retrieveUser(String username) {
		return userDao.findByUsername(username);
	}

	@Override
	@Transactional
	public User updateUser(User user) {
		return userDao.update(user);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean userExists(String username) {
		return (userDao.findByUsername(username) != null);
	}

}
