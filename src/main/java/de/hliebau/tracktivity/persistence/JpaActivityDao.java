package de.hliebau.tracktivity.persistence;

import java.util.List;

import org.springframework.stereotype.Repository;

import de.hliebau.tracktivity.domain.Activity;
import de.hliebau.tracktivity.domain.User;

@Repository("activityDao")
public class JpaActivityDao extends AbstractJpaDao<Activity> implements ActivityDao {

	public List<Activity> findByUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}

}
