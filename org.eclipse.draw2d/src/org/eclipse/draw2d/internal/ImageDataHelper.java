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
package org.eclipse.draw2d.internal;

import org.eclipse.swt.graphics.*;

/**
 * This class provides some static helper methods formanipulating the SWT ImageData in 
 * special ways that are not provided by the image data itself.
 */
public class ImageDataHelper {

/**
 * Answer the nearest color index to the RGB color passed in from
 * the color array.
 */
public static int getNearestColorIndex(int red, int green, int blue, RGB[] colors) {
	// I really don't know how to determine nearest color. So I choose the algorithm
	// of treating each color as a vector in a 3D coordinate system where red, green, and blue
	// are each an axis in that system. Then the nearest color is the shortest vector between
	// the input color and each color in the RGB table.
	//
	// The difference vector will result from the difference between each RGB.
	// The length of this vector will be the Square root of r*r + g*g + b*b.
	// The length will not be directly computed, the square of the length will be, and
	// this is the value that min will be found for. We don't need to take the square root
	// because if  |a| < |b| then a*a < b*b.

	int minLength = Integer.MAX_VALUE;
	int minIndex = 0;
	for (int i = 0; i < colors.length; i++) {
		RGB rgb = colors[i];
		int rgbRed = rgb.red;
		int rgbGreen = rgb.green;
		int rgbBlue = rgb.blue;
		if (rgbRed == red && rgbGreen == green && rgbBlue == blue)
			return i; // Go no further, we have an exact match

		int redVector = rgbRed - red;
		int greenVector = rgbGreen - green;
		int blueVector = rgbBlue - blue;
		int lengthSquared = redVector * redVector + greenVector * greenVector + blueVector * blueVector;
		if (lengthSquared < minLength) {
			minLength = lengthSquared;
			minIndex = i;
		}
	}
	return minIndex;
}
/**
 * getPixels: getPixels into int array. This should be done in ImageData, but they
 * don't have that yet.
 */
public static void getPixels(ImageData imageData, int x, int y, int width, int [] pixelRow) {
	byte[] data = imageData.data;
	int depth = imageData.depth;
	int bytesPerLine = imageData.bytesPerLine;
	if (depth == 1) {
		int index = (y * bytesPerLine) + (x >> 3);
		int theByte = data[index] & 0xFF;
		int mask = 1 << (7 - (x & 0x7));
		int i=0;
		for (int n=width; n > 1; n--, i++) {
			if ((theByte & mask) == 0) {
				pixelRow[i] = 0;
			} else {
				pixelRow[i] = 1;
			}
			if (mask == 1) {
				mask = 0x80;
				index++;
				theByte = data[index];
			} else {
				mask = mask >> 1;
			}
		}
		if ((theByte & mask) == 0) {
			pixelRow[i] = 0;
		} else {
			pixelRow[i] = 1;
		}
		return;
	}
	if (depth == 4) {
		int index = (y * bytesPerLine) + (x >> 1);
		int n = width;
		int i = 0;
		if ((x & 0x1) == 1) {
			int theByte = data[index++] & 0xFF;
			pixelRow[i++] = theByte & 0x0F;
			n--;
		}
		while(n>1) {
			int theByte = data[index++] & 0xFF;
			pixelRow[i++] = theByte >> 4;
			pixelRow[i++] = theByte & 0x0F;
			n -= 2;
		}
		if (n > 0) {
			int theByte = data[index] & 0xFF;
			pixelRow[i] = theByte >> 4;
		}
		return;
	}
	if (depth == 8) {
		int index = (y * bytesPerLine) + x ;
		for (int i=0; i < width; i++) {
			pixelRow[i] = data[index++] & 0xFF;
		}
		return;
	}
	if (depth == 16) {
		int index = (y * bytesPerLine) + (x * 2);
		for (int i=0; i<width; i++) {
			pixelRow[i] = ((data[index] & 0xFF) << 8) | (data[index+1] & 0xFF);
			index += 2;
		}
		return;
	}
	if (depth == 24) {
		int index = (y * bytesPerLine) + (x * 3);
		for (int i=0; i<width; i++) {
			pixelRow[i] = ((data[index] & 0xFF) << 16) | ((data[index+1] & 0xFF) << 8)
				| (data[index+2] & 0xFF);
			index += 3;
		}
		return;
	}
	if (depth == 32) {
		int index = (y * bytesPerLine) + (x * 4);
		for (int i=0; i<width; i++) {
			pixelRow[i] = ((data[index] & 0xFF) << 24) | ((data[index+1] & 0xFF) << 16)
				| ((data[index+2] & 0xFF) << 8) | (data[index + 3] & 0xFF);
			index += 4;
		}
		return;
	}
}	
/**
 * mixAlphaWithinRegion: Mix in the alpha value using the given background color within the
 * region specified. Answer the new mixed in ImageData
 */
public static ImageData mixAlphaWithinRegion(ImageData fromImageData, Region region, double alpha, RGB backColor) {
	// We are using the alpha formula that Java uses:
	// The formula is applied to each color component individually (Red, Green, Blue).
	//   Cn = Co*a + Cb*(1-a)
	//     Cn = New Color component
	//     Co = Old Color component (from the image data)
	//     a = The alpha value
	//     Cb = Background Color component
	//
	// The alpha is in float, but we will be using integer arithmetic, so appropriate conversions will be made.
	// We will need to find the appropriate fraction. We'll do that by turning it into a percentage. We will
	// use the integer arithmetic on the Co*a side. We'll use the float for figuring out the Cb*(1-a) side since
	// that side is constant.
	double cba = 1 - alpha; //	The Cb alpha multiplier
	int redCba = (int) (backColor.red * cba + .5); // Symetric round
	int greenCba = (int) (backColor.green * cba + .5);
	int blueCba = (int) (backColor.blue * cba + .5);
	int aNum = (int) (alpha * 100. + .5); // Figure out the alpha numerator, the denominator will be 100.

	// Now go through the data, converting as necessary
	PaletteData palette = fromImageData.palette;
	boolean isDirect = palette.isDirect;
	RGB[] colors = palette.getRGBs();
	byte[] fromData = fromImageData.data;
	byte[] toData = new byte[fromData.length];
	System.arraycopy(fromData, 0, toData, 0, fromData.length); // Copy over so that only changes need to be individually saved.
	int depth = fromImageData.depth;
	int bytesPerLine = fromImageData.bytesPerLine;

	// We will traverse one row at a time. We will only traverse the rows within
	// the outer bounds of the region since those are the only rows that could
	// require changing. We will go through the row one point at a time testing
	// if it is in the region. If so, we will merge in the alpha, otherwise we will
	// leave it unchanged. Inside this bounds we will do
	// individual tests because there could be independent areas not within any
	// of the rectangles of the region but still within the bounds of the region.
	// Intersect it with the bounds of the image so that we never leave the image
	// because the Region could be outside of the image bounds.
	Rectangle outerBounds = region.getBounds().intersection(new Rectangle(0, 0, fromImageData.width, fromImageData.height));
	if (depth == 1) {
		// Do nothing for depth == 1 because that is just black and white and alpha
		// makes little sense for this.
	} else {
		int y = outerBounds.y;
		int endY = outerBounds.y + outerBounds.height;
		for (; y < endY; y++) {
			if (depth == 4) {
				// Depth == 4 means we must have an indexed palette, can't get RGB into 4 bits.
				int x = outerBounds.x;
				int index = (y * bytesPerLine) + (x >> 1);
				boolean isUpperNibble = (outerBounds.x & 0x1) == 0;
				int endX = x + outerBounds.width;
				for (; x < endX; x++) {
					if (region.contains(x, y)) {
						// We need to convert this pixel
						int pixel;
						if (isUpperNibble)
							pixel = (toData[index] & 0xFF) >>> 4;
						else
							pixel = toData[index] & 0x0F;
						RGB color = colors[pixel];
						int red = (color.red * aNum) / 100 + redCba;
						int green = (color.green * aNum) / 100 + greenCba;
						int blue = (color.blue * aNum) / 100 + blueCba;
						pixel = getNearestColorIndex(red, green, blue, colors);
						if (isUpperNibble)
							toData[index] = (byte) ((toData[index] & 0x0F) | (byte) (pixel << 4));
						else
							toData[index] = (byte) ((toData[index] & 0xF0) | (byte) pixel);
					}
					if (!isUpperNibble)
						index++; // We just passed the lower nibble, go to next byte
					isUpperNibble = !isUpperNibble; // Flipping to other nibble
				}
				continue; // Go to next row
			}
			if (depth == 8) {
				// Depth == 8 means we must have an indexed palette, can't get RGB into 8 bits.
				int x = outerBounds.x;
				int index = (y * bytesPerLine) + x;
				int endX = x + outerBounds.width;
				for (; x < endX; x++) {
					if (region.contains(x, y)) {
						// We need to convert this pixel
						RGB color = colors[toData[index] & 0xFF];
						int red = (color.red * aNum) / 100 + redCba;
						int green = (color.green * aNum) / 100 + greenCba;
						int blue = (color.blue * aNum) / 100 + blueCba;
						toData[index] = (byte) getNearestColorIndex(red, green, blue, colors);
					}
					index++;
				}
				continue; // Go to next row
			}
			if (depth == 16) {
				// Depth == 16 can be either indexed or Direct
				int x = outerBounds.x;
				int index = (y * bytesPerLine) + (x * 2);
				int endX = x + outerBounds.width;
				for (; x < endX; x++) {
					if (region.contains(x, y)) {
						int red, green, blue;
						if (isDirect) {
							// Can get RGB right out of the pixel. Since data is stored least significant byte first (i.e. Intel(R) format),
							// the colors can be found as:
							//    xxxBBBBB xxxxxxxx
							//    GGGxxxxx xxxxxxgg
							//    xxxxxxxx xRRRRRxx
							// Which if loaded into a register would be: xRRRRRggGGGBBBBB
							blue = ((toData[index] & 0x1F) * aNum) / 100 + blueCba;
							green = (((toData[index] & 0xFF) >>> 5 | (toData[index + 1] & 0x03) << 3) * aNum) / 100 + greenCba;
							red = (((toData[index + 1] & 0xFF) >>> 2) * aNum) / 100 + redCba;
							toData[index] = (byte) (green << 5 | blue);
							toData[index + 1] = (byte) (red << 2 | green >>> 3);
						} else {
							RGB color = colors[ (toData[index] & 0xFF) + ((toData[index + 1] & 0xFF) << 8)];
							red = (color.red * aNum) / 100 + redCba;
							green = (color.green * aNum) / 100 + greenCba;
							blue = (color.blue * aNum) / 100 + blueCba;
							int newColorIndex = getNearestColorIndex(red, green, blue, colors);
							toData[index] = (byte) newColorIndex;
							toData[index + 1] = (byte) (newColorIndex >>> 8);
						}
					}
					index += 2;
				}
				continue; // Go to next row					
			}
			if (depth == 24 || depth == 32) {
				// Depth == 24 or 32 can be either indexed or Direct
				int byteDepth = depth == 32 ? 4 : 3;
				int x = outerBounds.x;
				int index = (y * bytesPerLine) + (x * byteDepth);
				int endX = x + outerBounds.width;
				for (; x < endX; x++) {
					if (region.contains(x, y)) {
						int red, green, blue;
						if (isDirect) {
							// Can get RGB right out of the pixel. The only difference between 23 and 32 is that 32 has an extra
							// byte of zeroes attached. The number of bits and locations of the colors are the same.
							// The data is stored as: (Stored in Intel(R) format, least significant first).
							//   Byte 0: Blue
							//   Byte 1: Green
							//   Byte 2: Red
							blue = ((toData[index] & 0xFF) * aNum) / 100 + blueCba;
							green = ((toData[index + 1] & 0xFF) * aNum) / 100 + greenCba;
							red = ((toData[index + 2] & 0xFF) * aNum) / 100 + redCba;
							toData[index] = (byte) blue;
							toData[index + 1] = (byte) green;
							toData[index + 2] = (byte) red;
						} else {
							RGB color = colors[ (toData[index] & 0xFF) + ((toData[index + 1] & 0xFF) << 8) + ((toData[index + 2] & 0xFF) << 16)];
							red = (color.red * aNum) / 100 + redCba;
							green = (color.green * aNum) / 100 + greenCba;
							blue = (color.blue * aNum) / 100 + blueCba;
							int newColorIndex = getNearestColorIndex(red, green, blue, colors);
							toData[index] = (byte) newColorIndex;
							toData[index + 1] = (byte) (newColorIndex >>> 8);
							toData[index + 2] = (byte) (newColorIndex >>> 16);
						}
					}
					index += byteDepth;
				}
				continue; // Go to next row					
			}
		}
	}

	// We've created the new byte array, now create the new ImageData
	return new ImageData(fromImageData.width, fromImageData.height, depth, palette, fromImageData.scanlinePad, toData);
}
/**
 * This method doesn't appear on ImageData, but it
 * should. But even so, they have bugs for depths
 * greater than 8.
 *
 * Copy width pixel values starting at offset x in
 * scanline y from the array pixels starting at
 * startIndex.
 *
 * @param x the x position of the pixel to set
 * @param y the y position of the pixel to set
 * @param putWidth the width of the pixels to set
 * @param pixels the pixels to set
 * @param startIndex the index at which to begin setting
 * @exception SWTError(ERROR_NULL_ARGUMENT)
 *	if pixels is null
 * @exception SWTError(ERROR_UNSUPPORTED_DEPTH)
 *	if the depth is not one of 1, 4, 8, 16, 24 or 32
 */
public static void setPixels(ImageData dest, int x, int y, int putWidth, int[] pixels, int startIndex) {
	if (pixels == null) return;
	int depth = dest.depth;
	int bytesPerLine = dest.bytesPerLine;
	byte[] data = dest.data;
	
	int index;
	int theByte;
	int mask;
	int n;
	int i;
	int pixel;
	if (putWidth <= 0)
		return;

	if (depth == 1) {
		index = (y * bytesPerLine) + (x >> 3);
		theByte = data[index];
		mask = 1 << (7 - (x & 0x7));
		n = putWidth;
		i = startIndex;
		while (n > 1) {
			if ((pixels[i] & 0x1) == 1)
				theByte = theByte | mask;
			else
				theByte = theByte & (mask ^ -1);
			i++;
			n--;
			if (mask == 1) {
				data[index] = (byte)theByte;
				mask = 128;
				index++;
				theByte = data[index];
			} else {
				mask = mask >> 1;
			}
		}
		if ((pixels[i] & 0x1) == 1) {
			theByte = theByte | mask;
		} else {
			theByte = theByte & (mask ^ -1);
		}
		data[index] = (byte)theByte;
		return;
	}
	if (depth == 4) {
		index = (y * bytesPerLine) + (x >> 1);
		n = putWidth;
		i = startIndex;
		if ((x & 0x1) == 1) {
			theByte = (data[index] & 0xF0) | (pixels[i] & 0x0F);
			data[index] = (byte)theByte;
			i++;
			n--;
			index++;
		}
		while (n > 1) {
			theByte = ((pixels[i] & 0x0F) << 4) | (pixels[i+1] & 0x0F);
			data[index] = (byte)theByte;
			i += 2;
			n -= 2;
			index++;
		}
		if (n > 0) {
			theByte = (data[index] & 0x0F) | ((pixels[i] & 0x0F) << 4);
			data[index] = (byte)theByte;
		}
		return;
	}
	if (depth == 8) {
		index = (y * bytesPerLine) + x ;
		i = startIndex;
		for (int j = 0; j < putWidth; j++) {
			data[index] = (byte)(pixels[i] & 0xFF);
			i++;
			index++;
		}
		return;
	}
	if (depth == 16) {
		// Bogus ---- ImageData was storing high-byte first. This is wrong on Intel, it is low byte first.
		index = (y * bytesPerLine) + (x * 2);
		i = startIndex;
		for (int j = 0; j < putWidth; j++) {
			pixel = pixels[i];
			data[index+1] = (byte)((pixel >> 8) & 0xFF);
			data[index] = (byte)(pixel & 0xFF);
			i++;
			index += 2;
		}
		return;
	}
	if (depth == 24) {
		index = (y * bytesPerLine) + (x * 3);
		i = startIndex;
		for (int j = 0; j < putWidth; j++) {
			pixel = pixels[i];
			data[index + 2] = (byte)((pixel >> 16) & 0xFF);
			data[index + 1] = (byte)((pixel >> 8) & 0xFF);
			data[index] = (byte)(pixel & 0xFF);
			i++;
			index += 3;
		}
		return;
	}
	if (depth == 32) {
		index = (y * bytesPerLine) + (x * 4);
		i = startIndex;
		for (int j = 0; j < putWidth; j++) {
			pixel = pixels[i];
			data[index] = (byte)((pixel >> 24) & 0xFF);
			data[index + 1] = (byte)((pixel >> 16) & 0xFF);
			data[index + 2] = (byte)((pixel >> 8) & 0xFF);
			data[index + 3] = (byte)(pixel & 0xFF);
			i++;
			index += 4;
		}
		return;
	}
}
}
