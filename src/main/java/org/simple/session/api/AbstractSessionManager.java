package org.simple.session.api;

import java.util.Properties;

import org.simple.session.api.impl.DefaultSessionIdGenerator;
import org.simple.session.api.impl.FastJsonSerializer;
import org.simple.session.util.PropertiesReaderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.base.Throwables;

/**
 * Abstract Session Manager
 * 
 * @author clx 2018/4/3.
 */
public abstract class AbstractSessionManager implements SessionManager {

	private final static Logger logger = LoggerFactory.getLogger(AbstractSessionManager.class);

	private static final String DEFAULT_PROPERTIES = "session.properties";

	protected SessionIdGenerator sessionIdGenerator;

	protected JsonSerializer jsonSerializer;

	public AbstractSessionManager() {
		this(DEFAULT_PROPERTIES);
	}

	/**
	 * @param propertiesFile
	 *            properties file in classpath, default is session.properties
	 */
	public AbstractSessionManager(String propertiesFile) {
		Properties properties = PropertiesReaderUtils.read(propertiesFile);
		initSessionIdGenerator(properties);
		initJsonSerializer(properties);
		init(properties);
	}

	/**
	 * 提供一个构造函数，接受Properties作为入口参数
	 * 
	 * @param properties
	 */
	public AbstractSessionManager(Properties properties) {
		initSessionIdGenerator(properties);
		initJsonSerializer(properties);
		init(properties);
	}

	/**
	 * init subclass
	 */
	protected void init(Properties props) {
	}

	private void initJsonSerializer(Properties properties) {
		String sessionSerializer = (String) properties.get("session.serializer");
		if (Strings.isNullOrEmpty(sessionSerializer)) {
			jsonSerializer = new FastJsonSerializer();
		} else {
			try {
				jsonSerializer = (JsonSerializer) (Class.forName(sessionSerializer).newInstance());
			} catch (Exception e) {
				logger.error("failed to init json generator: {}", Throwables.getStackTraceAsString(e));
			} finally {
				if (sessionIdGenerator == null) {
					logger.info("use default json serializer [FastJsonSerializer]");
					jsonSerializer = new FastJsonSerializer();
				}
			}
		}
	}

	private void initSessionIdGenerator(Properties props) {
		String sessionIdGeneratorClazz = (String) props.get("session.id.generator");
		if (Strings.isNullOrEmpty(sessionIdGeneratorClazz)) {
			sessionIdGenerator = new DefaultSessionIdGenerator();
		} else {
			try {
				sessionIdGenerator = (SessionIdGenerator) (Class.forName(sessionIdGeneratorClazz).newInstance());
			} catch (Exception e) {
				logger.error("failed to init session id generator: {}", Throwables.getStackTraceAsString(e));
			} finally {
				if (sessionIdGenerator == null) {
					logger.info("use default session id generator[DefaultSessionIdGenerator]");
					sessionIdGenerator = new DefaultSessionIdGenerator();
				}
			}
		}
	}
}
