package org.eclipse.gef.ui.actions;

import org.eclipse.jface.viewers.*;
import org.eclipse.ui.IEditorPart;

import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.palette.PaletteTemplateEntry;
import org.eclipse.gef.ui.palette.editparts.TemplateEditPart;

/**
 * Copies the currently selected template in the palatte to the default GEF 
 * {@link org.eclipse.gef.ui.actions.Clipboard}.
 * @author Eric Bordeau
 */
public class CopyTemplateAction 
	extends EditorPartAction
	implements ISelectionChangedListener
{

private TemplateEditPart selectedPart;

/**
 * Constructs a new CopyTemplateAction.  You must manually add this action to the palette
 * viewer's list of selection listeners.  Otherwise, this action's enabled state won't be
 * updated properly.
 * 
 * @see org.eclipse.gef.ui.actions.EditorPartAction#EditorPartAction(IEditorPart)
 */
public CopyTemplateAction(IEditorPart editor) {
	super(editor);
}

/**
 * Returns whether the selected EditPart is a {@link TemplateEditPart}.
 * @return whether the selected EditPart is a TemplateEditPart
 */
protected boolean calculateEnabled() {
	return selectedPart != null;
}

/**
 * @see org.eclipse.gef.ui.actions.EditorPartAction#dispose()
 */
public void dispose() {
	selectedPart = null;
}

/**
 * @see org.eclipse.gef.ui.actions.EditorPartAction#init()
 */
protected void init() {
	setId(GEFActionConstants.COPY);
	setText(GEFMessages.CopyAction_ActionLabelText);
}

/**
 * Sets the default {@link Clipboard Clipboard's} contents to be the currently selected
 * template.
 */
public void run() {
	PaletteTemplateEntry entry = (PaletteTemplateEntry)selectedPart.getModel();
	Clipboard.getDefault().setContents(entry.getTemplate());
}

/**
 * Sets the selected EditPart and refreshes the enabled state of this action.
 * 
 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
 */
public void selectionChanged(SelectionChangedEvent event) {
	ISelection s = event.getSelection();
	if (!(s instanceof IStructuredSelection))
		return;
	IStructuredSelection selection = (IStructuredSelection)s;
	selectedPart = null;
	if (selection != null && selection.size() == 1) {
		Object obj = selection.getFirstElement();
		if (obj instanceof TemplateEditPart)
			selectedPart = (TemplateEditPart)obj;
	}
	refresh();
}

}
