package org.eclipse.mylar.zest.core.internal.timelineview;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.mylar.zest.core.internal.timelineview.model.TimeLineDiagram;


public class TimeLineEditor extends GraphicalEditor {
	
	public TimeLineEditor() {
		setEditDomain(new DefaultEditDomain(this));
	}

	protected void initializeGraphicalViewer() {
		super.configureGraphicalViewer();
		
		GraphicalViewer viewer = getGraphicalViewer();
		viewer.setEditPartFactory(new TimeLineEditPartFactory());
		viewer.setRootEditPart(new ScalableFreeformRootEditPart());
		viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));
		
		viewer.setContents(new TimeLineDiagram() );

	}

	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	public void doSaveAs() {
		// TODO Auto-generated method stub

	}


	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

}
