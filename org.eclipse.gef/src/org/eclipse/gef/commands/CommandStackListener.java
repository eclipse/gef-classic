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
 * This is the interface used to listen to a {@link CommandStack}.
 */ 
public interface CommandStackListener
{
  public static final String copyright = "(c) Copyright IBM Corporation 2002.";  //$NON-NLS-1$

  /**
   * This is called with the {@link CommandStack}'s state has changed.
   */
  void commandStackChanged(EventObject event);
}
