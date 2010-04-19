package org.eclipse.zest.dot;

import org.eclipse.zest.core.widgets.*;
import org.eclipse.swt.SWT;

public class DotTemplate
{
  protected static String nl;
  public static synchronized DotTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    DotTemplate result = new DotTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "";
  protected final String TEXT_2 = " ";
  protected final String TEXT_3 = "{" + NL + "" + NL + "\t/* Global settings */" + NL + "\tnode[shape=box] //more like the Zest default node look" + NL + "\t" + NL + "\t/* Nodes */" + NL + "\t";
  protected final String TEXT_4 = " " + NL + "\t";
  protected final String TEXT_5 = "[label=\"";
  protected final String TEXT_6 = "\"];";
  protected final String TEXT_7 = NL + "\t" + NL + "\t/* Edges */" + NL + "\t";
  protected final String TEXT_8 = " " + NL + "\t";
  protected final String TEXT_9 = " " + NL + "\t";
  protected final String TEXT_10 = " ";
  protected final String TEXT_11 = " ";
  protected final String TEXT_12 = "[style=";
  protected final String TEXT_13 = " label=\"";
  protected final String TEXT_14 = "\"];" + NL + "\t";
  protected final String TEXT_15 = NL + "}";

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    /*******************************************************************************
 * Copyright (c) 2009 Fabian Steeg. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * <p/>
 * Contributors: Fabian Steeg - initial API and implementation; see bug 277380
 *******************************************************************************/
     Graph graph = (Graph) argument; 
     boolean digraph = graph.getConnectionStyle()==ZestStyles.CONNECTIONS_DIRECTED;
    stringBuffer.append(TEXT_1);
    stringBuffer.append( digraph ? "digraph" : "graph" );
    stringBuffer.append(TEXT_2);
    stringBuffer.append(new DotExport(graph).name());
    stringBuffer.append(TEXT_3);
     for(Object nodeObject : graph.getNodes()){ GraphNode node = (GraphNode) nodeObject; 
    stringBuffer.append(TEXT_4);
    stringBuffer.append(node.hashCode());
    stringBuffer.append(TEXT_5);
    stringBuffer.append(node.getText());
    stringBuffer.append(TEXT_6);
     
	}
    stringBuffer.append(TEXT_7);
     for(Object edgeObject : graph.getConnections()){ GraphConnection edge = (GraphConnection) edgeObject; 
    stringBuffer.append(TEXT_8);
    boolean dashed = edge.getLineStyle() == SWT.LINE_DASH; boolean dotted = edge.getLineStyle() == SWT.LINE_DOT;
    stringBuffer.append(TEXT_9);
    stringBuffer.append(edge.getSource().hashCode());
    stringBuffer.append(TEXT_10);
    stringBuffer.append( digraph ? "->" : "--" );
    stringBuffer.append(TEXT_11);
    stringBuffer.append(edge.getDestination().hashCode());
    stringBuffer.append(TEXT_12);
    stringBuffer.append(dashed?"dashed":dotted?"dotted":"solid");
    stringBuffer.append(TEXT_13);
    stringBuffer.append(edge.getText());
    stringBuffer.append(TEXT_14);
     }
    stringBuffer.append(TEXT_15);
    return stringBuffer.toString();
  }
}
