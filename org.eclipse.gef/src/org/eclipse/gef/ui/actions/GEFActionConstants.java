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
 * Value: <code>"org.eclipse.gef.align_bottom"</code>
 */
static final String ALIGN_BOTTOM = "org.eclipse.gef.align_bottom";//$NON-NLS-1$

/**
 * Align center (horizontal) action id.
 * Value: <code>"org.eclipse.gef.align_center"</code>
 */
static final String ALIGN_CENTER = "org.eclipse.gef.align_center";//$NON-NLS-1$

/**
 * Align left action id.
 * Value: <code>"org.eclipse.gef.align_left"</code>
 */
static final String ALIGN_LEFT = "org.eclipse.gef.align_left";//$NON-NLS-1$

/**
 * Align middle (vertical) action id.
 * Value: <code>"org.eclipse.gef.align_middle"</code>
 */
static final String ALIGN_MIDDLE = "org.eclipse.gef.align_middle";//$NON-NLS-1$

/**
 * Align right action id.
 * Value: <code>"org.eclipse.gef.align_right"</code>
 */
static final String ALIGN_RIGHT = "org.eclipse.gef.align_right";//$NON-NLS-1$

/**
 * Align top action id.
 * Value: <code>"org.eclipse.gef.align_top"</code>
 */
static final String ALIGN_TOP = "org.eclipse.gef.align_top";//$NON-NLS-1$

/** 
 * Context menu for an editor. 
 * Value: <code>"org.eclipse.gef.contextmenu.editor"</code>
 */
static final String CONTEXT_MENU_EDITOR = "org.eclipse.gef.contextmenu.editor"; //$NON-NLS-1$

/** 
 * Context menu for an outline. 
 * Value: <code>"org.eclipse.gef.contextmenu.outline"</code>
 */
static final String CONTEXT_MENU_OUTLINE = "org.eclipse.gef.contextmenu.outline"; //$NON-NLS-1$

/** 
 * Context menu for a palette. 
 * Value: <code>"org.eclipse.gef.contextmenu.palette"</code>
 */
static final String CONTEXT_MENU_PALETTE = "org.eclipse.gef.contextmenu.palette"; //$NON-NLS-1$

/**
 * Direct edit action id. 
 * Value: <code>"org.eclipse.gef.direct_edit"</code>
 */
static final String DIRECT_EDIT = "org.eclipse.gef.direct_edit";//$NON-NLS-1$

/** 
 * Context menu group for copy/paste related actions. 
 * Value: <code>"org.eclipse.gef.group.copy"</code>
 */
static final String GROUP_COPY = "org.eclipse.gef.group.copy"; //$NON-NLS-1$

/** 
 * Context menu group for EditPart manipulation actions. 
 * Value: <code>"org.eclipse.gef.group.edit"</code>
 */
static final String GROUP_EDIT = "org.eclipse.gef.group.edit"; //$NON-NLS-1$

/** 
 * Context menu group for find/replace related actions. 
 * Value: <code>"org.eclipse.gef.group.find"</code>
 */
static final String GROUP_FIND = "org.eclipse.gef.group.find"; //$NON-NLS-1$

/** 
 * Context menu group for print related actions. 
 * Value: <code>"org.eclipse.gef.group.print"</code>
 */
static final String GROUP_PRINT = "org.eclipse.gef.group.print"; //$NON-NLS-1$

/** 
 * Context menu group for actions which do not fit in one of the other categories. 
 * Value: <code>"org.eclipse.gef.group.rest"</code>
 */
static final String GROUP_REST = "org.eclipse.gef.group.rest"; //$NON-NLS-1$	

/** 
 * Context menu group for save related actions. 
 * Value: <code>"org.eclipse.gef.group.save"</code>
 */
static final String GROUP_SAVE = "org.eclipse.gef.group.save"; //$NON-NLS-1$

/** 
 * Context menu group for undo/redo related actions. 
 * Value: <code>"org.eclipse.gef.group.undo"</code>
 */
static final String GROUP_UNDO = "org.eclipse.gef.group.undo"; //$NON-NLS-1$

/** 
 * Zoom contribution id. 
 * Value: <code>"org.eclipse.gef.zoom"</code>
 */
static final String ZOOM = "org.eclipse.gef.zoom"; //$NON-NLS-1$

}
