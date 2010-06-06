/*******************************************************************************
 * Copyright (c) 2009, 2010 Fabian Steeg. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p/>
 * Contributors: Fabian Steeg - initial API and implementation; see bug 277380
 *******************************************************************************/

package org.eclipse.zest.internal.dot;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ResourceListSelectionDialog;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.dot.DotExport;
import org.eclipse.zest.dot.DotImport;

/**
 * View showing the Zest import for a DOT input. Listens to *.dot files and
 * other files with DOT content in the workspace and allows for image file
 * export via calling a local 'dot' (location is selected in a dialog and stored
 * in the preferences).
 * 
 * @author Fabian Steeg (fsteeg)
 */
public final class ZestGraphView extends ViewPart {

	public static final String ID = "org.eclipse.zest.dot.ZestView";

	private static final RGB BACKGROUND = JFaceResources.getColorRegistry()
			.getRGB("org.eclipse.jdt.ui.JavadocView.backgroundColor");

	// TODO externalize
	private static final String ADD_EXPORT_QUESTION = "Add Graph Export to WikiText Markup?";
	private static final String ADD_EXPORT_MESSAGE = "The Zest graph currently displayed was generated "
			+ "from a DOT representation embdedd in WikiText markup (%s). Should a reference to the "
			+ "exported image file be added to the WikiText markup?";

	private static final String LOAD = "Load...";
	private static final String RESET = "Ask for 'dot' app location...";
	private static final String LAYOUT = "Layout";
	private static final String EXPORT = "Export";

	private static final String RESOURCES_ICONS_OPEN_GIF = "resources/icons/open.gif";
	private static final String RESOURCES_ICONS_EXPORT_GIF = "resources/icons/export.gif";
	private static final String RESOURCES_ICONS_RESET = "resources/icons/ask.gif";
	private static final String RESOURCES_ICONS_LAYOUT = "resources/icons/layout.gif";

	private static final String EXTENSION = "dot";
	private static final String FORMAT_PDF = "pdf";
	private static final String FORMAT_PNG = "png";

	private Composite composite;
	private Graph graph;
	private IFile file;

	private String dotString = "";
	private boolean addReference = true;
	private boolean listenToDotContent = true; // TODO add toggle button

	/** Listener that passes a visitor if a resource is changed. */
	private IResourceChangeListener resourceChangeListener = new IResourceChangeListener() {
		public void resourceChanged(final IResourceChangeEvent event) {
			if (event.getType() != IResourceChangeEvent.POST_BUILD
					&& event.getType() != IResourceChangeEvent.POST_CHANGE) {
				return;
			}
			IResourceDelta rootDelta = event.getDelta();
			try {
				rootDelta.accept(resourceVisitor);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	};

	/**
	 * If a *.dot file or a file with DOT content is visited, we update the
	 * graph from it.
	 */
	private IResourceDeltaVisitor resourceVisitor = new IResourceDeltaVisitor() {
		public boolean visit(final IResourceDelta delta) {
			IResource resource = delta.getResource();
			if (resource.getType() == IResource.FILE) {
				try {
					final IFile f = (IFile) resource;
					if (!listenToDotContent
							&& !f.getLocation().toString().endsWith(EXTENSION)) {
						return true;
					}
					IWorkspaceRunnable workspaceRunnable = new IWorkspaceRunnable() {
						public void run(final IProgressMonitor monitor)
								throws CoreException {
							setGraph(f);
						}
					};
					IWorkspace workspace = ResourcesPlugin.getWorkspace();
					if (!workspace.isTreeLocked()) {
						workspace.run(workspaceRunnable, null);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return true;
		}

	};

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(final Composite parent) {
		composite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		composite.setBackground(new Color(composite.getDisplay(), BACKGROUND));
		if (file != null) {
			try {
				updateGraph();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		addLoadButton();
		addExportButton();
		addLayoutButton();
		addResetButton();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(
				resourceChangeListener,
				IResourceChangeEvent.POST_BUILD
						| IResourceChangeEvent.POST_CHANGE);
	}

	private void addLayoutButton() {
		Action layoutAction = new Action(LAYOUT) {
			public void run() {
				graph.applyLayout();
			}
		};
		layoutAction.setImageDescriptor(DotUiActivator
				.getImageDescriptor(RESOURCES_ICONS_LAYOUT));
		IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
		mgr.add(layoutAction);
		mgr.add(new Separator());
	}

	private void addExportButton() {
		Action exportAction = new Action(EXPORT) {
			public void run() {
				generateImageFromGraph(true, FORMAT_PDF);
			}
		};
		exportAction.setImageDescriptor(DotUiActivator
				.getImageDescriptor(RESOURCES_ICONS_EXPORT_GIF));
		getViewSite().getActionBars().getToolBarManager().add(exportAction);
	}

	private void addResetButton() {
		Action resetAction = new Action(RESET) {
			public void run() {
				DotDirStore.setDotDirPath();
			}
		};
		resetAction.setImageDescriptor(DotUiActivator
				.getImageDescriptor(RESOURCES_ICONS_RESET));
		getViewSite().getActionBars().getToolBarManager().add(resetAction);
	}

	private void addLoadButton() {
		Action loadAction = new Action(LOAD) {
			public void run() {
				Shell shell = PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getShell();
				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				ResourceListSelectionDialog dialog = new ResourceListSelectionDialog(
						shell, root, IResource.FILE);
				if (dialog.open() == ResourceListSelectionDialog.OK) {
					Object[] selected = dialog.getResult();
					if (selected != null) {
						file = (IFile) selected[0];
						setGraph(file);
					}
				}
			}
		};
		loadAction.setImageDescriptor(DotUiActivator
				.getImageDescriptor(RESOURCES_ICONS_OPEN_GIF));
		getViewSite().getActionBars().getToolBarManager().add(loadAction);
	}

	private void setGraph(final IFile file) {
		this.file = file;
		try {
			updateGraph();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	private void updateGraph() throws MalformedURLException {
		if (file == null || file.getLocationURI() == null || !file.exists()) {
			return;
		}
		final String currentDot = new DotExtractor(file).getDotString();
		if (currentDot.equals(dotString)
				|| currentDot.equals(DotExtractor.NO_DOT)) {
			return;
		}
		getViewSite().getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				if (graph != null) {
					graph.dispose();
				}
				if (composite != null) {
					dotString = currentDot;
					graph = new DotImport(dotString).newGraphInstance(
							composite, SWT.NONE);
					setupLayout();
					composite.layout();
					graph.applyLayout();
				}
				handleWikiText(currentDot);
			}
		});
	}

	private void handleWikiText(final String dot) {
		try {
			IEditorDescriptor editor = IDE.getEditorDescriptor(file);
			/*
			 * TODO get ID from registry, not MarkupEditor.ID internal API or
			 * hard-coded string IEditorRegistry registry =
			 * getSite().getWorkbenchWindow
			 * ().getWorkbench().getEditorRegistry();
			 */
			if (editor.getId().equals(
					"org.eclipse.mylyn.wikitext.ui.editor.markupEditor")) {
				try {
					// Or export dot directly, or via Zest?
					File image = generateImageFromGraph(true, FORMAT_PNG);
					File wikiFile = DotFileUtils.resolve(file.getLocationURI()
							.toURL());
					String imageLinkWiki = createImageLinkMarkup(image);
					if (!DotFileUtils.read(wikiFile).contains(imageLinkWiki)
							&& addReference && supported(file)) {
						String message = String.format(ADD_EXPORT_MESSAGE,
								file.getName());
						if (MessageDialog.openQuestion(getSite().getShell(),
								ADD_EXPORT_QUESTION, message)) {
							addReference(dot, wikiFile, imageLinkWiki);
						} else {
							addReference = false;
						}
					}
					refreshParent(file);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		} catch (PartInitException e) {
			e.printStackTrace();
		}

	}

	private boolean supported(final IFile wikiFile) {
		// TODO support other markup languages
		return wikiFile.getFileExtension().endsWith("textile");
	}

	private String createImageLinkMarkup(final File image) {
		// TODO support other markup languages
		return String.format("\n!%s!\n", image.getName());
	}

	private void addReference(final String dot, final File wikiFile,
			final String imageLinkWiki) {
		/*
		 * This approach only works for textile markup, where the code is marked
		 * only at the beginning
		 */
		String content = DotFileUtils.read(wikiFile).replace(dot,
				dot + "\n" + imageLinkWiki);
		DotFileUtils.write(content, wikiFile);
	}

	private void setupLayout() {
		if (graph != null) {
			GridData gd = new GridData(GridData.FILL_BOTH);
			graph.setLayout(new GridLayout());
			graph.setLayoutData(gd);
			Color color = new Color(graph.getDisplay(), BACKGROUND);
			graph.setBackground(color);
			graph.getParent().setBackground(color);
		}
	}

	private File generateImageFromGraph(final boolean refresh,
			final String format) {
		File image = new DotExport(graph).toImage(DotDirStore.getDotDirPath(),
				format);
		try {
			URL url = file.getParent().getLocationURI().toURL();
			File copy = DotFileUtils.copySingleFile(DotFileUtils.resolve(url),
					file.getName() + "." + format, image);
			return copy;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		if (refresh) {
			refreshParent(file);
		}
		return image;
	}

	private void refreshParent(final IFile file) {
		try {
			file.getParent().refreshLocal(IResource.DEPTH_ONE, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#dispose()
	 */
	public void dispose() {
		super.dispose();
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(
				resourceChangeListener);
		graph.dispose();
		composite.dispose();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	public void setFocus() {
		if (graph != null && !graph.isDisposed()) {
			graph.setFocus();
		}
	}
}
