package org.eclipse.gef.ui.actions;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.internal.InternalImages;
import org.eclipse.ui.actions.RetargetAction;

/**
 * @author Eric Bordeau
 */
public class AlignmentRetargetAction extends RetargetAction {

/**
 * Constructs a new AlignmentRetargetAction with the default ID, label and image based on
 * the given alignment constant.  Possible values are {@link PositionConstants#BOTTOM},
 * {@link PositionConstants#CENTER}, {@link PositionConstants#LEFT}, 
 * {@link PositionConstants#MIDDLE}, {@link PositionConstants#RIGHT}, and 
 * {@link PositionConstants#TOP}.
 * @param align the alignment.
 */
public AlignmentRetargetAction(int align) {
	super(null, null);
	switch (align) {
		case PositionConstants.BOTTOM: {
			setId(GEFActionConstants.ALIGN_BOTTOM);
			setText(GEFMessages.AlignBottomAction_Label);
			setToolTipText(GEFMessages.AlignBottomAction_Tooltip);
			setImageDescriptor(InternalImages.DESC_VERT_ALIGN_BOTTOM);
			break;
		}
		case PositionConstants.CENTER: {
			setId(GEFActionConstants.ALIGN_CENTER);
			setText(GEFMessages.AlignCenterAction_Label);
			setToolTipText(GEFMessages.AlignCenterAction_Tooltip);
			setImageDescriptor(InternalImages.DESC_HORZ_ALIGN_CENTER);
			break;
		}
		case PositionConstants.LEFT: {
			setId(GEFActionConstants.ALIGN_LEFT);
			setText(GEFMessages.AlignLeftAction_Label);
			setToolTipText(GEFMessages.AlignLeftAction_Tooltip);
			setImageDescriptor(InternalImages.DESC_HORZ_ALIGN_LEFT);
			break;
		}
		case PositionConstants.MIDDLE: {
			setId(GEFActionConstants.ALIGN_MIDDLE);
			setText(GEFMessages.AlignMiddleAction_Label);
			setToolTipText(GEFMessages.AlignMiddleAction_Tooltip);
			setImageDescriptor(InternalImages.DESC_VERT_ALIGN_MIDDLE);
			break;
		}
		case PositionConstants.RIGHT: {
			setId(GEFActionConstants.ALIGN_RIGHT);
			setText(GEFMessages.AlignRightAction_Label);
			setToolTipText(GEFMessages.AlignRightAction_Tooltip);
			setImageDescriptor(InternalImages.DESC_HORZ_ALIGN_RIGHT);
			break;
		}
		case PositionConstants.TOP: {
			setId(GEFActionConstants.ALIGN_TOP);
			setText(GEFMessages.AlignTopAction_Label);
			setToolTipText(GEFMessages.AlignTopAction_Tooltip);
			setImageDescriptor(InternalImages.DESC_VERT_ALIGN_TOP);
			break;
		}
	}
}

}
