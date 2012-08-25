package de.hliebau.tracktivity.persistence;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import de.hliebau.tracktivity.domain.Activity;
import de.hliebau.tracktivity.domain.Activity_;
import de.hliebau.tracktivity.domain.User;

@Repository("activityDao")
public class JpaActivityDao extends AbstractJpaDao<Activity> implements ActivityDao {

	@Override
	public List<Activity> getRecentActivities(int count) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Activity> q = cb.createQuery(Activity.class);
		Root<Activity> activity = q.from(Activity.class);
		CriteriaQuery<Activity> select = q.select(activity).orderBy(cb.desc(activity.get(Activity_.utcStart)));
		TypedQuery<Activity> typedQuery = entityManager.createQuery(select).setMaxResults(count);
		return typedQuery.getResultList();
	}

	@Override
	public List<Activity> getUserActivities(User user) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Activity> q = cb.createQuery(Activity.class);
		Root<Activity> activity = q.from(Activity.class);
		CriteriaQuery<Activity> select = q.select(activity).where(cb.equal(activity.get(Activity_.user), user))
				.orderBy(cb.desc(activity.get(Activity_.utcStart)));
		TypedQuery<Activity> typedQuery = entityManager.createQuery(select);
		return typedQuery.getResultList();
	}

}
