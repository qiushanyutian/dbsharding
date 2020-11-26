package com.wl.dbsharding.datasource;

import com.wl.dbsharding.util.DatabaseContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource{

	private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDataSource.class);

	@Override
	protected Object determineCurrentLookupKey() {
		LOGGER.info("execute from datasource:{}", DatabaseContextHolder.getDbType());
	 	return DatabaseContextHolder.getDbType();
	}

}
