package org.eclipse.gef.editpolicies;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;


import org.eclipse.jface.action.*;

import org.eclipse.gef.*;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.LayerManager;


/**
 * The base implementation for the EditPolicy interface.
 <P>Since this is the default implementation of an interface, this document
 deals with proper sub-classing of this implementation.  This class is not
 the API.  For documentation on proper usage of the public API, see the
 documentation for the interface itself: {@link EditPolicy}.
 <P><Table>
 	<tr><TD><img src="../doc-files/green.gif"/>
 		<TD>Indicates methods that are commonly overridden or even abstract
 	</tr><tr><TD><img src="../doc-files/blue.gif"/>
 		<TD>These methods might be overridden.  Especially if you were
 			extending this class directly.
 	</tr><tr><TD><img src="../doc-files/black.gif"/>
 		<TD>Should rarely be overridden.
 	</tr><tr><TD><img src="../doc-files/dblack.gif"/>
 		<TD>Essentially "internal" and should never be overridden.
</tr></table>
 */
abstract public class AbstractEditPolicy
	implements EditPolicy, RequestConstants
{

private EditPart host;

/**
 Called to activate this EditPolicy.
 When activated, this edit policy may wish to add listeners to its host,
 its host's model, or some other Object.  These listeners should be freed
 in {@link #deactivate()}.  {@link #initialize()} is called to do any setup
 work that does not need to be undone.  There is a current bug that initialize()
 is called every time activate is called.  It will only be called once in
 the future
*/
public void activate(){
	initialize();
}

public void deactivate(){}

protected void debugFeedback(String message){
	if (!GEF.DebugFeedback)
		return;
	GEF.debug("\tFEEDBACK:\t" + toString() + ":\t" + message);//$NON-NLS-2$//$NON-NLS-1$
}

public void eraseSourceFeedback(Request request){}

public void eraseTargetFeedback(Request request){}

public Command getCommand(Request request){return null;}

public EditPart getHost(){
	return host;
}

public EditPart getTargetEditPart(Request request) {
	return null;
}

/**
 * Initialize is called by activate().
 * There is no implied inverse to initialize().
 * Subclasses should override to perform one-time setup.
 */
protected void initialize(){}

public void setHost(EditPart host){
	if (this.host == host)
		return;
	this.host = host;
}

public void showSourceFeedback(Request request){}

public void showTargetFeedback(Request request){}

public String toString(){
	String c = getClass().getName();
	c = c.substring(c.lastIndexOf('.')+1);
	return getHost().toString()+"."+c;//$NON-NLS-1$
}

public boolean understandsRequest(Request req){
	return false;
}

}