package com.ibm.etools.gef.examples.logicdesigner.model;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import com.ibm.etools.gef.*;
import com.ibm.etools.draw2d.geometry.Point;
import com.ibm.etools.draw2d.Connection;
import com.ibm.etools.draw2d.Bendpoint;

public class Wire
	extends LogicElement
{

static final long serialVersionUID = 1;
protected boolean value;
protected LogicSubpart
	source,
	target;
protected String
	sourceTerminal,
	targetTerminal;
protected List bendpoints = new ArrayList();

public void attachSource(){
	if (getSource() == null)
		return;
	getSource().connectOutput(this);
}

public void attachTarget(){
	if (getTarget() == null)
		return;
	getTarget().connectInput(this);
}

public void detachSource(){
	if (getSource() == null)
		return;
	getSource().disconnectOutput(this);
}

public void detachTarget(){
	if (getTarget() == null)
		return;
	getTarget().disconnectInput(this);
}

public List getBendpoints() {
	return bendpoints;
}

public LogicSubpart getSource(){
	return source;
}

public String getSourceTerminal(){
	return sourceTerminal;
}

public LogicSubpart getTarget(){
	return target;
}

public String getTargetTerminal(){
	return targetTerminal;
}

public boolean getValue(){
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

public void setSource(LogicSubpart e){
	Object old = source;
	source = e;
	firePropertyChange("source", old, source);//$NON-NLS-1$
}

public void setSourceTerminal(String s){
	Object old = sourceTerminal;
	sourceTerminal = s;
	firePropertyChange("sourceTerminal", old, sourceTerminal);//$NON-NLS-1$
}

public void setTarget(LogicSubpart e){
	target = e;
}

public void setTargetTerminal(String s){
	targetTerminal = s;
}

public void setValue(boolean value){
	if (value == this.value) return;
	this.value = value;
	if (target != null)
		target.update();
	firePropertyChange("value", null, null);//$NON-NLS-1$
}

public String toString() {
	return "Wire(" + getSource() + "," + getSourceTerminal() + "->" + getTarget() + "," + getTargetTerminal() + ")";//$NON-NLS-5$//$NON-NLS-4$//$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$
}

}
