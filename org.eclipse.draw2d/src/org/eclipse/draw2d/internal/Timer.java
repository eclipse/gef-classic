/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.internal;


public class Timer
	implements Runnable
{
private Thread t;
private Runnable run;
private int initial, period;
private boolean started, stopped;

public void cancel(){
	stopped = true;
}

public Thread getThread(){
	if (t == null){
		t = new Thread(this);
		t.setPriority(Thread.MIN_PRIORITY);
		t.setDaemon(true);
	}
	return t;
}

protected Runnable getRunnable(){
	return run;
}

protected void preformRun(){
	if(getRunnable()!=null)
		getRunnable().run();
}

public synchronized void run(){
	while (!stopped){
		if (!started){
			started = true;
			try {
				wait(initial);
			} catch (InterruptedException exc){}
		} else 
			try {
				wait(period);
			} catch (InterruptedException exc){}
		if (stopped)
			return;
		preformRun();
	}
}

public void scheduleRepeatedly(Runnable r, int initial, int delay){
	run = r;
	this.initial = initial;
	this.period = delay;
	start();
}

protected void start(){
	getThread().start();
}

}