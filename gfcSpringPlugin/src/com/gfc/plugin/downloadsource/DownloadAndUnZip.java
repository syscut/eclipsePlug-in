package com.gfc.plugin.downloadsource;

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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
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
	private IEclipsePreferences prefs;

	public DownloadAndUnZip(Shell shell, IEclipsePreferences prefs) {
		this.shell = shell;
		this.prefs = prefs;
	}

	public void download() throws Exception {
		final String zipFile = prefs.get("location", "") + "/" + prefs.get("name", "demo") + ".zip";
		final String unZipFile = prefs.get("location", "") + "/" + prefs.get("name", "demo");
		final ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog(shell);
		final String url = BASE_URL + "name=" + prefs.get("name", "demo") + "&groupId="
				+ prefs.get("group", "com.example") + "&artifactId=" + prefs.get("artifact", "demo") + "&version="
				+ prefs.get("version", "0.0.1-SNAPSHOT") + "&description="
				+ prefs.get("discription", "Demo project for Spring Boot") + "&packageName="
				+ prefs.get("packageName", "com.example.demo") + TYPE + PACKAGING + JAVA_VERSION + LANGUAGE
				+ BOOT_VERSION + DEPENDENCIES;

		progressMonitorDialog.run(true, false, new IRunnableWithProgress() {
			@Override
			public void run(IProgressMonitor monitor) {
				monitor.setTaskName("Preparing Project");
				monitor.beginTask("Now Progress ...", 3);
				try {
					ReadableByteChannel readableByteChannel = Channels.newChannel(new URL(url).openStream());
					monitor.worked(1);
					monitor.subTask("Downloading...");
					@SuppressWarnings("resource")
					FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
					@SuppressWarnings("unused")
					FileChannel fileChannel = fileOutputStream.getChannel();
					fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
					monitor.worked(2);
					monitor.subTask("Decompressing...");
					unZip(zipFile, unZipFile);
					monitor.worked(3);
				} catch (Exception e) {
					e.printStackTrace();
					MessageDialog.openError(shell, "Error", "Prepare faild ...");
				}
				monitor.done();
			}
		});

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
					zis.close();
					MessageDialog.openError(shell, "Error", "Failed to create directory " + newFile);
					throw new IOException("DownloadAndUnZip line 65");
				}
			} else {
				// fix for Windows-created archives
				File parent = newFile.getParentFile();
				if (!parent.isDirectory() && !parent.mkdirs()) {
					zis.close();
					MessageDialog.openError(shell, "Error", "Failed to create directory " + parent);
					throw new IOException("DownloadAndUnZip line 71");
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
			throw new IOException("DownloadAndUnZip line 95");
		}
		return destFile;
	}

}
