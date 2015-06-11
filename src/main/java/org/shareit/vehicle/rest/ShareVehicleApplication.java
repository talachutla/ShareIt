package org.shareit.vehicle.rest;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.message.GZipEncoder;
import org.glassfish.jersey.message.filtering.EntityFilteringFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.EncodingFilter;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;
import org.shareit.vehicle.rest.errorhandling.AppExceptionMapper;
import org.shareit.vehicle.rest.errorhandling.CustomReasonPhraseExceptionMapper;
import org.shareit.vehicle.rest.errorhandling.GenericExceptionMapper;
import org.shareit.vehicle.rest.errorhandling.NotFoundExceptionMapper;
import org.shareit.vehicle.rest.filters.CORSResponseFilter;
import org.shareit.vehicle.rest.filters.LoggingResponseFilter;
import org.shareit.vehicle.rest.resource.podcast.VehicleLegacyResource;
import org.shareit.vehicle.rest.resource.podcast.VehiclesResource;

/**
 * Registers the components to be used by the JAX-RS application
 * 
 * @author Santosh
 * 
 */
public class ShareVehicleApplication extends ResourceConfig {

	/**
	 * Register JAX-RS application components.
	 */
	public ShareVehicleApplication() {
		
        packages("org.shareit.vehicle.rest");
        
      //  register application resources
        register(VehiclesResource.class);
        register(VehicleLegacyResource.class);
//
//		// register filters
        register(RequestContextFilter.class);
        register(LoggingResponseFilter.class);
        register(CORSResponseFilter.class);
//
//		// register exception mappers
        register(GenericExceptionMapper.class);
        register(AppExceptionMapper.class);
        register(CustomReasonPhraseExceptionMapper.class);
        register(NotFoundExceptionMapper.class);
//
//		// register features
        register(JacksonFeature.class);
		register(EntityFilteringFeature.class);
		EncodingFilter.enableFor(this, GZipEncoder.class);		
		
//		property(EntityFilteringFeature.ENTITY_FILTERING_SCOPE, new Annotation[] {VehicleDetailedView.Factory.get()});
	}
}
