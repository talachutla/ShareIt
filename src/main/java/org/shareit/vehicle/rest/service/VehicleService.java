package org.shareit.vehicle.rest.service;

import java.util.List;

import org.shareit.vehicle.rest.errorhandling.AppException;
import org.shareit.vehicle.rest.errorhandling.CustomReasonPhraseException;
import org.shareit.vehicle.rest.resource.podcast.Vehicle;

/**
 * 
 * @author ama
 */
public interface VehicleService {
	
	/*
	 * ******************** Create related methods **********************
	 * */
	public Long createPodcast(Vehicle vehicle) throws AppException;
	public void createPodcasts(List<Vehicle> vehicles) throws AppException;

		
	/*
	 ******************** Read related methods ********************
	  */ 	
	/**
	 * 
	 * @param orderByInsertionDate - if set, it represents the order by criteria (ASC or DESC) for displaying podcasts
	 * @param numberDaysToLookBack - if set, it represents number of days to look back for podcasts, null 
	 * @return list with podcasts coressponding to search criterias
	 * @throws AppException
	 */
	public List<Vehicle> getPodcasts(String orderByInsertionDate, Integer numberDaysToLookBack) throws AppException;
	
	/**
	 * Returns a podcast given its id
	 * 
	 * @param id
	 * @return
	 * @throws AppException 
	 */
	public Vehicle getPodcastById(Long id) throws AppException;
	/** 
	 * Returns all podcasts from "legacy" system
	 * @return
	 */
	public List<Vehicle> getLegacyPodcasts();
	
	/**
	 * Returns a "legacy" podcast given its id
	 * 
	 * @param id
	 * @return
	 */
	public Vehicle getLegacyPodcastById(Long id);
	
	
	/*
	 * ******************** Update related methods **********************
	 * */	
	public void updateFullyPodcast(Vehicle vehicle) throws AppException;
	public void updatePartiallyPodcast(Vehicle vehicle) throws AppException;	
	
		
	/*
	 * ******************** Delete related methods **********************
	 * */
	public void deletePodcastById(Long id);
	
	/** removes all podcasts */
	public void deletePodcasts();

	/*
	 * ******************** Helper methods **********************
	 * */
	public Vehicle verifyPodcastExistenceById(Long id);
	
	/**
	 * Empty method generating a Business Exception
	 * @throws CustomReasonPhraseException
	 */
	public void generateCustomReasonPhraseException() throws CustomReasonPhraseException;

}
