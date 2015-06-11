package org.shareit.vehicle.rest.resource.podcast;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.shareit.vehicle.rest.errorhandling.AppException;
import org.shareit.vehicle.rest.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 
 * Service class that handles REST requests
 * 
 * @author amacoder
 * 
 */
@Component
@Path("/legacy/podcasts")
public class VehicleLegacyResource {

	@Autowired
	private VehicleService vehicleService;

	/************************************ READ ************************************/
	/**
	 * Returns all resources (podcasts) from the database
	 * 
	 * @return
	 * @throws AppException 
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<Vehicle> getPodcasts() throws AppException {
		return vehicleService.getLegacyPodcasts();
	}

	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response findById(@PathParam("id") Long id) throws AppException {		
		Vehicle podcastById = vehicleService.getLegacyPodcastById(id);
		if (podcastById != null) {
			return Response.status(200).entity(podcastById).build();
		} else {
			String message = "The podcast with the id " + id + " does not exist"; 
			throw new AppException(404, 4004, message, message, "link");
		}
	}

}
