package org.eclipse.draw2d.examples;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import org.eclipse.draw2d.widgets.MultiLineLabel;

/**
 * A baseclass for draw2d examples.
 * @author hudsonr
 */
public abstract class MultiLineLabelExample {

private static MultiLineLabel label;

public static void main(String[] args) {
	Display d = Display.getDefault();
	Shell shell = new Shell(d);
	shell.setLayout(new GridLayout());
	label = new MultiLineLabel(shell);
	Text text = new Text(shell, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL);
	label.setText("This is a  oijeoi aeif jaoiewjf oaiew jfoaiew jfoiawjjoiajofije woifjafoiewjfo  oaiej foaijew foaiew foijewaf oiaewoif jaoiew jaijewoafijewfoaiew jfoaiew foaiew jfpwaoekf pwaoe foiewa fpaoew foahg oewajg oiwae gfpowaegpawepmulti-line label.");
	text.setText(label.getText());

	GridData data = new GridData(GridData.FILL_HORIZONTAL);
	data.heightHint = 300;
	text.setLayoutData(data);
	data = new GridData(GridData.FILL_HORIZONTAL);
	data.heightHint = 300;
	label.setLayoutData(data);
	shell.setSize(600,500);
	shell.open();
	while (!shell.isDisposed())
		while (!d.readAndDispatch())
			d.sleep();
}

}
