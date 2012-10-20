package de.hliebau.tracktivity.persistence;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import de.hliebau.tracktivity.domain.Activity;
import de.hliebau.tracktivity.domain.ActivityType;
import de.hliebau.tracktivity.domain.User;
import de.hliebau.tracktivity.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/application.xml" })
@Transactional
public class ActivityDaoTest {

	@Autowired
	private ActivityDao activityDao;

	@Autowired
	private UserService userService;

	protected User createTestUser() {
		User newUser = new User("testUserX");
		userService.createUser(newUser);
		return newUser;
	}

	@Test
	public void testAddNewActivity() {
		User testUser = createTestUser();
		long countBefore = activityDao.getCount();
		Activity newActivity = new Activity();
		newActivity.setUser(testUser);
		activityDao.save(newActivity);
		Assert.assertEquals("The activity count has not been increased by 1.", countBefore + 1, activityDao.getCount());

		// clean up
		activityDao.delete(newActivity);
		Assert.assertEquals("The activity count has not been decreased by 1.", countBefore, activityDao.getCount());
	}

	@Test
	public void testAddNewActivityWithType() {
		User testUser = createTestUser();
		long countBefore = activityDao.getCount();
		Activity newActivity = new Activity(ActivityType.CYCLING);
		newActivity.setUser(testUser);
		activityDao.save(newActivity);
		Assert.assertEquals("The activity count has not been increased by 1.", countBefore + 1, activityDao.getCount());

		// clean up
		activityDao.delete(newActivity);
		Assert.assertEquals("The activity count has not been decreased by 1.", countBefore, activityDao.getCount());
	}
}
