package com.mall.cloud.provider.uac.service;

import java.util.concurrent.TimeUnit;

public interface RedisService {

	/**
     * 获取key
	 *
	 * @param key the key
	 *
	 * @return the key
	 */
	String getKey(String key);

	/**
	 * 删除key.
	 *
	 * @param key the key
	 */
	void deleteKey(String key);

	/**
	 * 设置 key.
	 *
	 * @param key   the key
	 * @param value the value
	 */
	void setKey(String key, String value);

	/**
	 * 设置 key.
	 *
	 * @param key     the key
	 * @param value   the value
	 * @param timeout the timeout
	 * @param unit    the unit
	 */
	void setKey(String key, String value, final long timeout, final TimeUnit unit);
}
