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
package org.eclipse.gef.examples.logicdesigner.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.eclipse.draw2d.Bendpoint;

public class Wire extends LogicElement {

	static final long serialVersionUID = 1;
	protected boolean value;
	protected LogicSubpart source, target;
	protected String sourceTerminal, targetTerminal;
	protected List bendpoints = new ArrayList();

	public void attachSource() {
		if (getSource() == null
				|| getSource().getSourceConnections().contains(this))
			return;
		getSource().connectOutput(this);
	}

	public void attachTarget() {
		if (getTarget() == null
				|| getTarget().getTargetConnections().contains(this))
			return;
		getTarget().connectInput(this);
	}

	public void detachSource() {
		if (getSource() == null)
			return;
		getSource().disconnectOutput(this);
	}

	public void detachTarget() {
		if (getTarget() == null)
			return;
		getTarget().disconnectInput(this);
	}

	public List getBendpoints() {
		return bendpoints;
	}

	public LogicSubpart getSource() {
		return source;
	}

	public String getSourceTerminal() {
		return sourceTerminal;
	}

	public LogicSubpart getTarget() {
		return target;
	}

	public String getTargetTerminal() {
		return targetTerminal;
	}

	public boolean getValue() {
		return value;
	}

	public void insertBendpoint(int index, Bendpoint point) {
		getBendpoints().add(index, point);
		firePropertyChange("bendpoint", null, null);//$NON-NLS-1$
	}

	public void removeBendpoint(int index) {
		getBendpoints().remove(index);
		firePropertyChange("bendpoint", null, null);//$NON-NLS-1$
	}

	public void setBendpoint(int index, Bendpoint point) {
		getBendpoints().set(index, point);
		firePropertyChange("bendpoint", null, null);//$NON-NLS-1$
	}

	public void setBendpoints(Vector points) {
		bendpoints = points;
		firePropertyChange("bendpoint", null, null);//$NON-NLS-1$
	}

	public void setSource(LogicSubpart e) {
		Object old = source;
		source = e;
		firePropertyChange("source", old, source);//$NON-NLS-1$
	}

	public void setSourceTerminal(String s) {
		Object old = sourceTerminal;
		sourceTerminal = s;
		firePropertyChange("sourceTerminal", old, sourceTerminal);//$NON-NLS-1$
	}

	public void setTarget(LogicSubpart e) {
		Object old = target;
		target = e;
		firePropertyChange("target", old, target);//$NON-NLS-1$
	}

	public void setTargetTerminal(String s) {
		Object old = targetTerminal;
		targetTerminal = s;
		firePropertyChange("targetTerminal", old, targetTerminal);//$NON-NLS-1$
	}

	public void setValue(boolean value) {
		if (value == this.value)
			return;
		this.value = value;
		if (target != null)
			target.update();
		firePropertyChange("value", null, null);//$NON-NLS-1$
	}

	public String toString() {
		return "Wire(" + getSource() + "," + getSourceTerminal() + "->" + getTarget() + "," + getTargetTerminal() + ")";//$NON-NLS-5$//$NON-NLS-4$//$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$
	}

}
