package com.ibm.etools.gef.tools;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.lang.reflect.Constructor;

import com.ibm.etools.common.command.Command;
import com.ibm.etools.common.command.CommandStack;
import com.ibm.etools.gef.*;
import com.ibm.etools.gef.editparts.LayerManager;
import com.ibm.etools.gef.requests.*;
import com.ibm.etools.draw2d.*;
import com.ibm.etools.draw2d.geometry.*;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.events.*;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.*;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorListener;

abstract public class DirectEditManager {

private static final Color BLUE = ColorConstants.menuBackgroundSelected;
private static final Border BORDER_FRAME = new DirectEditBorder();

private static class DirectEditBorder
	extends AbstractBorder
{
	private static final Insets insets = new Insets(1,2,2,2);
	public Insets getInsets(IFigure figure) {
		return insets;
	}

	public boolean isOpaque() {
		return false;
	}

	public void paint(IFigure figure, Graphics graphics, Insets insets) {
		Rectangle rect = getPaintRectangle(figure, insets);
		graphics.setForegroundColor(ColorConstants.white);
		graphics.drawLine(rect.x, rect.y, rect.x, rect.bottom());
		rect.x++;
		rect.width--;
		rect.resize(-1, -1);
		graphics.setForegroundColor(ColorConstants.black);
		graphics.drawLine(rect.x+2, rect.bottom(), rect.right(), rect.bottom());
		graphics.drawLine(rect.right(), rect.bottom(), rect.right(), rect.y+2);

		rect.resize(-1, -1);
		graphics.setForegroundColor(BLUE);
		graphics.drawRectangle(rect);
	}
}

private AncestorListener ancestorListener;
private EditPartListener editPartListener;
private IFigure cellEditorFrame;
private FocusListener focusListener;
private ICellEditorListener cellEditorListener;
private boolean showingFeedback;
private boolean dirty;
private DirectEditRequest request;
private CellEditorLocator locator;
private GraphicalEditPart source;
private CellEditor ce;
private Class editorType;

public DirectEditManager(GraphicalEditPart source, Class editorType, CellEditorLocator locator){
//	if (!CellEditor.class.isAssignableFrom(editorType))
//		throw new RuntimeException("Class is not a cell editor");
	this.source = source;
	this.locator = locator;
	this.editorType = editorType;
}

protected void bringDown(){
	eraseFeedback();
	unhookCellEditor();
	getCellEditor().deactivate();
	getCellEditor().dispose();
	request = null;
	dirty = false;
	setCellEditor(null);
}

protected void commit(){
	try {
		eraseFeedback();
		if (isDirty()){
			CommandStack stack = getEditPart().getRoot().getViewer().getEditDomain().getCommandStack();
			Command command = getEditPart().getCommand(getDirectEditRequest());
			if (command != null && command.canExecute())
				stack.execute(command);
		}
	} finally {
		bringDown();
	}
}

protected CellEditor createCellEditorOn(Composite composite){
	try {
		Constructor constructor = editorType.getConstructor(new Class[]{Composite.class});
		return (CellEditor)constructor.newInstance(new Object[]{composite});
	} catch (Exception e){
		return null;
	}
}

protected DirectEditRequest createDirectEditRequest(){
	DirectEditRequest req = new DirectEditRequest();
	req.setCellEditor(getCellEditor());
	return req;
}

protected void eraseFeedback(){
	if (showingFeedback){
		LayerManager.Helper.find(getEditPart()).
			getLayer(LayerConstants.FEEDBACK_LAYER).
			remove(getCellEditorFrame());
		cellEditorFrame = null;
		getEditPart().eraseSourceFeedback(getDirectEditRequest());
		showingFeedback = false;
	}
}

protected CellEditor getCellEditor(){
	return ce;
}

private IFigure getCellEditorFrame(){
	if (cellEditorFrame != null)
		return cellEditorFrame;
	cellEditorFrame = new Figure();
	cellEditorFrame.setBorder(BORDER_FRAME);
	return cellEditorFrame;
}

private Control getControl(){
	return ce.getControl();
}

protected DirectEditRequest getDirectEditRequest(){
	if (request == null)
		request = createDirectEditRequest();
	return request;
}

protected GraphicalEditPart getEditPart() {
	return source;
}

private CellEditorLocator getLocator() {
	return locator;
}

private void handleValueChanged(){
	setDirty(true);
	showFeedback();
	placeCellEditor();
}

private void hookCellEditor(){
	ancestorListener = new AncestorListener.Stub() {
		public void ancestorMoved(IFigure source) {
			placeCellEditor();
		}
	};

	getEditPart().getFigure().addAncestorListener(ancestorListener);

	Control control = getControl();
	focusListener = new FocusAdapter(){
		public void focusLost(FocusEvent e){
			commit();
		}
	};

	control.addControlListener(new ControlAdapter() {
		public void controlMoved(ControlEvent e) {
			placeBorder();
		}

		public void controlResized(ControlEvent e) {
			placeBorder();
		}

	});

	control.addFocusListener(focusListener);

	cellEditorListener = new ICellEditorListener(){
		public void cancelEditor(){
			bringDown();
		}
		public void applyEditorValue(){
			commit();
		}
		public void editorValueChanged(boolean old, boolean newState){
			handleValueChanged();
		}
	};
	getCellEditor().addListener(cellEditorListener);
	
	editPartListener = new EditPartListener.Stub (){
		public void partDeactivated(EditPart editpart) {
			bringDown();
		}
	};
	getEditPart().addEditPartListener(editPartListener);
}

protected abstract void initCellEditor();

protected boolean isDirty(){
	return dirty;
}

private void placeBorder(){
	IFigure shadow = getCellEditorFrame();
	Rectangle rect = new Rectangle(getCellEditor().getControl().getBounds());
	rect.expand(shadow.getInsets());
	shadow.translateToRelative(rect);
	shadow.setBounds(rect);
}

private void placeCellEditor(){
	getLocator().relocate(getCellEditor());
}

protected void setCellEditor(CellEditor editor){
	ce = editor;
	if (ce == null)
		return;
	hookCellEditor();
}

protected void setDirty(boolean value){
	dirty = value;
}

protected void setEditPart(GraphicalEditPart source){
	this.source = source;
//	source.addEditPartListener();
}

/**
 * Sets the locator.
 * @param locator The locator to set
 */
public void setLocator(CellEditorLocator locator) {
	this.locator = locator;
}

public void show(){
	if (getCellEditor() != null)
		return;
	Composite composite = (Composite)source.getRoot().getViewer().getControl();
	setCellEditor(createCellEditorOn(composite));
	if (getCellEditor() == null)
		return;
	initCellEditor();
	getCellEditor().activate();
	locator.relocate(getCellEditor());
	getControl().setVisible(true);
	getControl().setFocus();
	showFeedback();
}

private void showCellEditorFrame(){
	LayerManager.Helper.find(getEditPart()).
		getLayer(LayerConstants.FEEDBACK_LAYER).
		add(getCellEditorFrame());
}

public void showFeedback(){
	if (showingFeedback == false)
		showCellEditorFrame();
	showingFeedback = true;
	showCellEditorFrame();
	getEditPart().showSourceFeedback(getDirectEditRequest());
}

protected void unhookCellEditor(){
	if (ancestorListener != null)
		getEditPart().getFigure().removeAncestorListener(ancestorListener);
	Control control = getCellEditor().getControl();
	control.removeFocusListener(focusListener);
	getCellEditor().removeListener(cellEditorListener);
	getEditPart().removeEditPartListener(editPartListener);
}

}