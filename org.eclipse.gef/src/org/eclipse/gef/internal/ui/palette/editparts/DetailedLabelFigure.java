/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.internal.ui.palette.editparts;

import java.util.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.Display;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.text.*;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.TextFlow;

import org.eclipse.gef.ui.palette.PaletteMessages;
import org.eclipse.gef.ui.palette.PaletteViewerPreferences;

/**
 * A customized figure used to represent entries in the GEF Palette.
 * 
 * @author Pratik Shah
 */
public class DetailedLabelFigure
	extends Figure
{

private static final FontCache FONTCACHE = new FontCache();
private static final Border PAGE_BORDER = new MarginBorder(0, 1, 0, 1);

private SelectableImageFigure image;
private FlowPage page;
private TextFlow nameText, descText;
private Font boldFont;
private boolean selectionState;
private int layoutMode = -1;
private Font cachedFont;

/**
 * Constructor
 */
public DetailedLabelFigure() {
	image = new SelectableImageFigure();
	image.setAlignment(PositionConstants.NORTH);
	page = new FocusableFlowPage();
	page.setBorder(PAGE_BORDER);

	nameText = new TextFlow();
	nameText.setLayoutManager(
		new ParagraphTextLayout(nameText, ParagraphTextLayout.WORD_WRAP_TRUNCATE));

	descText = new TextFlow();
	descText.setLayoutManager(
		new ParagraphTextLayout(descText, ParagraphTextLayout.WORD_WRAP_TRUNCATE));

	page.add(nameText);
	add(image);
	add(page);
	BorderLayout layout = new BorderLayout();
	layout.setHorizontalSpacing(2);
	layout.setVerticalSpacing(0);
	setLayoutManager(layout);
}

/**
 * @see java.lang.Object#finalize()
 */
protected void finalize() throws Throwable {
	if (boldFont != null) {
		FONTCACHE.checkIn(boldFont);
	}
}

/**
 * @see org.eclipse.draw2d.IFigure#handleFocusGained(FocusEvent)
 */
public void handleFocusGained(FocusEvent event) {
	super.handleFocusGained(event);
	updateColors();
}

/**
 * @see org.eclipse.draw2d.Figure#handleFocusLost(FocusEvent)
 */
public void handleFocusLost(FocusEvent event) {
	super.handleFocusLost(event);
	updateColors();
}

/**
 * @return whether this figure is selected or not
 */
public boolean isSelected() {
	return selectionState;
}

/**
 * @param	s	The description for this entry
 */
public void setDescription(String s) {
	String str = ""; //$NON-NLS-1$
	if (s != null && !s.trim().equals("")  && !s.trim().equals(nameText.getText().trim())) { //$NON-NLS-1$
		str = " " + PaletteMessages.NAME_DESCRIPTION_SEPARATOR + " " + s; //$NON-NLS-1$ //$NON-NLS-2$
	}
	if (descText.getText().equals(str)) {
		return;
	}
	descText.setText(str);
}

/**
 * Sets the icon for this figure
 * 
 * @param icon The new image
 */
public void setImage(Image icon) {
	image.setImage(icon);
}

/**
 * @param	layoutMode		the palette layout (any of the
 * 							PaletteViewerPreferences.LAYOUT_XXXX options)
 */
public void setLayoutMode(int layoutMode) {
	updateFont(layoutMode);
	
	if (layoutMode == this.layoutMode)
		return;
	
	this.layoutMode = layoutMode;

	add(page);
	if (page.getChildren().contains(descText))
		page.remove(descText);
	
	BorderLayout layout = (BorderLayout) getLayoutManager();
	if (layoutMode == PaletteViewerPreferences.LAYOUT_COLUMNS) {
		page.setHorizontalAligment(PositionConstants.CENTER);
		layout.setConstraint(image, BorderLayout.TOP);
		layout.setConstraint(page, BorderLayout.CENTER);
	} else if (layoutMode == PaletteViewerPreferences.LAYOUT_ICONS) {
		layout.setConstraint(image, BorderLayout.CENTER);
		remove(page);
	} else if (layoutMode == PaletteViewerPreferences.LAYOUT_LIST) {
		page.setHorizontalAligment(PositionConstants.LEFT);
		layout.setConstraint(image, BorderLayout.LEFT);
		layout.setConstraint(page, BorderLayout.CENTER);
	} else if (layoutMode == PaletteViewerPreferences.LAYOUT_DETAILS) {
		if (!descText.getText().equals("")) { //$NON-NLS-1$
			page.add(descText);
		}
		page.setHorizontalAligment(PositionConstants.LEFT);
		layout.setConstraint(image, BorderLayout.LEFT);
		layout.setConstraint(page, BorderLayout.CENTER);
	}
}

/**
 * @param	str		The new name for this entry
 */
public void setName(String str) {
	if (nameText.getText().equals(str)) {
		return;
	}
	nameText.setText(str);
}

/**
 * @param	state	<code>true</code> if this entry is to be set as selected
 */
public void setSelected(boolean state) {
	selectionState = state;
	updateColors();
}

private void updateColors() {
	if (isSelected()) {
		if (hasFocus()) {
			image.useShadedImage();
			setForegroundColor(ColorConstants.menuForegroundSelected);
			setBackgroundColor(ColorConstants.menuBackgroundSelected);
		} else {
			image.disposeShadedImage();
			setForegroundColor(null);
			setBackgroundColor(ColorConstants.button);
		}
	} else {
		image.disposeShadedImage();
		setForegroundColor(null);
		setBackgroundColor(null);
	}
}

/**
 * Creates an ImageData representing the given <code>Image</code> shaded with the given
 * <code>Color</code>.
 * 
 * @param fromImage	Image that has to be shaded
 * @param shade		The Color to be used for shading
 * @return A new ImageData that can be used to create an Image.
 */	
protected static ImageData createShadedImage(Image fromImage, Color shade) {
	org.eclipse.swt.graphics.Rectangle r = fromImage.getBounds();
	ImageData data = fromImage.getImageData();
	PaletteData palette = data.palette;
	if (!palette.isDirect) {
		/* Convert the palette entries */
		RGB [] rgbs = palette.getRGBs();
		for (int i = 0; i < rgbs.length; i++) {
			if (data.transparentPixel != i) {
				RGB color = rgbs [i];
				color.red = determineShading(color.red, shade.getRed());
				color.blue = determineShading(color.blue, shade.getBlue());
				color.green = determineShading(color.green, shade.getGreen());
			}
		}
		data.palette = new PaletteData(rgbs);
	} else {
		/* Convert the pixels. */
		int[] scanline = new int[r.width];
		int redMask = palette.redMask;
		int greenMask = palette.greenMask;
		int blueMask = palette.blueMask;
		int redShift = palette.redShift;
		int greenShift = palette.greenShift;
		int blueShift = palette.blueShift;
		for (int y = 0; y < r.height; y++) {
			data.getPixels(0, y, r.width, scanline, 0);
			for (int x = 0; x < r.width; x++) {
				int pixel = scanline[x];
				int red = pixel & redMask;
				red = (redShift < 0) ? red >>> -redShift : red << redShift;
				int green = pixel & greenMask;
				green = (greenShift < 0) ? green >>> -greenShift : green << greenShift;
				int blue = pixel & blueMask;
				blue = (blueShift < 0) ? blue >>> -blueShift : blue << blueShift;
				red = determineShading(red, shade.getRed());
				blue = determineShading(blue, shade.getBlue());
				green = determineShading(green, shade.getGreen());
				red = (redShift < 0) ? red << -redShift : red >> redShift;
				red &= redMask;
				green = (greenShift < 0) ? green << -greenShift : green >> greenShift;
				green &= greenMask;
				blue = (blueShift < 0) ? blue << -blueShift : blue >> blueShift;
				blue &= blueMask;
				scanline[x] = red | blue | green;
			}
			data.setPixels(0, y, r.width, scanline, 0);
		}
	}
	return data;
}

private void updateFont(int layout) {
	boolean layoutChanged = (layoutMode != layout);
	boolean fontChanged = (cachedFont == null || cachedFont != getFont());

	cachedFont = getFont();
	if (layoutChanged || fontChanged) {
		if (boldFont != null) {
			FONTCACHE.checkIn(boldFont);
			boldFont = null;
		}
		if (layout == PaletteViewerPreferences.LAYOUT_DETAILS) {
			boldFont = FONTCACHE.checkOut(cachedFont);
		}
		nameText.setFont(boldFont);
	}
}

private static int determineShading(int origColor, int shadeColor) {
	return (origColor + shadeColor) / 2;
}

private class FocusableFlowPage extends FlowPage {
	protected void paintFigure(Graphics g) {
		if (isSelected()) {
			Rectangle childBounds = null;
			List children = getChildren();
			for (int i = 0; i < children.size(); i++) {
				Figure child = (Figure) children.get(i);
				if (i == 0) {
					childBounds = child.getBounds().getCopy();
				} else {
					childBounds.union(child.getBounds());
				}
			}
			childBounds.expand(new Insets(2, 2, 0, 0));
			translateToParent(childBounds);
			childBounds.intersect(getBounds());
			g.fillRectangle(childBounds);
//			super.paintFigure(g);
			if (DetailedLabelFigure.this.hasFocus()) {
				g.setXORMode(true);
				g.setForegroundColor(ColorConstants.menuBackgroundSelected);
				g.setBackgroundColor(ColorConstants.white);
				g.drawFocus(childBounds.resize(-1, -1));
			}
		} else {
			super.paintFigure(g);
		}
	}
}

private class SelectableImageFigure extends ImageFigure {
	private Image shadedImage;
	public void useShadedImage() {
		disposeShadedImage();
		ImageData data = createShadedImage(super.getImage(), 
				ColorConstants.menuBackgroundSelected);
		shadedImage = new Image(null, data, data.getTransparencyMask());
	}
	private void disposeShadedImage() {
		if (shadedImage != null) {
			shadedImage.dispose();
			shadedImage = null;
		}
	}
	protected void finalize() throws Throwable {
		disposeShadedImage();
	}
	public Image getImage() {
		if (shadedImage != null)
			return shadedImage;
		return super.getImage();
	}
	public void setImage(Image image) {
		if (image == super.getImage())
			return;
		boolean wasShaded = shadedImage != null;
		disposeShadedImage();
		super.setImage(image);
		if (wasShaded)
			useShadedImage();
	}
}

private static class FontCache {
	private Hashtable table = new Hashtable();
	private class FontInfo {
		private Font boldFont;
		private int refCount;
	}
	
	/*
	 * Client can only check in fonts that they checked out from this cache, and should
	 * do only one check-in per checkout. If the given font is not found, a null pointer
	 * exception will be encountered.
	 */
	public void checkIn(Font boldFont) {
		FontInfo info = null;
		Map.Entry entry = null;
		Collection values = table.entrySet();
		for (Iterator iter = values.iterator(); iter.hasNext();) {
			Map.Entry tempEntry = (Map.Entry) iter.next();
			FontInfo tempInfo = (FontInfo)tempEntry.getValue();
			if (tempInfo.boldFont == boldFont) {
				info = tempInfo;
				entry = tempEntry;
				break;
			}
		}
		info.refCount--;
		if (info.refCount == 0) {
			boldFont.dispose();
			table.remove(entry.getKey());
		}
	}
	
	public Font checkOut(Font font) {
		FontInfo info = null;
		FontData key = font.getFontData()[0];
		Object obj = table.get(key);
		if (obj != null) {
			info = (FontInfo)obj;
		} else {
			info = new FontInfo();
			FontData[] boldDatas = font.getFontData();
			for (int i = 0; i < boldDatas.length; i++) {
				boldDatas[i].setStyle(SWT.BOLD);
			}
			info.boldFont = new Font(Display.getCurrent(), boldDatas);
			table.put(key, info);
		}
		info.refCount++;
		return info.boldFont;
	}
}

}