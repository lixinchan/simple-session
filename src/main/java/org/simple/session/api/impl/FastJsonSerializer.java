package org.simple.session.api.impl;

import java.util.Map;

import org.simple.session.api.JsonSerializer;
import org.simple.session.exception.SerializeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;

/**
 * FastJson Serializer
 * 
 * @author clx 2018/4/3.
 */
public class FastJsonSerializer implements JsonSerializer {

	private final static Logger log = LoggerFactory.getLogger(FastJsonSerializer.class);

	private final ObjectMapper objectMapper;

	private final JavaType mapType;

	public FastJsonSerializer() {
		objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		mapType = objectMapper.getTypeFactory().constructParametricType(Map.class, String.class, Object.class);
	}

	@Override
	public String serialize(Object o) {
		try {
			return objectMapper.writeValueAsString(o);
		} catch (Exception e) {
			log.error("failed to serialize http session {} to json,cause:{}", o, Throwables.getStackTraceAsString(e));
			throw new SerializeException("failed to serialize http session to json", e);
		}
	}

	@Override
	public Map<String, Object> deserialize(String o) {
		try {
			return objectMapper.readValue(o, mapType);
		} catch (Exception e) {
			log.error("failed to deserialize string  {} to http session,cause:{} ", o,
					Throwables.getStackTraceAsString(e));
			throw new SerializeException("failed to deserialize string to http session", e);
		}
	}
}
