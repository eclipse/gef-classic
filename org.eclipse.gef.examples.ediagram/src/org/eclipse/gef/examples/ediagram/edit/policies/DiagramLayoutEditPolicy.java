/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.ediagram.edit.policies;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

import org.eclipse.gef.examples.ediagram.edit.parts.PackageEditPart;
import org.eclipse.gef.examples.ediagram.editor.OutlineToDiagramTransfer;
import org.eclipse.gef.examples.ediagram.figures.PackageFigure;
import org.eclipse.gef.examples.ediagram.figures.SelectableLabel;
import org.eclipse.gef.examples.ediagram.model.Diagram;
import org.eclipse.gef.examples.ediagram.model.Link;
import org.eclipse.gef.examples.ediagram.model.NamedElementView;
import org.eclipse.gef.examples.ediagram.model.Node;
import org.eclipse.gef.examples.ediagram.model.ReferenceView;
import org.eclipse.gef.examples.ediagram.model.commands.ChangeBoundsCommand;
import org.eclipse.gef.examples.ediagram.model.commands.CreateNodeCommand;
import org.eclipse.gef.examples.ediagram.model.commands.ShowOppositeCommand;
import org.eclipse.gef.examples.ediagram.model.commands.TransferLinkCommand;
import org.eclipse.gef.examples.ediagram.model.util.Utilities;
import org.eclipse.gef.examples.ediagram.outline.InheritanceModel;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class DiagramLayoutEditPolicy 
	extends XYLayoutEditPolicy
{

protected Command createAddCommand(EditPart child, Object constraint) {
	return UnexecutableCommand.INSTANCE;
}

protected Command createChangeConstraintCommand(EditPart child, Object constraint) {
	return null;
}

protected Command createChangeConstraintCommand(ChangeBoundsRequest request,
		EditPart child, Object constraint) {
	Node node = (Node)child.getModel();
	Point newLocation = node.getLocation().getTranslated(request.getMoveDelta());
	int newWidth = request.getSizeDelta().width;
	if (newWidth != 0) {
		newWidth += ((GraphicalEditPart)child).getFigure().getBounds().width;
		if (newWidth < 10)
			return UnexecutableCommand.INSTANCE;
	} else
		newWidth = ((GraphicalEditPart)child).getFigure().getBounds().width;
	return new ChangeBoundsCommand(node, newLocation, newWidth);
}

protected EditPolicy createChildEditPolicy(EditPart child) {
	if (child instanceof PackageEditPart) {
		return new NonResizableEditPolicy() {
			private SelectableLabel getSelectableLabel() {
				return ((PackageFigure)((PackageEditPart)getHost()).getFigure())
						.getLabel();
			}
			protected void hideSelection() {
				super.hideSelection();
				getSelectableLabel().setSelected(false);
				getSelectableLabel().setFocus(false);
			}
			protected void showSelection() {
				super.showSelection();
				getSelectableLabel().setSelected(true);
				getSelectableLabel().setFocus(true);
			}
		};
	}
	ResizableEditPolicy childPolicy = new ResizableEditPolicy();
	childPolicy.setResizeDirections(PositionConstants.EAST_WEST);
	return childPolicy;
}

protected Command getCreateCommand(CreateRequest request) {
	if (request.getNewObject() instanceof Node) {
		Point loc = request.getLocation();
		getHostFigure().translateToRelative(loc);
		return new CreateNodeCommand((Node)request.getNewObject(), 
				(Diagram)getHost().getModel(), loc);
	} else if (request.getNewObject() instanceof List) {
		List views = (List)request.getNewObject();
		List businessModels = (List)request.getExtendedData()
				.get(OutlineToDiagramTransfer.TYPE_NAME);
		if (!(businessModels.get(businessModels.size() - 1) instanceof ProcessedMarker)) {
			processSelection(businessModels, views);
			// Once this list has been processed, we append a marker to it so that it's
			// not processed again
			businessModels.add(new ProcessedMarker());
		}
		if (views.isEmpty())
			return UnexecutableCommand.INSTANCE;
		CompoundCommand command = new CompoundCommand("Drag from Outline");
		Point loc = request.getLocation();
		getHostFigure().translateToRelative(loc);
		for (int i = 0; i < views.size(); i++) {
			Object view = views.get(i);
			if (view instanceof NamedElementView) {
				command.add(new CreateNodeCommand((Node)view, 
						(Diagram)getHost().getModel(), loc.getTranslated(i * 40, i * 40)));
			} else if (view instanceof LinkInfoHolder) {
				LinkInfoHolder info = (LinkInfoHolder)view;
				command.add(new TransferLinkCommand(info.link, info.src, info.target));
			} else if (view instanceof ShowOppositeMarker) {
				command.add(new ShowOppositeCommand(((ShowOppositeMarker)view).refView));
			}
		}
		return command;
	}
	return UnexecutableCommand.INSTANCE;
}

protected Command getDeleteDependantCommand(Request request) {
	return null;
}

private void processSelection(List models, List views) {
	List newModels = new ArrayList();
	List newViews = new ArrayList();
	for (int i = 0; i < models.size(); i++) {
		Object model = models.get(i);
		if (Utilities.findViewFor(model, getHost().getModel()) != null)
			continue;
		if (model instanceof EReference) {
			EReference ref = (EReference)model;
			ReferenceView oppView = (ReferenceView)Utilities
					.findViewFor(ref.getEOpposite(), getHost().getModel());
			int oppIndex = newModels.indexOf(ref.getEOpposite());
			if (oppView != null || oppIndex != -1) {
				// the opposite was found
				newModels.add(model);
				if (oppView != null) {
					newViews.add(new ShowOppositeMarker(oppView));
				} else {
					newViews.add(new ShowOppositeMarker((ReferenceView)
							((LinkInfoHolder)newViews.get(oppIndex)).link));
				}
				continue;
			}
			Node srcView = (Node)Utilities.findViewFor(
						ref.getEContainingClass(), getHost().getModel());
			Node destView = (Node)Utilities.findViewFor(
					ref.getEReferenceType(), getHost().getModel());
			if (srcView == null) {
				int index = models.indexOf(ref.getEContainingClass());
				srcView = index >= 0 ? (Node)views.get(index) : null;
			}
			if (destView == null) {
				int index = models.indexOf(ref.getEReferenceType());
				destView = index >= 0 ? (Node)views.get(index) : null;
			}
			if (srcView != null && destView != null) {
				ReferenceView refView = (ReferenceView)views.get(i);
				refView.setEReference(ref);
				newModels.add(model);
				newViews.add(new LinkInfoHolder(refView, srcView, destView));
			}
		} else if (model instanceof InheritanceModel) {
			InheritanceModel link = (InheritanceModel)model;
			Node srcView = (Node)Utilities.findViewFor(link.getSubType(), getHost().getModel());
			Node destView = (Node)Utilities.findViewFor(link.getSuperType(), getHost().getModel());
			if (srcView == null) {
				int index = models.indexOf(link.getSubType());
				srcView = index >= 0 ? (Node)views.get(index) : null;
			}
			if (destView == null) {
				int index = models.indexOf(link.getSuperType());
				destView = index >= 0 ? (Node)views.get(index) : null;
			}
			if (srcView != null && destView != null) {
				newModels.add(model);
				newViews.add(new LinkInfoHolder((Link)views.get(i), srcView, destView));
			}
		} else if (model instanceof ENamedElement) {
			NamedElementView classView = (NamedElementView)views.get(i);
			classView.setENamedElement((ENamedElement)model);
			newModels.add(0, model);
			newViews.add(0, classView);	
		}
	}
	models.clear();
	views.clear();
	models.addAll(newModels);
	views.addAll(newViews);
}

private static class LinkInfoHolder {
	private Link link;
	private Node src;
	private Node target;
	private LinkInfoHolder(Link link, Node src, Node target) {
		this.link = link;
		this.src = src;
		this.target = target;
	}
}

private static class ShowOppositeMarker {
	private ReferenceView refView;
	private ShowOppositeMarker(ReferenceView view) {
		refView = view;
	}
}

private static class ProcessedMarker {}

}