package org.eclipse.draw2d;

import org.eclipse.draw2d.geometry.Rectangle;

public interface UpdateListener {

void notifyPainting(Rectangle damage);

void notifyValidating();

}
