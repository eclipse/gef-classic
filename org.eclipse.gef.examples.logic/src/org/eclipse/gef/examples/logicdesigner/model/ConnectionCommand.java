package org.eclipse.gef.examples.logicdesigner.model;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.gef.commands.AbstractCommand;
import org.eclipse.gef.examples.logicdesigner.LogicMessages;

public class ConnectionCommand
	extends AbstractCommand
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
