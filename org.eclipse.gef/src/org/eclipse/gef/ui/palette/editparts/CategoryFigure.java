package org.eclipse.gef.ui.palette.editparts;


import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import org.eclipse.jface.resource.ImageDescriptor;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.internal.Internal;
import org.eclipse.gef.ui.palette.*;
import org.eclipse.gef.ui.palette.editparts.*;

/**
 * @author Pratik Shah
 */
public class CategoryFigure 
	extends Figure 
{

protected static SeparatorBorder SCROLL_PANE_BORDER = new SeparatorBorder();
protected static final Border MARGIN_BORDER = new MarginBorder(2, 2, 2, 2);
protected static final Border BORDER_TITLE_MARGIN = new CompoundBorder(
                                                           new PaletteContainerBorder(),
                                                      	   MARGIN_BORDER);
//@TODO:Pratik
// Is this the best way to do this?  How would I dispose this image?
protected static final Image pin = new Image(null, ImageDescriptor.createFromFile(
		Internal.class, "icons/pin_view.gif").getImageData()); //$NON-NLS-1$
protected static final Image disabledPin = new Image(null, pin, SWT.IMAGE_DISABLE);

static {
	SCROLL_PANE_BORDER.setLocation(SeparatorBorder.BOTTOM);
}

private final Label categoryLabel;
private final ScrollPane scrollpane;
private final ToggleButton pinFigure;
private final Toggle collapseToggle;
private boolean isAnimating;
private CategoryAnimationController controller;

public CategoryFigure(final Control control) {
	super();
	setLayoutManager(new ToolbarLayout());
	
	// The header is toggle button with the label
	Figure header = new Figure();
	header.setForegroundColor(ColorConstants.black);
	ToolbarLayout layout = new ToolbarLayout(ToolbarLayout.HORIZONTAL);
	layout.setSpacing(2);
	layout.setMinorAlignment(ToolbarLayout.ALIGN_CENTER);
	header.setLayoutManager(layout);
	categoryLabel = new Label();
	categoryLabel.setLabelAlignment(Label.LEFT);
	header.add(categoryLabel);
	collapseToggle = new Toggle(header);
	collapseToggle.setOpaque(true);
	collapseToggle.setSelected(true);	
//	collapseToggle.addActionListener(new ActionListener() {
//		public void actionPerformed(ActionEvent event) {
//			revalidate();
//		}
//	});
	collapseToggle.addChangeListener(new ChangeListener() {
		public void handleStateChanged(ChangeEvent event) {
			if (pinFigure == null) {
				return;
			}
			if (isExpanded()) {
				pinFigure.setEnabled(true);
			} else {
				pinFigure.setSelected(false);
				pinFigure.setEnabled(false);
			}
		}
	});

	// Title is the header + the pin
	Figure title = new Figure();
	title.add(collapseToggle);
	pinFigure = new ImageButton(pin, disabledPin);
	pinFigure.setBorder(new ButtonBorder(ButtonBorder.SCHEMES.TOOLBAR));
	pinFigure.setRolloverEnabled(true);
	pinFigure.setRequestFocusEnabled(false);
	title.add(pinFigure);
	BorderLayout borderLayout = new BorderLayout();
	borderLayout.setHorizontalSpacing(3);
	borderLayout.setConstraint(pinFigure, BorderLayout.RIGHT);
	borderLayout.setConstraint(collapseToggle, BorderLayout.CENTER);
	title.setLayoutManager(borderLayout);
	title.setBorder(BORDER_TITLE_MARGIN);

	// Create the scrollpane that is going to contain all the children
	scrollpane = new ScrollPane();
	scrollpane.getViewport().setContentsTracksWidth(true);
	scrollpane.setMinimumSize(new Dimension(0, 0));
	scrollpane.setHorizontalScrollBarVisibility(ScrollPane.NEVER);
	scrollpane.setVerticalScrollBar(new PaletteScrollBar());
	scrollpane.getVerticalScrollBar().setStepIncrement(22);
	scrollpane.setLayoutManager(new OverlayScrollPaneLayout());
	scrollpane.setBorder(SCROLL_PANE_BORDER);
	scrollpane.setBackgroundColor(ColorConstants.listBackground);
	scrollpane.setView(new Figure());
	scrollpane.getView().setBorder(MARGIN_BORDER);

	add(title);
	add(scrollpane);

	if (control == null) {
		return;
	}
	
	// If a control was provided, create the tipLabel -- if the text in the header is
	// truncated, it will display it as a tooltip.
	final Label tipLabel =	new Label();
	tipLabel.setOpaque(true);
	tipLabel.setBackgroundColor(ColorConstants.tooltipBackground);
	tipLabel.setForegroundColor(ColorConstants.tooltipForeground);	
	tipLabel.setBorder(new CategoryToolTipBorder());
	collapseToggle.addMouseMotionListener(new MouseMotionListener.Stub(){
		public void mouseMoved(MouseEvent e) {
			Rectangle labelBounds = categoryLabel.getBounds();
			if (categoryLabel.isTextTruncated() && labelBounds.contains(e.x, e.y)) {
				tipLabel.setText(categoryLabel.getText());
				tipLabel.setIcon(categoryLabel.getIcon());
				tipLabel.setFont(categoryLabel.getFont());
				EditPartTipHelper tipHelper = new EditPartTipHelper(control);
				Point labelLoc = categoryLabel.getLocation();
				org.eclipse.swt.graphics.Point absolute = control.toDisplay(
						new org.eclipse.swt.graphics.Point(labelLoc.x, labelLoc.y));
				
				/*
				 * @TODO:Pratik
				 * Move the stuff about re-positioning the tooltip so that it is
				 * completely visible on the screen in the tipHelper.
				 */
				int shiftX = 0;
				int shiftY = 0;
				Display display = Display.getCurrent();
				org.eclipse.swt.graphics.Rectangle area = display.getClientArea();
				org.eclipse.swt.graphics.Point end = 
						new org.eclipse.swt.graphics.Point(
										absolute.x + tipLabel.getPreferredSize().width,
										absolute.y + tipLabel.getPreferredSize().height);
				if (!area.contains(end)) {
					shiftX = end.x - (area.x + area.width);
					shiftY = end.y - (area.y + area.height);
					shiftX = shiftX < 0 ? 0 : shiftX;
					shiftY = shiftY < 0 ? 0 : shiftY;
				}
				tipHelper.displayToolTipAt(tipLabel, absolute.x - shiftX - 4, 
				                                     absolute.y - shiftY - 4);
			}
		}
	});
	tipLabel.addMouseListener(new MouseListener.Stub(){
		public void mousePressed(MouseEvent e) {
			setExpanded(!isExpanded());
		}
	}); 			
}

public IFigure getContentPane() {
	return scrollpane.getView();
}

public Toggle getCollapseToggle() {
	return collapseToggle;
}

/**
 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
 */
public Dimension getPreferredSize(int w, int h) {
	if (!isAnimating) {
		if (isExpanded())
			return super.getPreferredSize(w, h);
		else
			return getMinimumSize();
	}
	Dimension pref = super.getPreferredSize(w, h);
	Dimension min  = getMinimumSize();
	float scale = controller.getAnimationProgress();
	if (!isExpanded()) {
		scale = 1.0f - scale;
	}
	return pref.getScaled(scale).expand(min.getScaled(1.0f - scale));

}

public boolean isExpanded() {
	return collapseToggle.isSelected();
}

public boolean isPinnedOpen() {
	return isExpanded() && pinFigure.isSelected();
}

public void setAnimating(boolean value) {
	if (isAnimating == value)
		return;
	isAnimating = value;
	if (isAnimating)
		scrollpane.setVerticalScrollBarVisibility(ScrollPane.NEVER);
	else
		scrollpane.setVerticalScrollBarVisibility(ScrollPane.AUTOMATIC);
}

public void setController(CategoryAnimationController controller) {
	this.controller = controller;
}

public void setExpanded(boolean value) {
	collapseToggle.setSelected(value);
}

public void setLayoutMode(int layoutMode) {
	LayoutManager manager;
	if (layoutMode == PaletteViewerPreferences.LAYOUT_FOLDER) {
		manager = new FolderLayout();
	} else if (layoutMode == PaletteViewerPreferences.LAYOUT_ICONS) {
		manager = new FlowLayout();
	} else {
		ToolbarLayout toolbarLayout = new ToolbarLayout();
		toolbarLayout.setSpacing(3);
		manager = toolbarLayout;
	}
	getContentPane().setLayoutManager(manager);
}

public void setPinned(boolean pinned) {
	if (!isExpanded()) {
		return;
	}
	
	pinFigure.setSelected(pinned);
}

public void setTitle(String s) {
	categoryLabel.setText(s);
}

public void setTitleIcon(Image icon) {
	categoryLabel.setIcon(icon);
}

private class ImageButton extends ToggleButton {
	private ImageFigure image;
	private Image enabled, disabled;
	public ImageButton(Image enabled, Image disabled) {
		super();
		setContents(image = new ImageFigure());
		this.enabled = enabled;
		this.disabled = disabled;
		setEnabled(isEnabled());
	}
	public void setEnabled(boolean value) {
		image.setImage(value ? enabled : disabled);
		super.setEnabled(value);
	}
}

}
