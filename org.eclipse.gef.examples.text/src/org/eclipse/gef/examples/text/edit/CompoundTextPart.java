/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.gef.examples.text.edit;

import java.beans.PropertyChangeEvent;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.text.BlockFlow;
import org.eclipse.draw2d.text.CaretInfo;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.InlineFlow;
import org.eclipse.gef.examples.text.TextLocation;
import org.eclipse.gef.examples.text.figures.CommentPage;
import org.eclipse.gef.examples.text.model.Container;
import org.eclipse.gef.examples.text.requests.CaretRequest;
import org.eclipse.gef.examples.text.requests.SearchResult;

/**
 * @since 3.1
 */
public abstract class CompoundTextPart extends AbstractTextPart {

	public CompoundTextPart(Object model) {
		setModel(model);
	}

	public boolean acceptsCaret() {
		for (Iterator iter = getChildren().iterator(); iter.hasNext();) {
			TextEditPart part = (TextEditPart) iter.next();
			if (part.acceptsCaret())
				return true;
		}
		return false;
	}

	public void activate() {
		super.activate();
		getContainer().getStyle().addPropertyChangeListener(this);
	}

	protected void createEditPolicies() {
	}

	protected IFigure createFigure() {
		Figure figure = null;
		switch (getContainer().getType()) {
		case Container.TYPE_INLINE:
			figure = new InlineFlow();
			break;
		case Container.TYPE_COMMENT:
			figure = new CommentPage();
			break;
		case Container.TYPE_PARAGRAPH:
			figure = new BlockFlow();
			figure.setBorder(new MarginBorder(4, 2, 4, 0));
			break;
		case Container.TYPE_ROOT:
			figure = new FlowPage();
			figure.setBorder(new MarginBorder(4));
			break;
		default:
			throw new RuntimeException("unexpected container");
		}
		return figure;
	}

	public void deactivate() {
		getContainer().getStyle().removePropertyChangeListener(this);
		super.deactivate();
	}

	public CaretInfo getCaretPlacement(int offset, boolean trailing) {
		throw new RuntimeException("This part cannot place the caret");
	}

	protected Container getContainer() {
		return (Container) getModel();
	}

	/**
	 * @see TextEditPart#getLength()
	 */
	public int getLength() {
		return getChildren().size();
	}

	protected List getModelChildren() {
		return getContainer().getChildren();
	}

	/**
	 * @see TextEditPart#getTextLocation(int, TextLocation)
	 */
	public void getTextLocation(CaretRequest search, SearchResult result) {
		if (search.getType() == CaretRequest.LINE_BOUNDARY) {
			if (search.isForward)
				searchLineEnd(search, result);
			else
				searchLineBegin(search, result);
		} else if (search.getType() == CaretRequest.ROW
				|| search.getType() == CaretRequest.LOCATION) {
			if (search.isForward)
				searchLineBelow(search, result);
			else
				searchLineAbove(search, result);
		} else if (search.getType() == CaretRequest.COLUMN
				|| search.getType() == CaretRequest.WORD_BOUNDARY) {
			if (search.isForward)
				searchForward(search, result);
			else
				searchBackward(search, result);
		} else if (getParent() instanceof TextEditPart)
			getTextParent().getTextLocation(search, result);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("children"))
			refreshChildren();
	}

	protected void searchBackward(CaretRequest search, SearchResult result) {
		int childIndex = search.isRecursive ? getChildren().size() - 1
				: getChildren().indexOf(search.where.part) - 1;
		boolean wasRecursive = search.isRecursive;
		search.setRecursive(true);
		while (childIndex >= 0) {
			TextEditPart part = (TextEditPart) getChildren().get(childIndex--);
			part.getTextLocation(search, result);
			if (result.location != null)
				return;
		}
		search.setRecursive(wasRecursive);
		if (!search.isRecursive && getParent() instanceof TextEditPart) {
			if (this instanceof BlockTextPart)
				search.isInto = true;
			search.setReferenceTextLocation(this, 0);
			getTextParent().getTextLocation(search, result);
		}
	}

	protected void searchForward(CaretRequest search, SearchResult result) {
		int childIndex = search.isRecursive ? 0 : getChildren().indexOf(
				search.where.part) + 1;
		int childCount = getChildren().size();
		boolean wasRecursive = search.isRecursive;
		search.setRecursive(true);
		while (childIndex < childCount) {
			TextEditPart part = (TextEditPart) getChildren().get(childIndex++);
			part.getTextLocation(search, result);
			if (result.location != null)
				return;
		}
		search.setRecursive(wasRecursive);
		if (!search.isRecursive && getParent() instanceof TextEditPart) {
			if (this instanceof BlockTextPart)
				search.isInto = true;
			search.setReferenceTextLocation(this, getLength());
			getTextParent().getTextLocation(search, result);
		}
	}

	protected void searchLineAbove(CaretRequest search, SearchResult result) {
		int childIndex;
		TextEditPart part;
		if (search.isRecursive)
			childIndex = getChildren().size() - 1;
		else {
			childIndex = getChildren().indexOf(search.where.part);
			if (search.where.offset == 0)
				childIndex--;
		}

		boolean wasRecursive = search.isRecursive;
		search.setRecursive(true);
		while (childIndex >= 0) {
			part = (TextEditPart) getChildren().get(childIndex--);
			part.getTextLocation(search, result);
			if (result.bestMatchFound)
				return;
		}
		search.setRecursive(wasRecursive);
		if (!search.isRecursive && getParent() instanceof TextEditPart) {
			search.setReferenceTextLocation(this, 0);
			getTextParent().getTextLocation(search, result);
		}
	}

	protected void searchLineBegin(CaretRequest search, SearchResult result) {
		int childIndex = 0;
		int childCount = getChildren().size();
		search.setRecursive(true);
		while (childIndex < childCount) {
			TextEditPart newPart = (TextEditPart) getChildren().get(
					childIndex++);
			newPart.getTextLocation(search, result);
			if (result.location != null)
				return;
		}
	}

	protected void searchLineBelow(CaretRequest search, SearchResult result) {
		// The top of this figure must be below the bottom of the caret
		// if (getFigure().getBounds().y < caret.bottom())
		// return null;

		int childIndex;
		int childCount = getChildren().size();
		if (search.isRecursive || (!search.isRecursive && search.where == null))
			childIndex = 0;
		else {
			childIndex = getChildren().indexOf(search.where.part);
			if (search.where.offset == search.where.part.getLength())
				childIndex++;
		}

		boolean wasRecursive = search.isRecursive;
		search.setRecursive(true);
		while (childIndex < childCount) {
			TextEditPart part = (TextEditPart) getChildren().get(childIndex++);
			part.getTextLocation(search, result);
			if (result.bestMatchFound)
				return;
		}
		search.setRecursive(wasRecursive);
		if (!search.isRecursive && getParent() instanceof TextEditPart) {
			search.setReferenceTextLocation(this, getLength());
			getTextParent().getTextLocation(search, result);
		}
	}

	protected void searchLineEnd(CaretRequest search, SearchResult result) {
		int childIndex = getChildren().size() - 1;
		TextEditPart child;
		search.setRecursive(true);
		while (childIndex >= 0) {
			child = (TextEditPart) getChildren().get(childIndex--);
			child.getTextLocation(search, result);
			if (result.location != null)
				return;
		}
	}

}
