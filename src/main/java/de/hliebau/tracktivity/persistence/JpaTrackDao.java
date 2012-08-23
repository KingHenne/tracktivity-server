package de.hliebau.tracktivity.persistence;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import de.hliebau.tracktivity.domain.Track;

@Repository("trackDao")
public class JpaTrackDao extends AbstractJpaDao<Track> implements TrackDao {

	public List<Track> getRecentTracks(int count) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Track> criteriaQuery = criteriaBuilder.createQuery(Track.class);
		Root<Track> from = criteriaQuery.from(Track.class);
		CriteriaQuery<Track> select = criteriaQuery.select(from);
		TypedQuery<Track> typedQuery = entityManager.createQuery(select).setMaxResults(count);
		return typedQuery.getResultList();
	}

}
