/**
 * <copyright> 
 *
 * (C) COPYRIGHT International Business Machines Corporation 2000-2002.
 *
 * </copyright>
 */
package org.eclipse.gef.commands;

import java.util.EventObject;

/**
 * A CommandStackListener is notified whenever the {@link CommandStack}'s state has
 * changed.
 */
public interface CommandStackListener {

/**
 * Called when the {@link CommandStack}'s state has changed.
 * @param event the event
 */
void commandStackChanged(EventObject event);

}
