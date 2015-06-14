package org.shareit.vehicle.rest.dao;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 
 * @author Santosh
 */

public interface VehicleDao {

    VehicleEntity get(Long id);

    VehicleEntity save(VehicleEntity object);

    public List<VehicleEntity> getPodcasts(String orderByInsertionDate);

	public List<VehicleEntity> getRecentPodcasts(int numberOfDaysToLookBack);
	
	public VehicleEntity getPodcastById(Long id);
	
	public VehicleEntity getPodcastByFeed(String feed);	

	public void deletePodcastById(Long id);

	public Long createPodcast(VehicleEntity podcast);

	public void updatePodcast(VehicleEntity podcast);

	/** removes all podcasts */
	public void deletePodcasts();

	/** 
	 * Returns all podcasts from "legacy" system
	 * @return
	 */
	public List<VehicleEntity> getLegacyPodcasts();
	
	/**
	 * Returns a "legacy" podcast given its id
	 * 
	 * @param id
	 * @return
	 */
	public VehicleEntity getLegacyPodcastById(Long id);	

}
