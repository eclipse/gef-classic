package org.eclipse.gef.examples.logicdesigner;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.io.*;

import org.eclipse.draw2d.geometry.*;

import org.eclipse.gef.requests.CreateRequest;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.examples.logicdesigner.model.*;

public class LogicDiagramFactory {

LogicDiagram root;

protected static void connect(LogicSubpart e1, String t1, LogicSubpart e2, String t2) {
	Wire wire = new Wire();
	wire.setSource(e1);
	wire.setSourceTerminal(t1);
	wire.setTarget(e2);
	wire.setTargetTerminal(t2);
	wire.attachSource();
	wire.attachTarget();
}

public static Circuit createFullAdder() {
	final Gate or;
	final Circuit circuit, circuit1, circuit2;

	circuit1 = createHalfAdder();
	circuit2 = createHalfAdder();
	circuit1.setLocation(new Point(2,10));
	circuit2.setLocation(new Point(38,90));

	circuit= new Circuit();
	circuit.setSize(new Dimension (120,216));
	or = new OrGate();
	or.setLocation(new Point(22,162));

	circuit.addChild(circuit1);
	circuit.addChild(circuit2);

	connect(circuit, circuit.TERMINALS_OUT[0], circuit1, circuit1.TERMINALS_IN[0]);
	connect(circuit, circuit.TERMINALS_OUT[2], circuit1, circuit1.TERMINALS_IN[3]);
	connect(circuit, circuit.TERMINALS_OUT[3], circuit2, circuit2.TERMINALS_IN[3]);
	connect(circuit1,circuit1.TERMINALS_OUT[7],circuit2, circuit2.TERMINALS_IN[0]);

	circuit.addChild(or);
	connect(or, or.TERMINAL_OUT, circuit, circuit.TERMINALS_IN[4]);
	connect(circuit1, circuit1.TERMINALS_OUT[4], or, or.TERMINAL_A);
	connect(circuit2, circuit2.TERMINALS_OUT[4], or, or.TERMINAL_B);
	connect(circuit2, circuit2.TERMINALS_OUT[7], circuit, circuit.TERMINALS_IN[7]);

	return circuit;
}

public static Circuit createHalfAdder() {
	Gate and, xor;
	Circuit circuit;

	circuit = new Circuit();
	circuit.setSize(new Dimension (60,70));
	and = new AndGate();
	and.setLocation(new Point(2,12));
	xor = new XORGate();
	xor.setLocation(new Point(22,12));

	circuit.addChild(xor);
	circuit.addChild(and);

	connect(circuit, circuit.TERMINALS_OUT[0], and, and.TERMINAL_A);
	connect(circuit, circuit.TERMINALS_OUT[3], and, and.TERMINAL_B);
	connect(circuit, circuit.TERMINALS_OUT[0], xor, xor.TERMINAL_A);
	connect(circuit, circuit.TERMINALS_OUT[3], xor, xor.TERMINAL_B);

	connect(and, and.TERMINAL_OUT, circuit, circuit.TERMINALS_IN[4]);
	connect(xor, xor.TERMINAL_OUT, circuit, circuit.TERMINALS_IN[7]);
	return circuit;
}

protected static Object createLargeModel() {
	LogicDiagram root = new LogicDiagram();

	final Circuit circuit1, circuit2, circuit3, circuit4;
	final LED led1, led2, led3;

	led1 = new LED();
	led1.setValue(3);
	led2 = new LED();
	led2.setValue(7);
	led3 = new LED();
	led1.setLocation(new Point(170, 16));
	led2.setLocation(new Point(320, 16));
	led3.setLocation(new Point(245, 360));
	root.addChild(led1);
	root.addChild(led2);
	root.addChild(led3);
	//
	circuit1 = createHalfAdder();
	circuit1.setSize(new Dimension(64, 216));
	circuit1.setLocation(new Point(455, 104));
	root.addChild(circuit1);
	//
	circuit2 = createFullAdder();
	circuit2.setLocation(new Point(305, 104));
	root.addChild(circuit2);
	connect(circuit1, circuit1.TERMINALS_OUT[4], circuit2, circuit2.TERMINALS_IN[3]);
	//
	circuit3 = createFullAdder();
	circuit3.setLocation(new Point(155, 104));
	root.addChild(circuit3);
	connect(circuit2, circuit2.TERMINALS_OUT[4], circuit3, circuit3.TERMINALS_IN[3]);
	//
	circuit4 = createFullAdder();
	circuit4.setLocation(new Point(5, 104));
	//
	connect(led1, led1.TERMINAL_1_OUT, circuit1, circuit1.TERMINALS_IN[0]);
	connect(led1, led1.TERMINAL_2_OUT, circuit2, circuit2.TERMINALS_IN[0]);
	connect(led1, led1.TERMINAL_3_OUT, circuit3, circuit3.TERMINALS_IN[0]);
	connect(led2, led2.TERMINAL_1_OUT, circuit1, circuit1.TERMINALS_IN[3]);
	connect(led2, led2.TERMINAL_2_OUT, circuit2, circuit2.TERMINALS_IN[2]);
	connect(circuit1, circuit1.TERMINALS_OUT[7], led3, led3.TERMINAL_1_IN);
	connect(circuit2, circuit1.TERMINALS_OUT[7], led3, led3.TERMINAL_2_IN);
	connect(circuit3, circuit1.TERMINALS_OUT[7], led3, led3.TERMINAL_3_IN);
	//
	connect(led2, led2.TERMINAL_3_OUT, circuit3, circuit3.TERMINALS_IN[2]);
	root.addChild(circuit4);
	connect(led2, led2.TERMINAL_4_OUT, circuit4, circuit4.TERMINALS_IN[2]);
	connect(circuit3, circuit3.TERMINALS_OUT[4], circuit4, circuit4.TERMINALS_IN[3]);
	connect(led1, led1.TERMINAL_4_OUT, circuit4, circuit4.TERMINALS_IN[0]);
	connect(circuit4, circuit1.TERMINALS_OUT[7], led3, led3.TERMINAL_4_IN);

	return root;
}

public static CreateRequest.Factory getFullAdderFactory() {
	return new CreateRequest.Factory(){
		public Object getNewObject(){
			return createFullAdder();
		}
		public Object getObjectType(){
			return "Full Adder";	//$NON-NLS-1$
		}
	};
}

public static CreateRequest.Factory getHalfAdderFactory() {
	return new CreateRequest.Factory(){
		public Object getNewObject(){
			return createHalfAdder();
		}
		public Object getObjectType(){
			return "Half Adder";    //$NON-NLS-1$
		}
	};
}

public Object createEmptyModel() {
	root = new LogicDiagram();
	return root;
}

static public Object createModel() {

	LogicDiagram root = new LogicDiagram();

	Circuit circuit1;
	
	circuit1 = createHalfAdder();
	circuit1.setLocation(new Point(50,50));
	root.addChild(circuit1);
	return root;
}

public Object getRootElement() {
	if (root == null)
		createLargeModel();
	return root;
}

}
