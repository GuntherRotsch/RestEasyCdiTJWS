package net.gunther.testing;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.BeanManager;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

/**
 * Utility class for setting up and providing Weld CDI container as a singleton.
 * The class also contains some CDI related convenience methods.
 */
public final class CdiUtil {

	private CdiUtil() {
		// prevents instantiation of utility class
	}

	/*
	 * Weld container is implemented as class member, initialization triggered
	 * on load of class. This implementation pattern ensures CDI container to be
	 * a singleton.
	 */
	private static WeldContainer weld;

	static {
		weld = new Weld().initialize();
	}

	/**
	 * Selects an instance of given CDI type. The get() method can be called on
	 * returned <code>Instance</code> object, in order to get an actual instance
	 * (or proxy if CDI bean does have a normal scope) of given type.
	 *
	 * @param clazz
	 *            the type to be resolved by CDI
	 * @return an Instance object
	 */
	public static <T> Instance<T> select(Class<T> clazz) {
		return instance().select(clazz);
	}

	/**
	 * @return wrapper for all instances managed by container
	 */
	public static Instance<Object> instance() {
		return weld.instance();
	}

	/**
	 * @return the bean manager
	 */
	public static BeanManager getBeanManager() {
		return weld.getBeanManager();
	}
}
