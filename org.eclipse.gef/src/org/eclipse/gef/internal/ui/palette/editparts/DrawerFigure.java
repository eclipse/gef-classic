package org.eclipse.gef.internal.ui.palette.editparts;


import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;

import org.eclipse.jface.resource.ImageDescriptor;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.internal.Internal;
import org.eclipse.gef.ui.palette.*;

/**
 * @author Pratik Shah
 */
public class DrawerFigure
	extends Figure 
{

protected static final Border TOGGLE_BUTTON_BORDER = new RaisedBorder();
protected static final Border TITLE_MARGIN_BORDER = new MarginBorder(1, 0, 1, 0);
protected static final Border SCROLL_PANE_BORDER = new MarginBorder(3);
protected static final Border TOOLTIP_BORDER = new DrawerToolTipBorder();
protected static final Border BUTTON_BORDER = new ButtonBorder(
					ButtonBorder.SCHEMES.TOOLBAR);

//@TODO:Pratik
// This image needs to go in GEFSharedImages
protected static final Image PIN = new Image(null, ImageDescriptor.createFromFile(
		Internal.class, "icons/pin_view.gif").getImageData()); //$NON-NLS-1$

protected static final Color FG_COLOR = FigureUtilities.mixColors(
		ColorConstants.buttonDarker, ColorConstants.button);

private int layoutMode = -1;
private Label drawerLabel, tipLabel;
private ScrollPane scrollpane;
private ToggleButton pinFigure;
private Toggle collapseToggle;
private boolean isAnimating, showPin, animatingAlone, skipNextEvent;
private DrawerAnimationController controller;
private EditPartTipHelper tipHelper;

/**
 * Constructor
 * 
 * @param	control		The Control of the LWS to which this Figure belongs (it is used to
 * 						display the drawer header with an EditPartTipHelper, if the
 * 						header is not completely visible).  It can be <code>null</code>
 * 						(the tip won't be displayed).
 */
public DrawerFigure(final Control control) {
	setLayoutManager(new PaletteToolbarLayout());

	Figure title = new Figure();	
	title.setBorder(TITLE_MARGIN_BORDER);
	BorderLayout borderLayout = new BorderLayout();
	borderLayout.setHorizontalSpacing(2);
	title.setLayoutManager(borderLayout);
	
	drawerLabel = new Label();
	drawerLabel.setLabelAlignment(Label.LEFT);

	pinFigure = new ToggleButton(new ImageFigure(PIN));
	pinFigure.setBorder(BUTTON_BORDER);
	pinFigure.setRolloverEnabled(true);
	pinFigure.setRequestFocusEnabled(false);
	pinFigure.setToolTip(new Label(PaletteMessages.TOOLTIP_PIN_FIGURE));

	title.add(pinFigure, BorderLayout.RIGHT);
	title.add(drawerLabel, BorderLayout.CENTER);
	
	collapseToggle = new Toggle(title) {
		protected void paintFigure(Graphics g) {
			super.paintFigure(g);
			Rectangle r = Rectangle.SINGLETON;
			r.setBounds(getBounds());
			r.width = Math.min(50, r.width);
			g.setForegroundColor(FG_COLOR);
			g.fillGradient(Rectangle.SINGLETON, false);
		}

	};
	collapseToggle.setSelected(true);
	collapseToggle.setBorder(TOGGLE_BUTTON_BORDER);
	collapseToggle.setRequestFocusEnabled(true);
	collapseToggle.addChangeListener(new ChangeListener() {
		public void handleStateChanged(ChangeEvent e) {
			if (e.getPropertyName().equals(ButtonModel.SELECTED_PROPERTY)) {
				updatePin();
			}
		}
	});

	add(collapseToggle);
	createScrollpane();
	add(scrollpane);
	createHoverHelp(control);
}

private void createHoverHelp(final Control control) {
	if (control == null) {
		return;
	}
	// If a control was provided, create the tipLabel -- if the text in the header is
	// truncated, it will display it as a tooltip.
	tipLabel =	new Label() {
		protected void paintFigure(Graphics graphics) {
			Rectangle r = Rectangle.SINGLETON;
			r.setBounds(getBounds());
			r.width = Math.min(50, r.width);
			graphics.pushState();
			graphics.setForegroundColor(FG_COLOR);
			graphics.fillGradient(Rectangle.SINGLETON, false);
			graphics.popState();
			super.paintFigure(graphics);
		}
	};
	tipLabel.setOpaque(false);
	tipLabel.setBorder(TOOLTIP_BORDER);
	collapseToggle.addMouseMotionListener(new MouseMotionListener.Stub() {
		public void mouseEntered(MouseEvent e) {
			if (skipNextEvent) {
				skipNextEvent = false;
				return;
			}
			if (drawerLabel.isTextTruncated()) {
				tipLabel.setText(drawerLabel.getText());
				tipLabel.setIcon(drawerLabel.getIcon());
				tipLabel.setFont(drawerLabel.getFont());
				tipHelper = new EditPartTipHelper(control);
 				Point labelLoc = drawerLabel.getLocation();
				org.eclipse.swt.graphics.Point absolute = control.toDisplay(
						new org.eclipse.swt.graphics.Point(labelLoc.x, labelLoc.y));
				tipHelper.displayToolTipAt(tipLabel, absolute.x - 4, absolute.y - 4);
			}
		}
	});
	tipLabel.addMouseListener(new MouseListener.Stub() {
		public void mousePressed(MouseEvent e) {
			if (e.button == 1) {
				Rectangle original = getCollapseToggle().getBounds().getCopy();
				getCollapseToggle().requestFocus();
				setExpanded(!isExpanded());
				// Hide the tip if expanding the drawer causes the collapse toggle to move
				if (!original.equals(getCollapseToggle().getBounds())) {
					tipHelper.hide();
				}
			} else {		
				tipHelper.hide();
				if (e.button == 3) {
					skipNextEvent = true;
				}
			}
		}
	}); 			
}

private void createScrollpane() {
	scrollpane = new ScrollPane();
	scrollpane.getViewport().setContentsTracksWidth(true);
	scrollpane.setMinimumSize(new Dimension(0, 0));
	scrollpane.setHorizontalScrollBarVisibility(ScrollPane.NEVER);
	scrollpane.setVerticalScrollBar(new PaletteScrollBar());
	scrollpane.getVerticalScrollBar().setStepIncrement(33);
	scrollpane.setLayoutManager(new OverlayScrollPaneLayout());
	scrollpane.setView(new Figure());
	scrollpane.getView().setOpaque(true);
	scrollpane.getView().setBorder(SCROLL_PANE_BORDER);
}

/**
 * @return	The content pane for this figure, i.e. the Figure to which children can be
 * added.
 */
public IFigure getContentPane() {
	return scrollpane.getView();
}

/**
 * @return The Clickable that is used to expand/collapse the drawer.
 */
public Clickable getCollapseToggle() {
	return collapseToggle;
}

/**
 * @see org.eclipse.draw2d.Figure#getMinimumSize(int, int)
 */
public Dimension getMinimumSize(int wHint, int hHint) {
	if (animatingAlone)
		return getPreferredSize(wHint, hHint);
	return super.getMinimumSize(wHint, hHint);
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
	Dimension min = super.getMinimumSize(w, h);
	float scale = controller.getAnimationProgress();
	if (!isExpanded()) {
		scale = 1.0f - scale;
	}
	return pref.getScaled(scale).expand(min.getScaled(1.0f - scale));
}

/**
 * @return <code>true</code> if the drawer is expanded
 */
public boolean isExpanded() {
	return collapseToggle.isSelected();
}

/**
 * @return <code>true</code> if the drawer is expanded and is pinned (i.e., it can't be
 * automatically collapsed)
 */
public boolean isPinnedOpen() {
	return isExpanded() && pinFigure.isVisible() && pinFigure.isSelected();
}

public boolean isPinShowing() {
	return isExpanded() && showPin;
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

public void setController(DrawerAnimationController controller) {
	this.controller = controller;
}

public void setExpanded(boolean value) {
	collapseToggle.setSelected(value);
}

public void setLayoutMode(int layoutMode) {
	if (this.layoutMode == layoutMode) {
		return;
	}
	
	this.layoutMode = layoutMode;
	
	LayoutManager manager;
	if (layoutMode == PaletteViewerPreferences.LAYOUT_FOLDER) {
		manager = new FolderLayout();
	} else if (layoutMode == PaletteViewerPreferences.LAYOUT_ICONS) {
		FlowLayout fl = new FlowLayout();
		fl.setMinorSpacing(0);
		fl.setMajorSpacing(0);
		manager = fl;
	} else {
		manager = new ToolbarLayout();
	}
	getContentPane().setLayoutManager(manager);
}

/**
 * Pins or unpins the drawer.  The drawer can be pinned open only when it is expanded.
 * Attempts to pin it when it is collapsed will do nothing.
 * 
 * @param	pinned	<code>true</code> if the drawer is to be pinned
 */
public void setPinned(boolean pinned) {
	if (!isExpanded() || !showPin) {
		return;
	}
	
	pinFigure.setSelected(pinned);
}

void setAnimatingAlone(boolean newValue) {
	animatingAlone = newValue;
}

/**
 * Displays the given text in the drawer's header as its title.
 * 
 * @param s The title of the drawer
 */
public void setTitle(String s) {
	drawerLabel.setText(s);
}

/**
 * Displays the given image in the header as the drawer's icon.
 * 
 * @param icon		The icon for this drawer.
 */
public void setTitleIcon(Image icon) {
	drawerLabel.setIcon(icon);
}

public void setToolTipText(String text) {
	// The tool tip will only show up on the collapse toggle...
	if (collapseToggle.getToolTip() == null) {
		collapseToggle.setToolTip(new Label());
	}
	((Label)collapseToggle.getToolTip()).setText(text);
	
	// and the pop-up for the collapse toggle, if there is one
	if (tipLabel == null) {
		return;
	}
	if (tipLabel.getToolTip() == null) {
		tipLabel.setToolTip(new Label());
	}
	((Label)tipLabel.getToolTip()).setText(text);
}

public void showPin(boolean show) {
	showPin = show;
	updatePin();
}

protected void updatePin() {
	if (pinFigure == null) {
		return;
	}
	if (isExpanded() && showPin) {
		pinFigure.setVisible(true);
	} else {
		pinFigure.setVisible(false);
	}
}

}
