package org.eclipse.gef.examples.logicdesigner.palette;

import org.eclipse.gef.palette.*;
import org.eclipse.gef.ui.palette.PaletteCustomizer;
import org.eclipse.gef.ui.palette.customize.DefaultEntryPage;
import org.eclipse.gef.ui.palette.customize.DrawerEntryPage;
import org.eclipse.gef.ui.palette.customize.EntryPage;

/**
 * PaletteCustomizer for the logic example.
 * 
 * @author Pratik Shah
 */
public class LogicPaletteCustomizer 
	extends PaletteCustomizer 
{
	
protected static final String ERROR_MESSAGE = "Name contains invalid character: *";
	
/**
 * @see org.eclipse.gef.ui.palette.PaletteCustomizer#getPropertiesPage(PaletteEntry)
 */
public EntryPage getPropertiesPage(PaletteEntry entry) {
	if (entry.getType().equals(PaletteDrawer.PALETTE_TYPE_DRAWER)) {
		return new LogicDrawerEntryPage();
	}
	return new LogicEntryPage();
}

/**
 * @see org.eclipse.gef.ui.palette.PaletteCustomizer#revertToSaved()
 */
public void revertToSaved() {
}


/**
 * @see org.eclipse.gef.ui.palette.PaletteCustomizer#dialogClosed(PaletteEntry)
 */
public void save() {
}

private class LogicEntryPage extends DefaultEntryPage {
	protected void handleNameChanged(String text) {
		if (text.indexOf('*') >= 0) {
			getPageContainer().showProblem(ERROR_MESSAGE);
		} else {
			super.handleNameChanged(text);
			getPageContainer().clearProblem();
		}
	}
}

private class LogicDrawerEntryPage extends DrawerEntryPage {
	protected void handleNameChanged(String text) {
		if (text.indexOf('*') >= 0) {
			getPageContainer().showProblem(ERROR_MESSAGE);
		} else {
			super.handleNameChanged(text);
			getPageContainer().clearProblem();
		}
	}
}

}
