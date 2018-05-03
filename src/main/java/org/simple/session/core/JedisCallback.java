package org.simple.session.core;

import redis.clients.jedis.Jedis;

/**
 * Jedis Execute Callback
 * 
 * @author clx 2018/4/3.
 */
public interface JedisCallback<V> {
	/**
	 * Execute Callback
	 * 
	 * @param jedis
	 * @return
	 */
	V execute(Jedis jedis);
}
