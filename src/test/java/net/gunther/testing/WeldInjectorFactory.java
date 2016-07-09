package net.gunther.testing;

import javax.enterprise.inject.spi.BeanManager;

import org.jboss.resteasy.cdi.CdiInjectorFactory;

/**
 * RestEasy's <code>CdiInjectorFactory</code> glues together CDI and RestEasy
 * and ensures resolution of CDI beans which are injected into JAX/RS resources.
 * Out-of-the-box <code>CdiInjectorFactory</code> resolves bean manager either
 * by
 * <ul>
 * <li>JNDI lookup</li>
 * <li>Servlet context configuration</li>
 * </ul>
 * <p>
 * For testing purposes this bean manager resolution is overriden by looking it
 * up from our programmatically set up Weld CDI container.
 *
 */
public class WeldInjectorFactory extends CdiInjectorFactory {

	@Override
	protected BeanManager lookupBeanManager() {
		BeanManager beanManager = null;

		beanManager = CdiUtil.getBeanManager();
		if (beanManager != null) {
			return beanManager;
		}

		throw new RuntimeException("Unable to lookup BeanManager.");
	}
}
