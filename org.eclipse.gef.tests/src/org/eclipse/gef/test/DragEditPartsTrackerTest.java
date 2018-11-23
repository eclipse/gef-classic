/*******************************************************************************
 * Copyright (c) 2006, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.gef.test;

import java.util.List;

import junit.framework.TestCase;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.tools.DragEditPartsTracker;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;

public class DragEditPartsTrackerTest extends TestCase {
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

	private static class TestGraphicalEditPart extends
			AbstractGraphicalEditPart {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.gef.editparts.AbstractEditPart#register()
		 */
		protected void register() {
			// do nothing
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
		 */
		protected IFigure createFigure() {
			return new Figure();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
		 */
		protected void createEditPolicies() {
			// do nothing
		}
	}

	private class DummyEditorPart implements org.eclipse.ui.IEditorPart {

		public void addPropertyListener(IPropertyListener listener) {

		}

		public void createPartControl(Composite parent) {

		}

		public void dispose() {

		}

		public IWorkbenchPartSite getSite() {
			return null;
		}

		public String getTitle() {
			return null;
		}

		public Image getTitleImage() {
			return null;
		}

		public String getTitleToolTip() {
			return null;
		}

		public void removePropertyListener(IPropertyListener listener) {

		}

		public void setFocus() {

		}

		public IEditorInput getEditorInput() {
			return null;
		}

		public IEditorSite getEditorSite() {
			return null;
		}

		public void init(IEditorSite site, IEditorInput input)
				throws PartInitException {

		}

		public void doSave(IProgressMonitor monitor) {

		}

		public void doSaveAs() {

		}

		public boolean isDirty() {
			return false;
		}

		public boolean isSaveAsAllowed() {
			return false;
		}

		public boolean isSaveOnCloseNeeded() {
			return false;
		}

		public Object getAdapter(Class adapter) {
			return null;
		}

	}

	private class TestDragEditPartsTracker extends DragEditPartsTracker {

		public TestDragEditPartsTracker(EditPart sourceEditPart) {
			super(sourceEditPart);
		}

		public List createOperationSet() {
			return super.createOperationSet();
		}
	};

	public void test_createOperationSet() {
		TestDragEditPartsTracker dept = new TestDragEditPartsTracker(
				new TestGraphicalEditPart());

		dept.setEditDomain(new DefaultEditDomain(new DummyEditorPart()));
		dept.activate();
		List operationSet = dept.createOperationSet();
		assertTrue(operationSet != null);
		dept.deactivate();
	}

}
