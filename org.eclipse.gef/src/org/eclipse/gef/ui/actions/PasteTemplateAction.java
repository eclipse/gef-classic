package org.eclipse.gef.ui.actions;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;

/**
 * If the current object on the system clipboard is a template and the current viewer is
 * a graphical viewer, this action will paste the template to the viewer.
 * @author Eric Bordeau
 */
public abstract class PasteTemplateAction extends SelectionAction {

public static final String defID = "org.eclipse.ui.edit.paste";

private CreateRequest request;

/**
 * Constructor for PasteTemplateAction.
 * @param editor
 */
public PasteTemplateAction(IEditorPart editor) {
	super(editor);
}

/**
 * @see org.eclipse.gef.ui.actions.EditorPartAction#calculateEnabled()
 */
protected boolean calculateEnabled() {
	return getClipboardContents() != null;
}

protected Command createPasteCommand() {
	CreateRequest request = new CreateRequest();
	request.setFactory(getFactory(getClipboardContents()));
	request.setLocation(new Point(10, 10));
	EditPart ep = (EditPart)getSelectedObjects().get(0);
	return ep.getCommand(request);
}

protected Object getClipboardContents() {
	return Clipboard.getDefault().getContents();
}

/**
 * Returns the appropriate Factory object to be used for the specified template. This
 * Factory is used on the CreateRequest that is sent to the target EditPart.
 * @param template the template Object
 * @return a Factory
 */
protected abstract CreateRequest.Factory getFactory(Object template);

/**
 * @see org.eclipse.gef.ui.actions.EditorPartAction#init()
 */
protected void init() {
	setId(IWorkbenchActionConstants.PASTE);
	setText(GEFMessages.PasteAction_ActionLabelText);
	setActionDefinitionId(defID);	
}

/**
 * @see org.eclipse.jface.action.Action#run()
 */
public void run() {
	execute(createPasteCommand());
}

}
