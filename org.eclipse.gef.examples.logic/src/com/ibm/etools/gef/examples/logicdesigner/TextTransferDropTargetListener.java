package com.ibm.etools.gef.examples.logicdesigner;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;

import com.ibm.etools.gef.EditPartViewer;
import com.ibm.etools.gef.Request;
import com.ibm.etools.gef.dnd.AbstractTransferDropTargetListener;
import com.ibm.etools.gef.examples.logicdesigner.edit.NativeDropRequest;

public class TextTransferDropTargetListener 
	extends AbstractTransferDropTargetListener 
{

public TextTransferDropTargetListener(EditPartViewer viewer, Transfer xfer) {
	super(viewer, xfer);
}

protected boolean canHandleDrop(DropTargetEvent event) {
	return true;
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
