package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

class ButtonStateTransitionListener {

final protected void cancel() throws Exception{}
public void canceled(){}
final void cancelled() throws Exception{}

final protected void press() throws Exception{}
public void pressed(){}

final protected void release() throws Exception{}
public void released(){}

public void resume(){}

public void suspend(){}

}