/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.core.widgets;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * The BreadCrumbBar class holds a Group control which has a Link control inside it.
 * Adding breadcrumb items will put hyperlinks into the Link object separated by the separator
 * character.
 * 
 * @author ccallendar
 */
public class BreadCrumbBar extends Composite implements SelectionListener {

	public static final char SEPARATOR_CHAR = '>';

	private Group group;
	private ToolBar toolbar;
	private ToolItem backToolItem;
	private ToolItem forwardToolItem;
	private ToolItem upToolItem;
	private Link link;
	private GridData groupGridData;
	private GridData toolbarGridData;
	private GridData linkGridData;
	private GridData breadCrumbGridData;
	private ArrayList listeners;
	private ArrayList breadCrumbs;
	private char separatorChar;
	
	private ISharedImages sharedImages;
	
	
	/**
	 * Initializes the BreadCrumbBar.
	 * @param parent	The parent object.
	 * @param style		The style for this BreadCrumbBar.
	 */
	public BreadCrumbBar(Composite parent, int style) {
		super(parent, style);
		this.listeners = new ArrayList();
		this.breadCrumbs = new ArrayList();
		this.separatorChar = SEPARATOR_CHAR;
		this.sharedImages = PlatformUI.getWorkbench().getSharedImages();

		GridLayout layout = new GridLayout(1, true);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		this.setLayout(layout);
		this.breadCrumbGridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL, GridData.VERTICAL_ALIGN_BEGINNING, true, false);
		this.setLayoutData(breadCrumbGridData);

		initializeGroup();
		initializeToolbar();
		initializeLink();

		group.pack();
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Widget#dispose()
	 */
	public void dispose() {
		super.dispose();
	}

	
	private void initializeGroup() {
		this.group = new Group(this, SWT.SHADOW_ETCHED_OUT);
		//group.setText("Breadcrumb");
		GridLayout groupLayout = new GridLayout(2, false);
		groupLayout.marginHeight = 0;
		group.setLayout(groupLayout);
		groupGridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL, GridData.VERTICAL_ALIGN_FILL, true, true);
		groupGridData.widthHint = 600;
		groupGridData.heightHint = 24;
		groupGridData.minimumHeight = 18;
		group.setLayoutData(groupGridData);
	}
	
	/**
	 * Initializes the toolbar and adds the back/forward/up buttons and a separator.
	 * The three buttons are all disabled to start with.  As bread crumbs are added and clicked 
	 * the buttons will be enabled.
	 */
	private void initializeToolbar() {
		this.toolbar = new ToolBar(group, SWT.FLAT | SWT.HORIZONTAL | SWT.RIGHT);
		
		// Back button
		this.backToolItem = new ToolItem(toolbar, SWT.PUSH);
		//backToolItem.setText("Back");
		backToolItem.setToolTipText("Back");
		backToolItem.setImage(sharedImages.getImage(ISharedImages.IMG_TOOL_BACK));
		backToolItem.setDisabledImage(sharedImages.getImage(ISharedImages.IMG_TOOL_BACK_DISABLED));
		backToolItem.addSelectionListener(this);
		backToolItem.setEnabled(false);
		// Forward button
		this.forwardToolItem = new ToolItem(toolbar, SWT.PUSH);
		//forwardToolItem.setText("Forward");
		forwardToolItem.setToolTipText("Forward");
		forwardToolItem.setImage(sharedImages.getImage(ISharedImages.IMG_TOOL_FORWARD));
		forwardToolItem.setDisabledImage(sharedImages.getImage(ISharedImages.IMG_TOOL_FORWARD_DISABLED));
		forwardToolItem.addSelectionListener(this);
		forwardToolItem.setEnabled(false);
		// Up button
		this.upToolItem = new ToolItem(toolbar, SWT.PUSH);
		//upToolItem.setText(UP);
		upToolItem.setToolTipText("Up one level");
		upToolItem.setImage(sharedImages.getImage(ISharedImages.IMG_TOOL_UP));
		upToolItem.setDisabledImage(sharedImages.getImage(ISharedImages.IMG_TOOL_UP_DISABLED));
		upToolItem.addSelectionListener(this);
		upToolItem.setEnabled(false);
		
		new ToolItem(toolbar, SWT.SEPARATOR);
		
		this.toolbarGridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING, GridData.VERTICAL_ALIGN_BEGINNING, false, false);
		toolbar.setLayoutData(toolbarGridData);		
	}
	
	private void initializeLink() {
		this.link = new Link(group, SWT.NONE);
		link.addSelectionListener(this);
		this.linkGridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL, GridData.VERTICAL_ALIGN_BEGINNING, true, false);
		link.setLayoutData(linkGridData);
	}
	
	// SelectionListener methods
	public void widgetDefaultSelected(SelectionEvent e) {
		widgetSelected(e);
	}
		
	public void widgetSelected(SelectionEvent e) {
		Object src = e.getSource();
		if (src instanceof Link) {
			linkSelected((Link)src, e.text);
		} else if (src instanceof ToolItem) {
			toolItemSelected((ToolItem)src);
		}
	}
	
	private void linkSelected(Link lnk, String text) {
		ArrayList list = (ArrayList)lnk.getData();
		int index = -1;
		try {
			index = Integer.parseInt(text);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if ((index >= 0) && (index < list.size())) {
			BreadCrumbItem item = (BreadCrumbItem)list.get(index);
			for (Iterator iter = listeners.iterator(); iter.hasNext(); ) {
				IBreadCrumbListener bcl = (IBreadCrumbListener)iter.next();
				bcl.breadCrumbSelected(item);
			}
		}
	}
	
	private void toolItemSelected(ToolItem toolItem) {
		//Object data = toolItem.getData();
		if (toolItem == upToolItem) {
			up();
		} else if (toolItem == backToolItem) {
			back();
		} else if (toolItem == forwardToolItem) {
			forward();
		}
	}
	
	private void back() {
		for (Iterator iter = listeners.iterator(); iter.hasNext(); ) {
			IBreadCrumbListener bcl = (IBreadCrumbListener)iter.next();
			bcl.handleBackButtonSelected();
		}
	}
	
	private void forward() {
		for (Iterator iter = listeners.iterator(); iter.hasNext(); ) {
			IBreadCrumbListener bcl = (IBreadCrumbListener)iter.next();
			bcl.handleForwardButtonSelected();
		}
	}
	
	private void up() {
		for (Iterator iter = listeners.iterator(); iter.hasNext(); ) {
			IBreadCrumbListener bcl = (IBreadCrumbListener)iter.next();
			bcl.handleUpButtonSelected();
		}
	}
	
	/**
	 * Adds a listener to the list.  When a breadcrumb is selected 
	 * any listener in the list will be notified
	 * @param listener
	 */
	public void addBreadCrumbListener(IBreadCrumbListener listener) {
		this.listeners.add(listener);
	}

	/**
	 * Sets the back button to be enabled or disabled.
	 * @param enabled
	 */
	public void setBackEnabled(boolean enabled) {
		backToolItem.setEnabled(enabled);
	}

	/**
	 * Sets the forward button to be enabled or disabled.
	 * @param enabled
	 */
	public void setForwardEnabled(boolean enabled) {
		forwardToolItem.setEnabled(enabled);
	}

	/**
	 * Sets the up button to be enabled or disabled.
	 * @param enabled
	 */
	public void setUpEnabled(boolean enabled) {
		upToolItem.setEnabled(enabled);
	}

	/**
	 * Gets the last breadcrumb in the list, or null if the list is empty.
	 * @return BreadCrumbItem the last in the list
	 */
	protected BreadCrumbItem getLastBreadCrumb() {
		BreadCrumbItem item = null;
		if (breadCrumbs.size() > 0) {
			item = (BreadCrumbItem)breadCrumbs.get(breadCrumbs.size() - 1);
		}
		return item;
	}

	/**
	 * Clears all the breadCrumbs.  The old breadcrumbs
	 * are saved in case the back button is clicked.
	 */
	public void clearItems() {
		if (breadCrumbs.size() > 0) {
			breadCrumbs.clear();
		}
	}

	/**
	 * Adds an item at the given index and updates the link text with the given item.
	 * @param item	The item to add.
	 * @param index The position to add the new item.
	 */
	public void createItem(BreadCrumbItem item, int index) {
		breadCrumbs.add(index, item);
		updateLinkText();
	}
	
	
	private static final String AOPEN = "<a href=\"X\">";
	private static final String ACLOSE = "</a>";
	private static final String SPC = " ";
	
	private void updateLinkText() {
		StringBuffer buffer = new StringBuffer();
		StringBuffer notags = new StringBuffer(SPC);
		final String SEP = "  " + separatorChar + "  ";
		for (int i = 0; i < breadCrumbs.size(); i++) {
			String href = "" + i;
			BreadCrumbItem item = (BreadCrumbItem)breadCrumbs.get(i);
			if (buffer.length() > 0) {
				buffer.append(SEP);
				notags.append(SEP);
			}
			if (i < (breadCrumbs.size()-1)) {
				String anchor = AOPEN.replaceFirst("X", href);
				buffer.append(SPC + anchor + item.getText() + ACLOSE + SPC);
				notags.append(SPC + item.getText() + SPC);
			} else {
				buffer.append(SPC + item.getText());
				notags.append(SPC + item.getText());
			}
		}
		link.setData(breadCrumbs);
		link.setText(buffer.toString());
		
		if (link.getFont() != null) {
			Dimension d = FigureUtilities.getStringExtents(notags.toString(), link.getFont());
			link.setSize(d.width, d.height);
		}
	}
	
	/**
	 * Sets the layout width.
	 * @param widthHint
	 */
	public void setLayoutWidth(int widthHint) {
		breadCrumbGridData.widthHint = widthHint;
		breadCrumbGridData.minimumWidth = widthHint;
		groupGridData.widthHint = widthHint;
		groupGridData.minimumWidth = widthHint;
		group.setSize(widthHint, group.getSize().y);
	}

	/////////////////////////////////
	// Getter/Setter methods
	/////////////////////////////////
	
	/**
	 * Gets the {@link Group} control.
	 * @return Group object which contains the link which has the breadcrumb text.
	 */
	public Group getGroup() {
		return group;
	}
	
	/**
	 * Returns the {@link Toolbar} control.
	 * @return ToolBar
	 */
	public ToolBar getToolBar() {
		return toolbar;
	}

	/**
	 * Gets the {@link Link} control.
	 * @return the Link object which has the breadcrumb text
	 */
	public Link getLink() {
		return link;
	}

	/**
	 * Gets the number of breadcrumb items.
	 * @return
	 */
	public int getItemCount() {
		return breadCrumbs.size();
	}
	
	/**
	 * Sets the separator character to put between breadCrumbs.
	 * @param separatorChar
	 */
	public void setSeparatorChar(char separatorChar) {
		this.separatorChar = separatorChar;
	}

	/**
	 * Sets the title for the group box.
	 * @param title the title
	 */
	public void setTitle(String title) {
		group.setText((title == null ? "" : title));
	}
	
	/**
	 * Sets the text for the link.  Not recommended - a better way is to add items.
	 * @param linkText
	 */
	public void setLinkText(String linkText) {
		link.setText((linkText == null ? "" : linkText));
	}
	
	/**
	 * Sets the foreground color of the breadcrumb links.
	 * @param color
	 */
	public void setLinkForeground(Color color) {
		link.setForeground(color);
	}
	
	/**
	 * Sets the background color of the breadcrumb links.
	 * @param color
	 */
	public void setLinkBackground(Color color) {
		link.setBackground(color);
	}
	
	/**
	 * Sets the foreground color of the Group control.
	 * @param color
	 */
	public void setGroupForeground(Color color) {
		super.setForeground(color);
		group.setForeground(color);
	}
	
	/**
	 * Sets the background color of the Group control.
	 * @param color
	 */
	public void setGroupBackground(Color color) {
		group.setBackground(color);
	}
	
}
