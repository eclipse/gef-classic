package org.eclipse.gef.ui.palette;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.palette.PaletteEntry;

public class EntryEditPart
	extends PaletteEditPart
{
private static final Color COLOR_ENTRY_SELECTED = ColorConstants.button;

private static final Border BORDER_LABEL_MARGIN = new MarginBorder(new Insets(1,1,1,2));

private ToggleButton toolTipButton;

public EntryEditPart(PaletteEntry paletteEntry) {
	super(paletteEntry);
}

public void activate() {
	super.activate();
	
	final Clickable button = (Clickable)getFigure();
//  	final DetailedLabelFigure buttonLabel = (DetailedLabelFigure)button.getChildren().get(0);	
//	final Control ctrl = getViewer().getControl();
	
//	button.addMouseMotionListener(new MouseMotionListener.Stub(){
//		private EditPartTipHelper tipHelper;
//		
//		public void mouseEntered(MouseEvent e){
//			/* Only show tooltip if the buttton's text is truncated */
//			if(buttonLabel.isTextTruncated()){
//				tipHelper = new EditPartTipHelper(ctrl);
//				tipHelper.setBackgroundColor(button.getParent().getBackgroundColor());
//				Point buttonLoc = ((Figure)(button)).getLocation();
//				org.eclipse.swt.graphics.Point absolute;
//				absolute = ctrl.toDisplay(new org.eclipse.swt.graphics.Point(buttonLoc.x, 
//														    buttonLoc.y));
//				toolTipButton.getModel().setMouseOver(true);
//				toolTipButton.getModel().setSelected(button.isSelected());
//				tipHelper.displayToolTipAt(toolTipButton, absolute.x, absolute.y);
//			}
//		}
//	});
	button.addFocusListener(new FocusListener.Stub() {
		public void focusGained(FocusEvent fe) {
			getRoot().getViewer().select(EntryEditPart.this);
		}
	});
}

protected AccessibleEditPart createAccessible() {
	return new AccessibleGraphicalEditPart (){
		public void getDescription(AccessibleEvent e) {
			e.result = getPaletteEntry().getDescription();
		}

		public void getName(AccessibleEvent e) {
			e.result = getPaletteEntry().getLabel();
		}

		public void getRole(AccessibleControlEvent e) {
			e.detail = ACC.ROLE_PUSHBUTTON;
		}

		public void getState(AccessibleControlEvent e) {
			e.detail = getButtonModel().isSelected()
				? ACC.STATE_SELECTED
				: ACC.STATE_SELECTABLE;
		}
	};
}

public IFigure createFigure() {
	final Clickable button = new ToggleButton(createLabel());
	button.setBorder(new ButtonBorder(ButtonBorder.SCHEMES.TOOLBAR));
	button.setRolloverEnabled(true);
	button.setBackgroundColor(COLOR_ENTRY_SELECTED);
	button.setOpaque(false);

	button.addChangeListener(new ChangeListener(){
		public void handleStateChanged(ChangeEvent e) {
			if (e.getPropertyName().equals("selected")) { //$NON-NLS-1$
				button.setOpaque(button.isSelected());
				button.setForegroundColor(
					button.isSelected() ? ColorConstants.black : null);
				if (button.isSelected())
					getPaletteViewer().setSelection(getPaletteEntry());
			}
		}
	});
	return button;
}

protected Figure createLabel() {
	return new DetailedLabelFigure();
}

private void createToolTipButton() {
	final ToggleButton tipButton = ((ToggleButton)createFigure());
	setToolTipButton(tipButton);
		
	/* When toolTipButton is clicked, click on the underlying button as well */
	tipButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			((Clickable)getFigure()).doClick();
			((Clickable)getFigure()).requestFocus();
			tipButton.setSelected(((Clickable)getFigure()).isSelected());
		}
	});
}

private ButtonGroup getButtonGroup() {
	PaletteViewer pv = (PaletteViewer)getViewer();
	return pv.getButtonGroup();
}

private ButtonModel getButtonModel() {
	Clickable c = (Clickable)getFigure();
	return c.getModel();
}

private PaletteViewer getPaletteViewer() {
	return (PaletteViewer)getViewer();
}

private ToggleButton getToolTipButton() {
	if (toolTipButton == null)
		createToolTipButton();	
	return toolTipButton;
}	

protected void refreshVisuals() {
	Clickable button = (Clickable)getFigure();
	PaletteEntry entry = getPaletteEntry();

	DetailedLabelFigure fig = (DetailedLabelFigure)(button.getChildren().get(0));
	fig.setName(entry.getLabel());
	fig.setDescription(entry.getDescription());
	boolean large = getPreferenceSource().useLargeIconsCurrently();
	Image icon;
	if (large) {
		icon = entry.getLargeIcon();
	} else {
		icon = entry.getSmallIcon();
	}
	fig.setImage(icon);
	fig.setLayoutMode(getPreferenceSource().getLayoutSetting());
	
//	Label toolTipButtonLabel = (Label)(getToolTipButton().getChildren().get(0));
//	toolTipButtonLabel.setText(entryModel.getLabel());
//	toolTipButtonLabel.setIcon(entryModel.getSmallIcon());

	super.refreshVisuals();	
}

protected void register() {
	super.register();
	getButtonGroup().add(getButtonModel());
	if (getPaletteEntry().isDefault())
		getButtonGroup().setDefault(getButtonModel());
}

public void select() {
	getButtonModel().setSelected(true);
}

public void setSelected(int value) {
	super.setSelected(value);
	if (value == SELECTED_PRIMARY)
		getFigure().requestFocus();
}

private void setToolTipButton(ToggleButton button) {
	toolTipButton = button;
}

protected void unregister() {
	getButtonGroup().remove(getButtonModel());
	super.unregister();
}

}