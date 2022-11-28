package com.gfc.plugin.runtest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import com.gfc.plugin.colorPrinter.Printer;

public class RunMavenTest {

	public void runCommandAtPath(String projectPath) throws Exception {

		Runtime runtime = Runtime.getRuntime();

		Process p = runtime.exec(new String[] { "cmd.exe", "/c", projectPath.substring(0, 2) + " && cd " + projectPath
				+ " && mvn spring-boot:start && mvn spring-boot:stop" });

		new Thread() {
			@Override
			public void run() {
				Printer printer = new Printer();
				MessageConsole messageConsole = new MessageConsole("Maven Test", null);
				messageConsole.clearConsole();
				ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[] { messageConsole });

				MessageConsoleStream msgConsoleStream = messageConsole.newMessageStream();
				messageConsole.activate();
				BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String line;
				// BUILD SUCCESS
				try {
					while ((line = r.readLine()) != null) {
						if (line.matches("(?i).*Started .+Application.*")
								|| line.matches("(?i).*HikariPool.+Start completed.*")
								|| line.matches("(?i).*BUILD SUCCESS.*")) {
							msgConsoleStream.println(printer.greenText(line));
						} else if (line.matches("(?i).*BUILD FAILURE.*")) {
							msgConsoleStream.println(printer.redText(line));
						} else {
							msgConsoleStream.println(line);
						}
					}
				} catch (IOException e) {

					e.printStackTrace();
				}
				try {
					msgConsoleStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();

//		ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "cd " + projectPath + " && mvn spring-boot:run");
//		builder.redirectErrorStream(true);
//		Process p = builder.start();

	}
}
