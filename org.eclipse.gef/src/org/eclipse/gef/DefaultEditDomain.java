package org.eclipse.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.ui.IEditorPart;

import org.eclipse.gef.palette.PaletteListener;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.tools.SelectionTool;
import org.eclipse.gef.ui.palette.PaletteViewer;

/**
 * A default implementation of {@link EditDomain}. A {@link
 * org.eclipse.gef.commands.DefaultCommandStack} is used by default. An {@link
 * IEditorPart} is required in the constructor, but it can be <code>null</code>.
 * <P>
 * A {@link org.eclipse.gef.tools.SelectionTool} will be the active Tool until:
 * <UL>
 *   <LI>{@link #setTool(Tool)} is called with some other Tool
 *   <LI>A {@link org.eclipse.gef.palette.PaletteRoot} is provided which contains a
 *   default entry which is a {@link org.eclipse.gef.palette.ToolEntry}. In which
 *   case that entry's tool is made the active Tool.
 * </UL>
 * <P>
 * DefaultEditDomain can be configured with a {@link PaletteViewer}. When provided, the
 * DefaultEditDomain will listen for {@link org.eclipse.gef.palette.PaletteEvent
 * PaletteEvents}, and will switch the active Tool automatically in response.
 */
public class DefaultEditDomain
	extends EditDomain
{

private IEditorPart editorPart;

/**
 * Constructs a DefaultEditDomain with the specified IEditorPart
 * @param editorPart <code>null</code> or an IEditorPart
 */	
public DefaultEditDomain(IEditorPart editorPart) {
	setEditorPart(editorPart);
}

/** * @return the IEditorPart for this EditDomain */
public IEditorPart getEditorPart() {
	return editorPart;
}

/**
 * Sets the IEditorPart for this EditDomain.
 * @param editorPart the editor */
protected void setEditorPart(IEditorPart editorPart) {
	this.editorPart = editorPart;
}


}
