package com.gfc.plugin.connection;

import java.io.InputStream;

public class GetSourceInputStream {
	/**
	 * @param source
	 * @return InputStream
	 */
	public InputStream getSourceInputStream(String source) {
		InputStream in = getClass().getResourceAsStream(source);
		return in;
	}
}
