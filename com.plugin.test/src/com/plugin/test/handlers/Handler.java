package com.plugin.test.handlers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.net.URL;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

/**
 * <b>Warning</b> : As explained in <a href=
 * "http://wiki.eclipse.org/Eclipse4/RCP/FAQ#Why_aren.27t_my_handler_fields_being_re-injected.3F">this
 * wiki page</a>, it is not recommended to define @Inject fields in a handler.
 * <br/>
 * <br/>
 * <b>Inject the values in the @Execute methods</b>
 */
public class Handler {

	private static final String USER_DATA = "/com.plugin.test/src/com/plugin/test/data/UserData.java";

	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell s) {
		GetSourceInputStream getSourceInputStream = new GetSourceInputStream();
		Form form = new Form(s);
		if (form.open() == Window.OK) {
			InputStream sourceStream;
			String user = form.getUser();
			String password = form.getPassword();
			Integer d = form.getDataBase();
			String database = "";
			switch (d) {
			case 0:
				database = "192.6.3.1";
				break;
			case 1:
				database = "192.6.3.8";
				break;
			case 2:
				database = "192.6.3.1 & 192.6.3.8";
				break;
			case 3:
				database = "192.6.3.12";
				break;
			default:
				break;
			}
			sourceStream = getSourceInputStream.getSourceInputStream(USER_DATA);
			URL resourceUrl = getClass().getResource(USER_DATA);
			try {
				File file = new File(resourceUrl.toURI());
				BufferedReader reader = new BufferedReader(new InputStreamReader(sourceStream));
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
				String line;
				while ((line = reader.readLine()) != null) {
					if (line.contains("name")) {
						writer.write(
								line.replaceAll("(private String name = \").*(\";)", "$1" + form.getName() + "$2"));
					} else {
						writer.write(line);
					}
					writer.newLine();
				}
				reader.close();
				writer.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			MessageDialog.openInformation(s, "finish",
					"name = " + form.getName() + "\n password = " + password + "\n database = " + database);
		}

	}

}
