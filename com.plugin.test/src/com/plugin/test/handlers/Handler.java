package com.plugin.test.handlers;

import javax.inject.Named;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.osgi.service.prefs.BackingStoreException;

import com.plugin.test.downloadsource.DownloadAndUnZip;
import com.plugin.test.encrypt.Encrypt;
import com.plugin.test.importproject.ImportMavenProject;

/**
 * <b>Warning</b> : As explained in <a href=
 * "http://wiki.eclipse.org/Eclipse4/RCP/FAQ#Why_aren.27t_my_handler_fields_being_re-injected.3F">this
 * wiki page</a>, it is not recommended to define @Inject fields in a handler.
 * <br/>
 * <br/>
 * <b>Inject the values in the @Execute methods</b>
 */
public class Handler {

	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell s) throws Exception {
		Form form = new Form(s);
		ImportMavenProject importMavenProject = new ImportMavenProject();
		DownloadAndUnZip downloadAndUnZip = new DownloadAndUnZip(s);
		Encrypt encrypt = new Encrypt();
		IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode("com.plugin.test");
		if (form.open() == Window.OK) {
			prefs.put("name", form.getName());
			prefs.put("location", form.getLocation());
			prefs.put("group", form.getGroup());
			prefs.put("artifact", form.getArtifact());
			prefs.put("version", form.getVersion());
			prefs.put("discription", form.getDiscription());
			prefs.put("packageName", form.getPackageName());
			prefs.putInt("dataBase", form.getDataBase());
			prefs.put("port", form.getPort());
			prefs.put("user", form.getUser());
			try {
				prefs.put("password", encrypt.encode(form.getPassword()));
			} catch (Exception e) {
				MessageDialog.openError(s, "Error", String.valueOf(e.getCause()));
			}
			try {
				// prefs are automatically flushed during a plugin's "super.stop()".
				prefs.flush();
			} catch (BackingStoreException e1) {
				MessageDialog.openError(s, "Error", String.valueOf(e1.getCause()));
			}

			if (downloadAndUnZip.download()) {
				IProject iProject = importMavenProject
						.importExistingMavenProjects(form.getLocation() + "/" + form.getName(), form.getName());
				if (!iProject.exists()) {
					MessageDialog.openError(s, "Error", "Import Maven Project Faild");
					return;
				}
			} else {
				MessageDialog.openError(s, "Error", "Download Faild");
				return;
			}

		}

	}

}
