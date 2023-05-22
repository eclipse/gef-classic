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
	private Shell dialog;
	private List<GraphNode> nodes;

	private List<GraphNode> searchNodes;
	private GraphNode prevNode;
	private int index = 0;

	public NodeSearchDialog(Shell parent, List<GraphNode> nodes) {
		this.nodes = nodes;
		searchNodes = new ArrayList<>();
		this.dialog = createDialog(parent);
	}

	private Shell createDialog(Shell parentShell) {
		Shell dialog = new Shell(parentShell, SWT.DIALOG_TRIM | SWT.MAX);
		dialog.setText("Find");
		dialog.setSize(300, 200);
		GridLayout layout = new GridLayout(2, false);
		dialog.setLayout(layout);

		// 1st row
		final Label label = new Label(dialog, SWT.NONE);
		label.setText("Find:");

		final Text text = new Text(dialog, SWT.BORDER);
		text.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				String search = text.getText();
				if (searchNodes.isEmpty() || !searchNodes.get(0).getText().equals(search)) {
					index = 0;
					searchNodes.clear();
					for (GraphNode node : (nodes)) {
						if (node.getText().equals(search)) {
							searchNodes.add(node);
						}
					}
				}
			}
		});
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		text.setLayoutData(gridData);

		// 2nd row
		new Label(dialog, SWT.NULL);

		Composite comp = new Composite(dialog, 0);
		comp.setLayout(new GridLayout(3, false));

		Button nextButton = new Button(comp, SWT.PUSH);
		nextButton.setText("Next");
		nextButton.addListener(SWT.Selection, e -> {
			if (searchNodes.isEmpty()) {
				return;
			}
			GraphNode node = searchNodes.get(index % searchNodes.size());
			node.getGraphModel().setSelection(new GraphNode[] { node }); // select node
			index++;
			if (prevNode != null && prevNode != node) {
				prevNode.unhighlight();
			}
			prevNode = node;
		});

		Button prevButton = new Button(comp, SWT.PUSH);
		prevButton.setText("Previous");
		prevButton.addListener(SWT.Selection, e -> {
			if (searchNodes.isEmpty()) {
				return;
			}
			if (index < 0) {
				index = searchNodes.size() - 1;
			}
			GraphNode node = searchNodes.get(index % searchNodes.size());
			node.highlight();
			node.getGraphModel().setSelection(new GraphNode[] { node }); // select node
			index--;
			if (prevNode != null && prevNode != node) {
				prevNode.unhighlight();
			}
			prevNode = node;
		});

		Button closeButton = new Button(comp, SWT.PUSH);
		closeButton.setText("Close");
		closeButton.addListener(SWT.Selection, e -> dialog.close());

		return dialog;
	}

	public void open() {
		dialog.open();
	}
}
