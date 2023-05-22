package org.eclipse.zest.core.widgets.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.zest.core.widgets.GraphNode;

public class NodeSearchDialog {
	private Shell shell;
	private List<GraphNode> nodes;

	private List<GraphNode> searchNodes;
	private GraphNode prevNode;
	private int index = 0;

	public NodeSearchDialog(Shell shell, List<GraphNode> nodes) {
		this.nodes = nodes;
		this.shell = shell;
		searchNodes = new ArrayList<>();
	}

	public void open() {
		Shell dialog = new Shell(this.shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		dialog.setText("Find");
		dialog.setSize(300, 200);
		dialog.setLayout(new RowLayout());

		final Text text = new Text(dialog, SWT.SHADOW_IN);
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

		Button nextButton = new Button(dialog, SWT.PUSH);
		nextButton.setText("Next");
		nextButton.addListener(SWT.Selection, e -> {
			GraphNode node = searchNodes.get(index % searchNodes.size());
			node.getGraphModel().setSelection(new GraphNode[] { node });	// select node
			index++;
			if (prevNode != null && prevNode != node) {
				prevNode.unhighlight();
			}
			prevNode = node;
		});

		Button prevButton = new Button(dialog, SWT.PUSH);
		prevButton.setText("Previous");
		prevButton.addListener(SWT.Selection, e -> {
			if (index < 0) {
				index = searchNodes.size() - 1;
			}
			GraphNode node = searchNodes.get(index % searchNodes.size());
			node.highlight();
			node.getGraphModel().setSelection(new GraphNode[] { node });	// select node
			index--;
			if (prevNode != null && prevNode != node) {
				prevNode.unhighlight();
			}
			prevNode = node;
		});

		Button cancelButton = new Button(dialog, SWT.PUSH);
		cancelButton.setText("Cancel");
		cancelButton.addListener(SWT.Selection, e -> dialog.close());

		dialog.open();
	}
}
