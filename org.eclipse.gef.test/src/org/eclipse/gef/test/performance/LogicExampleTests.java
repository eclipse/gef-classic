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
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.test.performance.Dimension;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
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
import org.eclipse.gef.examples.logicdesigner.model.LogicDiagram;
import org.eclipse.gef.examples.logicdesigner.model.LogicGuide;
import org.eclipse.gef.examples.logicdesigner.model.LogicLabel;
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
	
	int x = -1000, y = -1000;
	for (int i = 0; i < 100; i++) {
		Circuit fullAdder = createFullAdder();
		fullAdder.setLocation(new Point(x,y));
		diagram.addChild(fullAdder);
		x += 20;
		y += 20;
	}
	
	LogicLabel label = new LogicLabel();
	label.setLabelContents("Bart Simpson: I don't know why I did it, I don't know why " +
			"I enjoyed it and I don't know why I'll do it again.");
	label.setLocation(new Point(0, 0));
	label.setSize(new org.eclipse.draw2d.geometry.Dimension(-1, -1));
	diagram.addChild(label);
	
	return diagram;
}

private IEditorPart openEditor() throws PartInitException {
	return IDE.openEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
			.getActivePage(), file);
}

protected void setUp() throws Exception {	
	IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
	IProject project = root.getProject(prjName);
	if (!project.exists())
		project.create(null);
	else
		project.refreshLocal(IResource.DEPTH_INFINITE, null);
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
	} else
		file.refreshLocal(IResource.DEPTH_ZERO, null);
	
	super.setUp();
}

protected void tearDown() throws Exception {
	super.tearDown();
	ResourcesPlugin.getWorkspace().getRoot().getProject(prjName).delete(true, false, null);
	file = null;
}

public void testEditorLayout() throws PartInitException {
	tagAsGlobalSummary("Editor Layout", Dimension.CPU_TIME);
	IEditorPart editor = openEditor();
	Display d = editor.getSite().getWorkbenchWindow().getShell().getDisplay();
	GraphicalViewer viewer = (GraphicalViewer)editor.getAdapter(GraphicalViewer.class);
	viewer.setProperty(RulerProvider.PROPERTY_RULER_VISIBILITY, Boolean.TRUE);
	Shell editorWindow = editor.getEditorSite().getPage().getWorkbenchWindow().getShell();
	int[] delta = new int[] {2, 4, 6, 8, 10, 0};
	org.eclipse.swt.graphics.Point origSize = editorWindow.getSize();
	
	int warmupRuns = getWarmupRuns();
	int measuredRuns = getMeasuredRuns();	
	for (int i = 0; i < warmupRuns + measuredRuns; i++) {
		if (i >= warmupRuns)
			startMeasuring();
		
		for (int j = 0; j < delta.length; j++) {
			editorWindow.setSize(origSize.x + delta[j], origSize.y + delta[j]);
			while (d.readAndDispatch()) {}
		}
		
		if (i >= warmupRuns)
			stopMeasuring();
	}
	closeEditor(editor);
	commitMeasurements();
	assertPerformance();
}

public void testEditorOpen() throws PartInitException {
	tagAsGlobalSummary("Open Logic Editor (~2800 editparts)", 
			new Dimension[] {Dimension.CPU_TIME, Dimension.USED_JAVA_HEAP});
	
	Display d = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().getDisplay();
	int warmupRuns = getWarmupRuns();
	int measuredRuns = getMeasuredRuns();	
	for (int i = 0; i < warmupRuns + measuredRuns; i++) {
		System.gc();
		if (i >= warmupRuns)
			startMeasuring();

		// open the editor
		IEditorPart editor = openEditor();
		while (d.readAndDispatch()) {}

		System.gc();
		if (i >= warmupRuns)
			stopMeasuring();

		// close the editor
		closeEditor(editor);
		while (d.readAndDispatch()) {}		
	}
	commitMeasurements();
	assertPerformance();
}

public void testPaletteSwitching() throws PartInitException {
	tagAsSummary("Palette Slurping", Dimension.CPU_TIME);
	IEditorPart editor = openEditor();
	Display d = editor.getSite().getWorkbenchWindow().getShell().getDisplay();
	IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	
	int warmupRuns = getWarmupRuns();
	int measuredRuns = getMeasuredRuns();	
	for (int i = 0; i < warmupRuns + measuredRuns; i++) {
		if (i >= warmupRuns)
			startMeasuring();
		
		IViewPart view = page.showView(PaletteView.ID);
		while (d.readAndDispatch()) {}

		page.hideView(view);
		while (d.readAndDispatch()) {}
		
		if (i >= warmupRuns)
			stopMeasuring();
	}
	closeEditor(editor);
	commitMeasurements();
	assertPerformance();
}

public void testZoom() throws PartInitException {
	tagAsSummary("Zooming", Dimension.CPU_TIME);
	IEditorPart editor = openEditor();
	Display d = editor.getSite().getWorkbenchWindow().getShell().getDisplay();
	GraphicalViewer viewer = (GraphicalViewer)editor.getAdapter(GraphicalViewer.class);
	ZoomManager zoomMgr = (ZoomManager)viewer.getProperty(ZoomManager.class.toString());
	zoomMgr.setZoom(0.1);
	
	int warmupRuns = getWarmupRuns();
	int measuredRuns = getMeasuredRuns();	
	for (int i = 0; i < warmupRuns + measuredRuns; i++) {
		if (i >= warmupRuns)
			startMeasuring();
		
		while (zoomMgr.canZoomIn()) {
			zoomMgr.zoomIn();
			while (d.readAndDispatch()) {}
		}
		
		while (zoomMgr.canZoomOut()) {
			zoomMgr.zoomOut();
			while (d.readAndDispatch()) {}
		}
		
		if (i >= warmupRuns)
			stopMeasuring();
	}
	closeEditor(editor);
	commitMeasurements();
	assertPerformance();
}

}