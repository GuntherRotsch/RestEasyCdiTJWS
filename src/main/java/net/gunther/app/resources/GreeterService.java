package net.gunther.app.resources;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class GreeterService {

	public String greet() {
		return "Many greetings from " + this.getClass().getSimpleName();
	}
}
