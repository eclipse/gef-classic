package com.ibm.etools.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/**
 * A specialization of
 * {@link GraphicalEditPart GraphicalEditPart}
 * for representing connections.
 * Connections must join a Source and Target. They are typically a line between two nodes.
 * A connection could also be a source or target for other connections.  Connections that
 * can exist on their own without a source and target are Associations. Do not use this
 * Type if you are creating associations.
 */
public interface ConnectionEditPart
	extends GraphicalEditPart
{

/**
 * Returns the EditPart at the Source of this connection.
 */
EditPart getSource();

/**
 * Returns the EditPart at the Target of this connection.
 */
EditPart getTarget();

/**
 * Sets the source Editpart of this connection.
 */
void setSource(EditPart source);

/**
 * Sets the target EditPart of this connection.
 *
 * @param target the target EditPart for this connection.
 */
void setTarget(EditPart target);
}
