package org.shareit.taxi.rest.service.integration;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.shareit.vehicle.rest.resource.podcast.Vehicle;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.junit.Assert;
import org.junit.Test;

public class ShareVehicleServiceIT {

	@Test
	public void testGetPodcasts() throws JsonGenerationException,
			JsonMappingException, IOException {

		ClientConfig clientConfig = new ClientConfig();
		clientConfig.register(JacksonFeature.class);

		Client client = ClientBuilder.newClient(clientConfig);

		WebTarget webTarget = client
				.target("http://localhost:8888/shareIt/podcasts/");

		Builder request = webTarget.request();
		request.header("Content-type", MediaType.APPLICATION_JSON);

		Response response = request.get();
		Assert.assertTrue(response.getStatus() == 200);

		List<Vehicle> vehicles = response
				.readEntity(new GenericType<List<Vehicle>>() {
				});

		ObjectMapper mapper = new ObjectMapper();
		System.out.print(mapper.writerWithDefaultPrettyPrinter()
				.writeValueAsString(vehicles));

		Assert.assertTrue("At least one podcast is present",
				vehicles.size() > 0);
	}

	@Test
	public void testGetPodcast() throws JsonGenerationException,
			JsonMappingException, IOException {

		ClientConfig clientConfig = new ClientConfig();
		clientConfig.register(JacksonFeature.class);

		Client client = ClientBuilder.newClient(clientConfig);

		WebTarget webTarget = client
				.target("http://localhost:8888/shareIt/podcasts/2");

		Builder request = webTarget.request(MediaType.APPLICATION_JSON);

		Response response = request.get();
		Assert.assertTrue(response.getStatus() == 200);

		Vehicle vehicle = response.readEntity(Vehicle.class);

		ObjectMapper mapper = new ObjectMapper();
		System.out
				.print("Received podcast from database *************************** "
						+ mapper.writerWithDefaultPrettyPrinter()
								.writeValueAsString(vehicle));

	}
	
	@Test
	public void testGetLegacyPodcasts() throws JsonGenerationException,
			JsonMappingException, IOException {

		ClientConfig clientConfig = new ClientConfig();
		clientConfig.register(JacksonFeature.class);

		Client client = ClientBuilder.newClient(clientConfig);

		WebTarget webTarget = client
				.target("http://localhost:8888/shareIt/legacy/podcasts/");

		Builder request = webTarget.request();
		request.header("Content-type", MediaType.APPLICATION_JSON);

		Response response = request.get();
		Assert.assertTrue(response.getStatus() == 200);

		List<Vehicle> vehicles = response
				.readEntity(new GenericType<List<Vehicle>>() {
				});

		ObjectMapper mapper = new ObjectMapper();
		System.out.print(mapper.writerWithDefaultPrettyPrinter()
				.writeValueAsString(vehicles));

		Assert.assertTrue("At least one podcast is present in the LEGACY",
				vehicles.size() > 0);
	}

	@Test
	public void testGetLegacyPodcast() throws JsonGenerationException,
			JsonMappingException, IOException {

		ClientConfig clientConfig = new ClientConfig();
		clientConfig.register(JacksonFeature.class);

		Client client = ClientBuilder.newClient(clientConfig);

		WebTarget webTarget = client
				.target("http://localhost:8888/shareIt/legacy/podcasts/2");

		Builder request = webTarget.request(MediaType.APPLICATION_JSON);

		Response response = request.get();
		Assert.assertTrue(response.getStatus() == 200);

		Vehicle vehicle = response.readEntity(Vehicle.class);

		ObjectMapper mapper = new ObjectMapper();
		System.out
				.print("Received podcast from LEGACY database *************************** "
						+ mapper.writerWithDefaultPrettyPrinter()
								.writeValueAsString(vehicle));

	}
	
}
