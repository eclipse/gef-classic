package org.eclipse.gef.examples.ediagram.editor;

import java.io.IOException;
import java.util.Collections;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheetPage;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.Resource.IOWrappedException;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.DanglingHREFException;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;

import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.FanRouter;
import org.eclipse.draw2d.ShortestPathConnectionRouter;

import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.MouseWheelZoomHandler;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.gef.ui.actions.DirectEditAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.palette.PaletteCustomizer;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite.FlyoutPreferences;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;

import org.eclipse.gef.examples.ediagram.EDiagramPlugin;
import org.eclipse.gef.examples.ediagram.edit.parts.EDiagramPartFactory;
import org.eclipse.gef.examples.ediagram.model.Diagram;
import org.eclipse.gef.examples.ediagram.model.commands.DeleteCommand;
import org.eclipse.gef.examples.ediagram.model.ecore.provider.EcoreItemProviderAdapterFactory;
import org.eclipse.gef.examples.ediagram.model.properties.emf.UndoablePropertySheetEntry;
import org.eclipse.gef.examples.ediagram.model.provider.ModelItemProviderAdapterFactory;
import org.eclipse.gef.examples.ediagram.outline.EDiagramOutlinePage;

public class EDiagramEditor 
	extends GraphicalEditorWithFlyoutPalette 
{

protected static final String PALETTE_DOCK_LOCATION = "Dock location"; //$NON-NLS-1$
protected static final String PALETTE_SIZE = "Palette Size"; //$NON-NLS-1$
protected static final String PALETTE_STATE = "Palette state"; //$NON-NLS-1$

private Diagram diagram;
private PaletteRoot paletteRoot;
private ResourceSet rsrcSet;

public EDiagramEditor() {
	setEditDomain(new DefaultEditDomain(this));
}

public void commandStackChanged(EventObject event) {
	firePropertyChange(PROP_DIRTY);
	super.commandStackChanged(event);
}

protected void configureGraphicalViewer() {
	super.configureGraphicalViewer();
	GraphicalViewer viewer = getGraphicalViewer();

	viewer.setRootEditPart(new ScalableFreeformRootEditPart());
	viewer.setEditPartFactory(EDiagramPartFactory.getInstance());
	
	KeyHandler keyHandler = new GraphicalViewerKeyHandler(viewer) {
		public boolean keyPressed(KeyEvent event) {
			if (event.stateMask == SWT.CTRL && event.keyCode == SWT.DEL) {
				List objects = getGraphicalViewer().getSelectedEditParts();
				if (objects == null || objects.isEmpty())
					return true;
				GroupRequest deleteReq = new GroupRequest(RequestConstants.REQ_DELETE);
				deleteReq.getExtendedData().put(
						DeleteCommand.KEY_PERM_DELETE, Boolean.TRUE);
				CompoundCommand compoundCmd = new CompoundCommand(
						GEFMessages.DeleteAction_ActionDeleteCommandName);
				for (int i = 0; i < objects.size(); i++) {
					EditPart object = (EditPart) objects.get(i);
					Command cmd = object.getCommand(deleteReq);
					if (cmd != null) compoundCmd.add(cmd);
				}
				getCommandStack().execute(compoundCmd);
				return true;
			}
			return super.keyPressed(event);
		}
	};
	keyHandler.put(KeyStroke.getPressed(SWT.F2, 0),
			getActionRegistry().getAction(GEFActionConstants.DIRECT_EDIT));
	viewer.setKeyHandler(keyHandler);
	// Scroll-wheel Zoom
	viewer.setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.CTRL), 
			MouseWheelZoomHandler.SINGLETON);
}

protected void createActions() {
	super.createActions();
	
	Action action = new DirectEditAction((IWorkbenchPart)this);
	getActionRegistry().registerAction(action);
	getSelectionActions().add(action.getId());
}

protected PaletteViewerProvider createPaletteViewerProvider() {
	return new PaletteViewerProvider(getEditDomain()) {
		protected void configurePaletteViewer(PaletteViewer viewer) {
			super.configurePaletteViewer(viewer);
			viewer.setCustomizer(new PaletteCustomizer() {
				public void revertToSaved() {}
				public void save() {}
			});
			viewer.addDragSourceListener(new TemplateTransferDragSourceListener(viewer));
		}
	};
}

protected FlyoutPreferences getPalettePreferences() {
	return new FlyoutPreferences() {
		public int getDockLocation() {
			return EDiagramPlugin.getPlugin().getPluginPreferences()
					.getInt(PALETTE_DOCK_LOCATION);
		}
		public int getPaletteState() {
			return EDiagramPlugin.getPlugin().getPluginPreferences()
					.getInt(PALETTE_STATE);
		}
		public int getPaletteWidth() {
			return EDiagramPlugin.getPlugin().getPluginPreferences()
					.getInt(PALETTE_SIZE);
		}
		public void setDockLocation(int location) {
			EDiagramPlugin.getPlugin().getPluginPreferences()
					.setValue(PALETTE_DOCK_LOCATION, location);
		}
		public void setPaletteState(int state) {
			EDiagramPlugin.getPlugin().getPluginPreferences()
				.setValue(PALETTE_STATE, state);
		}
		public void setPaletteWidth(int width) {
			EDiagramPlugin.getPlugin().getPluginPreferences()
				.setValue(PALETTE_SIZE, width);
		}
	};
}

public Object getAdapter(Class type) {
	if (type == IPropertySheetPage.class) {
		PropertySheetPage page = new PropertySheetPage();
		UndoablePropertySheetEntry rootEntry = 
				new UndoablePropertySheetEntry(getCommandStack());
		AdapterFactory[] factories = new AdapterFactory[2];
		factories[0] = new ModelItemProviderAdapterFactory(); 
		factories[1] = new EcoreItemProviderAdapterFactory();
		ComposedAdapterFactory superFactory = new ComposedAdapterFactory(factories);
		rootEntry.setPropertySourceProvider(new GEFPropertySourceProvider(superFactory));
		page.setRootEntry(rootEntry);
		return page;
	} else if (type == IContentOutlinePage.class)
		return new EDiagramOutlinePage(diagram, getActionRegistry());
	return super.getAdapter(type);
}

protected PaletteRoot getPaletteRoot() {
	if (paletteRoot == null)
		paletteRoot = EDiagramPaletteFactory.createPalette();
	return paletteRoot;
}

public void doSave(IProgressMonitor monitor) {
	try {
		for (Iterator iter = rsrcSet.getResources().iterator(); iter.hasNext();)
			((Resource)iter.next()).save(Collections.EMPTY_MAP);
		getCommandStack().markSaveLocation();
	} catch (IOException ioe) {
		String error = "The resource cannot be saved while there are errors:\n\n"
				+ ioe.getLocalizedMessage();
		if (ioe instanceof IOWrappedException && ((IOWrappedException)ioe)
				.getWrappedException() instanceof DanglingHREFException)
			error += "\n\nYou most likely either have references to deleted elements " +
					"or some newly created element hasn't been assigned a container yet.";
		EDiagramPlugin.INSTANCE.log(ioe);
		MessageDialog dialog = new MessageDialog(getGraphicalControl().getShell(), 
				"Errors Detected", null, error, MessageDialog.WARNING, 
				new String[] {"OK"}, 0);
		dialog.open();
	}
}

public void doSaveAs() {
}

protected void initializeGraphicalViewer() {
	super.initializeGraphicalViewer();
	GraphicalViewer viewer = getGraphicalViewer();
	viewer.setContents(diagram);
	viewer.addDropTargetListener(new DiagramDropTargetListener(viewer));
	viewer.addDropTargetListener(new EDiagramPaletteDropListener(viewer));
	
	// add the router
	ScalableFreeformRootEditPart root = 
			(ScalableFreeformRootEditPart)viewer.getRootEditPart();
	ConnectionLayer connLayer =
			(ConnectionLayer)root.getLayer(LayerConstants.CONNECTION_LAYER);
	GraphicalEditPart contentEditPart = (GraphicalEditPart)root.getContents();
	FanRouter router = new FanRouter();
	router.setSeparation(20);
	ShortestPathConnectionRouter spRouter = 
			new ShortestPathConnectionRouter(contentEditPart.getFigure()); 
	router.setNextRouter(spRouter);
	connLayer.setConnectionRouter(router);
	contentEditPart.getContentPane().addLayoutListener(spRouter.getLayoutListener());
}

public boolean isSaveAsAllowed() {
	return false;
}

protected void setInput(IEditorInput input) {
	super.setInput(input);

	IFile file = ((IFileEditorInput)input).getFile();
	rsrcSet = new ResourceSetImpl();
	URI uri = URI.createURI(file.getFullPath().toString());
	Resource resource = rsrcSet.getResource(uri, true);
	diagram = (Diagram)resource.getContents().get(0);
	
	setPartName(file.getName());
	setContentDescription(file.getFullPath().toString());
}

}