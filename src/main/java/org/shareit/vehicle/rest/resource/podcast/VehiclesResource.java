package org.shareit.vehicle.rest.resource.podcast;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.shareit.vehicle.rest.dao.VehicleEntity;
import org.shareit.vehicle.rest.errorhandling.AppException;
import org.shareit.vehicle.rest.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * 
 * Service class that handles REST requests
 * 
 * @author amacoder
 * 
 */
@Component
@Path("/podcasts")
@Produces({ MediaType.APPLICATION_JSON})
public class VehiclesResource {

	@Autowired(required = true)
	private VehicleService vehicleService;


    @GET
    @Path("/test")
    public Response testWebService(){
        System.out.println("Inside the webservice......................");
        return  Response.status(Response.Status.CREATED)// 201
                .entity("testttttttttttttttttttt").build();

    }
    @GET
    @Path("/delete/{id}")
    public Response deleteEntity(@PathParam("id") Long id){
        System.out.println("Inside the delete webservice......................");
        vehicleService.deletePodcastById(id);
        return  Response.status(Response.Status.CREATED)// 201
                .entity("Successfully deleted the entity").build();

    }
    @GET
    @Path("/save")
    public Response saveEntity(){
        System.out.println("Inside the save webservice......................");
        VehicleEntity entity=new VehicleEntity();
        entity.setDescription("desc");
        entity.setFeed("feed");
        entity.setInsertionDate(new Date());
        entity.setTitle("new entity...");
        vehicleService.save(entity);
        return  Response.status(Response.Status.CREATED)// 201
                .entity("Successfully saved the entity").build();

    }

	/*
	 * *********************************** CREATE ***********************************
	 */

	/**
	 * Adds a new resource (podcast) from the given json format (at least title
	 * and feed elements are required at the DB level)
	 *
	 * @param vehicle
	 * @return
	 * @throws AppException
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.TEXT_HTML })
	public Response createPodcast(Vehicle vehicle) throws AppException {
		Long createPodcastId = vehicleService.createPodcast(vehicle);
		return Response.status(Response.Status.CREATED)// 201
				.entity("A new podcast has been created")
				.header("Location",
						"http://localhost:8888/shareIt/podcasts/"
								+ String.valueOf(createPodcastId)).build();
	}

	/**
	 * Adds a new podcast (resource) from "form" (at least title and feed
	 * elements are required at the DB level)
	 *
	 * @param title
	 * @param linkOnPodcastpedia
	 * @param feed
	 * @param description
	 * @return
	 * @throws AppException
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.TEXT_HTML })
	public Response createPodcastFromApplicationFormURLencoded(
			@FormParam("title") String title,
			@FormParam("linkOnPodcastpedia") String linkOnPodcastpedia,
			@FormParam("feed") String feed,
			@FormParam("description") String description) throws AppException {

		Vehicle vehicle = new Vehicle(title, linkOnPodcastpedia, feed,
				description);
		Long createPodcastid = vehicleService.createPodcast(vehicle);

		return Response
				.status(Response.Status.CREATED)// 201
				.entity("A new podcast/resource has been created at /shareIt/podcasts/"
						+ createPodcastid)
				.header("Location",
						"http://localhost:8888/shareIt/podcasts/"
								+ String.valueOf(createPodcastid)).build();
	}

	/**
	 * A list of resources (here podcasts) provided in json format will be added
	 * to the database.
	 *
	 * @param vehicles
	 * @return
	 * @throws AppException
	 */
	@POST
	@Path("list")
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response createPodcasts(List<Vehicle> vehicles) throws AppException {
		vehicleService.createPodcasts(vehicles);
		return Response.status(Response.Status.CREATED) // 201
				.entity("List of podcasts was successfully created").build();
	}

	/*
	 * *********************************** READ ***********************************
	 */
	/**
	 * Returns all resources (podcasts) from the database
	 *
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 * @throws AppException
	 */
	@GET
	//@Compress //can be used only if you want to SELECTIVELY enable compression at the method level. By using the EncodingFilter everything is compressed now.
	public List<Vehicle> getPodcasts(
			@QueryParam("orderByInsertionDate") String orderByInsertionDate,
			@QueryParam("numberDaysToLookBack") Integer numberDaysToLookBack)
			throws IOException,	AppException {
		List<Vehicle> vehicles = vehicleService.getPodcasts(
				orderByInsertionDate, numberDaysToLookBack);
		return vehicles;
	}

	@GET
	@Path("{id}")
	public Response getPodcastById(@PathParam("id") Long id, @QueryParam("detailed") boolean detailed)
			throws IOException,	AppException {
		Vehicle podcastById = vehicleService.getPodcastById(id);
		return Response.status(200)
				.entity(podcastById, detailed ? new Annotation[]{VehicleDetailedView.Factory.get()} : new Annotation[0])
				.header("Access-Control-Allow-Headers", "X-extra-header")
				.allow("OPTIONS").build();
	}
	
//	@GET
//	@Path("{id}")
//	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
//	@VehicleDetailedView
//	public Vehicle getPodcastById(@PathParam("id") Long id, @QueryParam("detailed") boolean detailed)
//			throws IOException,	AppException {
//		Vehicle podcastById = vehicleService.getPodcastById(id);
//
//		return podcastById;
////		return Response.status(200)
////				.entity(podcastById, detailed ? new Annotation[]{VehicleDetailedView.Factory.get()} : new Annotation[0])
////				.header("Access-Control-Allow-Headers", "X-extra-header")
////				.allow("OPTIONS").build();
//	}

	/*
	 * *********************************** UPDATE ***********************************
	 */

	/**
	 * The method offers both Creation and Update resource functionality. If
	 * there is no resource yet at the specified location, then a podcast
	 * creation is executed and if there is then the resource will be full
	 * updated.
	 *
	 * @param id
	 * @param vehicle
	 * @return
	 * @throws AppException
	 */
	@PUT
	@Path("{id}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.TEXT_HTML })
	public Response putPodcastById(@PathParam("id") Long id, Vehicle vehicle)
			throws AppException {

		Vehicle podcastById = vehicleService.verifyPodcastExistenceById(id);

		if (podcastById == null) {
			// resource not existent yet, and should be created under the
			// specified URI
			Long createPodcastId = vehicleService.createPodcast(vehicle);
			return Response
					.status(Response.Status.CREATED)
					// 201
					.entity("A new podcast has been created AT THE LOCATION you specified")
					.header("Location",
							"http://localhost:8888/shareIt/podcasts/"
									+ String.valueOf(createPodcastId)).build();
		} else {
			// resource is existent and a full update should occur
			vehicleService.updateFullyPodcast(vehicle);
			return Response
					.status(Response.Status.OK)
					// 200
					.entity("The podcast you specified has been fully updated created AT THE LOCATION you specified")
					.header("Location",
							"http://localhost:8888/shareIt/podcasts/"
									+ String.valueOf(id)).build();
		}
	}

	// PARTIAL update
	@POST
	@Path("{id}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.TEXT_HTML })
	public Response partialUpdatePodcast(@PathParam("id") Long id,
			Vehicle vehicle) throws AppException {
		vehicle.setId(id);
		vehicleService.updatePartiallyPodcast(vehicle);
		return Response
				.status(Response.Status.OK)
				// 200
				.entity("The podcast you specified has been successfully updated")
				.build();
	}

	/*
	 * *********************************** DELETE ***********************************
	 */
	@DELETE
	@Path("{id}")
	@Produces({ MediaType.TEXT_HTML })
	public Response deletePodcastById(@PathParam("id") Long id) {
		vehicleService.deletePodcastById(id);
		return Response.status(Response.Status.NO_CONTENT)// 204
				.entity("Vehicle successfully removed from database").build();
	}

	@DELETE
	@Produces({ MediaType.TEXT_HTML })
	public Response deletePodcasts() {
		vehicleService.deletePodcasts();
		return Response.status(Response.Status.NO_CONTENT)// 204
				.entity("All podcasts have been successfully removed").build();
	}

	public void setpodcastService(VehicleService vehicleService) {
		this.vehicleService = vehicleService;
	}

}
