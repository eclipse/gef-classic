package org.eclipse.gef.examples.logicdesigner.actions;

import org.eclipse.ui.IEditorPart;

import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.SelectionAction;

import org.eclipse.gef.examples.logicdesigner.LogicMessages;

/**
 * If the current object on the default GEF {@link org.eclipse.gef.ui.actions.Clipboard}
 * is a template and the current viewer is a graphical viewer, this action will paste the
 * template to the viewer.
 * @author Eric Bordeau
 */
public abstract class PasteTemplateAction extends SelectionAction {

private CreateRequest request;

/**
 * Constructor for PasteTemplateAction.
 * @param editor
 */
public PasteTemplateAction(IEditorPart editor) {
	super(editor);
}

/**
 * Returns <code>true</code> if the {@link Clipboard clipboard's} contents are not
 * <code>null</code> and the command returned by {@link #createPasteCommand()} can
 * execute.
 */
protected boolean calculateEnabled() {
	Command command = createPasteCommand();
	return getClipboardContents() != null && command != null && command.canExecute();
}

/**
 * Creates and returns a command (which may be <code>null</code>) to create a new EditPart
 * based on the template on the clipboard.
 * @return the paste command
 */
protected Command createPasteCommand() {
	if (getSelectedObjects() == null || getSelectedObjects().isEmpty())
		return null;
	CreateRequest request = new CreateRequest();
	request.setFactory(getFactory(getClipboardContents()));
	request.setLocation(new Point(10, 10));
	EditPart ep = (EditPart)getSelectedObjects().get(0);
	return ep.getCommand(request);
}

/**
 * Returns the contents of the default GEF {@link Clipboard}.
 * @return the clipboard's contents
 */
protected Object getClipboardContents() {
	return Clipboard.getDefault().getContents();
}

/**
 * Returns the appropriate Factory object to be used for the specified template. This
 * Factory is used on the CreateRequest that is sent to the target EditPart.
 * @param template the template Object
 * @return a Factory
 */
protected abstract CreationFactory getFactory(Object template);

/**
 * @see org.eclipse.gef.ui.actions.EditorPartAction#init()
 */
protected void init() {
	setId(GEFActionConstants.PASTE);
	setText(LogicMessages.PasteAction_ActionLabelText);
}

/**
 * Executes the command returned by {@link #createPasteCommand()}.
 */
public void run() {
	execute(createPasteCommand());
}

}
