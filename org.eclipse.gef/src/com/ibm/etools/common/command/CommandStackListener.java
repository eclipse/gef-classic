/**
 * <copyright> 
 *
 * (C) COPYRIGHT International Business Machines Corporation 2000-2002.
 *
 * </copyright>
 *
 * plugins/com.ibm.etools.common.command/src/org/eclipse/emf/common/command/CommandStackListener.java, emf.common, org.eclipse.dev, 20020529_1802
 * @version 1.1 5/29/02
 */
package com.ibm.etools.common.command;


import java.util.EventObject;


/**
 * This is the interface used to listen to a {@link CommandStack}.
 */ 
public interface CommandStackListener
{
  public static final String copyright = "(c) Copyright IBM Corporation 2002.";

  /**
   * This is called with the {@link CommandStack}'s state has changed.
   */
  void commandStackChanged(EventObject event);
}
