package com.ibm.etools.gef.ui.palette;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.draw2d.*;
import com.ibm.etools.draw2d.FocusEvent;
import com.ibm.etools.gef.palette.*;
import com.ibm.etools.gef.*;
import java.util.*;
import com.ibm.etools.gef.editparts.*;
import com.ibm.etools.draw2d.geometry.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;
import com.ibm.etools.draw2d.Label;

public class CategoryEditPart 
	extends PaletteEditPart
{
		
private ScrollPane scrollpane = null;
private Toggle collapseToggle = null;
private Orientable arrow = null;
private Label categoryLabel = null;

private final static Border BORDER_TITLE_MARGIN = new MarginBorder(2,5,2,2);
private final static Color COLOR_TITLE_BACKGROUND = 
	FigureUtilities.mixColors(ColorConstants.button, ColorConstants.white);

public CategoryEditPart(PaletteContainer category){
	setModel(category);
}

public void activate(){
	super.activate();
	
	final Label tipLabel = new Label(categoryLabel.getText(),categoryLabel.getIcon());	
	final Control ctrl = getViewer().getControl();
	
	tipLabel.setOpaque(true);
	tipLabel.setBackgroundColor(ColorConstants.tooltipBackground);
	tipLabel.setForegroundColor(ColorConstants.tooltipForeground);	
	tipLabel.setBorder(new CategoryToolTipBorder());
	
	collapseToggle.addMouseMotionListener(new MouseMotionListener.Stub(){
		private EditPartTipHelper tipHelper;
		public void mouseMoved(MouseEvent e){
			Rectangle labelBounds = categoryLabel.getBounds();
			if(categoryLabel.isTextTruncated() && labelBounds.contains(e.x,e.y)){
				if(tipHelper == null){
					tipHelper = new EditPartTipHelper(ctrl);	
					Point labelLoc = categoryLabel.getLocation();
					org.eclipse.swt.graphics.Point absolute;
					absolute = ctrl.toDisplay(new org.eclipse.swt.graphics.Point(labelLoc.x, 
														    labelLoc.y));
					// Adjust position to give "raised" appearance
					tipHelper.displayToolTipAt(tipLabel, absolute.x-2, absolute.y-5);
				}
				else
					tipHelper = null;	
			}
			
		}	
	});

	collapseToggle.addFocusListener(new FocusListener.Stub() {
		public void focusGained(FocusEvent fe) {
			getRoot().getViewer().select(CategoryEditPart.this);
		}
	});

	tipLabel.addMouseListener(new MouseListener.Stub(){
		public void mousePressed(MouseEvent e){
			if(!collapseToggle.isSelected())
				collapseToggle.setSelected(true);
			else
				collapseToggle.setSelected(false);
		}
	}); 			
}	

private Figure createCollapseLabel(PaletteContainer categoryModel){
	final Figure figure = new Figure();
	figure.setForegroundColor(ColorConstants.black);
	ToolbarLayout layout = new ToolbarLayout(ToolbarLayout.HORIZONTAL);
	layout.setSpacing(2);
	layout.setMinorAlignment(ToolbarLayout.ALIGN_CENTER);
	figure.setLayoutManager(layout);
	Triangle triangle = getArrowRight();
	triangle.setMaximumSize(new Dimension(10,10));
	arrow = triangle;
	figure.add(triangle);
	categoryLabel = new Label(categoryModel.getLabel(),categoryModel.getSmallIcon());
	categoryLabel.setLabelAlignment(Label.LEFT);
	figure.add(categoryLabel);
	return figure;
}

private Toggle createCollapseToggle(){
	Toggle toggle = new Toggle(createCollapseLabel((PaletteContainer)getModel()));
	toggle.setBackgroundColor(COLOR_TITLE_BACKGROUND);
	toggle.setOpaque(true);
	toggle.setSelected(true);
	toggle.addChangeListener(new ChangeListener(){
		public void handleStateChanged(ChangeEvent event){
			if(event.getPropertyName().equals(ButtonModel.SELECTED_PROPERTY)){
				refreshVisuals();
			}
		}
	});
	toggle.setBorder(BORDER_TITLE_MARGIN);
	return toggle;
}

public IFigure createFigure(){
	AnimatableFigure mainFigure = new AnimatableFigure();
	mainFigure.setBackgroundColor(ColorConstants.white);
	mainFigure.setLayoutManager(new ToolbarLayout());

	collapseToggle = createCollapseToggle();
	scrollpane = new ScrollPane();
	scrollpane.getViewport().setContentsTracksWidth(true);
	scrollpane.setMinimumSize(new Dimension(0,0));
	scrollpane.setHorizontalScrollBarVisibility(ScrollPane.NEVER);
	scrollpane.setVerticalScrollBar(new PaletteScrollBar());
	scrollpane.getVerticalScrollBar().setStepIncrement(22);
	scrollpane.setLayoutManager(new OverlayScrollPaneLayout());
//	scrollpane.setBorder(new SeparatorBorder());
	scrollpane.setView(new Figure());
	scrollpane.getView().setLayoutManager(new ToolbarLayout());
	mainFigure.add(collapseToggle);
	mainFigure.add(scrollpane);
	return mainFigure;
}

public IFigure getContentPane() {
	return scrollpane.getView();
}

public boolean isExpanded(){
	return collapseToggle.isSelected();
}

protected void addChildVisual(EditPart childEditPart, int index){
	if((index>0) && (childEditPart instanceof GroupEditPart))
		((GroupEditPart)childEditPart).getFigure().setBorder(new SeparatorBorder());
	super.addChildVisual(childEditPart, index);
}

/**
 * Sets the minimum preferred size of certain figures, as
 * not doing so results in the figure's minimum size being
 * its preferred sizes, or the preferred size of the scrollbar's
 * which in-turn doesnt allow for compression.
 * Sometimes the scrollbar's minimum size if far more than
 * the compression desired, hence specific setting is required.
 */
public void refreshChildren(){
	super.refreshChildren();
//	Dimension minSize = collapseToggle.getPreferredSize();
//	minSize.height+=getFigure().getInsets().getHeight();
//	getFigure().setMinimumSize(new Dimension(0,minSize.height));
//	scrollpane.setMinimumSize(new Dimension(0,0));
}

protected void refreshVisuals(){
	super.refreshVisuals();
	if(collapseToggle.isSelected()){
		scrollpane.setVisible(true);
		arrow.setDirection(Orientable.SOUTH);
		((AnimatableFigure)getFigure()).expand();
		scrollpane.setVerticalScrollBarVisibility(ScrollPane.AUTOMATIC);
	}else{
		arrow.setDirection(Orientable.EAST);
		scrollpane.setVerticalScrollBarVisibility(ScrollPane.NEVER);
		((AnimatableFigure)getFigure()).collapse();
		scrollpane.setVisible(false);
	}
}

public void setExpanded(boolean expanded){
	if (expanded == collapseToggle.isSelected())
		return;
	collapseToggle.setSelected(expanded);	
}

public void setSelected(int value){
	super.setSelected(value);
	if (value == SELECTED_PRIMARY)
		collapseToggle.requestFocus();
}

}