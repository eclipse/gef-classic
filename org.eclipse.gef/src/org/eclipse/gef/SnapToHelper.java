package org.eclipse.gef;

import org.eclipse.draw2d.PositionConstants;
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
 * @author Pratik Shah
 */
public interface SnapToHelper 
	extends PositionConstants
{

public static final String CTRL_KEY = "org.eclipse.gef.SnapToHelper.ctrl"; //$NON-NLS-1$
public static final String SHIFT_KEY = "org.eclipse.gef.SnapToHelper.shift"; //$NON-NLS-1$

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
 * 							<code>EAST_WEST</code> or <code>NORTH_SOUTH</code>
 * 							or both.
 * @return	The direction in which snapping still needs to occur 
 * 			(<code>EAST_WEST</code> or <code>NORTH_SOUTH</code> or both or none)
 */
int snapMoveRequest(ChangeBoundsRequest request, PrecisionRectangle baseRect,
                    PrecisionRectangle compoundRect, int snapOrientation);

int snapResizeRequest(ChangeBoundsRequest req, PrecisionRectangle baseRec,
                      int snapOrientation);

}