package org.eclipse.gef.internal.resources;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */

import java.util.*;

public class ResourceRegistry {

List in = new ArrayList();
List out = new ArrayList();

protected void checkin(ResourceRecord record){
	record.decrementUsers();
}

protected Object checkout(ResourceRecord record){
	record.incrementUsers();
	return record.getResource();
}

}