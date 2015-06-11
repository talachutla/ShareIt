package org.shareit.vehicle.rest.dao;

import java.util.List;

/**
 * 
 * @author Santosh
 */
public interface VehicleDao {
	
	public List<VehicleEntity> getPodcasts(String orderByInsertionDate);

	public List<VehicleEntity> getRecentPodcasts(int numberOfDaysToLookBack);
	
	/**
	 * Returns a podcast given its id
	 * 
	 * @param id
	 * @return
	 */
	public VehicleEntity getPodcastById(Long id);
	
	/**
	 * Find podcast by feed
	 * 
	 * @param feed
	 * @return the podcast with the feed specified feed or null if not existent 
	 */
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
