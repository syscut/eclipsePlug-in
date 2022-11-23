package com.plugin.test.downloadsource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

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

	private IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode("com.plugin.test");

	public Boolean download() throws Exception {
		final String url = BASE_URL + "name=" + prefs.get("name", "demo") + "&groupId="
				+ prefs.get("group", "com.example") + "&artifactId=" + prefs.get("artifact", "demo") + "&version="
				+ prefs.get("version", "0.0.1-SNAPSHOT") + "&description="
				+ prefs.get("discription", "Demo project for Spring Boot") + "&packageName="
				+ prefs.get("packageName", "com.example.demo") + TYPE + PACKAGING + JAVA_VERSION + LANGUAGE
				+ BOOT_VERSION + DEPENDENCIES;
		final String zipFile = prefs.get("location", "/") + "/" + prefs.get("name", "demo") + ".zip";
		System.out.println("target = " + zipFile);
		ReadableByteChannel readableByteChannel = Channels.newChannel(new URL(url).openStream());
		@SuppressWarnings("resource")
		FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
		@SuppressWarnings("unused")
		FileChannel fileChannel = fileOutputStream.getChannel();
		fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
		unZip(zipFile, prefs.get("location", "/") + "/" + prefs.get("name", "demo"));
		File file = new File(zipFile);
		return file.delete();
	}

	public void unZip(String file, String dest) throws IOException {
		File destDir = new File(dest);
		byte[] buffer = new byte[1024];
		ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
		ZipEntry zipEntry = zis.getNextEntry();
		while (zipEntry != null) {
			File newFile = newFile(destDir, zipEntry);
			if (zipEntry.isDirectory()) {
				if (!newFile.isDirectory() && !newFile.mkdirs()) {
					MessageDialog.openError(shell, "Error", "Failed to create directory " + newFile);
				}
			} else {
				// fix for Windows-created archives
				File parent = newFile.getParentFile();
				if (!parent.isDirectory() && !parent.mkdirs()) {
					MessageDialog.openError(shell, "Error", "Failed to create directory " + parent);
				}

				// write file content
				FileOutputStream fos = new FileOutputStream(newFile);
				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
			}
			zipEntry = zis.getNextEntry();
		}
		zis.closeEntry();
		zis.close();
	}

	public File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
		File destFile = new File(destinationDir, zipEntry.getName());

		String destDirPath = destinationDir.getCanonicalPath();
		String destFilePath = destFile.getCanonicalPath();

		if (!destFilePath.startsWith(destDirPath + File.separator)) {
			MessageDialog.openError(shell, "Error", "Entry is outside of the target dir: " + zipEntry.getName());
		}
		return destFile;
	}

}
