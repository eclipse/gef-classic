package org.eclipse.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */


/**
 * The listener interface for receiving Connection events from
 * EditParts that serve as connection nodes.
 */
public interface NodeListener {

/**
 * Called prior to removing the connection from its source node. The source is not passed,
 * but can still be obtained at this point by calling {@link ConnectionEditPart#getSource
 * connection.getSource()}
 * @param connection the connection
 * @param index the index
 */
void removingSourceConnection(ConnectionEditPart connection, int index);

/**
 * Called prior to removing the connection from its target node. The target is not passed,
 * but can still be obtained at this point by calling {@link ConnectionEditPart#getTarget
 * connection.getTarget()}
 * @param connection the connection
 * @param index the index
 */
void removingTargetConnection(ConnectionEditPart connection, int index);

/**
 * Called after the connection has been added to its source node.
 * @param connection the connection
 * @param index the index
 */
void sourceConnectionAdded(ConnectionEditPart connection, int index);

/**
 * Called after the connection has been added to its target node.
 * @param connection the connection
 * @param index the index
 */
void targetConnectionAdded(ConnectionEditPart connection, int index);

}