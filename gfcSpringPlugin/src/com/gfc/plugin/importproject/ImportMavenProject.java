package com.gfc.plugin.importproject;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.MavenModelManager;
import org.eclipse.m2e.core.project.LocalProjectScanner;
import org.eclipse.m2e.core.project.MavenProjectInfo;
import org.eclipse.m2e.core.project.ProjectImportConfiguration;
import org.eclipse.m2e.core.ui.internal.wizards.ImportMavenProjectsJob;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkingSet;

@SuppressWarnings("restriction")
public class ImportMavenProject {
	Shell shell;
	Integer status;

	public ImportMavenProject(Shell shell) {
		this.shell = shell;
	}

	final ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog(shell);

	public Integer importExistingMavenProjects(String path, String projectName) throws Exception {
		List<String> folders = new LinkedList<String>();
		folders.add(path);
		MavenModelManager modelManager = MavenPlugin.getMavenModelManager();
		LocalProjectScanner scanner = new LocalProjectScanner(folders, false, modelManager);

		progressMonitorDialog.run(true, false, new IRunnableWithProgress() {
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				scanner.run(monitor);
				monitor.done();
			}
		});

		List<MavenProjectInfo> infos = new ArrayList<MavenProjectInfo>();
		infos.addAll(scanner.getProjects());
		for (MavenProjectInfo info : scanner.getProjects()) {
			infos.addAll(info.getProjects());
		}
		ImportMavenProjectsJob job = new ImportMavenProjectsJob(infos, new ArrayList<IWorkingSet>(),
				new ProjectImportConfiguration());
		job.setRule(MavenPlugin.getProjectConfigurationManager().getRule());

		progressMonitorDialog.run(true, false, new IRunnableWithProgress() {
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				IStatus istatus = job.runInWorkspace(monitor);
				monitor.done();
				status = istatus.getCode();
			}

		});

		return status;
	}

//	private static IProject waitForProject(String projectName) throws Exception {
//		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
//		return project;
//	}
}
