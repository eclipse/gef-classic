package org.eclipse.mylar.zest.core.internal.nestedgraphviewer.policies;

import java.util.Collections;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy;
import org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

/**
 * A null-edit policy doesn't allow figures to be moved, or resized in the layout.
 * @author Del Myers
 *
 */
//@tag bug(152613-Client-Supplier(fix)) : disallow anything top-level from being moved around
public final class NullLayoutEditPolicy extends ConstrainedLayoutEditPolicy {
	protected Command createChangeConstraintCommand(EditPart child, Object constraint) {
		return createCommand();
	}

	protected Object getConstraintFor(Point point) {
		return null;
	}

	protected Object getConstraintFor(Rectangle rect) {
		return null;
	}

	protected Command getCreateCommand(CreateRequest request) {
		return createCommand();
	}

	private Command createCommand() {
		return new Command(){
			public boolean canExecute() {
				return false;
			}
			public boolean canUndo() {
				return false;
			}
		};
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.ConstrainedLayoutEditPolicy#createChildEditPolicy(org.eclipse.gef.EditPart)
	 */
	protected EditPolicy createChildEditPolicy(EditPart child) {
		return new SelectionHandlesEditPolicy(){
			protected List createSelectionHandles() {
				return Collections.EMPTY_LIST;
			}
		};
	}
}
