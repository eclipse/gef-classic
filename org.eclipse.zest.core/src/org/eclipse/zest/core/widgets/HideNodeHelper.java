package org.eclipse.zest.core.widgets;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Button;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.zest.core.widgets.internal.ContainerFigure;
import org.eclipse.zest.core.widgets.internal.GraphLabel;

/**
 * @since 1.8
 */
public class HideNodeHelper {
	public static final int HIDEBUTTONSIZE = 13;
	public static final int MARGIN = 5;

	public GraphNode node;

	private IFigure hideContainer = new ContainerFigure();
	private Button hideButton = new Button("-");
	private Button revealButton = new Button("+");
	private int hiddenNodeCount = 0;
	private Label hiddenNodesLabel = new GraphLabel("0", false);

	private HideNodeListener thisHideNodeListener;
	private List<HideNodeListener> hideNodeListeners = new ArrayList<>();

	public HideNodeHelper(GraphNode node) {
		this.node = node;
		thisHideNodeListener = new HideNodeListener(node);
		createHideButtons(node.getNodeFigure());
	}

	/**
	 * @since 1.8
	 */
	public void createHideButtons(IFigure figure) {
		// create buttons and label for hiding nodes
		hideButton.setVisible(false);
		revealButton.setVisible(false);
		hiddenNodesLabel.setVisible(false);

		hideButton.addActionListener(event -> node.setVisible(false));
		revealButton.addActionListener(event -> {
			for (HideNodeListener hideNodeListener : hideNodeListeners) {
				hideNodeListener.revealNode(); // try to reveal all connected nodes
			}
			revealButton.setVisible(false);
		});

		hiddenNodesLabel.setBackgroundColor(ColorConstants.red);
		hiddenNodesLabel.setForegroundColor(ColorConstants.black);

		hideContainer.add(hideButton);
		hideContainer.add(revealButton);
		hideContainer.add(hiddenNodesLabel);

		figure.add(hideContainer);
	}

	public void updateHideButtonFigure() {
		Rectangle bounds = node.getNodeFigure().getBounds();

		hideContainer.setBounds(bounds);
		hideButton.setBounds(new Rectangle(node.getLocation(), new Dimension(HIDEBUTTONSIZE, HIDEBUTTONSIZE)));
		revealButton.setBounds(new Rectangle(bounds.x + bounds.width - HIDEBUTTONSIZE,
				bounds.y + bounds.height - HIDEBUTTONSIZE, HIDEBUTTONSIZE, HIDEBUTTONSIZE));
		hiddenNodesLabel.setBounds(new Rectangle(node.getLocation().x + bounds.width - HIDEBUTTONSIZE,
				node.getLocation().y, HIDEBUTTONSIZE, HIDEBUTTONSIZE));
	}

	public void setHideButtonVisible(boolean visible) {
		hideButton.setVisible(visible);
		updateHideButtonFigure();
	}

	public void setRevealButtonVisible(boolean visible) {
		if (hiddenNodeCount > 0) {
			revealButton.setVisible(visible);
			updateHideButtonFigure();
		}
	}

	public void increaseHiddenNodes() {
		this.hiddenNodeCount++;
		hiddenNodesLabel.setVisible(true);
		hiddenNodesLabel.setText(Integer.toString(hiddenNodeCount));
		updateHideButtonFigure();
	}

	public void decreaseHiddenNode() {
		this.hiddenNodeCount--;
		hiddenNodesLabel.setVisible(hiddenNodeCount > 0); // true if hidden node still exists
		hiddenNodesLabel.setText(Integer.toString(hiddenNodeCount));
		updateHideButtonFigure();
	}

	public void setBounds(Rectangle bounds) {
		node.getNodeFigure().setBounds(bounds);
		node.getModelFigure()
				.setBounds(new Rectangle(bounds.x + HideNodeHelper.MARGIN, bounds.y + HideNodeHelper.MARGIN,
						bounds.width - HideNodeHelper.MARGIN * 2, bounds.height - HideNodeHelper.MARGIN * 2));

	}

	/**
	 * @since 1.8
	 */
	public void addHideNodeListener(HideNodeListener listener) {
		if (!this.hideNodeListeners.contains(listener) && listener != thisHideNodeListener) {
			// new HideNodeListener of other node
			this.hideNodeListeners.add(listener);
		}
	}

	/**
	 * @since 1.8
	 */
	public void removeHideNodeListener(HideNodeListener listener) {
		this.hideNodeListeners.remove(listener);
	}

	/**
	 * @since 1.8
	 */
	public List<HideNodeListener> getHideNodesListeners() {
		return this.hideNodeListeners;
	}

	/**
	 * @since 1.8
	 */
	public HideNodeListener getHideNodesListener() {
		return this.thisHideNodeListener;
	}

	class HideNodeListener {
		private GraphNode node;

		public HideNodeListener(GraphNode node) {
			this.node = node;
		}

		public void fireNodeHidden() {
			increaseHiddenNodes();
		}

		public void fireNodeRevealed() {
			decreaseHiddenNode();
		}

		public void revealNode() {
			if (this.node.isVisible()) {
				return; // node already visible
			}
			this.node.setVisible(true);
		}
	}
}
