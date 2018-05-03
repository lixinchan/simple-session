package org.simple.session.core;

import java.io.IOException;
import java.util.Properties;

import org.simple.session.api.AbstractSessionFilter;
import org.simple.session.api.SessionManager;

/**
 * Redis Session Filter based on Redis
 * 
 * @author clx 2018/4/3.
 */
public class RedisSessionFilter extends AbstractSessionFilter {
	private Properties prop;

	public RedisSessionFilter() {
	}

	public RedisSessionFilter(Properties prop) {
		this.prop = prop;
	}

	/**
	 * subclass create session manager
	 * 
	 * @return session manager
	 */
	@Override
	protected SessionManager createSessionManager() throws IOException {
		if (prop != null) {
			return new RedisSessionManager(prop);
		}
		return new RedisSessionManager();
	}
}
