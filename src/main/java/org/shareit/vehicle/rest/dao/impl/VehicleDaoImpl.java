package org.shareit.vehicle.rest.dao.impl;

import org.shareit.vehicle.rest.dao.VehicleDao;
import org.shareit.vehicle.rest.dao.VehicleEntity;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VehicleDaoImpl implements VehicleDao {
	
    private HibernateTemplate template;

    public void setTemplate(HibernateTemplate template) {
        this.template = template;
    }

    public void deletePodcastById(Long id) {
        template.delete(get(id));
    }
    public VehicleEntity get(Long id) {
        VehicleEntity entity = template.get(VehicleEntity.class, id);

        if (entity == null) {

            throw new ObjectRetrievalFailureException(VehicleEntity.class, id);
        }
        return entity;
    }
    public VehicleEntity save(VehicleEntity object) {
        template.save(object);
        return object;
    }

	public List<VehicleEntity> getPodcasts(String orderByInsertionDate) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<VehicleEntity> getRecentPodcasts(int numberOfDaysToLookBack) {
		// TODO Auto-generated method stub
		return null;
	}

	public VehicleEntity getPodcastById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	public VehicleEntity getPodcastByFeed(String feed) {
		// TODO Auto-generated method stub
		return null;
	}

	public Long createPodcast(VehicleEntity podcast) {
		// TODO Auto-generated method stub
		return null;
	}

	public void updatePodcast(VehicleEntity podcast) {
		// TODO Auto-generated method stub
		
	}

	public void deletePodcasts() {
		// TODO Auto-generated method stub
		
	}

	public List<VehicleEntity> getLegacyPodcasts() {
		// TODO Auto-generated method stub
		return null;
	}

	public VehicleEntity getLegacyPodcastById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	/*@PersistenceContext(unitName="demoRestPersistence")
	private EntityManager entityManager;

	@PersistenceContext(unitName="demoRestPersistenceLegacy")
	private EntityManager entityManagerLegacy;
	
	public List<VehicleEntity> getPodcasts(String orderByInsertionDate) {
		String sqlString = null;
		if(orderByInsertionDate != null){
			sqlString = "SELECT p FROM VehicleEntity p" + " ORDER BY p.insertionDate " + orderByInsertionDate;
		} else {
			sqlString = "SELECT p FROM VehicleEntity p";
		}		 
		TypedQuery<VehicleEntity> query = entityManager.createQuery(sqlString, VehicleEntity.class);		

		return query.getResultList();
	}

	public List<VehicleEntity> getRecentPodcasts(int numberOfDaysToLookBack) {
		
		Calendar calendar = new GregorianCalendar();
		calendar.setTimeZone(TimeZone.getTimeZone("UTC+1"));//Munich time 
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, -numberOfDaysToLookBack);//substract the number of days to look back 
		Date dateToLookBackAfter = calendar.getTime();
		
		String qlString = "SELECT p FROM VehicleEntity p where p.insertionDate > :dateToLookBackAfter ORDER BY p.insertionDate DESC";
		TypedQuery<VehicleEntity> query = entityManager.createQuery(qlString, VehicleEntity.class);		
		query.setParameter("dateToLookBackAfter", dateToLookBackAfter, TemporalType.DATE);

		return query.getResultList();
	}
	
	public VehicleEntity getPodcastById(Long id) {
		
		try {
			String qlString = "SELECT p FROM VehicleEntity p WHERE p.id = ?1";
			TypedQuery<VehicleEntity> query = entityManager.createQuery(qlString, VehicleEntity.class);		
			query.setParameter(1, id);

			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public VehicleEntity getPodcastByFeed(String feed) {
		
		try {
			String qlString = "SELECT p FROM VehicleEntity p WHERE p.feed = ?1";
			TypedQuery<VehicleEntity> query = entityManager.createQuery(qlString, VehicleEntity.class);		
			query.setParameter(1, feed);

			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	

	public void deletePodcastById(Long id) {
		
		VehicleEntity podcast = entityManager.find(VehicleEntity.class, id);
		entityManager.remove(podcast);
		
	}

	public Long createPodcast(VehicleEntity podcast) {
		
		podcast.setInsertionDate(new Date());
		entityManager.merge(podcast);
		entityManager.flush();//force insert to receive the id of the podcast
		
		return podcast.getId();
	}

	public void updatePodcast(VehicleEntity podcast) {
		//TODO think about partial update and full update 
		entityManager.merge(podcast);		
	}
	
	public void deletePodcasts() {
		Query query = entityManager.createNativeQuery("TRUNCATE TABLE podcasts");		
		query.executeUpdate();
	}

	public List<VehicleEntity> getLegacyPodcasts() {
		
		String qlString = "SELECT p FROM VehicleEntity p";
		TypedQuery<VehicleEntity> query = entityManagerLegacy.createQuery(qlString, VehicleEntity.class);		

		return query.getResultList();
	}

	public VehicleEntity getLegacyPodcastById(Long id) {
		try {
			String qlString = "SELECT p FROM VehicleEntity p WHERE p.id = ?1";
			TypedQuery<VehicleEntity> query = entityManagerLegacy.createQuery(qlString, VehicleEntity.class);		
			query.setParameter(1, id);

			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}*/

}
