package org.eclipse.draw2d;

import org.eclipse.draw2d.geometry.Rectangle;

/**
 * An interface used to notify listeners that the listened to object is updating.
 */
public interface UpdateListener {

/**
 * Notifies the listener that the listened to object is painting.
 * @param damage The area being painted */
void notifyPainting(Rectangle damage);

/**
 * Notifies the listener that the listened to object is validating.
 */
void notifyValidating();

}
