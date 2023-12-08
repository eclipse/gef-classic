/*******************************************************************************
 * Copyright (c) 2004, 2023 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package awt;

import java.awt.AlphaComposite;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * @since 3.1
 */
public class BenchmarkAlphaLines {

	public static void main(String[] args) {
		Frame frame = new Frame() {
			@Override
			public void paint(Graphics gr) {
				super.paint(gr);
				Graphics2D g = (Graphics2D) gr;
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.1f));
				for (int i = 0; i < 1000; i++) {
					g.drawLine(0, 1000 - i, i, 0);
				}

			}
		};
		frame.setSize(400, 400);
		frame.setVisible(true);
	}

}
