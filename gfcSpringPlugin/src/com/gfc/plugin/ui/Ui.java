package com.gfc.plugin.ui;

import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class Ui {

	public IStatusLineManager getStatusLine() {
		IStatusLineManager statusLineManager = null;
		final IWorkbenchWindow workBench = getWorkbenchWindow();
		final IWorkbenchPage activePage = workBench.getActivePage();
		final IWorkbenchPart workbenchPart = activePage.getActivePart();

		if (workbenchPart instanceof IViewPart) {
			final IViewPart viewPart = (IViewPart) workbenchPart;
			statusLineManager = viewPart.getViewSite().getActionBars().getStatusLineManager();
		} else if (activePage.getActiveEditor() != null) {
			final IEditorSite editorSite = activePage.getActiveEditor().getEditorSite();
			statusLineManager = editorSite.getActionBars().getStatusLineManager();
		}

		return statusLineManager;
	}

	public IWorkbenchWindow getWorkbenchWindow() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	}
}
