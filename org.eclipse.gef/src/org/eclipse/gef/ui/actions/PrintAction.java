package org.eclipse.gef.ui.actions;

import org.eclipse.swt.SWT;
import org.eclipse.swt.printing.PrintDialog;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.PrintGraphicalViewerOperation;

/**
 * @author hudsonr
 * @since 2.1
 */
public class PrintAction extends EditorPartAction {

/**
 * Constructor for PrintAction.
 * @param editor The EditorPart associated with this PrintAction
 */
public PrintAction(IEditorPart editor) {
	super(editor);
}

/**
 * @see org.eclipse.gef.ui.actions.EditorPartAction#calculateEnabled()
 */
protected boolean calculateEnabled() {
	return true;
}

/**
 * @see org.eclipse.jface.action.Action#getId()
 */
public String getId() {
	return IWorkbenchActionConstants.PRINT;
}

/**
 * @see org.eclipse.jface.action.Action#run()
 */
public void run() {
	GraphicalViewer viewer;
	viewer = (GraphicalViewer)getEditorPart().getAdapter(GraphicalViewer.class);
	
	PrintDialog dialog = new PrintDialog(viewer.getControl().getShell(), SWT.NULL);
	PrinterData data = dialog.open();
	
	if (data != null) {
		PrintGraphicalViewerOperation op = 
					new PrintGraphicalViewerOperation(new Printer(data), viewer);
		op.run(getEditorPart().getTitle());
	}	
}

}