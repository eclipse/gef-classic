package org.eclipse.gef.examples.logicdesigner.dnd;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.dnd.AbstractTransferDropTargetListener;
import org.eclipse.gef.examples.logicdesigner.edit.NativeDropRequest;
import org.eclipse.swt.dnd.Transfer;

public class TextTransferDropTargetListener 
	extends AbstractTransferDropTargetListener 
{

public TextTransferDropTargetListener(EditPartViewer viewer, Transfer xfer) {
	super(viewer, xfer);
}

protected Request createTargetRequest() {
	return new NativeDropRequest();
}

protected NativeDropRequest getNativeDropRequest() {
	return (NativeDropRequest)getTargetRequest();
}

protected void updateTargetRequest(){
	getNativeDropRequest().setData(getCurrentEvent().data);
}

}
