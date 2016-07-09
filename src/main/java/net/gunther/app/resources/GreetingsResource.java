package net.gunther.app.resources;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/greetings")
public class GreetingsResource {

	@Inject
	GreeterService greeter;

	@GET
	@Path("/")
	public Response printMessage() {
		String result = greeter.greet();
		return Response.status(200).entity(result).build();
	}
}
