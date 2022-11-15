/*******************************************************************************
 * Copyright (c) 2000, 2022 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.logicdesigner.figures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.geometry.Point;

public class NodeFigure extends Figure {

	protected Map<String, ConnectionAnchor> connectionAnchors = new HashMap<>(7);
	protected List<ConnectionAnchor> inputConnectionAnchors = new ArrayList<>(2);
	protected List<ConnectionAnchor> outputConnectionAnchors = new ArrayList<>(2);

	public ConnectionAnchor connectionAnchorAt(Point p) {
		ConnectionAnchor closest = null;
		long min = Long.MAX_VALUE;

		for (ConnectionAnchor c : getSourceConnectionAnchors()) {
			Point p2 = c.getLocation(null);
			long d = p.getDistance2(p2);
			if (d < min) {
				min = d;
				closest = c;
			}
		}
		for (ConnectionAnchor c : getTargetConnectionAnchors()) {
			Point p2 = c.getLocation(null);
			long d = p.getDistance2(p2);
			if (d < min) {
				min = d;
				closest = c;
			}
		}
		return closest;
	}

	public ConnectionAnchor getConnectionAnchor(String terminal) {
		return connectionAnchors.get(terminal);
	}

	public String getConnectionAnchorName(ConnectionAnchor c) {
		for (Entry<String, ConnectionAnchor> entry : connectionAnchors.entrySet()) {
			if (entry.getValue().equals(c)) {
				return entry.getKey();
			}
		}
		return null;
	}

	public ConnectionAnchor getSourceConnectionAnchorAt(Point p) {
		ConnectionAnchor closest = null;
		long min = Long.MAX_VALUE;

		for (ConnectionAnchor c : getSourceConnectionAnchors()) {
			Point p2 = c.getLocation(null);
			long d = p.getDistance2(p2);
			if (d < min) {
				min = d;
				closest = c;
			}
		}
		return closest;
	}

	public List<ConnectionAnchor> getSourceConnectionAnchors() {
		return outputConnectionAnchors;
	}

	public ConnectionAnchor getTargetConnectionAnchorAt(Point p) {
		ConnectionAnchor closest = null;
		long min = Long.MAX_VALUE;

		for (ConnectionAnchor c : getTargetConnectionAnchors()) {
			Point p2 = c.getLocation(null);
			long d = p.getDistance2(p2);
			if (d < min) {
				min = d;
				closest = c;
			}
		}
		return closest;
	}

	public List<ConnectionAnchor> getTargetConnectionAnchors() {
		return inputConnectionAnchors;
	}

}
