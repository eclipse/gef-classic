package org.eclipse.gef;

import org.eclipse.draw2d.geometry.PrecisionRectangle;

import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

/**
 * Tools use SnapToHelpers on a per-operation basis.  For example, for a move operation,
 * the life-cycle of a SnapToHelper begins when a drag is initiated, and ends when the
 * drag is over.  If another drag is initiated right after the first one is completed,
 * new SnapToHelpers are employed.  The same applies for creation and resize operations.
 * 
 * @author Randy Hudson
 */
public interface SnapToHelper {

public static final int SNAP_HORIZONTAL = 1;
public static final int SNAP_VERTICAL = 2;

int snapCreateRequest(CreateRequest request, PrecisionRectangle baseRect, 
                      int snapOrientation);

/**
 * This method should modify the given move or clone request in order to snap the figure
 * being dragged.  The receiver may modify the baseRect and compoundRect parameters for 
 * whatever purpose.
 * 
 * @param	request			The move or clone request
 * @param	baseRect		The bounds of the figure that received the mouse down that 
 * 							initiated the drag
 * @param	compoundRect	The unioned bounds of all the figures being dragged
 * @param	snapOrientation	Indicates the direction in which snapping should occur:
 * 							<code>SNAP_HORIZONTAL</code> or <code>SNAP_VERTICAL</code>
 * 							or both.
 * @return	The direction in which snapping still needs to occur 
 * 			(<code>SNAP_HORIZONTAL</code> or <code>SNAP_VERTICAL</code> or both or none)
 */
int snapMoveRequest(ChangeBoundsRequest request, PrecisionRectangle baseRect,
                    PrecisionRectangle compoundRect, int snapOrientation);

int snapResizeRequest(ChangeBoundsRequest req, PrecisionRectangle baseRec,
                      int snapOrientation);

}