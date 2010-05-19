/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.test;

import junit.framework.TestCase;

import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteStack;
import org.eclipse.gef.ui.palette.PaletteCustomizer;
import org.eclipse.gef.ui.palette.customize.PaletteDrawerFactory;
import org.eclipse.gef.ui.palette.customize.PaletteSeparatorFactory;
import org.eclipse.gef.ui.palette.customize.PaletteStackFactory;

public class PaletteCustomizerTest extends TestCase {

	class TestCustomizer extends PaletteCustomizer {
		public void revertToSaved() {
		}

		public void save() {
		}
	}

	private TestCustomizer customizer = new TestCustomizer();
	private PaletteDrawer drawerA;
	private PaletteDrawer drawerB;
	private PaletteDrawer drawerC;
	private PaletteRoot root;
	private PaletteEntry selection;
	private PaletteStack stackA;
	private PaletteStack stackB;

	private PaletteDrawerFactory getDrawerFactory() {
		return new PaletteDrawerFactory();
	}

	private PaletteSeparatorFactory getSeparatorFactory() {
		return new PaletteSeparatorFactory();
	}

	private PaletteStackFactory getStackFactory() {
		return new PaletteStackFactory();
	}

	private void reset() {
		root = new PaletteRoot();
		drawerA = new PaletteDrawer("A", null);
		drawerB = new PaletteDrawer("B", null);
		drawerC = new PaletteDrawer("C", null);
	}

	private void resetBottom() {
		reset();
		drawerA.add(new CombinedTemplateCreationEntry("TA", null, null, null,
				null, null));
		stackA = new PaletteStack("Stack", null, null);
		stackA.add(new CombinedTemplateCreationEntry("STA", null, null, null,
				null, null));
		stackA.add(selection = new CombinedTemplateCreationEntry("Selection",
				null, null, null, null, null));
		selection
				.setUserModificationPermission(PaletteEntry.PERMISSION_LIMITED_MODIFICATION);
		stackB = new PaletteStack("Stack2", null, null);
		stackB.setUserModificationPermission(PaletteEntry.PERMISSION_FULL_MODIFICATION);
		stackB.add(new CombinedTemplateCreationEntry("STB", null, null, null,
				null, null));
		drawerA.add(stackA);
		drawerA.add(stackB);
		root.add(drawerA);
		root.add(drawerB);
	}

	private void resetMiddle() {
		reset();
		drawerA.add(new CombinedTemplateCreationEntry("TA", null, null, null,
				null, null));
		drawerA.add(selection = new CombinedTemplateCreationEntry("Selection",
				null, null, null, null, null));
		drawerA.add(new CombinedTemplateCreationEntry("TB", null, null, null,
				null, null));
		root.add(drawerA);
		root.add(drawerB);
	}

	private void resetTop() {
		reset();
		drawerA.add(new CombinedTemplateCreationEntry("TA", null, null, null,
				null, null));
		drawerA.add(new CombinedTemplateCreationEntry("TB", null, null, null,
				null, null));
		selection = new PaletteStack("Stack", null, null);
		((PaletteStack) selection).add(new CombinedTemplateCreationEntry("STA",
				null, null, null, null, null));
		selection
				.setUserModificationPermission(PaletteEntry.PERMISSION_FULL_MODIFICATION);
		drawerB.add(selection);
		drawerB.add(new CombinedTemplateCreationEntry("BTB", null, null, null,
				null, null));
		root.add(drawerA);
		root.add(drawerB);
		root.add(drawerC);
	}

	/**
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * 
	 * 1-Root 2-Drawer A 3-Tool 4-Stack A 5-Tool 6-Selection (Limited
	 * Permissions) 8-Stack B (Full Permissions) 9-Tool 10-Drawer B
	 * 
	 */
	public void testBottomSelection() {
		resetBottom();
		drawerA.setUserModificationPermission(PaletteEntry.PERMISSION_FULL_MODIFICATION);
		stackA.setUserModificationPermission(PaletteEntry.PERMISSION_FULL_MODIFICATION);

		assertTrue(customizer.canMoveDown(selection));
		assertTrue(customizer.canMoveUp(selection));
		assertTrue(!customizer.canDelete(selection));

		assertTrue(getDrawerFactory().canCreate(selection));
		assertTrue(!getSeparatorFactory().canCreate(selection));
		assertTrue(!getStackFactory().canCreate(selection));

		customizer.performMoveUp(selection);
		assertEquals(0, stackA.getChildren().indexOf(selection));
		customizer.performMoveDown(selection);
		customizer.performMoveDown(selection);
		assertEquals(2, drawerA.getChildren().indexOf(selection));

		resetBottom();
		drawerA.setUserModificationPermission(PaletteEntry.PERMISSION_FULL_MODIFICATION);
		stackA.setUserModificationPermission(PaletteEntry.PERMISSION_LIMITED_MODIFICATION);

		assertTrue(customizer.canMoveUp(selection));
		assertTrue(!customizer.canMoveDown(selection));
		assertTrue(!customizer.canDelete(selection));

		assertTrue(getDrawerFactory().canCreate(selection));
		assertTrue(!getSeparatorFactory().canCreate(selection));
		assertTrue(!getStackFactory().canCreate(selection));

		customizer.performMoveUp(selection);
		assertEquals(0, stackA.getChildren().indexOf(selection));
		// only possible if move changes
		// customizer.performMoveDown(selection);
		// customizer.performMoveDown(selection);
		// assertEquals(2, drawerA.getChildren().indexOf(selection));

		resetBottom();
		drawerA.setUserModificationPermission(PaletteEntry.PERMISSION_FULL_MODIFICATION);
		stackA.setUserModificationPermission(PaletteEntry.PERMISSION_NO_MODIFICATION);

		assertTrue(!customizer.canMoveDown(selection));
		assertTrue(!customizer.canMoveUp(selection));
		assertTrue(!customizer.canDelete(selection));

		assertTrue(getDrawerFactory().canCreate(selection));
		assertTrue(!getSeparatorFactory().canCreate(selection));
		assertTrue(!getStackFactory().canCreate(selection));

		// move must change
		// customizer.performMoveUp(selection);
		// assertEquals(0, stackA.getChildren().indexOf(selection));
		// customizer.performMoveDown(selection);
		// customizer.performMoveDown(selection);
		// assertEquals(2, drawerA.getChildren().indexOf(selection));

		resetBottom();
		drawerA.setUserModificationPermission(PaletteEntry.PERMISSION_LIMITED_MODIFICATION);
		stackA.setUserModificationPermission(PaletteEntry.PERMISSION_FULL_MODIFICATION);

		assertTrue(customizer.canMoveDown(selection));
		assertTrue(customizer.canMoveUp(selection));
		assertTrue(!customizer.canDelete(selection));

		assertTrue(getDrawerFactory().canCreate(selection));
		assertTrue(!getSeparatorFactory().canCreate(selection));
		assertTrue(!getStackFactory().canCreate(selection));

		customizer.performMoveUp(selection);
		assertEquals(0, stackA.getChildren().indexOf(selection));
		customizer.performMoveDown(selection);
		customizer.performMoveDown(selection);
		assertEquals(0, stackB.getChildren().indexOf(selection));

		resetBottom();
		drawerA.setUserModificationPermission(PaletteEntry.PERMISSION_LIMITED_MODIFICATION);
		stackA.setUserModificationPermission(PaletteEntry.PERMISSION_LIMITED_MODIFICATION);

		assertTrue(customizer.canMoveUp(selection));
		assertTrue(!customizer.canMoveDown(selection));
		assertTrue(!customizer.canDelete(selection));

		assertTrue(getDrawerFactory().canCreate(selection));
		assertTrue(!getSeparatorFactory().canCreate(selection));
		assertTrue(!getStackFactory().canCreate(selection));

		customizer.performMoveUp(selection);
		assertEquals(0, stackA.getChildren().indexOf(selection));
		// customizer.performMoveDown(selection);
		// customizer.performMoveDown(selection);
		// assertEquals(0, stackB.getChildren().indexOf(selection));

		resetBottom();
		drawerA.setUserModificationPermission(PaletteEntry.PERMISSION_LIMITED_MODIFICATION);
		stackA.setUserModificationPermission(PaletteEntry.PERMISSION_NO_MODIFICATION);

		assertTrue(!customizer.canMoveDown(selection));
		assertTrue(!customizer.canMoveUp(selection));
		assertTrue(!customizer.canDelete(selection));

		assertTrue(getDrawerFactory().canCreate(selection));
		assertTrue(!getSeparatorFactory().canCreate(selection));
		assertTrue(!getStackFactory().canCreate(selection));

		// customizer.performMoveUp(selection);
		// assertEquals(0, stackA.getChildren().indexOf(selection));
		// customizer.performMoveDown(selection);
		// customizer.performMoveDown(selection);
		// assertEquals(0, stackB.getChildren().indexOf(selection));
	}

	/**
	 * 
	 * 1-Root 2-Drawer A 3-Tool 4-Selection 5-Tool 6-Drawer B
	 * 
	 */
	public void testMiddleSelection() {
		resetMiddle();
		drawerA.setUserModificationPermission(PaletteEntry.PERMISSION_FULL_MODIFICATION);

		assertTrue(customizer.canMoveDown(selection));
		assertTrue(customizer.canMoveUp(selection));
		assertTrue(customizer.canDelete(selection));

		customizer.performMoveUp(selection);
		assertEquals(0, drawerA.getChildren().indexOf(selection));
		customizer.performMoveDown(selection);
		assertEquals(1, drawerA.getChildren().indexOf(selection));

		assertTrue(getDrawerFactory().canCreate(selection));
		assertTrue(getSeparatorFactory().canCreate(selection));
		assertTrue(getStackFactory().canCreate(selection));

		resetMiddle();
		drawerA.setUserModificationPermission(PaletteEntry.PERMISSION_LIMITED_MODIFICATION);

		assertTrue(customizer.canMoveDown(selection));
		assertTrue(customizer.canMoveUp(selection));
		assertTrue(customizer.canDelete(selection));

		customizer.performMoveUp(selection);
		assertEquals(0, drawerA.getChildren().indexOf(selection));
		customizer.performMoveDown(selection);
		assertEquals(1, drawerA.getChildren().indexOf(selection));

		assertTrue(getDrawerFactory().canCreate(selection));
		assertTrue(!getSeparatorFactory().canCreate(selection));
		assertTrue(!getStackFactory().canCreate(selection));

		resetMiddle();
		drawerA.setUserModificationPermission(PaletteEntry.PERMISSION_NO_MODIFICATION);

		assertTrue(!customizer.canMoveDown(selection));
		assertTrue(!customizer.canMoveUp(selection));
		assertTrue(customizer.canDelete(selection));

		// these tests could be enabled if the PaletteContainer move's method
		// respected permissions
		// int i = drawerA.getChildren().indexOf(selection);
		//
		// customizer.performMoveUp(selection);
		// assertEquals(i, drawerA.getChildren().indexOf(selection));
		// customizer.performMoveDown(selection);
		// assertEquals(i, drawerA.getChildren().indexOf(selection));

		assertTrue(getDrawerFactory().canCreate(selection));
		assertTrue(!getSeparatorFactory().canCreate(selection));
		assertTrue(!getStackFactory().canCreate(selection));
	}

	/**
	 * 
	 * 1-Root 2-Drawer A 3-Tool 4-Tool 5-Drawer B 6-Stack (Full Permissions)
	 * (Selection) 7-Tool 8-Tool 9-Drawer C
	 */
	public void testTopSelection() {
		resetTop();

		drawerB.setUserModificationPermission(PaletteEntry.PERMISSION_FULL_MODIFICATION);
		drawerA.setUserModificationPermission(PaletteEntry.PERMISSION_FULL_MODIFICATION);

		assertTrue(customizer.canMoveUp(selection));
		assertTrue(customizer.canMoveDown(selection));
		assertTrue(customizer.canDelete(selection));

		assertTrue(getDrawerFactory().canCreate(selection));
		assertTrue(!getSeparatorFactory().canCreate(selection));
		assertTrue(!getStackFactory().canCreate(selection));

		customizer.performMoveUp(selection);
		assertEquals(2, drawerA.getChildren().indexOf(selection));
		customizer.performMoveDown(selection);
		customizer.performMoveDown(selection);
		assertEquals(1, drawerB.getChildren().indexOf(selection));

		resetTop();
		drawerB.setUserModificationPermission(PaletteEntry.PERMISSION_LIMITED_MODIFICATION);
		drawerA.setUserModificationPermission(PaletteEntry.PERMISSION_FULL_MODIFICATION);

		assertTrue(!customizer.canMoveUp(selection));
		assertTrue(customizer.canMoveDown(selection));
		assertTrue(customizer.canDelete(selection));

		assertTrue(getDrawerFactory().canCreate(selection));
		assertTrue(!getSeparatorFactory().canCreate(selection));
		assertTrue(!getStackFactory().canCreate(selection));

		// customizer.performMoveUp(selection);
		// assertEquals(2, drawerA.getChildren().indexOf(selection));
		customizer.performMoveDown(selection);
		assertEquals(1, drawerB.getChildren().indexOf(selection));

		resetTop();
		drawerB.setUserModificationPermission(PaletteEntry.PERMISSION_NO_MODIFICATION);
		drawerA.setUserModificationPermission(PaletteEntry.PERMISSION_FULL_MODIFICATION);

		assertTrue(!customizer.canMoveUp(selection));
		assertTrue(!customizer.canMoveDown(selection));
		assertTrue(customizer.canDelete(selection));

		assertTrue(getDrawerFactory().canCreate(selection));
		assertTrue(!getSeparatorFactory().canCreate(selection));
		assertTrue(!getStackFactory().canCreate(selection));

		// customizer.performMoveUp(selection);
		// assertEquals(2, drawerA.getChildren().indexOf(selection));
		// customizer.performMoveDown(selection);
		// assertEquals(1, drawerB.getChildren().indexOf(selection));

		resetTop();
		drawerB.setUserModificationPermission(PaletteEntry.PERMISSION_FULL_MODIFICATION);
		drawerA.setUserModificationPermission(PaletteEntry.PERMISSION_LIMITED_MODIFICATION);

		assertTrue(!customizer.canMoveUp(selection));
		assertTrue(customizer.canMoveDown(selection));
		assertTrue(customizer.canDelete(selection));

		assertTrue(getDrawerFactory().canCreate(selection));
		assertTrue(!getSeparatorFactory().canCreate(selection));
		assertTrue(!getStackFactory().canCreate(selection));

		// customizer.performMoveUp(selection);
		// assertEquals(2, drawerA.getChildren().indexOf(selection));
		customizer.performMoveDown(selection);
		assertEquals(1, drawerB.getChildren().indexOf(selection));

		resetTop();
		drawerB.setUserModificationPermission(PaletteEntry.PERMISSION_LIMITED_MODIFICATION);
		drawerA.setUserModificationPermission(PaletteEntry.PERMISSION_LIMITED_MODIFICATION);

		assertTrue(!customizer.canMoveUp(selection));
		assertTrue(customizer.canMoveDown(selection));
		assertTrue(customizer.canDelete(selection));

		assertTrue(getDrawerFactory().canCreate(selection));
		assertTrue(!getSeparatorFactory().canCreate(selection));
		assertTrue(!getStackFactory().canCreate(selection));

		// customizer.performMoveUp(selection);
		// assertEquals(2, drawerA.getChildren().indexOf(selection));
		customizer.performMoveDown(selection);
		assertEquals(1, drawerB.getChildren().indexOf(selection));

		resetTop();

		drawerB.setUserModificationPermission(PaletteEntry.PERMISSION_NO_MODIFICATION);
		drawerA.setUserModificationPermission(PaletteEntry.PERMISSION_LIMITED_MODIFICATION);

		assertTrue(!customizer.canMoveUp(selection));
		assertTrue(!customizer.canMoveDown(selection));
		assertTrue(customizer.canDelete(selection));

		assertTrue(getDrawerFactory().canCreate(selection));
		assertTrue(!getSeparatorFactory().canCreate(selection));
		assertTrue(!getStackFactory().canCreate(selection));

		// customizer.performMoveUp(selection);
		// assertEquals(2, drawerA.getChildren().indexOf(selection));
		// customizer.performMoveDown(selection);
		// assertEquals(1, drawerB.getChildren().indexOf(selection));
	}

}
