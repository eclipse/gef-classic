package org.eclipse.gef.examples.logicdesigner;

import org.eclipse.gef.palette.DefaultPaletteEntry;
import org.eclipse.gef.ui.palette.customize.DefaultEntryPage;

/**
 * @author Pratik Shah
 */
public class LogicEntryPage extends DefaultEntryPage {
	
protected static final String ERROR_MESSAGE = "Name contains invalid character: *";

/**
 * @see org.eclipse.gef.ui.palette.customize.DefaultEntryPage#handleDescriptionChanged(String)
 */
protected void handleDescriptionChanged(String text) {
	((DefaultPaletteEntry)getEntry()).setDescription(text);
}

/**
 * @see org.eclipse.gef.ui.palette.customize.DefaultEntryPage#handleHiddenSelected(boolean)
 */
protected void handleHiddenSelected(boolean b) {
	((DefaultPaletteEntry)getEntry()).setVisible(!b);
}

/**
 * @see org.eclipse.gef.ui.palette.customize.DefaultEntryPage#handleNameChanged(String)
 */
protected void handleNameChanged(String text) {
	if( text.indexOf('*') >= 0 ){
		getPageContainer().showProblem(ERROR_MESSAGE);
	} else {
		((DefaultPaletteEntry)getEntry()).setLabel(text);
		getPageContainer().clearProblem();
	}
}

}
