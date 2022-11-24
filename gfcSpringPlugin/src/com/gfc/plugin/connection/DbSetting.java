package com.gfc.plugin.connection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import com.gfc.plugin.encrypt.Encrypt;

public class DbSetting {
	GetSourceInputStream getSourceInputStream = new GetSourceInputStream();
	Encrypt encrypt = new Encrypt();
	Shell shell;

	public DbSetting(Shell shell) {
		this.shell = shell;
	}

	public void deployFiles(Integer db, IEclipsePreferences prefs) throws Exception {
		final String root = prefs.get("location", "") + "/" + prefs.get("name", "demo");
		final String applicationPropertiesPath = root + "/src/main/resources/application.properties";
		final String projectMainPath = root + "/src/main/java/"
				+ prefs.get("packageName", "com.example.demo").replaceAll("\\.", "/");
		String sourceInput;
		switch (db) {
		case 0:
			sourceInput = "3.1application.properties";
			break;
		case 1:
			sourceInput = "3.8application.properties";
			break;
		case 2:
			sourceInput = "application.properties";
			break;
		default:
			sourceInput = "3.12application.properties";
		}
		try {
			InputStream sourceFileStream = getSourceInputStream
					.getSourceInputStream("/applicationProperties/" + sourceInput);
			BufferedReader reader = new BufferedReader(new InputStreamReader(sourceFileStream));
			BufferedWriter writer = new BufferedWriter(new FileWriter(applicationPropertiesPath, true));
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.contains("{userName}")) {
					writer.write(line.replaceAll("\\{userName\\}", prefs.get("user", "username")));
				} else if (line.contains("{password}")) {
					writer.write(line.replaceAll("\\{password\\}",
							encrypt.decode(prefs.get("password", "xronGIMbAiYh5NTZf4heKg=="))));
				} else if (line.contains("{portNumber}")) {
					writer.write(line.replaceAll("\\{portNumber\\}", prefs.get("port", "5000")));
				} else {
					writer.write(line);
				}
				writer.newLine();
			}
			reader.close();
			writer.close();
		} catch (Exception e) {
			MessageDialog.openError(shell, "Error", String.valueOf(e.getCause()));
			e.printStackTrace();
		}
		checkAndUpdatePom(root + "/pom.xml");
		if (db == 2) {
			createFolders(projectMainPath);
			final LinkedList<String> streamSource = new LinkedList<>(Arrays.asList(
					"/dataSourceConfig/DataSourceConfiguration.java", "/dataSourceConfig/DataSourceConfigPrimary.java",
					"/dataSourceConfig/DataSourceConfigSecondary.java"));
			streamSource.forEach(s -> {
				try {
					copyDataSourceFile(getSourceInputStream.getSourceInputStream(s), projectMainPath + s,
							prefs.get("packageName", "com.example.demo"));
				} catch (IOException e) {
					MessageDialog.openError(shell, "Error", String.valueOf(e.getCause()));
					e.printStackTrace();
				}
			});

		}

	}

	private void checkAndUpdatePom(String path) throws Exception {
		try {
			long offset = 0, dependenciesOffset = 0;
			String line;
			BufferedReader reader = new BufferedReader(new FileReader(path));
			while ((line = reader.readLine()) != null) {
				offset += (line.getBytes().length + 1);
				if (line.contains("</dependencies>")) {
					dependenciesOffset = offset;
				}
			}
			reader.close();

			if (dependenciesOffset == 0) {
				MessageDialog.openError(shell, "Error", "can not find dependencies end tag in pom.xml");
				throw new Exception("DbSetting line 112");

			}
			byte[] informixDependency = ("		<dependency>\n" + "			<groupId>com.ibm.informix</groupId>\n"
					+ "			<artifactId>jdbc</artifactId>\n" + "			<version>4.50.7</version>\n"
					+ "		</dependency>\n").getBytes();
			insert(path, dependenciesOffset - 17, informixDependency);

		} catch (IOException e) {
			MessageDialog.openError(shell, "Error", String.valueOf(e.getCause()));
			e.printStackTrace();
		}

	}

	private void insert(String filename, long offset, byte[] content) {
		try {
			RandomAccessFile r = new RandomAccessFile(new File(filename), "rw");
			RandomAccessFile rtemp = new RandomAccessFile(new File(filename + "~"), "rw");
			long fileSize = r.length();
			FileChannel sourceChannel = r.getChannel();
			FileChannel targetChannel = rtemp.getChannel();
			sourceChannel.transferTo(offset, (fileSize - offset), targetChannel);
			sourceChannel.truncate(offset);
			r.seek(offset);
			r.write(content);
			long newOffset = r.getFilePointer();
			targetChannel.position(0L);
			sourceChannel.transferFrom(targetChannel, newOffset, (fileSize - offset));
			sourceChannel.close();
			targetChannel.close();
			File tmpPom = new File(filename + "~");
			tmpPom.delete();
		} catch (FileNotFoundException e) {
			MessageDialog.openError(shell, "Error", String.valueOf(e.getCause()));
			e.printStackTrace();
		} catch (IOException e) {
			MessageDialog.openError(shell, "Error", String.valueOf(e.getCause()));
			e.printStackTrace();
		}
	}

	private void createFolders(String path) throws Exception {
		try {
			Files.createDirectories(Paths.get(path + "/dataSourceConfig"));
			Files.createDirectories(Paths.get(path + "/primarySource"));
			Files.createDirectories(Paths.get(path + "/secondarySource"));
		} catch (IOException e) {
			MessageDialog.openError(shell, "Error", String.valueOf(e.getCause()));
			e.printStackTrace();
		}
	}

	private void copyDataSourceFile(InputStream sourceFileStream, String fileName, String packageName)
			throws IOException {

		BufferedReader reader = new BufferedReader(new InputStreamReader(sourceFileStream));
		BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
		String line;

		while ((line = reader.readLine()) != null) {
			if (line.contains("{packageName}")) {
				writer.write(line.replaceAll("\\{packageName\\}", packageName));
			} else {
				writer.write(line);
			}
			writer.newLine();
		}
		reader.close();
		writer.close();
	}

}
