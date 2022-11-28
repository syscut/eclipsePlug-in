package com.gfc.plugin.handlers;

import java.io.File;

import javax.inject.Named;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.osgi.service.prefs.BackingStoreException;

import com.gfc.plugin.connection.DbSetting;
import com.gfc.plugin.downloadsource.DownloadAndUnZip;
import com.gfc.plugin.encrypt.Encrypt;
import com.gfc.plugin.importproject.ImportMavenProject;
import com.gfc.plugin.runtest.RunMavenTest;

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
		IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode("com.gfc.plugin");
		Form form = new Form(s);
		ImportMavenProject importMavenProject = new ImportMavenProject(s);
		DownloadAndUnZip downloadAndUnZip = new DownloadAndUnZip(s, prefs);
		DbSetting dbSetting = new DbSetting(s);
		Encrypt encrypt = new Encrypt();
		RunMavenTest runMavenTest = new RunMavenTest();

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
				throw new Exception("Handler line 53");
			}
			try {
				// prefs are automatically flushed during a plugin's "super.stop()".
				prefs.flush();
			} catch (BackingStoreException e1) {
				MessageDialog.openError(s, "Error", String.valueOf(e1.getCause()));
				throw new Exception("Handler line 60");
			}

			downloadAndUnZip.download();

			dbSetting.deployFiles(prefs.getInt("dataBase", 3), prefs);

			Integer status = importMavenProject.importExistingMavenProjects(form.getLocation() + "/" + form.getName(),
					form.getName());

			if (status == 0) {
				File deleteFile = new File(form.getLocation() + "/" + form.getName() + ".zip");
				deleteFile.delete();
				runMavenTest.runCommandAtPath(form.getLocation() + "/" + form.getName() + "/");
			} else {
				MessageDialog.openError(s, "Error", "Import faild ...");
			}
		}

	}

}
