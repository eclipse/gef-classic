package org.eclipse.draw2d.util;
/*
 * Licensed Material - Property of IBM
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved.
 * US Government Users Restricted Rights - Use, duplication or disclosure
 * restricted by GSA ADP Schedule Contract with IBM Corp.
 */


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