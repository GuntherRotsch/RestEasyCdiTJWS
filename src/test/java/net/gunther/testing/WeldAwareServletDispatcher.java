package net.gunther.testing;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.resteasy.plugins.server.tjws.TJWSServletDispatcher;
import org.jboss.weld.context.bound.BoundRequestContext;

/**
 * RestEasy's <code>TJWSServletDispatcher</code> contains glue code between TJWS
 * server and RestEasy resources. Each JAX/RS request gets dispatched by this
 * class, more precisely, the <code>service</code> method basically allows
 * intercepting of JAX/RS requests.
 * <p>
 * For testing purposes the <code>service</code> method is overriden in order to
 * activate/deactivate CDI request context before/after performing JAX/RS calls.
 */
public class WeldAwareServletDispatcher extends TJWSServletDispatcher {

	@Override
	public void service(String httpMethod, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		BoundRequestContext requestContext = CdiUtil.select(BoundRequestContext.class).get();
		Map<String, Object> requestMap = new HashMap<String, Object>();
		requestContext.associate(requestMap);
		requestContext.activate();
		try {
			super.service(httpMethod, request, response);
		} finally {
			requestContext.invalidate();
			requestContext.deactivate();
			requestContext.dissociate(requestMap);
		}
	}
}
