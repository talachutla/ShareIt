package org.shareit.taxi.rest.service;

import static org.mockito.Mockito.*;

import org.shareit.vehicle.rest.dao.VehicleDao;
import org.shareit.vehicle.rest.dao.VehicleEntity;
import org.shareit.vehicle.rest.errorhandling.AppException;
import org.shareit.vehicle.rest.resource.podcast.Vehicle;
import org.shareit.vehicle.rest.service.impl.VehicleServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class VehicleServiceImplTest {

	private static final Long CREATED_PODCAST_RESOURCE_ID = Long.valueOf(1);
	private static final String SOME_FEED = "some_feed";
	private static final String SOME_TITLE = "some title";
	private static final String EXISTING_FEED = "http://quarks.de/feed";
	private static final Long SOME_ID = 13L;
	
	@Rule
	public ExpectedException exception = ExpectedException.none();	

	VehicleServiceImpl sut;//system under test
	
	@Mock
	VehicleDao vehicleDao;
	
	@Before
	public void setUp() throws Exception {		
		sut = new VehicleServiceImpl();
		sut.setPodcastDao(vehicleDao);
	}

	@Test
	public void testCreatePodcast_successful() throws AppException {
		
		when(vehicleDao.getPodcastByFeed(SOME_FEED)).thenReturn(null);		
		when(vehicleDao.createPodcast(any(VehicleEntity.class))).thenReturn(CREATED_PODCAST_RESOURCE_ID);
		
		Vehicle vehicle = new Vehicle();
		vehicle.setFeed(SOME_FEED);
		vehicle.setTitle(SOME_TITLE);
		Long createPodcast = sut.createPodcast(vehicle);
		
		verify(vehicleDao).getPodcastByFeed(SOME_FEED);//verifies if the method vehicleDao.getPodcastByFeed has been called exactly once with that exact input parameter
		verify(vehicleDao, times(1)).getPodcastByFeed(SOME_FEED);//same as above
		verify(vehicleDao, times(1)).getPodcastByFeed(eq(SOME_FEED));//same as above
		verify(vehicleDao, times(1)).getPodcastByFeed(anyString());//verifies if the method vehicleDao.getPodcastByFeed has been called exactly once with any string as input
		verify(vehicleDao, atLeastOnce()).getPodcastByFeed(SOME_FEED);//verifies if the method vehicleDao.getPodcastByFeed has been called at least once with that exact input parameter		
		verify(vehicleDao, atLeast(1)).getPodcastByFeed(SOME_FEED);//verifies if the method vehicleDao.getPodcastByFeed has been called at least once with that exact input parameter
		verify(vehicleDao, times(1)).createPodcast(any(VehicleEntity.class));
		verify(vehicleDao, never()).getLegacyPodcastById(anyLong());//verifies the method vehicleDao.getLegacyPodcastById has never been called
		
		Assert.assertTrue(createPodcast == CREATED_PODCAST_RESOURCE_ID);
	}

	@Test(expected=AppException.class)	
	public void testCreatePodcast_error() throws AppException {
		
		VehicleEntity existingPodcast = new VehicleEntity();
		when(vehicleDao.getPodcastByFeed(EXISTING_FEED)).thenReturn(existingPodcast);			
		
		Vehicle vehicle = new Vehicle();
		vehicle.setFeed(EXISTING_FEED);
		vehicle.setTitle(SOME_TITLE);
		sut.createPodcast(vehicle);

	}
	
	@Test	
	public void testCreatePodcast_validation_missingFeed() throws AppException {
		
		exception.expect(AppException.class);
		exception.expectMessage("Provided data not sufficient for insertion");
						
		sut.createPodcast(new Vehicle());

	}		
	
	@Test	
	public void testCreatePodcast_validation_missingTitle() throws AppException {
		
		exception.expect(AppException.class);
		exception.expectMessage("Provided data not sufficient for insertion");
						
		Vehicle vehicle = new Vehicle();
		vehicle.setFeed(EXISTING_FEED);
		sut.createPodcast(vehicle);

	}	
	

	@Test
	public void testUpdatePartiallyPodcast_successful() throws AppException {
		
		VehicleEntity vehicleEntity = new VehicleEntity();
		vehicleEntity.setId(SOME_ID);
		when(vehicleDao.getPodcastById(SOME_ID)).thenReturn(vehicleEntity);		
		doNothing().when(vehicleDao).updatePodcast(any(VehicleEntity.class));
		
		Vehicle vehicle = new Vehicle(vehicleEntity);
		vehicle.setFeed(SOME_FEED);
		vehicle.setTitle(SOME_TITLE);
		sut.updatePartiallyPodcast(vehicle);
		
		verify(vehicleDao).getPodcastById(SOME_ID);//verifies if the method vehicleDao.getPodcastById has been called exactly once with that exact input parameter
		verify(vehicleDao).updatePodcast(any(VehicleEntity.class));		
		
		Assert.assertTrue(vehicle.getFeed() == SOME_FEED);
		Assert.assertTrue(vehicle.getTitle() == SOME_TITLE);
	}
	
	@Test
	public void testUpdatePartiallyPodcast_not_existing_podcast() {
		
		when(vehicleDao.getPodcastById(SOME_ID)).thenReturn(null);
		
		Vehicle vehicle = new Vehicle();
		vehicle.setId(SOME_ID);
		try {
			sut.updatePartiallyPodcast(vehicle);
			Assert.fail("Should have thrown an exception"); 
		} catch (AppException e) {
			verify(vehicleDao).getPodcastById(SOME_ID);//verifies if the method vehicleDao.getPodcastById has been called exactly once with that exact input parameter
			Assert.assertEquals(e.getCode(), 404);
		}
		
	}	
	
}
