
/*******************************************************************************
 * Copyright (c) 2005, 2023 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class ModifyVerify4Resizing {

	public static void main(String[] args) {
		final Display display = new Display();
		final Color black = display.getSystemColor(SWT.COLOR_BLACK);
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		final Tree tree = new Tree(shell, SWT.BORDER);
		for (int i = 0; i < 16; i++) {
			TreeItem item = new TreeItem(tree, SWT.NONE);
			item.setText("Item " + i); //$NON-NLS-1$
		}
		final TreeItem[] lastItem = new TreeItem[1];
		final TreeEditor editor = new TreeEditor(tree);
		tree.addListener(SWT.Selection, e -> {
			final TreeItem item = (TreeItem) e.item;
			if ((item != null) && (item == lastItem[0])) {
				final Composite composite = new Composite(tree, SWT.NONE);
				composite.setBackground(black);
				final Text text = new Text(composite, SWT.NONE);
				composite.addListener(SWT.Resize, e1 -> {
					Rectangle rect = composite.getClientArea();
					text.setBounds(rect.x + 1, rect.y + 1, rect.width - 2, rect.height - 2);
				});

				Listener textListener = e1 -> {
					switch (e1.type) {
					case SWT.DefaultSelection:
						item.setText(text.getText());
						// FALL THROUGH
					case SWT.FocusOut:
						composite.dispose();
						break;
					case SWT.Verify:
						String newText = text.getText();
						String leftText = newText.substring(0, e1.start);
						String rightText = newText.substring(e1.end, newText.length());
						GC gc = new GC(text);
						Point size = gc.textExtent(leftText + e1.text + rightText);
						gc.dispose();
						size = text.computeSize(size.x, SWT.DEFAULT);
						editor.horizontalAlignment = SWT.LEFT;
						Rectangle itemRect = item.getBounds(), rect = tree.getClientArea();
						editor.minimumWidth = Math.max(size.x, itemRect.width) + 2;
						int left = itemRect.x, right = rect.x + rect.width;
						editor.minimumWidth = Math.min(editor.minimumWidth, right - left);
						editor.layout();
						break;
					case SWT.Traverse:
						if (e1.detail == SWT.TRAVERSE_ESCAPE) {
							composite.dispose();
							e1.doit = false;
						}
						break;
					}
				};
				text.addListener(SWT.DefaultSelection, textListener);
				text.addListener(SWT.FocusOut, textListener);
				text.addListener(SWT.Traverse, textListener);
				text.addListener(SWT.Verify, textListener);
				editor.setEditor(composite, item);
				text.setText(item.getText());
				text.selectAll();
				text.setFocus();
			}
			lastItem[0] = item;
		});
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}
