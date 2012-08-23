package de.hliebau.tracktivity.persistence;

import java.util.List;

import de.hliebau.tracktivity.domain.Activity;
import de.hliebau.tracktivity.domain.User;

public interface ActivityDao extends CommonDao<Activity> {

	public List<Activity> findByUser(User user);

}
