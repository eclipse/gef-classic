/*******************************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.logicdesigner.rulers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.rulers.RulerChangeListener;
import org.eclipse.gef.rulers.RulerProvider;

import org.eclipse.gef.examples.logicdesigner.model.LogicGuide;
import org.eclipse.gef.examples.logicdesigner.model.LogicRuler;
import org.eclipse.gef.examples.logicdesigner.model.commands.*;

/**
 * @author Pratik Shah
 */
public class LogicRulerProvider
	extends RulerProvider
{
	
private LogicRuler ruler;
/*
 * @TODO:Pratik   when can you remove these two listeners?  right now, they are never
 * removed.  but that might be okay since you want to listen to them for as long as they
 * exist.
 */
private PropertyChangeListener rulerListener = new PropertyChangeListener() {
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(LogicRuler.PROPERTY_CHILDREN)) {
			LogicGuide guide = (LogicGuide)evt.getNewValue();
			if (getGuides().contains(guide)) {
				guide.addPropertyChangeListener(guideListener);
			} else {
				guide.removePropertyChangeListener(guideListener);
			}
			for (int i = 0; i < listeners.size(); i++) {
				((RulerChangeListener)listeners.get(i))
						.notifyGuideReparented(guide);
			}
		} else {
			for (int i = 0; i < listeners.size(); i++) {
				((RulerChangeListener)listeners.get(i))
						.notifyUnitsChanged(ruler.getUnit());
			}
		}
	}
};
private PropertyChangeListener guideListener = new PropertyChangeListener() {
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(LogicGuide.PROPERTY_CHILDREN)) {
			for (int i = 0; i < listeners.size(); i++) {
				((RulerChangeListener)listeners.get(i))
						.notifyPartAttachmentChanged(evt.getNewValue(), evt.getSource());
			}
		} else {
			for (int i = 0; i < listeners.size(); i++) {
				((RulerChangeListener)listeners.get(i))
						.notifyGuideMoved(evt.getSource());
			}
		}
	}
};

public LogicRulerProvider(LogicRuler ruler) {
	this.ruler = ruler;
	this.ruler.addPropertyChangeListener(rulerListener);
	List guides = getGuides();
	for (int i = 0; i < guides.size(); i++) {
		((LogicGuide)guides.get(i)).addPropertyChangeListener(guideListener);
	}
}

public Command getCreateGuideCommand(int position) {
	return new CreateGuideCommand(ruler, position);
}

public Command getDeleteGuideCommand(Object guide) {
	return new DeleteGuideCommand((LogicGuide)guide, ruler);
}

public Command getMoveGuideCommand(Object guide, int pDelta) {
	return new MoveGuideCommand((LogicGuide)guide, pDelta);
}

public int[] getGuidePositions() {
	List guides = getGuides();
	int[] result = new int[guides.size()];
	for (int i = 0; i < guides.size(); i++) {
		result[i] = ((LogicGuide)guides.get(i)).getPosition();
	}
	return result;
}

public Object getRuler() {
	return ruler;
}

public int getUnit() {
	return ruler.getUnit();
}

public void setUnit(int newUnit) {
	ruler.setUnit(newUnit);
}

public int getGuidePosition(Object guide) {
	return ((LogicGuide)guide).getPosition();
}

public List getGuides() {
	return ruler.getGuides();
}

}