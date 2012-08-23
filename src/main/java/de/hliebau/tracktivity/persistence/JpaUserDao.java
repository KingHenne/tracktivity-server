package de.hliebau.tracktivity.persistence;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import de.hliebau.tracktivity.domain.User;
import de.hliebau.tracktivity.domain.User_;

@Repository("userDao")
public class JpaUserDao extends AbstractJpaDao<User> implements UserDao {

	@Override
	public User findByUsername(String username) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
		Root<User> from = criteriaQuery.from(User.class);
		CriteriaQuery<User> select = criteriaQuery.where(criteriaBuilder.like(
				criteriaBuilder.lower(from.get(User_.username)), username.toLowerCase()));
		try {
			return entityManager.createQuery(select).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public List<User> getUserList() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
		Root<User> from = criteriaQuery.from(User.class);
		CriteriaQuery<User> select = criteriaQuery.select(from).orderBy(//
				criteriaBuilder.asc(from.get(User_.lastname)),//
				criteriaBuilder.asc(from.get(User_.firstname)));
		return entityManager.createQuery(select).getResultList();
	}

}
