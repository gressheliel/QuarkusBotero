package org.elhg;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path("/hello")
public class ExampleResource {

    @ConfigProperty(name = "greeting")
    private String greeting;


    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from Quarkus REST";
    }

    @GET
    @Path("properties")
    @Produces(MediaType.TEXT_PLAIN)
    public String helloProperties() {
        return greeting;
    }

    @GET
    @Path("custom/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public String customHello(@PathParam("name") String name) {
        return "Hello "+name+" How are you?";
    }

}
