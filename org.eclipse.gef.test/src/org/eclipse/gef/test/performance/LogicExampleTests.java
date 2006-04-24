/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.test.performance;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.test.performance.Dimension;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.rulers.RulerProvider;
import org.eclipse.gef.ui.views.palette.PaletteView;

import org.eclipse.gef.examples.logicdesigner.model.AndGate;
import org.eclipse.gef.examples.logicdesigner.model.Circuit;
import org.eclipse.gef.examples.logicdesigner.model.Gate;
import org.eclipse.gef.examples.logicdesigner.model.LED;
import org.eclipse.gef.examples.logicdesigner.model.LogicDiagram;
import org.eclipse.gef.examples.logicdesigner.model.LogicGuide;
import org.eclipse.gef.examples.logicdesigner.model.LogicRuler;
import org.eclipse.gef.examples.logicdesigner.model.LogicSubpart;
import org.eclipse.gef.examples.logicdesigner.model.OrGate;
import org.eclipse.gef.examples.logicdesigner.model.SimpleOutput;
import org.eclipse.gef.examples.logicdesigner.model.Wire;
import org.eclipse.gef.examples.logicdesigner.model.XORGate;

public class LogicExampleTests 
	extends BasePerformanceTestCase
{

private IFile file;
private String fileName = "perfTest.logic";
private String prjName = "org.eclipse.gef.test.performance.logic";

private void closeEditor(IEditorPart editor) {
	editor.getEditorSite().getPage().closeEditor(editor, false);
}

private void connect(LogicSubpart part1, String conn1, LogicSubpart part2, String conn2) {
	Wire wire = new Wire();
	wire.setSource(part1);
	wire.setSourceTerminal(conn1);
	wire.setTarget(part2);
	wire.setTargetTerminal(conn2);
	wire.attachSource();
	wire.attachTarget();
}

private Circuit createFullAdder() {
	final Gate or;
	final Circuit circuit, circuit1, circuit2;

	circuit1 = createHalfAdder();
	circuit2 = createHalfAdder();
	circuit1.setLocation(new Point(2,10));
	circuit2.setLocation(new Point(38,90));

	circuit= new Circuit();
	circuit.setSize(new org.eclipse.draw2d.geometry.Dimension(120,216));
	or = new OrGate();
	or.setLocation(new Point(22,162));

	circuit.addChild(circuit1);
	circuit.addChild(circuit2);

	connect(circuit, Circuit.TERMINALS_OUT[0], circuit1, Circuit.TERMINALS_IN[0]);
	connect(circuit, Circuit.TERMINALS_OUT[2], circuit1, Circuit.TERMINALS_IN[3]);
	connect(circuit, Circuit.TERMINALS_OUT[3], circuit2, Circuit.TERMINALS_IN[3]);
	connect(circuit1,Circuit.TERMINALS_OUT[7], circuit2, Circuit.TERMINALS_IN[0]);

	circuit.addChild(or);
	connect(or, SimpleOutput.TERMINAL_OUT, circuit, Circuit.TERMINALS_IN[4]);
	connect(circuit1, Circuit.TERMINALS_OUT[4], or, Gate.TERMINAL_A);
	connect(circuit2, Circuit.TERMINALS_OUT[4], or, Gate.TERMINAL_B);
	connect(circuit2, Circuit.TERMINALS_OUT[7], circuit, Circuit.TERMINALS_IN[7]);

	return circuit;
}

private void createGuides(LogicRuler ruler) {
	LogicGuide guide = new LogicGuide(!ruler.isHorizontal());
	guide.setPosition(-1000);
	ruler.addGuide(guide);
	guide = new LogicGuide(!ruler.isHorizontal());
	guide.setPosition(100);
	ruler.addGuide(guide);
	guide = new LogicGuide(!ruler.isHorizontal());
	guide.setPosition(1000);
	ruler.addGuide(guide);
}

private Circuit createHalfAdder() {
	Gate and, xor;
	Circuit circuit;

	circuit = new Circuit();
	circuit.setSize(new org.eclipse.draw2d.geometry.Dimension(60,70));
	and = new AndGate();
	and.setLocation(new Point(2,12));
	xor = new XORGate();
	xor.setLocation(new Point(22,12));

	circuit.addChild(xor);
	circuit.addChild(and);

	connect(circuit, Circuit.TERMINALS_OUT[0], and, Gate.TERMINAL_A);
	connect(circuit, Circuit.TERMINALS_OUT[3], and, Gate.TERMINAL_B);
	connect(circuit, Circuit.TERMINALS_OUT[0], xor, Gate.TERMINAL_A);
	connect(circuit, Circuit.TERMINALS_OUT[3], xor, Gate.TERMINAL_B);

	connect(and, SimpleOutput.TERMINAL_OUT, circuit, Circuit.TERMINALS_IN[4]);
	connect(xor, SimpleOutput.TERMINAL_OUT, circuit, Circuit.TERMINALS_IN[7]);
	return circuit;
}

private LogicDiagram createLogicDiagram() {
	LogicDiagram diagram = new LogicDiagram();
	createGuides(diagram.getRuler(PositionConstants.NORTH));
	createGuides(diagram.getRuler(PositionConstants.WEST));
	
	// Add one child at 200,50.  When the outline is open, this will be the child
	// that will be selected.  Since it will already be visible when the editor opens,
	// the editor won't have to scroll to it (causing unnecessary paints and improper
	// performance results).
	LED selection = new LED();
	selection.setLocation(new Point(200, 50));
	diagram.addChild(selection);
	
	int x = -1000, y = -1000;
	for (int i = 0; i < 100; i++) {
		Circuit fullAdder = createFullAdder();
		fullAdder.setLocation(new Point(x, y));
		diagram.addChild(fullAdder);
		x += 20;
		y += 20;
	}
	
	y = -1000;
	for (int i = 0; i < 100; i++) {
		LED led = new LED();
		led.setLocation(new Point(x, y));
		diagram.addChild(led);
		x -= 20;
		y += 20;
	}
	
	return diagram;
}

private IEditorPart openEditor() throws PartInitException {
	return IDE.openEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
			.getActivePage(), file);
}

private void runOpenEditorTest(boolean garbageCollect) throws PartInitException {
	Display display = Display.getCurrent();
	int warmupRuns = getWarmupRuns();
	int measuredRuns = getMeasuredRuns();	
	for (int i = 0; i < warmupRuns + measuredRuns; i++) {
		while (display.readAndDispatch()) {}
		System.gc();
		if (i >= warmupRuns)
			startMeasuring();

		// open the editor
		IEditorPart editor = openEditor();
		while (display.readAndDispatch()) {}		
		if (garbageCollect)
			System.gc();

		if (i >= warmupRuns)
			stopMeasuring();

		// close the editor
		closeEditor(editor);
	}
	commitMeasurements();
	assertPerformanceInRelativeBand(
			garbageCollect ? Dimension.USED_JAVA_HEAP : Dimension.CPU_TIME, -100, 10);
}

protected void setUp() throws Exception {	
	super.setUp();
	
	// Create the logic file if it doesn't exist
	IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
	IProject project = root.getProject(prjName);
	if (!project.exists())
		project.create(null);
	if (!project.isOpen())
		project.open(null);
	file = project.getFile(fileName);
	if (!file.exists()) {
		LogicDiagram diagram = createLogicDiagram();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(out);
		oos.writeObject(diagram);
		oos.close();
		file.create(new ByteArrayInputStream(out.toByteArray()), false, null);
		out.close();
	}
	
	// Close all perspectives but Resource, and all views
	IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	IWorkbenchPage page = window.getActivePage();
	boolean foundPerspective = false;
	IPerspectiveDescriptor[] perspectives = page.getOpenPerspectives();
	for (int i = 0; i < perspectives.length; i++) {
		IPerspectiveDescriptor perspective = perspectives[i];
		if (perspective.getId().equals(RESOURCE_PERSPECTIVE_ID))
			foundPerspective = true;
		else
			page.closePerspective(perspective, false, false);
	}
	if (!foundPerspective)
		PlatformUI.getWorkbench().showPerspective(RESOURCE_PERSPECTIVE_ID, window);
	
	page.hideActionSet("org.eclipse.ui.edit.text.actionSet.annotationNavigation");
	page.hideActionSet("org.eclipse.ui.externaltools.ExternalToolsSet");
	
	IViewReference[] views = page.getViewReferences();
	for (int i = 0; i < views.length; i++)
		page.hideView(views[i]);
	window.getShell().setSize(1000, 800);
}

public void testEditorLayout() throws PartInitException {
	tagAsGlobalSummary("Editor Layout", Dimension.CPU_TIME);
	IEditorPart editor = openEditor();
	Display display = Display.getCurrent();
	GraphicalViewer viewer = (GraphicalViewer)editor.getAdapter(GraphicalViewer.class);
	viewer.setProperty(RulerProvider.PROPERTY_RULER_VISIBILITY, Boolean.TRUE);
	Shell editorWindow = editor.getEditorSite().getPage().getWorkbenchWindow().getShell();
	int[] delta = new int[] {3, 6, 0};
	org.eclipse.swt.graphics.Point origSize = editorWindow.getSize();
	
	int warmupRuns = getWarmupRuns();
	int measuredRuns = getMeasuredRuns();
	for (int i = 0; i < warmupRuns + measuredRuns; i++) {
		while (display.readAndDispatch()) {}
		System.gc();
		if (i >= warmupRuns)
			startMeasuring();
		
		for (int j = 0; j < delta.length; j++) {
			editorWindow.setSize(origSize.x + delta[j], origSize.y + delta[j]);
			while (display.readAndDispatch()) {}
		}
		
		if (i >= warmupRuns)
			stopMeasuring();
	}
	closeEditor(editor);
	commitMeasurements();
	assertPerformanceInRelativeBand(Dimension.CPU_TIME, -100, 10);
}

public void testEditorOpen() throws PartInitException {
	tagAsGlobalSummary("Open Logic Editor (~2900 editparts)", Dimension.CPU_TIME);
	runOpenEditorTest(false);
}

public void testEditorOpenWithOutline() throws PartInitException {
	tagAsGlobalSummary("Open Logic Editor With Outline", Dimension.CPU_TIME);
	IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
			.getActivePage();
	page.showView(OUTLINE_VIEW_ID);
	runOpenEditorTest(false);
	page.hideView(page.findViewReference(OUTLINE_VIEW_ID));
}

public void testMemoryConsumption() throws PartInitException {
	tagAsGlobalSummary("Memory Consumption in Logic Editor", Dimension.USED_JAVA_HEAP);
	IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
			.getActivePage();
	page.showView(OUTLINE_VIEW_ID);
	runOpenEditorTest(true);
	page.hideView(page.findViewReference(OUTLINE_VIEW_ID));
}

public void testPaletteSwitching() throws PartInitException {
	tagAsGlobalSummary("Palette Switching", Dimension.CPU_TIME);
	IEditorPart editor = openEditor();
	IWorkbenchPage page = editor.getSite().getPage();
	Display display = Display.getCurrent();
	
	int warmupRuns = getWarmupRuns();
	int measuredRuns = getMeasuredRuns();	
	for (int i = 0; i < warmupRuns + measuredRuns; i++) {
		while (display.readAndDispatch()) {}
		System.gc();
		if (i >= warmupRuns)
			startMeasuring();
		
		IViewPart view = page.showView(PaletteView.ID);
		while (display.readAndDispatch()) {}

		page.hideView(view);
		while (display.readAndDispatch()) {}
		
		if (i >= warmupRuns)
			stopMeasuring();
	}
	closeEditor(editor);
	commitMeasurements();
	assertPerformanceInRelativeBand(Dimension.CPU_TIME, -100, 10);
}

public void testZoom() throws PartInitException {
	tagAsGlobalSummary("Zooming in Logic Editor", Dimension.CPU_TIME);
	IEditorPart editor = openEditor();
	Display display = Display.getCurrent();
	GraphicalViewer viewer = (GraphicalViewer)editor.getAdapter(GraphicalViewer.class);
	ZoomManager zoomMgr = (ZoomManager)viewer.getProperty(ZoomManager.class.toString());
	zoomMgr.setZoom(0.1);
	
	int warmupRuns = getWarmupRuns();
	int measuredRuns = getMeasuredRuns();	
	for (int i = 0; i < warmupRuns + measuredRuns; i++) {
		zoomMgr.setZoom(zoomMgr.getMinZoom());
		
		while (display.readAndDispatch()) {}
		System.gc();
		if (i >= warmupRuns)
			startMeasuring();
		
		for (int j = 0; j < 3; j++) {
			zoomMgr.zoomIn();
			while (display.readAndDispatch()) {}
		}
		
		if (i >= warmupRuns)
			stopMeasuring();
	}
	closeEditor(editor);
	commitMeasurements();
	assertPerformanceInRelativeBand(Dimension.CPU_TIME, -100, 10);
}

}