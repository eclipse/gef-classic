package org.eclipse.gef.ui.actions;

import org.eclipse.gef.dnd.NativeTemplateTransfer;
import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.palette.PaletteEvent;
import org.eclipse.gef.palette.PaletteListener;
import org.eclipse.gef.palette.PaletteTemplateEntry;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;

/**
 * Copies the currently selected template in the palatte to the system clipboard.
 * @author Eric Bordeau
 */
public class CopyTemplateAction 
	extends EditorPartAction
	implements PaletteListener
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
	viewer.addPaletteListener(this);
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
 * @see org.eclipse.gef.palette.PaletteListener#entrySelected(org.eclipse.gef.palette.PaletteEvent)
 */
public void entrySelected(PaletteEvent event) {
	if (event.getEntry() instanceof PaletteTemplateEntry)
		selectedEntry = (PaletteTemplateEntry)event.getEntry();
	else 
		selectedEntry = null;
	refresh();
}

/**
 * @see org.eclipse.gef.ui.actions.EditorPartAction#init()
 */
protected void init() {
	setId(IWorkbenchActionConstants.COPY);
	setText(GEFMessages.CopyAction_ActionLabelText);
}

/**
 * @see org.eclipse.jface.action.Action#run()
 */
public void run() {
	Clipboard clipboard = new Clipboard(Display.getCurrent());
	clipboard.setContents(new Object[] {selectedEntry.getTemplate()}, 
							new Transfer[] {NativeTemplateTransfer.getInstance()});
	clipboard.dispose();
}

}
