package com.ibm.etools.gef.editparts;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */


import com.ibm.etools.gef.EditPart;

import com.ibm.etools.draw2d.*;

public interface LayerManager {

Object ID = new Object();

IFigure getLayer(Object key);

class Helper {
	static public LayerManager find(EditPart part){
		return (LayerManager)part.getRoot().getViewer().getEditPartRegistry().get(ID);
	}
}

}