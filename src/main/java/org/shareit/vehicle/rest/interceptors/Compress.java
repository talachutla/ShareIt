package org.shareit.vehicle.rest.interceptors;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.ws.rs.NameBinding;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

//@Compress annotation is the name binding annotation
@Target({ElementType.TYPE, ElementType.METHOD})
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface Compress {}
