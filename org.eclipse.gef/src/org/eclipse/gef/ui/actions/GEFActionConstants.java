package org.eclipse.gef.ui.actions;

import org.eclipse.ui.IWorkbenchActionConstants;

/**
 * Defines the names of those actions which are preregistered with the
 * {@link org.eclipse.gef.ui.parts.GraphicalEditor}. This interface extends the set of
 * names available from <code>IWorkbenchActionConstants</code>. It also defines the names
 * of the menu groups in a graphical editor's context menu.
 */
public interface GEFActionConstants extends IWorkbenchActionConstants {

/**
 * Align bottom action id.
 * Value: <code>"eclipse.gef.align_bottom"</code>
 */
static final String ALIGN_BOTTOM = "eclipse.gef.align_bottom";//$NON-NLS-1$

/**
 * Align center (horizontal) action id.
 * Value: <code>"eclipse.gef.align_center"</code>
 */
static final String ALIGN_CENTER = "eclipse.gef.align_center";//$NON-NLS-1$

/**
 * Align left action id.
 * Value: <code>"eclipse.gef.align_left"</code>
 */
static final String ALIGN_LEFT = "eclipse.gef.align_left";//$NON-NLS-1$

/**
 * Align middle (vertical) action id.
 * Value: <code>"eclipse.gef.align_middle"</code>
 */
static final String ALIGN_MIDDLE = "eclipse.gef.align_middle";//$NON-NLS-1$

/**
 * Align right action id.
 * Value: <code>"eclipse.gef.align_right"</code>
 */
static final String ALIGN_RIGHT = "eclipse.gef.align_right";//$NON-NLS-1$

/**
 * Align top action id.
 * Value: <code>"eclipse.gef.align_top"</code>
 */
static final String ALIGN_TOP = "eclipse.gef.align_top";//$NON-NLS-1$

/**
 * Direct edit action id. 
 * Value: <code>"eclipse.gef.direct_edit"</code>
 */
static final String DIRECT_EDIT = "eclipse.gef.direct_edit";//$NON-NLS-1$

/** 
 * Context menu group for copy/paste related actions. 
 * Value: <code>"eclipse.gef.group.copy"</code>
 */
static final String GROUP_COPY = "eclipse.gef.group.copy"; //$NON-NLS-1$

/** 
 * Context menu group for text manipulation actions. 
 * Value: <code>"eclipse.gef.group.edit"</code>
 */
static final String GROUP_EDIT = "eclipse.gef.group.edit"; //$NON-NLS-1$

/** 
 * Context menu group for find/replace related actions. 
 * Value: <code>"eclipse.gef.group.find"</code>
 */
static final String GROUP_FIND = "eclipse.gef.group.find"; //$NON-NLS-1$

/** 
 * Context menu group for print related actions. 
 * Value: <code>"eclipse.gef.group.print"</code>
 */
static final String GROUP_PRINT = "eclipse.gef.group.print"; //$NON-NLS-1$

/** 
 * Context menu group for actions which do not fit in one of the other categories. 
 * Value: <code>"eclipse.gef.group.rest"</code>
 */
static final String GROUP_REST = "eclipse.gef.group.rest"; //$NON-NLS-1$	

/** 
 * Context menu group for save related actions. 
 * Value: <code>"eclipse.gef.group.save"</code>
 */
static final String GROUP_SAVE = "eclipse.gef.group.save"; //$NON-NLS-1$

/** 
 * Context menu group for undo/redo related actions. 
 * Value: <code>"eclipse.gef.group.undo"</code>
 */
static final String GROUP_UNDO = "eclipse.gef.group.undo"; //$NON-NLS-1$

}
