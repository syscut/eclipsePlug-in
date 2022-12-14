package com.gfc.plugin.importproject;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.MavenModelManager;
import org.eclipse.m2e.core.project.LocalProjectScanner;
import org.eclipse.m2e.core.project.MavenProjectInfo;
import org.eclipse.m2e.core.project.ProjectImportConfiguration;
import org.eclipse.m2e.core.ui.internal.wizards.ImportMavenProjectsJob;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkingSet;

import com.gfc.plugin.ui.Ui;

@SuppressWarnings("restriction")
public class ImportMavenProject {
	Shell shell;
	Integer status;

	public ImportMavenProject(Shell shell) {
		this.shell = shell;
	}

	private final Ui ui = new Ui();

	public Integer importExistingMavenProjects(String path, String projectName) throws Exception {
		List<String> folders = new LinkedList<String>();
		folders.add(path);
		MavenModelManager modelManager = MavenPlugin.getMavenModelManager();
		LocalProjectScanner scanner = new LocalProjectScanner(folders, false, modelManager);

		scanner.run(new NullProgressMonitor());

		List<MavenProjectInfo> infos = new ArrayList<MavenProjectInfo>();
		infos.addAll(scanner.getProjects());
		for (MavenProjectInfo info : scanner.getProjects()) {
			infos.addAll(info.getProjects());
		}
		ImportMavenProjectsJob job = new ImportMavenProjectsJob(infos, new ArrayList<IWorkingSet>(),
				new ProjectImportConfiguration());
		job.setRule(MavenPlugin.getProjectConfigurationManager().getRule());

		ui.getWorkbenchWindow().run(true, false, new IRunnableWithProgress() {
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				monitor.done();
				IStatus istatus = job.run(monitor);

				status = istatus.getCode();

			};
		});

		return status;
	}

//	private static IProject waitForProject(String projectName) throws Exception {
//		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
//		return project;
//	}
}
