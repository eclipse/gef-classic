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
package org.eclipse.mylar.zest.layouts.exampleUses;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import org.eclipse.mylar.zest.layouts.InvalidLayoutConfiguration;
import org.eclipse.mylar.zest.layouts.LayoutAlgorithm;
import org.eclipse.mylar.zest.layouts.LayoutEntity;
import org.eclipse.mylar.zest.layouts.LayoutRelationship;
import org.eclipse.mylar.zest.layouts.LayoutStyles;
import org.eclipse.mylar.zest.layouts.algorithms.FadeLayoutAlgorithm;
import org.eclipse.mylar.zest.layouts.algorithms.GridLayoutAlgorithm;
import org.eclipse.mylar.zest.layouts.algorithms.HorizontalLayoutAlgorithm;
import org.eclipse.mylar.zest.layouts.algorithms.HorizontalTreeLayoutAlgorithm;
import org.eclipse.mylar.zest.layouts.algorithms.RadialLayoutAlgorithm;
import org.eclipse.mylar.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.eclipse.mylar.zest.layouts.algorithms.TreeLayoutAlgorithm;
import org.eclipse.mylar.zest.layouts.algorithms.VerticalLayoutAlgorithm;
import org.eclipse.mylar.zest.layouts.exampleStructures.SimpleNode;
import org.eclipse.mylar.zest.layouts.exampleStructures.SimpleRelationship;
import org.eclipse.mylar.zest.layouts.progress.ProgressEvent;
import org.eclipse.mylar.zest.layouts.progress.ProgressListener;


/**
 * @author Rob Lintern
 *
 * A simple example of using layout algorithms with a Swing application.
 */
public class SimpleSwingExample {	
    private static final Color NODE_NORMAL_COLOR = new Color (225, 225, 255);
    private static final Color NODE_SELECTED_COLOR = new Color(255, 125, 125);
    //private static final Color NODE_ADJACENT_COLOR = new Color (255, 200, 125); 
    private static final Color BORDER_NORMAL_COLOR = new Color(0, 0, 0);
    private static final Color BORDER_SELECTED_COLOR = new Color (255, 0, 0);
    //private static final Color BORDER_ADJACENT_COLOR = new Color (255, 128, 0);   
    private static final Stroke BORDER_NORMAL_STROKE = new BasicStroke (1.0f);
    private static final Stroke BORDER_SELECTED_STROKE = new BasicStroke (2.0f);
    private static final Color RELATIONSHIP_NORMAL_COLOR = Color.BLUE;
    //private static final Color RELATIONSHIP_HIGHLIGHT_COLOR = new Color (255, 200, 125); 
    
    public static final FadeLayoutAlgorithm FADE = new FadeLayoutAlgorithm ( LayoutStyles.NONE );
    public static final SpringLayoutAlgorithm SPRING = new SpringLayoutAlgorithm ( LayoutStyles.NONE );
    public static final TreeLayoutAlgorithm TREE_VERT = new TreeLayoutAlgorithm ( LayoutStyles.NONE );
    public static final HorizontalTreeLayoutAlgorithm TREE_HORIZ = new HorizontalTreeLayoutAlgorithm ( LayoutStyles.NONE );
    public static final RadialLayoutAlgorithm RADIAL = new RadialLayoutAlgorithm ( LayoutStyles.NONE );
    public static final GridLayoutAlgorithm GRID = new GridLayoutAlgorithm ( LayoutStyles.NONE );
    public static final HorizontalLayoutAlgorithm HORIZ = new HorizontalLayoutAlgorithm ( LayoutStyles.NONE );
    public static final VerticalLayoutAlgorithm VERT = new VerticalLayoutAlgorithm ( LayoutStyles.NONE );       
    
    private List algorithms = new ArrayList();
    private List algorithmNames = new ArrayList();
    
	
    private static final int INITIAL_PANEL_WIDTH = 700;
	private static final int INITIAL_PANEL_HEIGHT = 500;
	
	private static final boolean RENDER_HIGH_QUALITY = true;
	
	private static final double INITIAL_NODE_WIDTH = 20;
	private static final double INITIAL_NODE_HEIGHT = 20;
    private static final int ARROW_HALF_WIDTH = 4;
    private static final int ARROW_HALF_HEIGHT = 6;
    private static final Shape ARROW_SHAPE = new Polygon (new int [] {-ARROW_HALF_HEIGHT, ARROW_HALF_HEIGHT, -ARROW_HALF_HEIGHT}, new int []{-ARROW_HALF_WIDTH, 0, ARROW_HALF_WIDTH}, 3);
    private static final Stroke ARROW_BORDER_STROKE = new BasicStroke (0.5f);
    private static final Color ARROW_HEAD_FILL_COLOR = new Color (125, 255, 125);
    private static final Color ARROW_HEAD_BORDER_COLOR = Color.BLACK;
	
	private long updateGUICount = 0;
	
	private JFrame mainFrame;
	private JPanel mainPanel;
	private List entities;
	private List relationships;
	private JToolBar toolBar;
	private JLabel lblProgress;
    private JToggleButton btnContinuous;
    private JToggleButton btnAsynchronous;
    private JButton btnStop;
	
	private LayoutAlgorithm currentLayoutAlgorithm;
    private String currentLayoutAlgorithmName;
    protected LayoutEntity selectedEntity;
    protected Point mouseDownPoint;
    protected Point selectedEntityPositionAtMouseDown;
    private long idCount;
	
    
	public SimpleSwingExample () {
        SPRING.setIterations(1000);
        // initialize layouts
        TREE_VERT.setComparator(new Comparator () {        
            public int compare(Object o1, Object o2) {
                if (o1 instanceof Comparable && o2 instanceof Comparable) {
                    return ((Comparable)o1).compareTo(o2);
                }
                return 0;
            }
        
        });
        GRID.setRowPadding(20);
        addAlgorithm (FADE, "FADE", false);
        addAlgorithm (SPRING, "Spring", false);
        addAlgorithm (TREE_VERT, "Tree-V", false);
        addAlgorithm (TREE_HORIZ, "Tree-H", false);
        addAlgorithm (RADIAL, "Radial", false);
        addAlgorithm (GRID, "Grid", false);
        addAlgorithm (HORIZ, "Horiz", false);
        addAlgorithm (VERT, "Vert", false);
	}
    
    protected void addAlgorithm (LayoutAlgorithm algorithm, String name, boolean animate) {
        algorithms.add(algorithm);
        algorithmNames.add(name);
        //algorithmAnimates.add(Boolean.valueOf(animate));
    }
    
    public void start() {
        //try {
           // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        //} catch (Exception e) {
            //e.printStackTrace();
        //}
        mainFrame = new JFrame ("Simple Swing Layout Example");
        toolBar = new JToolBar ();
        mainFrame.getContentPane().setLayout(new BorderLayout());
        mainFrame.getContentPane().add(toolBar, BorderLayout.NORTH);
        lblProgress = new JLabel ("Progress: ");
        mainFrame.getContentPane().add(lblProgress, BorderLayout.SOUTH);
        
        
        for (int i = 0; i < algorithms.size(); i++) {
            final LayoutAlgorithm algorithm = (LayoutAlgorithm) algorithms.get(i);
            final String algorithmName = (String) algorithmNames.get(i);
            //final boolean algorithmAnimate = ((Boolean)algorithmAnimates.get(i)).booleanValue();
            JButton algorithmButton = new JButton (algorithmName);
            algorithmButton.addActionListener(new ActionListener () {
                public void actionPerformed(ActionEvent e) {
                    currentLayoutAlgorithm = algorithm; 
                    currentLayoutAlgorithmName = algorithmName;
                    algorithm.setEntityAspectRatio((double)mainPanel.getWidth()/(double)mainPanel.getHeight());
                    //animate = algorithmAnimate;
                    performLayout();
                }
            });
            toolBar.add(algorithmButton);
        }
        createMainPanel();
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                stop();
                System.exit(0);
            }
        });
        
        
        btnContinuous = new JToggleButton ("continuous", false);
        btnAsynchronous = new JToggleButton ("asynchronous", false);
        
        toolBar.add(btnContinuous);
        toolBar.add(btnAsynchronous);
        
        btnStop = new JButton("Stop");
        btnStop.addActionListener(new ActionListener() {        
            public void actionPerformed(ActionEvent e) {
                stop();
            }        
        });
        toolBar.add(btnStop);
        
        JButton btnCreateGraph = new JButton ("New graph");
        btnCreateGraph.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                stop();
                createGraph(true);
            }
        });
        toolBar.add(btnCreateGraph);
        JButton btnCreateTree = new JButton ("New tree");
        btnCreateTree.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                stop();
                createGraph(false);
            }
        });
        toolBar.add(btnCreateTree);
        
        createGraph(false);

        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        mainFrame.setLocation((int) (screenSize.getWidth() - INITIAL_PANEL_WIDTH)/2, (int) (screenSize.getHeight() - INITIAL_PANEL_HEIGHT)/2);
        mainFrame.pack();
        mainFrame.setVisible(true);
        mainFrame.repaint();
        
    }
    
    private void stop() {
        if (currentLayoutAlgorithm != null && currentLayoutAlgorithm.isRunning()) {
            currentLayoutAlgorithm.stop();
        }
    }
	

    private void performLayout () {
        stop();
	    final Cursor cursor = mainFrame.getCursor();
	    updateGUICount = 0;
	    placeRandomly();
        final boolean continuous = btnContinuous.isSelected();
        final boolean asynchronous = btnAsynchronous.isSelected();
	    ProgressListener progressListener = new ProgressListener () {
            public void progressUpdated(final ProgressEvent e) {
                //if (asynchronous) {
                    updateGUI();
                //}
                lblProgress.setText("Progress: " + e.getStepsCompleted() + " of " + e.getTotalNumberOfSteps() + " completed ...");
                lblProgress.paintImmediately(0,0,lblProgress.getWidth(), lblProgress.getHeight());
            }

			public void progressStarted(ProgressEvent e) {
                if (!asynchronous) {
                    mainFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                }
                lblProgress.setText("Layout started ...");
                lblProgress.paintImmediately(0,0,lblProgress.getWidth(), lblProgress.getHeight());
			}

			public void progressEnded(ProgressEvent e) {
                lblProgress.setText("Layout completed ...");
                lblProgress.paintImmediately(0,0,lblProgress.getWidth(), lblProgress.getHeight());
                currentLayoutAlgorithm.removeProgressListener(this);
                if (!asynchronous) {
                    mainFrame.setCursor(cursor);
                }
			}
        };
		currentLayoutAlgorithm.addProgressListener(progressListener);
		try {
            LayoutEntity [] layoutEntities = new LayoutEntity [entities.size()];
            entities.toArray(layoutEntities);
            LayoutRelationship [] layoutRelationships = new LayoutRelationship [relationships.size()];
            relationships.toArray(layoutRelationships);
			currentLayoutAlgorithm.applyLayout(layoutEntities, layoutRelationships, 0, 0, mainPanel.getWidth(), mainPanel.getHeight(), asynchronous, continuous);
            //if (!animate) {
                updateGUI();
            //}
		} catch (InvalidLayoutConfiguration e) {
            JOptionPane.showMessageDialog(mainFrame, "Not a valid layout configuration:\nlayout='" + currentLayoutAlgorithmName + "', continuous='" + continuous + "', asynchronous='" + asynchronous +"'");
        } catch (StackOverflowError e) {
            e.printStackTrace();
		} finally {
        }
	}
	
	private void createMainPanel () {
		mainPanel = new JPanel() {
			private static final long serialVersionUID = 1;
		    // instead of letting Swing paint all the JPanels for us, we will just do our own painting here
            protected void paintChildren(Graphics g) {
                if (g instanceof Graphics2D && RENDER_HIGH_QUALITY) {
					((Graphics2D)g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
					((Graphics2D)g).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
					((Graphics2D)g).setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON); 	
                }
                
                // paint the nodes
				for (Iterator iter = entities.iterator(); iter.hasNext();) {
                    LayoutEntity entity = (SimpleNode) iter.next();
                    boolean isSelected = selectedEntity != null && selectedEntity.equals(entity);
                    g.setColor(isSelected ? NODE_SELECTED_COLOR : NODE_NORMAL_COLOR);
                    g.fillOval((int)entity.getXInLayout(), (int)entity.getYInLayout(), (int)entity.getWidthInLayout(), (int)entity.getHeightInLayout());
                    g.setColor( isSelected ? BORDER_SELECTED_COLOR : BORDER_NORMAL_COLOR);
                    String name = entity.toString();
                    Rectangle2D nameBounds = g.getFontMetrics().getStringBounds(name, g);
       				g.drawString(name, (int) (entity.getXInLayout() + entity.getWidthInLayout()/2.0 - nameBounds.getWidth()/2.0), (int)(entity.getYInLayout() - nameBounds.getHeight() - nameBounds.getY()));
                    if (g instanceof Graphics2D) {
                        ((Graphics2D)g).setStroke(isSelected ? BORDER_SELECTED_STROKE : BORDER_NORMAL_STROKE);
                    }
                    g.drawOval((int)entity.getXInLayout(), (int)entity.getYInLayout(), (int)entity.getWidthInLayout(), (int)entity.getHeightInLayout());
                 }				
				
				// paint the relationships 
				for (Iterator iter = relationships.iterator(); iter.hasNext();) {
					LayoutRelationship rel = (LayoutRelationship) iter.next();
					LayoutEntity src = rel.getSourceInLayout();
					LayoutEntity dest = rel.getDestinationInLayout();
                    double srcX = src.getXInLayout() + src.getWidthInLayout()/2.0 ;
                    double srcY = src.getYInLayout() + src.getHeightInLayout()/2.0;
                    double destX = dest.getXInLayout() + dest.getWidthInLayout()/2.0;
                    double destY = dest.getYInLayout() + dest.getHeightInLayout()/2.0;
                    
                    // draw an arrow in the middle of the line
                    double dx = destX - srcX;
                    double dy = destY - srcY;
                    // make sure dx is not zero or too small
                    if (dx < 0.01 && dx > -0.01) {
                        if (dx > 0) {
                            dx = 0.01;
                        } else if (dx < 0) {
                            dx = -0.01;
                        }
                    }
                    double theta = Math.atan2(dy, dx);
                    
                    double reverseTheta = theta > 0.0d ? theta - Math.PI : theta + Math.PI;
                    Point2D.Double srcIntersectionP = getEllipseIntersectionPoint(theta, src.getWidthInLayout(), src.getHeightInLayout());
                    Point2D.Double destIntersectionP = getEllipseIntersectionPoint(reverseTheta, dest.getWidthInLayout(), dest.getHeightInLayout());
                    g.setColor(RELATIONSHIP_NORMAL_COLOR);
                    g.drawLine((int)(srcX + srcIntersectionP.getX()), (int)(srcY + srcIntersectionP.getY()), (int)(destX + destIntersectionP.getX()), (int)(destY + destIntersectionP.getY())); 
                    
                    AffineTransform tx = new AffineTransform();
                    double arrX = srcX + (dx)/2.0;
                    double arrY = srcY + (dy)/2.0;
                    tx.translate(arrX, arrY);
                    tx.rotate(theta);
                    Shape arrowTx = tx.createTransformedShape(ARROW_SHAPE);
                    if (g instanceof Graphics2D) {
                        g.setColor(ARROW_HEAD_FILL_COLOR);
                        ((Graphics2D)g).fill(arrowTx);
                        ((Graphics2D)g).setStroke(ARROW_BORDER_STROKE);
                        g.setColor(ARROW_HEAD_BORDER_COLOR);
                        ((Graphics2D)g).draw(arrowTx);
                    }
				}
				
				
				
            }          
		};
		mainPanel.setPreferredSize(new Dimension(INITIAL_PANEL_WIDTH, INITIAL_PANEL_HEIGHT));
		mainPanel.setBackground(Color.WHITE);
		mainPanel.setLayout(null);
		mainFrame.getContentPane().add(new JScrollPane(mainPanel), BorderLayout.CENTER);
		mainPanel.addMouseListener(new MouseAdapter () {
            public void mousePressed(MouseEvent e) {
                selectedEntity = null;
                for (Iterator iter = entities.iterator(); iter.hasNext() && selectedEntity == null;) {
                    LayoutEntity entity = (LayoutEntity) iter.next();
                    double x = entity.getXInLayout();
                    double y = entity.getYInLayout();
                    double w = entity.getWidthInLayout();
                    double h = entity.getHeightInLayout();
                    Rectangle2D.Double rect = new Rectangle2D.Double (x, y, w, h);
                    if (rect.contains(e.getX(), e.getY())) {
                        selectedEntity = entity;
                    }
                }
                if (selectedEntity != null) {
                    mouseDownPoint = e.getPoint();
                    selectedEntityPositionAtMouseDown = new Point ((int)selectedEntity.getXInLayout(), (int)selectedEntity.getYInLayout());
                } else {
                    mouseDownPoint = null;
                    selectedEntityPositionAtMouseDown = null;
                }
                updateGUI();
            }

            public void mouseReleased(MouseEvent e) {
                selectedEntity = null;
                mouseDownPoint = null;
                selectedEntityPositionAtMouseDown = null;
                updateGUI();
            }
        });
		
		mainPanel.addMouseMotionListener(new MouseMotionListener () {
            public void mouseDragged(MouseEvent e) {
//                if (selectedEntity != null) {
//					//TODO: Add mouse moving
//                    //selectedEntity.setLocationInLayout(selectedEntityPositionAtMouseDown.x + dx, selectedEntityPositionAtMouseDown.y + dy);
//                    updateGUI();
//                } 
            }

            public void mouseMoved(MouseEvent e) {
            }
        });
	}
	
	
    private void createGraph(boolean addNonTreeRels) {
        entities = new ArrayList();
        relationships = new ArrayList();
        selectedEntity = null;
        
        createTreeGraph(2, 4, 2, 5, true, true, addNonTreeRels);
        //createCustomGraph();
        
        placeRandomly();
        System.out.println("num entities: " + entities.size());
        System.out.println("num relationships: " + relationships.size());
        mainPanel.repaint();
    }
    
	/**
	 * 
	 * @param maxLevels Max number of levels wanted in tree	
	 * @param maxChildren Max number of children for each node in the tree
	 * @param randomNumChildren Whether or not to pick random number of levels (from 1 to maxLevels) and 
	 * random number of children (from 1 to maxChildren)
	 */
	private void createTreeGraph (int minChildren, int maxChildren, int minLevels, int maxLevels, boolean randomNumChildren, boolean randomLevels, boolean addNonTreeRels) {
	    LayoutEntity currentParent = createSimpleNode(getNextID());
	    entities.add(currentParent);
	    createTreeGraphRecursive (currentParent, minChildren, maxChildren, minLevels, maxLevels, 1, randomNumChildren, randomLevels, addNonTreeRels);
	}
    
	private void createTreeGraphRecursive (LayoutEntity currentParentNode, int minChildren, int maxChildren, int minLevel, int maxLevel, int level, boolean randomNumChildren, boolean randomLevels, boolean addNonTreeRels) {
	    if (level > maxLevel) return;
        if (randomLevels) {
            if (level > minLevel) {
                double zeroToOne = Math.random();
                if (zeroToOne < 0.75) {
                    return;
                }
            }
        }
	    int numChildren = randomNumChildren ? Math.max (minChildren, (int) (Math.random() * maxChildren + 1)) : maxChildren;
        for (int i = 0; i < numChildren; i++) {
            LayoutEntity newNode = createSimpleNode(getNextID()); 
            entities.add(newNode);
            if (addNonTreeRels && entities.size() % 5 == 0) {
                int index = (int) (Math.random() * entities.size());
                LayoutRelationship rel = new SimpleRelationship ((LayoutEntity) entities.get(index), newNode, false);
                relationships.add(rel);
            } 
            LayoutRelationship rel = new SimpleRelationship (currentParentNode, newNode, false);
            relationships.add(rel);
            createTreeGraphRecursive(newNode, minChildren, maxChildren, minLevel, maxLevel, level + 1, randomNumChildren, randomLevels, addNonTreeRels);
        }  
	}
    /*
    private void createCustomGraph() {
        LayoutEntity _1 = createSimpleNode("1");
        LayoutEntity _2 = createSimpleNode("2");
        LayoutEntity _3 = createSimpleNode("3");
        LayoutEntity _4 = createSimpleNode("4");
//        LayoutEntity _5 = createSimpleNode("5");
//        LayoutEntity _6 = createSimpleNode("6");
        //LayoutEntity _7 = createSimpleNode("7");
        entities.add(_1);
        entities.add(_2);
        entities.add(_3);
        entities.add(_4);
//        entities.add(_5);
//        entities.add(_6);
        //entities.add(_7);
        LayoutRelationship r1 = new SimpleRelationship (_1, _2, false);
        LayoutRelationship r2 = new SimpleRelationship (_2, _3, false);
        LayoutRelationship r3 = new SimpleRelationship (_2, _4, false);
        LayoutRelationship r4 = new SimpleRelationship (_3, _4, false);
        LayoutRelationship r5 = new SimpleRelationship (_1, _4, false);
        LayoutRelationship r6 = new SimpleRelationship (_1, _3, false);
//        LayoutRelationship r7 = new SimpleRelationship (_6, _3, false);
        //LayoutRelationship r8 = new SimpleRelationship (_6, _2, false);
        relationships.add(r4);
        relationships.add(r5);
        relationships.add(r1);
        relationships.add(r2);
        relationships.add(r3);
        relationships.add(r6);
//        relationships.add(r7);
        //relationships.add(r8);
    }
    */
	
	private String getNextID () {
	    String id = "" + idCount;
	    idCount++;
	    return id;
	}
	
	/** Places nodes randomly on the screen **/
	private void placeRandomly () {
	    for (Iterator iter = entities.iterator(); iter.hasNext();) {
            SimpleNode simpleNode = (SimpleNode) iter.next();
    		double x = Math.random() * INITIAL_PANEL_WIDTH - INITIAL_NODE_WIDTH;
    		double y = Math.random() * INITIAL_PANEL_HEIGHT - INITIAL_NODE_HEIGHT;
			simpleNode.setLocationInLayout( x, y );
			simpleNode.setSizeInLayout( INITIAL_NODE_WIDTH, INITIAL_NODE_HEIGHT );
        }
	}
	

	
	/**
	 * Creates a SimpleNode
	 * @param name
	 * @return
	 */
	private SimpleNode createSimpleNode (String name) {
		SimpleNode simpleNode = new SimpleNode (name);
		return simpleNode;
	}
	
	private void updateGUI () {
		updateGUICount++;
		if (updateGUICount > 0) {
			mainPanel.paintImmediately(0, 0, mainPanel.getWidth(), mainPanel.getHeight());
		}
	}
	
    private static Point2D.Double getEllipseIntersectionPoint (double theta, double ellipseWidth, double ellipseHeight) {
        double nhalfw = ellipseWidth/2.0; // half elllipse width
        double nhalfh = ellipseHeight/2.0; // half ellipse height
        double tanTheta = Math.tan(theta);
        
        double a = nhalfw;
        double b = nhalfh;
        double x = (a*b)/Math.sqrt( Math.pow(b, 2) + Math.pow(a, 2) * Math.pow(tanTheta, 2));
        if ((theta > Math.PI/2.0 && theta < 1.5*Math.PI) || (theta < -Math.PI/2.0 && theta > -1.5*Math.PI)) {
            x = -x;
        }
        double y = tanTheta*x;
        Point2D.Double p = new Point2D.Double(x, y);
        return p;
    }
    
    public static void main(String[] args) {
        (new SimpleSwingExample ()).start();
    }
    

}
