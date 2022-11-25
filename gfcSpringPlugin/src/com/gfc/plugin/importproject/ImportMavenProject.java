package com.gfc.plugin.importproject;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
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

	public ImportMavenProject(Shell shell) {
		this.shell = shell;
	}

	final ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog(shell);

	public String importExistingMavenProjects(String path, String projectName) throws Exception {
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
//		job.schedule();
//		IStatus status = job.getResult();
//		while (status == null) {
//			System.out.println("status is : " + status);
//			Thread.sleep(1000);
//			status = job.getResult();
//		}

		progressMonitorDialog.run(false, false, new IRunnableWithProgress() {
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				Integer nowRunning = 0;
				monitor.setTaskName("import");
				monitor.beginTask("import", 100);
				monitor.worked(nowRunning);

				IStatus status = job.runInWorkspace(monitor);
				System.out.println(status.getCode());
				while (status == null) {
					nowRunning = nowRunning < 99 ? nowRunning + 9 : nowRunning;
					monitor.worked(nowRunning);
					Thread.sleep(500);
					status = job.getResult();
					System.out.println(status.getCode());
				}
				monitor.worked(100);
				monitor.done();
			}

		});

		return "finished";
//		if (status.isOK()) {
//			Thread.sleep(500);
//			return "finished";
//		} else {
//			return "faild";
//		}
	}

//	private static IProject waitForProject(String projectName) throws Exception {
//		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
//		return project;
//	}
}
