package org.eclipse.gef.ui.actions;

import java.util.List;

import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.palette.PaletteTemplateEntry;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.TemplateEditPart;
import org.eclipse.ui.IEditorPart;

/**
 * Copies the currently selected template in the palatte to the system clipboard.
 * @author Eric Bordeau
 */
public class CopyTemplateAction 
	extends SelectionAction
{

private PaletteTemplateEntry selectedEntry;

/**
 * CopyTemplateAction constructor.  Adds this action as a palette listener to the palette
 * veiwer that was passed in.
 * @param editor the editor
 * @param viewer the palette viewer
 */
public CopyTemplateAction(IEditorPart editor, PaletteViewer viewer) {
	super(editor);
}

/**
 * If you use this constructor, you must manually add this action to the palette viewer's
 * list of palette listeners.  Otherwise, this action's enabled state won't be updated
 * properly. 
 * 
 * @see org.eclipse.gef.ui.actions.EditorPartAction#EditorPartAction(IEditorPart)
 */
public CopyTemplateAction(IEditorPart editor) {
	super(editor);
}

/**
 * @see org.eclipse.gef.ui.actions.EditorPartAction#calculateEnabled()
 */
protected boolean calculateEnabled() {
	return selectedEntry != null;
}

/**
 * @see org.eclipse.gef.ui.actions.EditorPartAction#dispose()
 */
public void dispose() {
	selectedEntry = null;
}

/**
 * @see org.eclipse.gef.ui.actions.SelectionAction#handleSelectionChanged()
 */
protected void handleSelectionChanged() {
	selectedEntry = null;
	List sel = getSelectedObjects();
	if (sel != null && sel.size() == 1) {
		Object obj = sel.get(0);
		if (obj instanceof TemplateEditPart) {
			TemplateEditPart ep = (TemplateEditPart)obj;
			selectedEntry = (PaletteTemplateEntry)ep.getModel();
		}
	}
	super.handleSelectionChanged();
}

/**
 * @see org.eclipse.gef.ui.actions.EditorPartAction#init()
 */
protected void init() {
	setId(GEFActionConstants.COPY);
	setText(GEFMessages.CopyAction_ActionLabelText);
}

/**
 * @see org.eclipse.jface.action.Action#run()
 */
public void run() {
	Clipboard.getDefault().setContents(selectedEntry.getTemplate());
}

}
