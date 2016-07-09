package net.gunther.testing;

import org.jboss.resteasy.plugins.server.tjws.TJWSEmbeddedJaxrsServer;

/**
 * Extension of RestEasy's <code>TJWSEmbeddedJaxrsServer</code> configures
 * embedded server in a way, that custom testing framework classes
 * <ul>
 * <li>Servlet dispatcher <code>WeldAwareServletDispatcher</code></li>
 * <li>Injector factory <code>WeldInjectorFactory</code></li>
 * </ul>
 * get in place.
 * <p>
 * Note: Port intentionally defined public to allow reconfiguration. Default
 * value 9876 turned out being a good choice, because it avoids clashes with
 * local servers usually running on 8080.
 */
public class TJWSEmbeddedJaxrsCdiServer extends TJWSEmbeddedJaxrsServer {

	public static int PORT = 9876;

	public TJWSEmbeddedJaxrsCdiServer() {
		servlet = new WeldAwareServletDispatcher();
		setPort(PORT);

		getDeployment().setInjectorFactoryClass(WeldInjectorFactory.class.getName());
	}
}
