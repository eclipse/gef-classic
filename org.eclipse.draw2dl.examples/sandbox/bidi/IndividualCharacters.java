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
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;

public class IndividualCharacters {

public static void main(String[] args) {
	final Display display = new Display();
	final Shell shell = new Shell(SWT.SHELL_TRIM | SWT.RIGHT_TO_LEFT);
	shell.setLayout(new GridLayout());
	final String message = "\u202e\u0634\u0636\u202c\u0634";
	
	StyledText text = new StyledText(shell, SWT.MULTI | SWT.WRAP | SWT.RIGHT_TO_LEFT);
	text.setLayoutData(new GridData(GridData.FILL_BOTH));
	text.setText(message);
	
	final Group canvas = new Group(shell, SWT.BORDER | SWT.LEFT_TO_RIGHT);
	canvas.setText("Left to Right");
	canvas.setLayoutData(new GridData(GridData.FILL_BOTH));

	Group canvas2 = new Group(shell, SWT.BORDER | SWT.RIGHT_TO_LEFT);
	canvas2.setLayoutData(new GridData(GridData.FILL_BOTH));
	canvas2.setText("Right to Left");

	PaintListener pl = new PaintListener() {
		public void paintControl(PaintEvent e) {
			int y = ((Scrollable)e.widget).getClientArea().y;
			int x = ((Scrollable)e.widget).getClientArea().x;
			TextLayout layout = new TextLayout(null);
			TextLayout layout2 = new TextLayout(null);
			layout.setOrientation(e.widget.getStyle() & (SWT.RIGHT_TO_LEFT  | SWT.LEFT_TO_RIGHT));
			layout2.setOrientation(layout.getOrientation());
			layout.setWidth(canvas.getClientArea().width);
			String rtol = message;
			layout.setText(rtol);
			
			layout.draw(e.gc, x, y);
			e.gc.drawString(rtol, x, 12 + y);
			e.gc.drawText(rtol, x, 24 + y);
			for (int i = 0; i < rtol.length(); i++) {
				Point where = layout.getLocation(i, layout.getLevel(i) % 2 == 1);
				where.y += y;
				where.x += x;
				System.out.println(where.x);
				//System.out.print(where.x + ", ");
				layout2.setText(Character.toString(rtol.charAt(i)));
				//layout2.draw(e.gc, where.x, where.y + 36);
				
				e.gc.drawString(Character.toString(rtol.charAt(i)), where.x, where.y + 36, true);
				e.gc.drawString("" + layout.getLevel(i), where.x, where.y + 48 +(i % 2 == 0?0:12), true);
			}
			layout.dispose();
		}
	};
	
	canvas.addPaintListener(pl);
	canvas2.addPaintListener(pl);
	
	shell.setSize(400, 300);
	shell.open();

	while (!shell.isDisposed())
		if (!display.readAndDispatch())
			display.sleep();
}

}