package org.eclipse.gef;

import org.eclipse.draw2d.geometry.Point;

/**
 * @author hudsonr
 */
public interface AutoexposeHelper {

boolean detect(Point where);

boolean step(Point where);

}
