package org.eclipse.gef.examples.flow.policies;

import org.eclipse.gef.examples.flow.figures.StartTag;
import org.eclipse.gef.examples.flow.figures.SubgraphFigure;
import org.eclipse.gef.requests.DirectEditRequest;

/**
 * StructuredActivityDirectEditPolicy
 * @author Daniel Lee
 */
public class StructuredActivityDirectEditPolicy
	extends ActivityDirectEditPolicy {

/**
 * @see DirectEditPolicy#showCurrentEditValue(org.eclipse.gef.requests.DirectEditRequest)
 */
protected void showCurrentEditValue(DirectEditRequest request) {
	String value = (String)request.getCellEditor().getValue();
	((StartTag)((SubgraphFigure)getHostFigure()).getHeader()).setText(value);
	
	//hack to prevent async layout from placing the cell editor twice.
	getHostFigure().getUpdateManager().performUpdate();
}

}
