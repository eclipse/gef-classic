package org.eclipse.gef.ui.palette;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.ExposeHelper;
import org.eclipse.gef.editparts.ViewportExposeHelper;
import org.eclipse.gef.internal.Internal;
import org.eclipse.gef.palette.PaletteContainer;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

public class CategoryEditPart 
	extends PaletteEditPart
{

private ScrollPane scrollpane = null;
private Toggle collapseToggle = null;
private Label categoryLabel = null;
private int cachedLayout = -1;

private static Border SCROLL_PANE_BORDER = null;

public CategoryEditPart(PaletteContainer category) {
	super(category);
}

public void activate() {
	super.activate();
	final Label tipLabel =
		new Label(categoryLabel.getText(), categoryLabel.getIcon());
	final Control ctrl = getViewer().getControl();
	
	tipLabel.setOpaque(true);
	tipLabel.setBackgroundColor(ColorConstants.tooltipBackground);
	tipLabel.setForegroundColor(ColorConstants.tooltipForeground);	
	tipLabel.setBorder(new CategoryToolTipBorder());
	
	collapseToggle.addMouseMotionListener(new MouseMotionListener.Stub(){
		private EditPartTipHelper tipHelper;
		public void mouseMoved(MouseEvent e) {
			Rectangle labelBounds = categoryLabel.getBounds();
			if (categoryLabel.isTextTruncated()
				&& labelBounds.contains(e.x, e.y)) {
				if (tipHelper == null) {
					tipHelper = new EditPartTipHelper(ctrl);
					Point labelLoc = categoryLabel.getLocation();
					org.eclipse.swt.graphics.Point absolute = ctrl.toDisplay(
							new org.eclipse.swt.graphics.Point(labelLoc.x, labelLoc.y));
					
					int shiftX = 0;
					int shiftY = 0;
					Display display = Display.getCurrent();
					org.eclipse.swt.graphics.Rectangle area = display.getClientArea();
					org.eclipse.swt.graphics.Point end = 
							new org.eclipse.swt.graphics.Point(
											absolute.x + tipLabel.getBounds().width,
											absolute.y + tipLabel.getBounds().height);
					if (!area.contains(end)) {
						shiftX = end.x - (area.x + area.width);
						shiftY = end.y - (area.y + area.height);
						shiftX = shiftX < 0 ? 0 : shiftX;
						shiftY = shiftY < 0 ? 0 : shiftY;
					}
					tipHelper.displayToolTipAt(tipLabel, absolute.x - 4 - shiftX, 
					                                     absolute.y - 4 - shiftY);
				} else
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
		public void mousePressed(MouseEvent e) {
			if (!collapseToggle.isSelected())
				collapseToggle.setSelected(true);
			else
				collapseToggle.setSelected(false);
		}
	}); 			
}

/**
 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#addChildVisual(EditPart, int)
 */
protected void addChildVisual(EditPart childEditPart, int index) {
	super.addChildVisual(childEditPart, index);
}

protected Figure createCollapseLabel() {
	final Figure figure = new Figure();
	figure.setForegroundColor(ColorConstants.black);
	ToolbarLayout layout = new ToolbarLayout(ToolbarLayout.HORIZONTAL);
	layout.setSpacing(2);
	layout.setMinorAlignment(ToolbarLayout.ALIGN_CENTER);
	figure.setLayoutManager(layout);
	categoryLabel = new Label();
	categoryLabel.setLabelAlignment(Label.LEFT);
	figure.add(categoryLabel);
	return figure;
}

protected Toggle createCollapseToggle() {
	Toggle toggle = new Toggle(createCollapseLabel());
	toggle.setOpaque(true);
	toggle.setSelected(true);
	toggle.addChangeListener(new ChangeListener(){
		public void handleStateChanged(ChangeEvent event) {
			if (event
				.getPropertyName()
				.equals(ButtonModel.SELECTED_PROPERTY)) {
				refreshVisuals(true);
			}
		}
	});
	return toggle;
}

public IFigure createFigure() {
	AnimatableFigure mainFigure = new AnimatableFigure();
	mainFigure.setLayoutManager(new ToolbarLayout());

	collapseToggle = createCollapseToggle();
	scrollpane = new ScrollPane();
	scrollpane.getViewport().setContentsTracksWidth(true);
	scrollpane.setMinimumSize(new Dimension(0, 0));
	scrollpane.setHorizontalScrollBarVisibility(ScrollPane.NEVER);
	scrollpane.setVerticalScrollBar(new PaletteScrollBar());
	scrollpane.getVerticalScrollBar().setStepIncrement(22);
	scrollpane.setLayoutManager(new OverlayScrollPaneLayout());
	if (SCROLL_PANE_BORDER == null) {
		SeparatorBorder border = new SeparatorBorder();
		border.setLocation(SeparatorBorder.BOTTOM);
		SCROLL_PANE_BORDER = border;
	}
	scrollpane.setBorder(SCROLL_PANE_BORDER);
	scrollpane.setBackgroundColor(ColorConstants.listBackground);
	scrollpane.setView(new Figure());
	scrollpane.getView().setBorder(MARGIN_BORDER);

	Figure title = new Figure();
	title.add(collapseToggle);
	//@TODO:Pratik
	// Change this image
	Image pin = new Image(null, ImageDescriptor.createFromFile(Internal.class, 
	                             "icons/pin_view.gif").getImageData()); //$NON-NLS-1$
	ImageFigure imageFigure = new ImageFigure(pin);
	ToggleButton pinFigure = new ToggleButton(imageFigure);
	pinFigure.setBorder(new ButtonBorder(ButtonBorder.SCHEMES.TOOLBAR));
	pinFigure.setRolloverEnabled(true);
	pinFigure.setRequestFocusEnabled(false);
	title.add(pinFigure);
	BorderLayout layout = new BorderLayout();
	layout.setHorizontalSpacing(3);
	layout.setConstraint(pinFigure, BorderLayout.RIGHT);
	layout.setConstraint(collapseToggle, BorderLayout.CENTER);
	title.setLayoutManager(layout);
	title.setBorder(BORDER_TITLE_MARGIN);

	mainFigure.add(title);
	mainFigure.add(scrollpane);
	return mainFigure;
}

public Object getAdapter(Class key) {
	if (key == ExposeHelper.class) {
		ViewportExposeHelper helper = new ViewportExposeHelper(this);
		helper.setMinimumFrameCount(6);
		return helper;
	}
	return super.getAdapter(key);
}

public IFigure getContentPane() {
	return scrollpane.getView();
}

public boolean isExpanded() {
	return collapseToggle.isSelected();
}

/**
 * @see org.eclipse.gef.ui.palette.PaletteEditPart#createAccessible()
 */
protected AccessibleEditPart createAccessible() {
	return new AccessibleGraphicalEditPart(){
		public void getDescription(AccessibleEvent e) {
			e.result = getPaletteEntry().getDescription();
		}

		public void getName(AccessibleEvent e) {
			e.result = getPaletteEntry().getLabel();
		}

		public void getRole(AccessibleControlEvent e) {
			e.detail = ACC.ROLE_TREE;
		}

		public void getState(AccessibleControlEvent e) {
			e.detail = isExpanded() ? ACC.STATE_EXPANDED : ACC.STATE_COLLAPSED;
		}
	};
}

/**
 * Sets the minimum preferred size of certain figures, as
 * not doing so results in the figure's minimum size being
 * its preferred sizes, or the preferred size of the scrollbar's
 * which in-turn doesnt allow for compression.
 * Sometimes the scrollbar's minimum size if far more than
 * the compression desired, hence specific setting is required.
 */
public void refreshChildren() {
	super.refreshChildren();
//	Dimension minSize = collapseToggle.getPreferredSize();
//	minSize.height+=getFigure().getInsets().getHeight();
//	getFigure().setMinimumSize(new Dimension(0,minSize.height));
//	scrollpane.setMinimumSize(new Dimension(0,0));
}

/**
 * Updates the title label.
 */
protected void refreshTitle() {
	categoryLabel.setText(getPaletteEntry().getLabel());
	categoryLabel.setIcon(getPaletteEntry().getSmallIcon());
}

protected void refreshVisuals() {
	refreshVisuals (false);
}

/**
 * Refreshes the Figure for this part.  If the animate flag is true, then the category's
 * Figure will slide open or closed.
 */
protected void refreshVisuals(boolean animate) {
	refreshTitle();
	updateLayout();
	if (collapseToggle.isSelected()) {
		scrollpane.setVisible(true);
		scrollpane.setVerticalScrollBarVisibility(ScrollPane.NEVER);
		if (animate)
			 ((AnimatableFigure) getFigure()).expand();
		else
			 ((AnimatableFigure) getFigure()).setExpanded(true);
		scrollpane.setVerticalScrollBarVisibility(ScrollPane.AUTOMATIC);
	} else {
		scrollpane.setVerticalScrollBarVisibility(ScrollPane.NEVER);
		if (animate)
			((AnimatableFigure)getFigure()).collapse();
		else
			((AnimatableFigure)getFigure()).setExpanded(false);
		scrollpane.setVisible(false);
	}
}

public void setExpanded(boolean expanded) {
	if (expanded == collapseToggle.isSelected())
		return;
	collapseToggle.setSelected(expanded);	
}

public void setSelected(int value) {
	super.setSelected(value);
	if (value == SELECTED_PRIMARY)
		collapseToggle.requestFocus();
}

protected void updateLayout() {
	int layout = getPreferenceSource().getLayoutSetting();
	if (cachedLayout == layout) {
		return;
	}
	
	cachedLayout = layout;
	
	LayoutManager manager;
	if (layout == PaletteViewerPreferences.LAYOUT_FOLDER) {
		manager = new FolderLayout();
	} else if (layout == PaletteViewerPreferences.LAYOUT_ICONS) {
		manager = new FlowLayout();
	} else {
		ToolbarLayout toolbarLayout = new ToolbarLayout();
		toolbarLayout.setSpacing(3);
		manager = toolbarLayout;
	}
	scrollpane.getView().setLayoutManager(manager);
}

}