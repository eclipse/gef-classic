/*******************************************************************************
 * Copyright (c) 2000, 2023 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.logicdesigner.model.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.commands.Command;

import org.eclipse.gef.examples.logicdesigner.LogicMessages;
import org.eclipse.gef.examples.logicdesigner.model.AndGate;
import org.eclipse.gef.examples.logicdesigner.model.Circuit;
import org.eclipse.gef.examples.logicdesigner.model.GroundOutput;
import org.eclipse.gef.examples.logicdesigner.model.LED;
import org.eclipse.gef.examples.logicdesigner.model.LiveOutput;
import org.eclipse.gef.examples.logicdesigner.model.LogicDiagram;
import org.eclipse.gef.examples.logicdesigner.model.LogicElement;
import org.eclipse.gef.examples.logicdesigner.model.LogicFlowContainer;
import org.eclipse.gef.examples.logicdesigner.model.LogicGuide;
import org.eclipse.gef.examples.logicdesigner.model.LogicLabel;
import org.eclipse.gef.examples.logicdesigner.model.LogicSubpart;
import org.eclipse.gef.examples.logicdesigner.model.OrGate;
import org.eclipse.gef.examples.logicdesigner.model.Wire;
import org.eclipse.gef.examples.logicdesigner.model.WireBendpoint;
import org.eclipse.gef.examples.logicdesigner.model.XORGate;

public class CloneCommand extends Command {

	private final List<LogicSubpart> parts;
	private List<LogicSubpart> newTopLevelParts;
	private List<Wire> newConnections;
	private LogicDiagram parent;
	private Map<LogicSubpart, Rectangle> bounds;
	private Map<LogicSubpart, Integer> indices;
	private Map<LogicSubpart, LogicSubpart> connectionPartMap;
	private ChangeGuideCommand vGuideCommand, hGuideCommand;
	private LogicGuide hGuide, vGuide;
	private int hAlignment, vAlignment;

	public CloneCommand() {
		super(LogicMessages.CloneCommand_Label);
		parts = new LinkedList<>();
	}

	public void addPart(LogicSubpart part, Rectangle newBounds) {
		parts.add(part);
		if (bounds == null) {
			bounds = new HashMap<>();
		}
		bounds.put(part, newBounds);
	}

	public void addPart(LogicSubpart part, int index) {
		parts.add(part);
		if (indices == null) {
			indices = new HashMap<>();
		}
		indices.put(part, Integer.valueOf(index));
	}

	protected void clonePart(LogicSubpart oldPart, LogicDiagram newParent, Rectangle newBounds,
			List<Wire> newConnections, Map<LogicSubpart, LogicSubpart> connectionPartMap, int index) {
		LogicSubpart newPart = null;

		if (oldPart instanceof AndGate) {
			newPart = new AndGate();
		} else if (oldPart instanceof Circuit) {
			newPart = new Circuit();
		} else if (oldPart instanceof GroundOutput) {
			newPart = new GroundOutput();
		} else if (oldPart instanceof LED) {
			newPart = new LED();
			newPart.setPropertyValue(LED.P_VALUE, oldPart.getPropertyValue(LED.P_VALUE));
		} else if (oldPart instanceof LiveOutput) {
			newPart = new LiveOutput();
		} else if (oldPart instanceof LogicLabel) {
			newPart = new LogicLabel();
			((LogicLabel) newPart).setLabelContents(((LogicLabel) oldPart).getLabelContents());
		} else if (oldPart instanceof OrGate) {
			newPart = new OrGate();
		} else if (oldPart instanceof LogicFlowContainer) {
			newPart = new LogicFlowContainer();
		} else if (oldPart instanceof XORGate) {
			newPart = new XORGate();
		}

		if (oldPart instanceof LogicDiagram ld) {
			for (LogicElement element : ld.getChildren()) {
				// for children they will not need new bounds
				clonePart((LogicSubpart) element, (LogicDiagram) newPart, null, newConnections, connectionPartMap, -1);
			}
		}

		for (Wire connection : oldPart.getTargetConnections()) {
			Wire newConnection = new Wire();
			newConnection.setValue(connection.getValue());
			newConnection.setTarget(newPart);
			newConnection.setTargetTerminal(connection.getTargetTerminal());
			newConnection.setSourceTerminal(connection.getSourceTerminal());
			newConnection.setSource(connection.getSource());
			newConnection.setBendpoints(cloneBendPoints(connection));
			newConnections.add(newConnection);
		}

		if (index < 0) {
			newParent.addChild(newPart);
		} else {
			newParent.addChild(newPart, index);
		}

		newPart.setSize(oldPart.getSize());

		if (newBounds != null) {
			newPart.setLocation(newBounds.getTopLeft());
		} else {
			newPart.setLocation(oldPart.getLocation());
		}

		// keep track of the new parts so we can delete them in undo
		// keep track of the oldpart -> newpart map so that we can properly
		// attach
		// all connections.
		if (newParent == parent) {
			newTopLevelParts.add(newPart);
		}
		connectionPartMap.put(oldPart, newPart);
	}

	private static List<WireBendpoint> cloneBendPoints(Wire connection) {
		List<WireBendpoint> newBendPoints = new ArrayList<>(connection.getBendpoints().size());

		for (WireBendpoint bendPoint : connection.getBendpoints()) {
			WireBendpoint newBendPoint = new WireBendpoint();
			newBendPoint.setRelativeDimensions(bendPoint.getFirstRelativeDimension(),
					bendPoint.getSecondRelativeDimension());
			newBendPoint.setWeight(bendPoint.getWeight());
			newBendPoints.add(newBendPoint);
		}
		return newBendPoints;
	}

	@Override
	public void execute() {
		connectionPartMap = new HashMap<>();
		newConnections = new LinkedList<>();
		newTopLevelParts = new LinkedList<>();

		for (LogicSubpart part : parts) {
			if (bounds != null && bounds.containsKey(part)) {
				clonePart(part, parent, bounds.get(part), newConnections, connectionPartMap, -1);
			} else if (indices != null && indices.containsKey(part)) {
				clonePart(part, parent, null, newConnections, connectionPartMap, indices.get(part).intValue());
			} else {
				clonePart(part, parent, null, newConnections, connectionPartMap, -1);
			}
		}

		for (Wire conn : newConnections) {
			LogicSubpart source = conn.getSource();
			if (connectionPartMap.containsKey(source)) {
				conn.setSource(connectionPartMap.get(source));
				conn.attachSource();
				conn.attachTarget();
			}
		}

		if (hGuide != null) {
			hGuideCommand = new ChangeGuideCommand(connectionPartMap.get(parts.get(0)), true);
			hGuideCommand.setNewGuide(hGuide, hAlignment);
			hGuideCommand.execute();
		}

		if (vGuide != null) {
			vGuideCommand = new ChangeGuideCommand(connectionPartMap.get(parts.get(0)), false);
			vGuideCommand.setNewGuide(vGuide, vAlignment);
			vGuideCommand.execute();
		}
	}

	public void setParent(LogicDiagram parent) {
		this.parent = parent;
	}

	@Override
	public void redo() {
		newTopLevelParts.forEach(nTLP -> parent.addChild(nTLP));
		for (Wire conn : newConnections) {
			LogicSubpart source = conn.getSource();
			if (connectionPartMap.containsKey(source)) {
				conn.setSource(connectionPartMap.get(source));
				conn.attachSource();
				conn.attachTarget();
			}
		}
		if (hGuideCommand != null) {
			hGuideCommand.redo();
		}
		if (vGuideCommand != null) {
			vGuideCommand.redo();
		}
	}

	public void setGuide(LogicGuide guide, int alignment, boolean isHorizontal) {
		if (isHorizontal) {
			hGuide = guide;
			hAlignment = alignment;
		} else {
			vGuide = guide;
			vAlignment = alignment;
		}
	}

	@Override
	public void undo() {
		if (hGuideCommand != null) {
			hGuideCommand.undo();
		}
		if (vGuideCommand != null) {
			vGuideCommand.undo();
		}
		newTopLevelParts.forEach(nTLP -> parent.removeChild(nTLP));
	}

}
