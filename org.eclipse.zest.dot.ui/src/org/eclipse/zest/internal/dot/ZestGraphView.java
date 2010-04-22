/*******************************************************************************
 * Copyright (c) 2009 Fabian Steeg. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ResourceListSelectionDialog;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.dot.DotExport;
import org.eclipse.zest.dot.DotImport;
import org.eclipse.zest.internal.dot.DotFileUtils;

/**
 * View showing the Zest import for a DOT input. Listens to *.dot files in the workspace and allows for image
 * file export via calling a local 'dot' (location is selected in a dialog and stored in the preferences).
 * @author Fabian Steeg (fsteeg)
 */
public final class ZestGraphView extends ViewPart {
    // TODO externalize
    private static final String LOAD = "Load...";
    private static final String RESET = "Ask for 'dot' app location...";
    private static final String LAYOUT = "Layout";
    private static final String EXPORT = "Export";

    private static final String RESOURCES_ICONS_OPEN_GIF = "resources/icons/open.gif";
    private static final String RESOURCES_ICONS_EXPORT_GIF = "resources/icons/export.gif";
    private static final String RESOURCES_ICONS_RESET = "resources/icons/ask.gif";
    private static final String RESOURCES_ICONS_LAYOUT = "resources/icons/layout.gif";

    private static final String EXTENSION = "dot";
    protected static final String FORMAT_PDF = "pdf";

    private Composite composite;
    private Graph graph;
    private IFile file;

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

    /** If a *.dot file is visited, we update the graph from it. */
    private IResourceDeltaVisitor resourceVisitor = new IResourceDeltaVisitor() {
        public boolean visit(final IResourceDelta delta) {
            IResource resource = delta.getResource();
            if (resource.getType() == IResource.FILE
                    && EXTENSION.equalsIgnoreCase(resource.getFileExtension())) {
                try {
                    final IFile f = (IFile) resource;
                    final String l = f.getLocation().toString();
                    IWorkspaceRunnable workspaceRunnable = new IWorkspaceRunnable() {
                        public void run(final IProgressMonitor monitor) throws CoreException {
                            if (l.endsWith("." + EXTENSION)) {
                                setGraph(f);
                            }
                        }
                    };
                    IWorkspace workspace = ResourcesPlugin.getWorkspace();
                    workspace.run(workspaceRunnable, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
    };

    /**
     * {@inheritDoc}
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    public void createPartControl(final Composite parent) {
        composite = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        composite.setLayout(layout);
        if (file != null) {
            graph = new DotImport(file).newGraphInstance(composite, SWT.NONE);
        }
        addLoadButton();
        addExportButton();
        addLayoutButton();
        addResetButton();
        ResourcesPlugin.getWorkspace().addResourceChangeListener(resourceChangeListener,
                IResourceChangeEvent.POST_BUILD | IResourceChangeEvent.POST_CHANGE);
    }

    private void addLayoutButton() {
        Action layoutAction = new Action(LAYOUT) {
            public void run() {
                graph.applyLayout();
            }
        };
        layoutAction.setImageDescriptor(DotUiActivator.getImageDescriptor(RESOURCES_ICONS_LAYOUT));
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
        exportAction.setImageDescriptor(DotUiActivator.getImageDescriptor(RESOURCES_ICONS_EXPORT_GIF));
        getViewSite().getActionBars().getToolBarManager().add(exportAction);
    }

    private void addResetButton() {
        Action resetAction = new Action(RESET) {
            public void run() {
                DotDirStore.setDotDirPath();
            }
        };
        resetAction.setImageDescriptor(DotUiActivator.getImageDescriptor(RESOURCES_ICONS_RESET));
        getViewSite().getActionBars().getToolBarManager().add(resetAction);
    }

    private void addLoadButton() {
        Action loadAction = new Action(LOAD) {
            public void run() {
                Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
                IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
                ResourceListSelectionDialog dialog =
                        new ResourceListSelectionDialog(shell, root, IResource.FILE);
                if (dialog.open() == ResourceListSelectionDialog.OK) {
                    Object[] selected = dialog.getResult();
                    if (selected != null) {
                        file = (IFile) selected[0];
                        setGraph(file);
                    }
                }
            }
        };
        loadAction.setImageDescriptor(DotUiActivator.getImageDescriptor(RESOURCES_ICONS_OPEN_GIF));
        getViewSite().getActionBars().getToolBarManager().add(loadAction);
    }

    private void setGraph(final IFile file) {
        this.file = file;
        if (file.getFileExtension().equals(EXTENSION)) {
            updateGraph();
        }
    }

    private void updateGraph() {
        getViewSite().getShell().getDisplay().asyncExec(new Runnable() {
            public void run() {
                if (graph != null) {
                    graph.dispose();
                }
                if (composite != null) {
                    graph = new DotImport(file).newGraphInstance(composite, SWT.NONE);
                    setupLayout();
                    composite.layout();
                    graph.applyLayout();
                }
            }
        });
    }

    private void setupLayout() {
        if (graph != null) {
            GridData gd = new GridData(GridData.FILL_BOTH);
            graph.setLayout(new GridLayout());
            graph.setLayoutData(gd);
        }
    }

    private void generateImageFromGraph(final boolean refresh, final String format) {
        File image = new DotExport(graph).toImage(DotDirStore.getDotDirPath(), FORMAT_PDF);
        try {
            URL url = file.getParent().getLocationURI().toURL();
            DotFileUtils.copySingleFile(DotFileUtils.resolve(url), file.getName() + "." + format, image);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (refresh) {
            try {
                file.getParent().refreshLocal(IResource.DEPTH_ONE, null);
            } catch (CoreException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.ui.part.WorkbenchPart#dispose()
     */
    public void dispose() {
        super.dispose();
        ResourcesPlugin.getWorkspace().removeResourceChangeListener(resourceChangeListener);
        graph.dispose();
        composite.dispose();
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
     */
    public void setFocus() {
        if (graph != null) {
            graph.setFocus();
        }
    }
}
