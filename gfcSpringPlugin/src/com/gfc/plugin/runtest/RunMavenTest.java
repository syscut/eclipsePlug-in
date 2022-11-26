package com.gfc.plugin.runtest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

public class RunMavenTest {

	public void runCommandAtPath(String projectPath) throws Exception {

		Runtime runtime = Runtime.getRuntime();

		Process p = runtime.exec(new String[] { "cmd.exe", "/c", "cd " + projectPath + " && mvn spring-boot:run" });

		new Thread() {
			public void run() {
				MessageConsole messageConsole = new MessageConsole("Maven Test", null);
				messageConsole.clearConsole();
				ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[] { messageConsole });

				MessageConsoleStream msgConsoleStream = messageConsole.newMessageStream();
				messageConsole.activate();
				BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String line;
				try {
					while ((line = r.readLine()) != null) {
						msgConsoleStream.println(line);
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
