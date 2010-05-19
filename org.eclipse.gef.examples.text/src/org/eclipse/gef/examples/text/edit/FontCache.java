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

import java.util.Hashtable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

/**
 * @author Pratik Shah
 * @since 3.1
 */
public class FontCache {

	private static Hashtable table = new Hashtable();

	private FontCache() {
	}

	/**
	 * Clients should only check in fonts that they checked out from this cache,
	 * and should do only one check-in per checkout. If the given font is not
	 * found, a null pointer exception will be encountered.
	 */
	public static void checkIn(Font font) {
		FontData fd = font.getFontData()[0];
		Key key = new Key(fd.getName(), fd.getHeight(),
				(fd.getStyle() & SWT.BOLD) != 0,
				(fd.getStyle() & SWT.ITALIC) != 0);
		Value value = (Value) table.get(key);
		value.refCount--;
		if (value.refCount == 0) {
			table.remove(key);
			value.font.dispose();
		}
	}

	public static Font checkOut(String fontName, int fontHeight,
			boolean isBold, boolean isItalic) {
		Key key = new Key(fontName, fontHeight, isBold, isItalic);
		Value value = (Value) table.get(key);
		if (value == null) {
			value = new Value();
			int style = (isBold ? SWT.BOLD : SWT.NONE)
					| (isItalic ? SWT.ITALIC : SWT.NONE);
			value.font = new Font(null, new FontData(fontName, fontHeight,
					style));
			table.put(key, value);
		}
		value.refCount++;
		return value.font;
	}

	private static class Key {
		private String fontName;
		private int height;
		private boolean isBold, isItalic;

		private Key(String fontName, int height, boolean isBold,
				boolean isItalic) {
			this.fontName = fontName;
			this.height = height;
			this.isBold = isBold;
			this.isItalic = isItalic;
		}

		public boolean equals(Object obj) {
			boolean result = obj == this;
			if (!result && (obj instanceof Key)) {
				Key key = (Key) obj;
				result = fontName.equalsIgnoreCase(key.fontName)
						&& height == key.height && isBold == key.isBold
						&& isItalic == key.isItalic;
			}
			return result;
		}

		public int hashCode() {
			return fontName.toLowerCase().hashCode() + height;
		}
	}

	private static class Value {
		private Font font;
		private int refCount;
	}

}
