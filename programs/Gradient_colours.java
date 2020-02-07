package programs;

import hsa2.GraphicsConsole;

import java.awt.*;

public class Gradient_colours {
	GraphicsConsole gc = new GraphicsConsole(1000,360,"Easy Gradients");

	public static void main(String[] args)
	{
		new Gradient_colours();
	}

	Gradient_colours(){
		gc.setLocationRelativeTo(null);
		gc.setAntiAlias(true);

		//vary the hue over 20 circles
		for (int i = 0; i < 20; i++){

			Color[] colors = new Color[]{new Color(Color.HSBtoRGB(i/20.0f,1.0f, 0.6f)), Color.BLACK};
			float[] fractions = new float[]{0.0001f, 1.0f};

			//Linear Gradient (Stripes)
			GraphicsConsole.GradientType gradientType = GraphicsConsole.GradientType.GRADIENT_LINEAR;

			gc.setColorGradient(gradientType, 0+50*i, 75, 0+50*i+50,75, colors, fractions);
			gc.fillOval(0+50*i, 50, 50, 50);

			//Radial Gradient (Concentric Circles)
			gradientType = GraphicsConsole.GradientType.GRADIENT_RADIAL;

			gc.setColorGradient(gradientType, 25+50*i, 175, 0+50*i+50,175, colors, fractions);
			gc.fillOval(0+50*i, 150, 50, 50);

			//Conical Gradient (Sweep effect)
			gradientType = GraphicsConsole.GradientType.GRADIENT_CONICAL;

			gc.setColorGradient(gradientType, 25+50*i, 275, 0+50*i+50,275, colors, fractions);
			gc.fillOval(0+50*i, 250, 50, 50);
		}
	}
}