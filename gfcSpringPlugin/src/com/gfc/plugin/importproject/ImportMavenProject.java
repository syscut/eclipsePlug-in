package com.gfc.plugin.importproject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.MavenModelManager;
import org.eclipse.m2e.core.project.LocalProjectScanner;
import org.eclipse.m2e.core.project.MavenProjectInfo;
import org.eclipse.m2e.core.project.ProjectImportConfiguration;
import org.eclipse.m2e.core.ui.internal.wizards.ImportMavenProjectsJob;
import org.eclipse.ui.IWorkingSet;

@SuppressWarnings("restriction")
public class ImportMavenProject {

	synchronized public String importExistingMavenProjects(String path, String projectName) throws Exception {
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
		job.schedule();
		IStatus status = job.getResult();
		while (status == null) {
			Thread.sleep(1000);
			status = job.getResult();
		}
		if (status.isOK()) {
			return "finished";
		} else {
			return "faild";
		}
	}

//	private static IProject waitForProject(String projectName) throws Exception {
//		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
//		return project;
//	}
}
