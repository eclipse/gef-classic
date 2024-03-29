/*******************************************************************************
 * Copyright (c) 2024 Johannes Kepler University Linz.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Alois Zoitl - initial API and implementation
 *******************************************************************************/

package org.eclipse.gef.examples.shapes;

import org.eclipse.osgi.util.NLS;

@SuppressWarnings("squid:S3008") // tell sonar the java naming convention does not make sense for this class
public class ShapesExampleMessages extends NLS {
	private static final String BUNDLE_NAME = ShapesExampleMessages.class.getPackageName() + ".messages"; //$NON-NLS-1$
	public static String ConnectionCreateCommand_ConnectionCreation;
	public static String ConnectionDeleteCommand_ConnectionDeletion;
	public static String ConnectionReconnectCommand_MoveConnectionEndPoint;
	public static String ConnectionReconnectCommand_MoveConnectionStartPoint;
	public static String EllipticalShape_Ellipse;
	public static String RectangularShape_Rectangle;
	public static String Shape_Height;
	public static String Shape_NotANumber;
	public static String Shape_ValueMustBeGreaterOrEqualZero;
	public static String Shape_Width;
	public static String Shape_X;
	public static String Shape_Y;
	public static String ShapeCreateCommand_ShapeCreation;
	public static String ShapeDeleteCommand_ShapeDeletion;
	public static String ShapesCreationWizard_CreateANewFile;
	public static String ShapesCreationWizard_FileNameMustEndWith;
	public static String ShapesEditorPaletteFactory_CreateDashedLineConnection;
	public static String ShapesEditorPaletteFactory_CreateEllipticalShape;
	public static String ShapesEditorPaletteFactory_CreateRectangularShape;
	public static String ShapesEditorPaletteFactory_CreateSolidLineConnection;
	public static String ShapesEditorPaletteFactory_DashedConnection;
	public static String ShapesEditorPaletteFactory_Ellipse;
	public static String ShapesEditorPaletteFactory_Rectangle;
	public static String ShapesEditorPaletteFactory_Shapes;
	public static String ShapesEditorPaletteFactory_SolidConnection;
	public static String ShapesEditorPaletteFactory_Tools;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, ShapesExampleMessages.class);
	}

	private ShapesExampleMessages() {
	}
}
