package org.shareit.vehicle.rest.resource.podcast;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.shareit.vehicle.rest.errorhandling.CustomReasonPhraseException;
import org.shareit.vehicle.rest.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;

@Path("/mocked-custom-reason-phrase-exception")
public class CustomReasonPhraseExceptionMockResource {
	
	@Autowired
	private VehicleService vehicleService;
	
	@GET
	public void testReasonChangedInResponse() throws CustomReasonPhraseException{
		vehicleService.generateCustomReasonPhraseException();
	}
}
