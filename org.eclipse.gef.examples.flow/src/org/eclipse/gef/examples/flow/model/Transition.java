package org.eclipse.gef.examples.flow.model;

/**
 * @author hudsonr
 * Created on Jun 30, 2003
 */
public class Transition extends FlowElement {

public Activity source, target;

public Transition(Activity source, Activity target) {
	this.source = source;
	this.target = target;

	source.addOutput(this);
	target.addInput(this);
}

}
