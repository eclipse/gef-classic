/*******************************************************************************
 * Copyright (c) 2004, 2010 Elias Volanakis and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Elias Volanakis - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.shapes;

import org.eclipse.jface.resource.ImageDescriptor;

import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteToolbar;
import org.eclipse.gef.palette.PanningSelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.requests.SimpleFactory;

import org.eclipse.gef.examples.shapes.model.Connection;
import org.eclipse.gef.examples.shapes.model.EllipticalShape;
import org.eclipse.gef.examples.shapes.model.RectangularShape;

/**
 * Utility class that can create a GEF Palette.
 *
 * @see #createPalette()
 * @author Elias Volanakis
 */
final class ShapesEditorPaletteFactory {

	/** Create the "Shapes" drawer. */
	private static PaletteContainer createShapesDrawer() {
		PaletteDrawer componentsDrawer = new PaletteDrawer(ShapesExampleMessages.ShapesEditorPaletteFactory_Shapes);

		CombinedTemplateCreationEntry component = new CombinedTemplateCreationEntry(ShapesExampleMessages.ShapesEditorPaletteFactory_Ellipse,
				ShapesExampleMessages.ShapesEditorPaletteFactory_CreateEllipticalShape, EllipticalShape.class, new SimpleFactory<>(EllipticalShape.class),
				ImageDescriptor.createFromFile(ShapesPlugin.class, "icons/ellipse16.gif"), //$NON-NLS-1$
				ImageDescriptor.createFromFile(ShapesPlugin.class, "icons/ellipse24.gif")); //$NON-NLS-1$
		componentsDrawer.add(component);

		component = new CombinedTemplateCreationEntry(ShapesExampleMessages.ShapesEditorPaletteFactory_Rectangle, ShapesExampleMessages.ShapesEditorPaletteFactory_CreateRectangularShape, RectangularShape.class,
				new SimpleFactory<>(RectangularShape.class),
				ImageDescriptor.createFromFile(ShapesPlugin.class, "icons/rectangle16.gif"), //$NON-NLS-1$
				ImageDescriptor.createFromFile(ShapesPlugin.class, "icons/rectangle24.gif")); //$NON-NLS-1$
		componentsDrawer.add(component);

		return componentsDrawer;
	}

	/**
	 * Creates the PaletteRoot and adds all palette elements. Use this factory
	 * method to create a new palette for your graphical editor.
	 *
	 * @return a new PaletteRoot
	 */
	static PaletteRoot createPalette() {
		PaletteRoot palette = new PaletteRoot();
		palette.add(createToolsGroup(palette));
		palette.add(createShapesDrawer());
		return palette;
	}

	/** Create the "Tools" group. */
	private static PaletteContainer createToolsGroup(PaletteRoot palette) {
		PaletteToolbar toolbar = new PaletteToolbar(ShapesExampleMessages.ShapesEditorPaletteFactory_Tools);

		// Add a selection tool to the group
		ToolEntry tool = new PanningSelectionToolEntry();
		toolbar.add(tool);
		palette.setDefaultEntry(tool);

		// Add a marquee tool to the group
		toolbar.add(new MarqueeToolEntry());

		// Add (solid-line) connection tool
		tool = new ConnectionCreationToolEntry(ShapesExampleMessages.ShapesEditorPaletteFactory_SolidConnection, ShapesExampleMessages.ShapesEditorPaletteFactory_CreateSolidLineConnection,
				new CreationFactory() {
					@Override
					public Object getNewObject() {
						return null;
					}

					// see ShapeEditPart#createEditPolicies()
					// this is abused to transmit the desired line style
					@Override
					public Object getObjectType() {
						return Connection.SOLID_CONNECTION;
					}
				}, ImageDescriptor.createFromFile(ShapesPlugin.class, "icons/connection_s16.gif"), //$NON-NLS-1$
				ImageDescriptor.createFromFile(ShapesPlugin.class, "icons/connection_s24.gif")); //$NON-NLS-1$
		toolbar.add(tool);

		// Add (dashed-line) connection tool
		tool = new ConnectionCreationToolEntry(ShapesExampleMessages.ShapesEditorPaletteFactory_DashedConnection, ShapesExampleMessages.ShapesEditorPaletteFactory_CreateDashedLineConnection,
				new CreationFactory() {
					@Override
					public Object getNewObject() {
						return null;
					}

					// see ShapeEditPart#createEditPolicies()
					// this is abused to transmit the desired line style
					@Override
					public Object getObjectType() {
						return Connection.DASHED_CONNECTION;
					}
				}, ImageDescriptor.createFromFile(ShapesPlugin.class, "icons/connection_d16.gif"), //$NON-NLS-1$
				ImageDescriptor.createFromFile(ShapesPlugin.class, "icons/connection_d24.gif")); //$NON-NLS-1$
		toolbar.add(tool);

		return toolbar;
	}

	/** Utility class. */
	private ShapesEditorPaletteFactory() {
		// Utility class
	}

}