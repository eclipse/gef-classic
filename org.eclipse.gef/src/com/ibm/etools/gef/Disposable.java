package com.ibm.etools.gef;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

/**
 * Marks objects that should be disposed of when no longer in use.
 */
public interface Disposable {

/**
 * Performs and necessary cleanup.
 */
public void dispose();

}