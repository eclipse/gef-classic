package com.ibm.etools.gef.ui.palette;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import com.ibm.etools.draw2d.*;
import com.ibm.etools.draw2d.FocusEvent;
import com.ibm.etools.gef.palette.*;
import org.eclipse.swt.graphics.Color;
import com.ibm.etools.draw2d.geometry.*;
import org.eclipse.swt.widgets.Control;

import com.ibm.etools.gef.EditPart;
import com.ibm.etools.gef.EditPartListener;
import com.ibm.etools.gef.EditPartViewer;

public class EntryEditPart
	extends PaletteEditPart
{
private static final Color COLOR_ENTRY_SELECTED = ColorConstants.button;

private static final Border BORDER_LABEL_MARGIN = new MarginBorder(new Insets(1,1,1,2));

private ToggleButton toolTipButton;

public EntryEditPart(PaletteEntry paletteEntry){
	setModel(paletteEntry);
}

public void activate(){
	super.activate();
	
	final Clickable button = (Clickable)getFigure();
  	final Label buttonLabel = (Label)button.getChildren().get(0);	
	final Control ctrl = getViewer().getControl();
	
	button.addMouseMotionListener(new MouseMotionListener.Stub(){
		private EditPartTipHelper tipHelper;
		
		public void mouseEntered(MouseEvent e){
			/* Only show tooltip if the buttton's text is truncated */
			if(buttonLabel.isTextTruncated()){
				tipHelper = new EditPartTipHelper(ctrl);
				tipHelper.setBackgroundColor(button.getParent().getBackgroundColor());
				Point buttonLoc = ((Figure)(button)).getLocation();
				org.eclipse.swt.graphics.Point absolute;
				absolute = ctrl.toDisplay(new org.eclipse.swt.graphics.Point(buttonLoc.x, 
														    buttonLoc.y));
				toolTipButton.getModel().setMouseOver(true);
				toolTipButton.getModel().setSelected(button.isSelected());
				tipHelper.displayToolTipAt(toolTipButton, absolute.x, absolute.y);
			}
		}
	});
	button.addFocusListener(new FocusListener.Stub() {
		public void focusGained(FocusEvent fe) {
			getRoot().getViewer().select(EntryEditPart.this);
		}
	});
}

public IFigure createFigure(){
	final Clickable button = new ToggleButton(createLabel()) {
		protected void paintFigure(Graphics graphics) {
			if (isOpaque())
				graphics.fillRectangle(getClientArea());
		}
	};
	button.setBorder(new PaletteEntryBorder());
	button.setRolloverEnabled(true);
	button.setBackgroundColor(COLOR_ENTRY_SELECTED);
	button.setOpaque(false);

	button.addChangeListener(new ChangeListener(){
		public void handleStateChanged(ChangeEvent e){
			if (e.getPropertyName().equals("selected")){//$NON-NLS-1$
				button.setOpaque(button.isSelected());
				button.setForegroundColor(
					button.isSelected() ?
						ColorConstants.black :
						null
				);
				if (button.isSelected())
					getPaletteViewer().setSelection(getPaletteEntry());
			}
		}
	});
	return button;
}

protected Figure createLabel(){
	Label label = new Label();
	label.setLabelAlignment(Label.LEFT);
	label.setBorder(BORDER_LABEL_MARGIN);
	return label;
}

private void createToolTipButton(){
	final ToggleButton tipButton = ((ToggleButton)createFigure());
	setToolTipButton(tipButton);
		
	/* When toolTipButton is clicked, click on the underlying button as well */
	tipButton.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e){
			((Clickable)getFigure()).doClick();
			((Clickable)getFigure()).requestFocus();
			tipButton.setSelected(((Clickable)getFigure()).isSelected());
		}
	});
}

private ButtonGroup getButtonGroup(){
	PaletteViewer pv = (PaletteViewer)getViewer();
	return pv.getButtonGroup();
}

private ButtonModel getButtonModel(){
	Clickable c = (Clickable)getFigure();
	return c.getModel();
}

private PaletteEntry getPaletteEntry(){
	return (PaletteEntry)getModel();
}

private PaletteViewer getPaletteViewer(){
	return (PaletteViewer)getViewer();
}

private ToggleButton getToolTipButton(){
	if (toolTipButton == null)
		createToolTipButton();	
	return toolTipButton;
}	

protected void refreshVisuals(){
	PaletteEntry entryModel = (PaletteEntry)getModel();
	Clickable button = (Clickable)getFigure();

	Label buttonLabel = (Label)(button.getChildren().get(0));
	Label toolTipButtonLabel = (Label)(getToolTipButton().getChildren().get(0));
	
	buttonLabel.setText(entryModel.getLabel());
	buttonLabel.setIcon(entryModel.getSmallIcon());
	
	toolTipButtonLabel.setText(entryModel.getLabel());
	toolTipButtonLabel.setIcon(entryModel.getSmallIcon());
	
	String desc = entryModel.getDescription();
	if(desc != null){
		if(!desc.equals("") && !desc.equals(entryModel.getLabel())){ //$NON-NLS-1$
			button.setToolTip(new Label(desc));
			getToolTipButton().setToolTip(new Label(desc));
		}
	}	
}

protected void register(){
	super.register();
	getButtonGroup().add(getButtonModel());
	if(getPaletteEntry().isDefault())
		getButtonGroup().setDefault(getButtonModel());
}

public void select(){
	getButtonModel().setSelected(true);
}

public void setSelected(int value){
	super.setSelected(value);
	if (value == SELECTED_PRIMARY)
		getFigure().requestFocus();
}

private void setToolTipButton(ToggleButton button){
	toolTipButton = button;
}

protected void unregister(){
	getButtonGroup().remove(getButtonModel());
	super.unregister();
}

}