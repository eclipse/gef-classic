package org.eclipse.gef.dnd;

import org.eclipse.gef.EditPartViewer;

/**
 * Used to move EditParts around in a single viewer.  
 */
public class LocalTransfer extends SimpleObjectTransfer {

private static final LocalTransfer instance = new LocalTransfer();
private static final String TYPE_NAME = "Local Transfer"//$NON-NLS-1$
	+ System.currentTimeMillis()
	+ ":" + instance.hashCode();//$NON-NLS-1$
private static final int TYPEID = registerType(TYPE_NAME);

private static EditPartViewer viewer;

/**
 * Returns the singleton instance.
 * @return The singleton instance */
public static LocalTransfer getInstance() {
	return instance;
}

private LocalTransfer() { }

/**
 * @see Transfer#getTypeIds()
 */
protected int[] getTypeIds() {
	return new int[] {TYPEID};
}

/**
 * @see Transfer#getTypeNames()
 */
protected String[] getTypeNames() {
	return new String[] {TYPE_NAME};
}

/**
 * Returns the viewer where the drag started.
 * @return The viewer where the drag started */
public EditPartViewer getViewer() {
	return viewer;
}

/**
 * Sets the viewer where the drag started.
 * @param epv The viewer */
public void setViewer(EditPartViewer epv) {
	viewer = epv;
}

}
