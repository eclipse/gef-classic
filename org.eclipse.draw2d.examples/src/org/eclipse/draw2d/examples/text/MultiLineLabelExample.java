package org.eclipse.draw2d.examples.text;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.eclipse.draw2d.widgets.MultiLineLabel;

/**
 * This example shows the MultiLineLabel widget.  MutliLineLabel is a widget for
 * displaying text that wraps, but *only* shows scrollbars when necessary.  For
 * comparison, a native Text control appears below.
 * @author hudsonr
 */
public abstract class MultiLineLabelExample {

private static MultiLineLabel label;

public static void main(String[] args) {
	Display d = Display.getDefault();
	Shell shell = new Shell(d);
	shell.setLayout(new GridLayout());
	label = new MultiLineLabel(shell);
	Text text = new Text(shell, SWT.MULTI | SWT.WRAP | SWT.H_SCROLL | SWT.READ_ONLY);
	label.setText("This is a  oijeoi aeif jaoiewjf oaiew jfoaiew" +
			"apijewpfk apoewkf paokwe pfokawpehfaowephf hawoiejf oaweijf paowkefp aoewkfpa " +
			"pawkfe paoewkf paowekf pawokefoaiwjefo iajewoifja oewijf poerk pgaokew faewfpokew f" +
			"f oaewkfp aewpofk apwejfoiuajweo foiwajef poawkefpawkje pfoakewpf aewpfkpaowke fpawf" +
			"gfpowaegpawepmulti-line label.");
	text.setText(label.getText());

	GridData data = new GridData(GridData.FILL_HORIZONTAL);
	data.widthHint = 170;
	data.heightHint = - 1;
	text.setLayoutData(data);
	data = new GridData(GridData.FILL_HORIZONTAL);
	data.widthHint = 170;
	data.heightHint = - 1;
	label.setLayoutData(data);
	shell.open();
	shell.setSize(600,500);
	while (!shell.isDisposed())
		while (!d.readAndDispatch())
			d.sleep();
}

}
