package org.eclipse.draw2d;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */


public class FocusEvent {

public IFigure loser, gainer;

public FocusEvent( IFigure loser, IFigure gainer ){
	this.loser = loser;
	this.gainer = gainer;
}	

}