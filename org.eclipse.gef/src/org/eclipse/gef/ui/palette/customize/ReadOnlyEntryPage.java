package org.eclipse.gef.ui.palette.customize;

/**
 * This is a read-only version of the {@link DefaultEntryPage}
 * 
 * @author Pratik Shah
 */
public class ReadOnlyEntryPage extends DefaultEntryPage {

/**
 * Does nothing for read-only pages.
 * 
 * @see DefaultEntryPage#handleDescriptionChanged(String) */
protected void handleDescriptionChanged(String text) { 
}

/**
 * Empty method
 * <p>
 * Since this is a read-only version, there is no need to update the entry.
 * </p>
 * 
 * @param	isChecked	Ignored
 * @see	DefaultEntryPage#handleHiddenSelected(boolean)
 */
protected void handleHiddenSelected(boolean isChecked) {
}

/**
 * Empty method
 * <p>
 * Since this is a read-only version, there is no need to update the entry.
 * </p>
 * 
 * @param	text	Ignored
 * @see	DefaultEntryPage#handleNameChanged(String)
 */
protected void handleNameChanged(String text) {
}

/**
 * Returns true since this is a read-only page
 * 
 * @see org.eclipse.gef.ui.palette.customize.DefaultEntryPage#isReadOnly()
 */
protected boolean isReadOnly() {
	return true;
}


}
