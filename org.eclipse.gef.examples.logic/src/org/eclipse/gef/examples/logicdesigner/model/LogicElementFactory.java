package org.eclipse.gef.examples.logicdesigner.model;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.requests.CreationFactory;

import org.eclipse.gef.examples.logicdesigner.TemplateConstants;

public class LogicElementFactory implements CreationFactory {
	
	private String template;
	
	public LogicElementFactory(String str) {
		template = str;
	}
	
	protected void connect(LogicSubpart part1, String conn1, LogicSubpart part2, String conn2) {
		Wire wire = new Wire();
		wire.setSource(part1);
		wire.setSourceTerminal(conn1);
		wire.setTarget(part2);
		wire.setTargetTerminal(conn2);
		wire.attachSource();
		wire.attachTarget();
	}
	
	public Circuit createFullAdder() {
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
	
		connect(circuit, Circuit.TERMINALS_OUT[0], circuit1, Circuit.TERMINALS_IN[0]);
		connect(circuit, Circuit.TERMINALS_OUT[2], circuit1, Circuit.TERMINALS_IN[3]);
		connect(circuit, Circuit.TERMINALS_OUT[3], circuit2, Circuit.TERMINALS_IN[3]);
		connect(circuit1,Circuit.TERMINALS_OUT[7],circuit2, Circuit.TERMINALS_IN[0]);
	
		circuit.addChild(or);
		connect(or, SimpleOutput.TERMINAL_OUT, circuit, Circuit.TERMINALS_IN[4]);
		connect(circuit1, Circuit.TERMINALS_OUT[4], or, Gate.TERMINAL_A);
		connect(circuit2, Circuit.TERMINALS_OUT[4], or, Gate.TERMINAL_B);
		connect(circuit2, Circuit.TERMINALS_OUT[7], circuit, Circuit.TERMINALS_IN[7]);
	
		return circuit;
	}

	public Circuit createHalfAdder() {
		Gate and, xor;
		Circuit circuit;
	
		circuit = new Circuit();
		circuit.setSize(new Dimension(60,70));
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
	
	public Object getNewObject() {
		if (TemplateConstants.TEMPLATE_AND_GATE.equals(template))
			return new AndGate();
		if (TemplateConstants.TEMPLATE_CIRCUIT.equals(template))
			return new Circuit();
		if (TemplateConstants.TEMPLATE_FLOW_CONTAINER.equals(template))
			return new LogicFlowContainer();
		if (TemplateConstants.TEMPLATE_FULL_ADDER.equals(template))
			return createFullAdder();
		if (TemplateConstants.TEMPLATE_GROUND.equals(template))
			return new GroundOutput();
		if (TemplateConstants.TEMPLATE_HALF_ADDER.equals(template))
			return createHalfAdder();
		if (TemplateConstants.TEMPLATE_LED.equals(template))
			return new LED();
		if (TemplateConstants.TEMPLATE_LIVE_OUTPUT.equals(template))
			return new LiveOutput();
		if (TemplateConstants.TEMPLATE_LOGIC_LABEL.equals(template))
			return new LogicLabel();
		if (TemplateConstants.TEMPLATE_OR_GATE.equals(template))
			return new OrGate();
		if (TemplateConstants.TEMPLATE_XOR_GATE.equals(template))
			return new XORGate();
		
		return null;
	}
	
	public Object getObjectType() {
		return template;
	}
}

