package org.eclipse.zest.core.widgets.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.zest.core.widgets.GraphNode;

public class NodeSearchDialog {
	private Shell parent;
	private Shell dialog;
	private List<GraphNode> nodes;

	private List<GraphNode> searchNodes;
	private int index = 0;
	private boolean isDisposed = false;

	private Text text;
	private Button nextButton;
	private Button prevButton;
	private Button caseSensButton;
	private Button wholeWordButton;

	public NodeSearchDialog(Shell parent, List<GraphNode> nodes) {
		this.nodes = nodes;
		this.parent = parent;

		searchNodes = new ArrayList<>();
		createDialog(parent);
	}

	private void createDialog(Shell parentShell) {
		dialog = new Shell(parentShell, SWT.DIALOG_TRIM | SWT.MAX | SWT.RESIZE);
		dialog.setText("Find");
		GridLayout layout = new GridLayout(2, false);
		dialog.setLayout(layout);

		// 1st row
		final Label label = new Label(dialog, SWT.NONE);
		label.setText("Find:");

		text = new Text(dialog, SWT.BORDER);
		text.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				searchForNodes();
			}
		});
		text.addModifyListener(e -> {
			if (text.getText().isBlank()) {
				nextButton.setEnabled(false);
				prevButton.setEnabled(false);
			} else {
				nextButton.setEnabled(true);
				prevButton.setEnabled(true);
			}
		});
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.minimumWidth = 200;
		text.setLayoutData(gridData);

		// 2nd row
		final Label optionsLabel = new Label(dialog, SWT.NONE);
		optionsLabel.setText("Options:");
		new Label(dialog, SWT.NULL);

		// 3rd row
		wholeWordButton = new Button(dialog, SWT.CHECK);
		wholeWordButton.addListener(SWT.Selection, e -> searchForNodes());
		final Label wholeWordLabel = new Label(dialog, SWT.NONE);
		wholeWordLabel.setText("Whole Word");

		// 4th row
		caseSensButton = new Button(dialog, SWT.CHECK);
		caseSensButton.addListener(SWT.Selection, e -> searchForNodes());
		final Label caseSensitiveLabel = new Label(dialog, SWT.NONE);
		caseSensitiveLabel.setText("Case sensitive");

		// 5th row
		new Label(dialog, SWT.NULL);

		Composite comp = new Composite(dialog, SWT.NONE);
		comp.setLayout(new GridLayout(2, false));

		nextButton = new Button(comp, SWT.PUSH);
		nextButton.setText("Next");
		nextButton.setEnabled(false);
		nextButton.addListener(SWT.Selection, e -> changeNode(true));

		prevButton = new Button(comp, SWT.PUSH);
		prevButton.setText("Previous");
		prevButton.setEnabled(false);
		prevButton.addListener(SWT.Selection, e -> changeNode(false));

		// 6th row
		new Label(dialog, SWT.NULL);
		comp = new Composite(dialog, SWT.NONE);
		comp.setLayout(new GridLayout(1, false));
		comp.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, true, true));

		Button closeButton = new Button(comp, SWT.PUSH);
		closeButton.setText("Close");
		closeButton.addListener(SWT.Selection, e -> dialog.close());

		dialog.addDisposeListener(e -> isDisposed = true);
		dialog.pack();
	}

	private void searchForNodes() {
		if (text.getText().isEmpty()) {
			return;
		}

		boolean searchWhole = wholeWordButton.getSelection();
		boolean caseSensitive = caseSensButton.getSelection();

		List<GraphNode> newNodes = new ArrayList<>();
		for (GraphNode node : nodes) {
			String nodeText;
			String search;
			if (caseSensitive) {
				nodeText = node.getText();
				search = text.getText();
			} else {
				nodeText = node.getText().toLowerCase();
				search = text.getText().toLowerCase();
			}

			if (searchWhole && nodeText.equals(search) || !searchWhole && nodeText.contains(search)) {
				newNodes.add(node);
			}
		}

		if (newNodes.size() != searchNodes.size() || !searchNodes.containsAll(newNodes)) {
			index = 0;
			searchNodes = newNodes;
		}
	}

	private void changeNode(boolean forward) {
		if (searchNodes.isEmpty()) {
			return;
		}
		if (index < 0) {
			index = searchNodes.size() - 1;
		}
		GraphNode node = searchNodes.get(index % searchNodes.size());
		node.getGraphModel().setSelection(new GraphNode[] { node }); // select node

		index = forward ? index + 1 : index - 1; // increase / decrease index
	}

	public void open() {
		if (isDisposed) {
			isDisposed = false;
			createDialog(this.parent);
		}
		dialog.open();
	}
}
