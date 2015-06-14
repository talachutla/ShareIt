package org.shareit.vehicle.rest.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.shareit.vehicle.rest.dao.VehicleDao;
import org.shareit.vehicle.rest.dao.VehicleEntity;
import org.shareit.vehicle.rest.errorhandling.AppException;
import org.shareit.vehicle.rest.errorhandling.CustomReasonPhraseException;
import org.shareit.vehicle.rest.filters.AppConstants;
import org.shareit.vehicle.rest.helpers.NullAwareBeanUtilsBean;
import org.shareit.vehicle.rest.resource.podcast.Vehicle;
import org.shareit.vehicle.rest.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional("transactionManager")

public class VehicleServiceImpl implements VehicleService {

	@Autowired(required = true)
	VehicleDao vehicleDao;

    public VehicleEntity get(Long id) {
        return vehicleDao.get(id);
    }

    public VehicleEntity save(VehicleEntity object) {
        return vehicleDao.save(object);
    }
    /********************* Create related methods implementation ***********************/
	
	public Long createPodcast(Vehicle vehicle) throws AppException {
		
		validateInputForCreation(vehicle);
		
		//verify existence of resource in the db (feed must be unique)
		VehicleEntity podcastByFeed = vehicleDao.getPodcastByFeed(vehicle.getFeed());
		if(podcastByFeed != null){
			throw new AppException(Response.Status.CONFLICT.getStatusCode(), 409, "Vehicle with feed already existing in the database with the id " + podcastByFeed.getId(),
					"Please verify that the feed and title are properly generated", AppConstants.BLOG_POST_URL);
		}
		
		return vehicleDao.createPodcast(new VehicleEntity(vehicle));
	}

	private void validateInputForCreation(Vehicle vehicle) throws AppException {
		if(vehicle.getFeed() == null){
			throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(), 400, "Provided data not sufficient for insertion",
					"Please verify that the feed is properly generated/set", AppConstants.BLOG_POST_URL);
		}
		if(vehicle.getTitle() == null){
			throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(), 400, "Provided data not sufficient for insertion",
					"Please verify that the title is properly generated/set", AppConstants.BLOG_POST_URL);
		}
		//etc...
	}
	
	@Transactional("transactionManager")
	public void createPodcasts(List<Vehicle> vehicles) throws AppException {
		for (Vehicle vehicle : vehicles) {
			createPodcast(vehicle);
		}		
	}	
	
	
	 // ******************** Read related methods implementation **********************		
	public List<Vehicle> getPodcasts(String orderByInsertionDate, Integer numberDaysToLookBack) throws AppException {
		
		//verify optional parameter numberDaysToLookBack first 
		if(numberDaysToLookBack!=null){
			List<VehicleEntity> recentPodcasts = vehicleDao.getRecentPodcasts(numberDaysToLookBack);			
			return getPodcastsFromEntities(recentPodcasts);			
		}
		
		if(isOrderByInsertionDateParameterValid(orderByInsertionDate)){
			throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(), 400, "Please set either ASC or DESC for the orderByInsertionDate parameter", null , AppConstants.BLOG_POST_URL);
		}			
		List<VehicleEntity> podcasts = vehicleDao.getPodcasts(orderByInsertionDate);
		
		return getPodcastsFromEntities(podcasts);
	}

	private boolean isOrderByInsertionDateParameterValid(
			String orderByInsertionDate) {
		return orderByInsertionDate!=null 
				&& !("ASC".equalsIgnoreCase(orderByInsertionDate) || "DESC".equalsIgnoreCase(orderByInsertionDate));
	}
	
	public Vehicle getPodcastById(Long id) throws AppException {		
		VehicleEntity podcastById = vehicleDao.getPodcastById(id);
		if(podcastById == null){
			throw new AppException(Response.Status.NOT_FOUND.getStatusCode(), 
					404, 
					"The podcast you requested with id " + id + " was not found in the database",
					"Verify the existence of the podcast with the id " + id + " in the database",
					AppConstants.BLOG_POST_URL);			
		}
		
		return new Vehicle(vehicleDao.getPodcastById(id));
	}	

	private List<Vehicle> getPodcastsFromEntities(List<VehicleEntity> vehicleEntities) {
		List<Vehicle> response = new ArrayList<Vehicle>();
		for(VehicleEntity vehicleEntity : vehicleEntities){
			response.add(new Vehicle(vehicleEntity));					
		}
		
		return response;
	}

	public List<Vehicle> getRecentPodcasts(int numberOfDaysToLookBack) {
		List<VehicleEntity> recentPodcasts = vehicleDao.getRecentPodcasts(numberOfDaysToLookBack);
		
		return getPodcastsFromEntities(recentPodcasts);
	}

	public List<Vehicle> getLegacyPodcasts() {
		List<VehicleEntity> legacyPodcasts = vehicleDao.getLegacyPodcasts();
		
		return getPodcastsFromEntities(legacyPodcasts);
	}

	public Vehicle getLegacyPodcastById(Long id) {
		return new Vehicle(vehicleDao.getLegacyPodcastById(id));
	}
	
	
	/********************* UPDATE-related methods implementation ***********************/	
	@Transactional("transactionManager")
	public void updateFullyPodcast(Vehicle vehicle) throws AppException {
		//do a validation to verify FULL update with PUT
		if(isFullUpdate(vehicle)){
			throw new AppException(Response.Status.BAD_REQUEST.getStatusCode(), 
					400, 
					"Please specify all properties for Full UPDATE",
					"required properties - id, title, feed, lnkOnPodcastpedia, description" ,
					AppConstants.BLOG_POST_URL);			
		}
		
		Vehicle verifyPodcastExistenceById = verifyPodcastExistenceById(vehicle.getId());
		if(verifyPodcastExistenceById == null){
			throw new AppException(Response.Status.NOT_FOUND.getStatusCode(), 
					404, 
					"The resource you are trying to update does not exist in the database",
					"Please verify existence of data in the database for the id - " + vehicle.getId(),
					AppConstants.BLOG_POST_URL);				
		}
				
		vehicleDao.updatePodcast(new VehicleEntity(vehicle));
	}

	/**
	 * Verifies the "completeness" of podcast resource sent over the wire
	 * 
	 * @param vehicle
	 * @return
	 */
	private boolean isFullUpdate(Vehicle vehicle) {
		return vehicle.getId() == null
				|| vehicle.getFeed() == null
				|| vehicle.getLinkOnPodcastpedia() == null
				|| vehicle.getTitle() == null
				|| vehicle.getDescription() == null;
	}
	
	/********************* DELETE-related methods implementation ***********************/
	@Transactional("transactionManager")
	public void deletePodcastById(Long id) {
		vehicleDao.deletePodcastById(id);
	}
	
	@Transactional("transactionManager")	
	public void deletePodcasts() {
		vehicleDao.deletePodcasts();		
	}

	public Vehicle verifyPodcastExistenceById(Long id) {
		VehicleEntity podcastById = vehicleDao.getPodcastById(id);
		if(podcastById == null){
			return null;
		} else {
			return new Vehicle(podcastById);			
		}
	}

	@Transactional("transactionManager")
	public void updatePartiallyPodcast(Vehicle vehicle) throws AppException {
		//do a validation to verify existence of the resource		
		Vehicle verifyPodcastExistenceById = verifyPodcastExistenceById(vehicle.getId());
		if(verifyPodcastExistenceById == null){
			throw new AppException(Response.Status.NOT_FOUND.getStatusCode(), 
					404, 
					"The resource you are trying to update does not exist in the database",
					"Please verify existence of data in the database for the id - " + vehicle.getId(),
					AppConstants.BLOG_POST_URL);				
		}
		copyPartialProperties(verifyPodcastExistenceById, vehicle);		
		vehicleDao.updatePodcast(new VehicleEntity(verifyPodcastExistenceById));
		
	}

	private void copyPartialProperties(Vehicle verifyPodcastExistenceById,
						Vehicle vehicle) {
		
		BeanUtilsBean notNull=new NullAwareBeanUtilsBean();
		try {
			notNull.copyProperties(verifyPodcastExistenceById, vehicle);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void generateCustomReasonPhraseException() throws CustomReasonPhraseException {		
		throw new CustomReasonPhraseException(4000, "message attached to the Custom Reason Phrase Exception");		
	}

	public void setPodcastDao(VehicleDao vehicleDao) {
		this.vehicleDao = vehicleDao;
	}
		
}
