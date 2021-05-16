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
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.custom.*;

public class ModifyVerify4Resizing {

public static void main (String [] args) {
	final Display display = new Display ();
	final Color black = display.getSystemColor (SWT.COLOR_BLACK);
	Shell shell = new Shell (display);
	shell.setLayout (new FillLayout ());
	final Tree tree = new Tree (shell, SWT.BORDER);
	for (int i=0; i<16; i++) {
		TreeItem item = new TreeItem (tree, SWT.NONE);
		item.setText ("Item " + i);
	}
	final TreeItem [] lastItem = new TreeItem [1];
	final TreeEditor editor = new TreeEditor (tree);
	tree.addListener (SWT.Selection, new Listener () {
		public void handleEvent (Event e) {
			final TreeItem item = (TreeItem) e.item;
			if (item != null && item == lastItem [0]) {
				final Composite composite = new Composite (tree, SWT.NONE);
				composite.setBackground (black);
				final Text text = new Text (composite, SWT.NONE);
				composite.addListener (SWT.Resize, new Listener () {
					public void handleEvent (Event e) {
						Rectangle rect = composite.getClientArea ();
						text.setBounds (rect.x + 1, rect.y + 1, rect.width - 2, rect.height - 2);
					}
				});
				Listener textListener = new Listener () {
					public void handleEvent (final Event e) {
						switch (e.type) {
							case SWT.DefaultSelection:
								item.setText (text.getText ());
								//FALL THROUGH
							case SWT.FocusOut:
								composite.dispose ();
								break;
							case SWT.Verify:
								String newText = text.getText ();
								String leftText = newText.substring (0, e.start);
								String rightText = newText.substring (e.end, newText.length ());
								GC gc = new GC (text);
								Point size = gc.textExtent (leftText + e.text + rightText);
								gc.dispose ();
								size = text.computeSize (size.x, SWT.DEFAULT);
								editor.horizontalAlignment = SWT.LEFT;
								Rectangle itemRect = item.getBounds (), rect = tree.getClientArea ();
								editor.minimumWidth = Math.max (size.x, itemRect.width) + 2;
								int left = itemRect.x, right = rect.x + rect.width;
								editor.minimumWidth = Math.min (editor.minimumWidth, right - left);
								editor.layout ();
								break;
							case SWT.Traverse:
								if (e.detail == SWT.TRAVERSE_ESCAPE) {
									composite.dispose ();
									e.doit = false;
								}
								break;
						}
					}
				};
				text.addListener (SWT.DefaultSelection, textListener);
				text.addListener (SWT.FocusOut, textListener);
				text.addListener (SWT.Traverse, textListener);
				text.addListener (SWT.Verify, textListener);
				editor.setEditor (composite, item);
				text.setText (item.getText ());
				text.selectAll ();
				text.setFocus ();
			}
			lastItem [0] = item;
		}
	});
	shell.pack ();
	shell.open ();
	while (!shell.isDisposed()) {
			if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}
