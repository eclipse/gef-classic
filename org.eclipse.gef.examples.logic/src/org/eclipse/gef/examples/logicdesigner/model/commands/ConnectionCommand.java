/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.logicdesigner.model.commands;

import java.util.Iterator;
import java.util.Vector;

import org.eclipse.gef.commands.Command;

import org.eclipse.gef.examples.logicdesigner.LogicMessages;
import org.eclipse.gef.examples.logicdesigner.model.LogicSubpart;
import org.eclipse.gef.examples.logicdesigner.model.Wire;

public class ConnectionCommand
	extends Command
{

protected LogicSubpart oldSource;
protected String oldSourceTerminal;
protected LogicSubpart oldTarget;
protected String oldTargetTerminal;
protected LogicSubpart source;
protected String sourceTerminal;
protected LogicSubpart target; 
protected String targetTerminal; 
protected Wire wire;

public ConnectionCommand() {
	super(LogicMessages.ConnectionCommand_Label);
}

public boolean canExecute(){
	if (target != null) {
		Vector conns = target.getConnections();
		Iterator i = conns.iterator();
		while (i.hasNext()) {
			Wire conn = (Wire)i.next();
			if (targetTerminal != null && conn.getTargetTerminal() != null)
				if (conn.getTargetTerminal().equals(targetTerminal))
					return false;
		}
	}
	return true;
}

public void execute() {
	if (source != null){
		wire.detachSource();
		wire.setSource(source);
		wire.setSourceTerminal(sourceTerminal);
		wire.attachSource();
	}
	if (target != null) {
		wire.detachTarget();
		wire.setTarget(target);
		wire.setTargetTerminal(targetTerminal);
		wire.attachTarget();
	}
	if (source == null && target == null){
		wire.detachSource();
		wire.detachTarget();
		wire.setTarget(null);
		wire.setSource(null);
	}
}

public String getLabel() {
	return LogicMessages.ConnectionCommand_Description;
}

public LogicSubpart getSource() {
	return source;
}

public java.lang.String getSourceTerminal() {
	return sourceTerminal;
}

public LogicSubpart getTarget() {
	return target;
}

public String getTargetTerminal() {
	return targetTerminal;
}

public Wire getWire() {
	return wire;
}

public void redo() { 
	execute(); 
}

public void setSource(LogicSubpart newSource) {
	source = newSource;
}

public void setSourceTerminal(String newSourceTerminal) {
	sourceTerminal = newSourceTerminal;
}

public void setTarget(LogicSubpart newTarget) {
	target = newTarget;
}

public void setTargetTerminal(String newTargetTerminal) {
	targetTerminal = newTargetTerminal;
}

public void setWire(Wire w) {
	wire = w;
	oldSource = w.getSource();
	oldTarget = w.getTarget();
	oldSourceTerminal = w.getSourceTerminal();
	oldTargetTerminal = w.getTargetTerminal();	
}

public void undo() {
	source = wire.getSource();
	target = wire.getTarget();
	sourceTerminal = wire.getSourceTerminal();
	targetTerminal = wire.getTargetTerminal();

	wire.detachSource();
	wire.detachTarget();

	wire.setSource(oldSource);
	wire.setTarget(oldTarget);
	wire.setSourceTerminal(oldSourceTerminal);
	wire.setTargetTerminal(oldTargetTerminal);

	wire.attachSource();
	wire.attachTarget();
}

}
