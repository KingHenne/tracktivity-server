package de.hliebau.tracktivity.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.hliebau.tracktivity.domain.Activity;
import de.hliebau.tracktivity.domain.ActivityType;
import de.hliebau.tracktivity.domain.User;
import de.hliebau.tracktivity.persistence.ActivityDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/application.xml" })
@Transactional
public class UserServiceTest {

	@Autowired
	private ActivityDao activityDao;

	@PersistenceContext
	protected EntityManager entityManager;

	private final String testUsername = "testUser";

	@Autowired
	private UserService userService;

	protected User addTestUserWithActivity() {
		User newUser = new User(testUsername);
		newUser.addActivity(new Activity(ActivityType.HIKING));
		userService.createUser(newUser);
		return newUser;
	}

	@Test
	public void testAddAndDeleteNewUser() {
		User testUser = userService.retrieveUser(testUsername, false);
		if (testUser != null) {
			userService.deleteUser(testUser);
		}

		User newUser = new User(testUsername);
		long countBefore = userService.getUserCount();
		userService.createUser(newUser);
		long countAfter = userService.getUserCount();
		Assert.assertEquals("The user count has not been increased by 1.", countBefore + 1, countAfter);

		countBefore = countAfter;
		userService.deleteUser(newUser);
		countAfter = userService.getUserCount();
		Assert.assertEquals("The user count has not been decreased by 1.", countBefore - 1, countAfter);
	}

	@Test
	public void testAddUserWithActivities() {
		User testUser = addTestUserWithActivity();
		Assert.assertNotNull(testUser);
		Assert.assertEquals(testUser.getActivities().size(), 1);
		Activity userActivity = testUser.getActivities().get(0);
		Assert.assertNotNull("The user activity does not exist.", activityDao.findById(userActivity.getId()));
	}

	@Test
	public void testDeleteUserWithActivities() {
		User testUser = addTestUserWithActivity();
		Assert.assertTrue(testUser.getActivities().size() > 0);
		Activity userActivity = testUser.getActivities().get(0);
		userService.deleteUser(testUser);
		Assert.assertNull("The user activity still exists.", activityDao.findById(userActivity.getId()));
	}

	@Test
	public void testFindNonexistentUser() {
		User user = userService.retrieveUser("nonexistenTestUser", false);
		Assert.assertNull("The return value was not null.", user);

		user = userService.retrieveUser(-1);
		Assert.assertNull("The return value was not null.", user);
	}

	@Test
	public void testRemoveActivityFromUser() {
		User testUser = addTestUserWithActivity();
		Activity userActivity = testUser.getActivities().get(0);
		testUser.deleteActivity(userActivity);
		Assert.assertFalse(testUser.getActivities().contains(userActivity));
		userService.updateUser(testUser);
		entityManager.flush();
		Assert.assertNull("The user activity still exists.", activityDao.findById(userActivity.getId()));
	}

	@Test
	public void testUsernameCaseInsensitivity() {
		User testUser = addTestUserWithActivity();
		User testUserUpper = userService.retrieveUser(testUser.getUsername().toUpperCase(), false);
		Assert.assertNotNull("Could not find the test user with uppercase letters.", testUserUpper);

		long countBefore = userService.getUserCount();
		User newUser = new User(testUser.getUsername().toUpperCase());
		try {
			userService.createUser(newUser);
		} catch (DataIntegrityViolationException e) {
			// this exception is expected
		} finally {
			long countAfter = userService.getUserCount();
			Assert.assertEquals("A user was added that should not have been added.", countAfter, countBefore);
		}
	}
}
