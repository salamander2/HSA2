package programs;

import hsa2.GraphicsConsole;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Rotate_colours {
	GraphicsConsole gc = new GraphicsConsole(1000,360,"Rotation, Translation, and Shear");

	public static void main(String[] args)
	{
		new Rotate_colours();
	}

	Rotate_colours(){

		int tx, ty, r;
		double sx, sy;
		tx = ty = r = 0;
		sx = sy = 0;

		gc.setLocationRelativeTo(null);
		gc.setAntiAlias(true);

		while (true) {
			gc.sleep(1);

			switch (gc.getKeyCode()){
				case KeyEvent.VK_W:
					ty--;
					break;
				case KeyEvent.VK_A:
					tx--;
					break;
				case KeyEvent.VK_S:
					ty++;
					break;
				case KeyEvent.VK_D:
					tx++;
					break;
				case KeyEvent.VK_UP:
					sy-=0.01;
					break;
				case KeyEvent.VK_LEFT:
					sx-=0.01;
					break;
				case KeyEvent.VK_DOWN:
					sy+=0.01;
					break;
				case KeyEvent.VK_RIGHT:
					sx+=0.01;
					break;
				case KeyEvent.VK_Z:
					r++;
					break;
				case KeyEvent.VK_X:
					r--;
					break;
			}

			synchronized (gc) {

				gc.clear();
				//vary the hue over 20 circles
				for (int i = 0; i < 20; i++) {

					gc.setRotation(r);
					gc.setTranslation((i * 50)+tx, ty);
					gc.setShear(sx, sy);

					Color[] colors = new Color[]{new Color(Color.HSBtoRGB(i / 20.0f, 1.0f, 0.6f)), Color.BLACK};
					float[] fractions = new float[]{0.0001f, 1.0f};

					//Linear Gradient (Stripes)
					GraphicsConsole.GradientType gradientType = GraphicsConsole.GradientType.GRADIENT_LINEAR;

					gc.setColorGradient(gradientType, 0, 75, 50, 75, colors, fractions);
					gc.fillOval(0, 50, 50, 50);

					//Radial Gradient (Concentric Circles)
					gradientType = GraphicsConsole.GradientType.GRADIENT_RADIAL;

					gc.setColorGradient(gradientType, 25, 175, 50, 175, colors, fractions);
					gc.fillOval(0, 150, 50, 50);

					//Conical Gradient (Sweep effect)
					gradientType = GraphicsConsole.GradientType.GRADIENT_CONICAL;

					gc.setColorGradient(gradientType, 25, 275, 50, 275, colors, fractions);
					gc.fillOval(0, 250, 50, 50);
				}

				gc.setRotation(0);
				gc.setTranslation(0, 0);
				gc.setShear(0, 0);
				gc.setColor(Color.RED);
				gc.drawString(String.format("Translation: (%d, %d) Shear: (%.2f %.2f) Rotation: (%d)",tx,ty,sx,sy,r), 20,20);
			}
		}
	}
}