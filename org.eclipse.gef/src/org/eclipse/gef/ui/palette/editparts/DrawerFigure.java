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

/**
 * @author Pratik Shah
 */
public class DrawerFigure
	extends Figure 
{

static final Border MARGIN_BORDER = new MarginBorder(1,1,1,1);
static final Border SCROLL_PANE_BORDER = new SeparatorBorder(0,0,1,0);
static final Border TOGGLE_BUTTON_BORDER = new RaisedBorder();
static final Border TITLE_MARGIN_BORDER = new MarginBorder(1,0,1,0);

//@TODO:Pratik
// Is this the best way to do this?  How would I dispose this image?
protected static final Image pin = new Image(null, ImageDescriptor.createFromFile(
		Internal.class, "icons/pin_view.gif").getImageData()); //$NON-NLS-1$
protected static final Image disabledPin = new Image(null, pin, SWT.IMAGE_DISABLE);

private Label categoryLabel;
private ScrollPane scrollpane;
private ToggleButton pinFigure;
private Toggle collapseToggle;
private boolean isAnimating;
private DrawerAnimationController controller;

public DrawerFigure(final Control control) {
	setLayoutManager(new ToolbarLayout());

	Figure title = new Figure() {
			protected void paintFigure(Graphics g) {
				super.paintFigure(g);
				Rectangle r = Rectangle.SINGLETON;
				r.setBounds(getBounds());
				r.width = Math.min(50, r.width);
				g.setForegroundColor(
					FigureUtilities.mixColors(ColorConstants.buttonDarker,ColorConstants.button));
	//			g.setBackgroundColor(
	//				FigureUtilities.mixColors(ColorConstants.buttonDarker,ColorConstants.button));
//				g.fillGradient(Rectangle.SINGLETON, false);
			}
	};
	
	title.setBorder(TITLE_MARGIN_BORDER);
	BorderLayout borderLayout = new BorderLayout();
	borderLayout.setHorizontalSpacing(2);
	title.setLayoutManager(borderLayout);
	
	categoryLabel = new Label();
	categoryLabel.setLabelAlignment(Label.LEFT);

	pinFigure = new ImageButton(pin, disabledPin);
	pinFigure.setBorder(new ButtonBorder(ButtonBorder.SCHEMES.TOOLBAR));
	pinFigure.setRolloverEnabled(true);
	pinFigure.setRequestFocusEnabled(false);

	title.add(pinFigure, BorderLayout.RIGHT);
	title.add(categoryLabel, BorderLayout.CENTER);
	
	collapseToggle = new Toggle(title) {
		protected void paintFigure(Graphics g) {
			super.paintFigure(g);
			Rectangle r = Rectangle.SINGLETON;
			r.setBounds(getBounds());
			r.width = Math.min(50, r.width);
			g.setForegroundColor(
				FigureUtilities.mixColors(ColorConstants.buttonDarker,ColorConstants.button));
//			g.setBackgroundColor(
//				FigureUtilities.mixColors(ColorConstants.buttonDarker,ColorConstants.button));
			g.fillGradient(Rectangle.SINGLETON, false);
		}

	};
	collapseToggle.setSelected(true);
	collapseToggle.setBorder(TOGGLE_BUTTON_BORDER);

	collapseToggle.addChangeListener(new ChangeListener() {
		public void handleStateChanged(ChangeEvent event) {
			if (pinFigure == null)
				return;
			if (isExpanded())
				pinFigure.setEnabled(true);
			else {
				pinFigure.setSelected(false);
				pinFigure.setEnabled(false);
			}
		}
	});

	add(collapseToggle);
	createScrollpane();
	add(scrollpane);
	createHoverHelp(control);
}

private void createHoverHelp(final Control control) {
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
				 * completely visible on the screen to the tipHelper.
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

private void createScrollpane() {
	scrollpane = new ScrollPane();
	scrollpane.getViewport().setContentsTracksWidth(true);
	scrollpane.setMinimumSize(new Dimension(0, 0));
	scrollpane.setHorizontalScrollBarVisibility(ScrollPane.NEVER);
	scrollpane.setVerticalScrollBar(new PaletteScrollBar());
	scrollpane.getVerticalScrollBar().setStepIncrement(22);
	scrollpane.setLayoutManager(new OverlayScrollPaneLayout());
//	scrollpane.setBorder(SCROLL_PANE_BORDER);
	scrollpane.setBorder(new MarginBorder(3));
	scrollpane.setBackgroundColor(ColorConstants.button);
	scrollpane.setView(new Figure());
	scrollpane.getView().setBorder(MARGIN_BORDER);
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

public void setController(DrawerAnimationController controller) {
	this.controller = controller;
}

public void setExpanded(boolean value) {
	collapseToggle.setSelected(value);
}

public void setLayoutMode(int layoutMode) {
	LayoutManager manager;
	if (layoutMode == PaletteViewerPreferences.LAYOUT_FOLDER)
		manager = new FolderLayout();
	else if (layoutMode == PaletteViewerPreferences.LAYOUT_ICONS) {
		FlowLayout fl = new FlowLayout();
		fl.setMinorSpacing(0);
		fl.setMajorSpacing(0);
		manager = fl;
	} else
		manager = new ToolbarLayout();
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
