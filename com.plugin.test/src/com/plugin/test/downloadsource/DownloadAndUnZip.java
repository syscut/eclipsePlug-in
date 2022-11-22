package com.plugin.test.downloadsource;

import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

import org.eclipse.swt.widgets.Shell;

import com.plugin.test.handlers.Form;

public class DownloadAndUnZip {
	private static final String BASE_URL = "https://start.spring.io/starter.zip?";
	private static final String TYPE = "&type=maven-project&";
	private static final String PACKAGING = "packaging=jar&";
	private static final String JAVA_VERSION = "javaVersion=11&";
	private static final String LANGUAGE = "language=java&";
	private static final String BOOT_VERSION = "bootVersion=2.7.5&";
	private static final String DEPENDENCIES = "dependencies=lombok&dependencies=configuration-processor&dependencies=data-jpa&dependencies=web";
	private Shell shell;

	public DownloadAndUnZip(Shell shell) {
		this.shell = shell;
	}

	Form f = new Form(shell);

	public void download() throws Exception {
		final String url = BASE_URL + "name=" + f.getName() + "&groupId=" + f.getGroup() + "&artifactId="
				+ f.getArtifact() + "&version=" + f.getVersion() + "&description=" + f.getDiscription()
				+ "&packageName=" + f.getPackageName() + TYPE + PACKAGING + JAVA_VERSION + LANGUAGE + BOOT_VERSION
				+ DEPENDENCIES;

		ReadableByteChannel readableByteChannel = Channels.newChannel(new URL(url).openStream());
		@SuppressWarnings("resource")
		FileOutputStream fileOutputStream = new FileOutputStream(System.getProperty("user.dir") + "/starter.zip");
		@SuppressWarnings("unused")
		FileChannel fileChannel = fileOutputStream.getChannel();
		fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
	}

}
