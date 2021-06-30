/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package bidi;
import java.text.Bidi;
import java.util.Random;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class BenchmarkLevelRuns {

public static void main(String[] args) {
	Display display = new Display();
	
	String words[] = new String [] {"International", "Business", "Machines",
			"\u0634\u0635\u0636\u0637\u0639\u0641\u0650",
			"\u0640\u0641\u0642\u0643\u0644\u0630\u0646\u0643",
			"\u0629\u0633\u0645\u0643\u0643\u0649"
	};
	
	StringBuffer buffer = new StringBuffer();
	Random rand = new Random();
	for (int i = 0; i < 1000; i++)
		buffer.append(words[rand.nextInt(words.length)] +" ");
	String sentence = buffer.toString();
	
	getSWTLevelRuns(sentence);
	getAWTLevelRuns(sentence);
	
	long end = - 1;
	long start = System.currentTimeMillis();
	
	for (int i = 0; i < 100; i++)
		getSWTLevelRuns(sentence);
	
	end = System.currentTimeMillis();
	System.out.println("SWT Ellapsed millis:" + (end - start));

	start = System.currentTimeMillis();
	for (int i = 0; i < 100; i++)
		getAWTLevelRuns(sentence);
	end = System.currentTimeMillis();
	System.out.println("AWT Ellapsed millis:" + (end - start));
	
	displayArray(getSWTLevelRuns(sentence));
	displayArray(getAWTLevelRuns(sentence));
	
	final Shell shell = new Shell(display);
	shell.setText("Shell");
	shell.setLayout(new FillLayout());
	StyledText text = new StyledText(shell, SWT.WRAP);
	text.setText(sentence);
	
	shell.setSize(200, 200);
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch())
			display.sleep();
	}
	display.dispose();
}

/**
 * @param levelRuns
 * @since 3.1
 */
private static void displayArray(int[] levelRuns) {
	for (int i = 0; i < levelRuns.length; i++)
		System.out.print(levelRuns[i] +",");
	System.out.println();
}

private static int[] getAWTLevelRuns(String sentence) {
	Bidi bidi = new Bidi(sentence, 0);
	int result[] = new int[bidi.getRunCount() * 2];
	for (int i = 0; i < result.length / 2; i++) {
		result[i * 2] = bidi.getRunStart(i);
		result[i * 2 + 1] = bidi.getRunLevel(i);
	}
	return result;
}

/**
 * @param sentence
 * @since 3.1
 */
private static int[] getSWTLevelRuns(String sentence) {
	int[] levels = new int[15];
	TextLayout layout = new TextLayout(null);
	layout.setText(sentence);
	
	int j = 0, offset = 0, prevLevel = -1;
	for (offset = 0; offset < sentence.length(); offset++) {
		int newLevel = layout.getLevel(offset);
		if (newLevel != prevLevel) {
			if (j + 3 > levels.length) {
				int temp[] = levels;
				levels = new int[levels.length * 2 + 1];
				System.arraycopy(temp, 0, levels, 0, temp.length);
			}
			levels[j++] = offset;
			levels[j++] = newLevel;
			prevLevel = newLevel;
		}
	}
	levels[j++] = offset;

	if (j != levels.length) {
		int[] newLevels = new int[j];
		System.arraycopy(levels, 0, newLevels, 0, j);
		levels = newLevels;
	}
	return levels;
}

}