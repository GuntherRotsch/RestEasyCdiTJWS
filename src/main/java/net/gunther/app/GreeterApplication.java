package net.gunther.app;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import net.gunther.app.resources.GreetingsResource;

public class GreeterApplication extends Application {

	private Set<Object> singletons = new HashSet<>();

	public GreeterApplication() {
		singletons.add(new GreetingsResource());
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}
