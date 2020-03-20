package programs;

import hsa2.GraphicsConsole;
import java.awt.Color;

public class HSB_colours {
	GraphicsConsole gc = new GraphicsConsole(1000,360,"Rainbow colours"); 
	
	public static void main(String[] args)
	{
		new HSB_colours();
	}
	
	HSB_colours(){
		gc.setLocationRelativeTo(null);
		gc.setAntiAlias(true);
		
		//vary the hue over 20 circles
		for (int i = 0; i < 20; i++){
			
			//dimmer colours: less brightness (60%)
			gc.setColor(new Color(Color.HSBtoRGB(i/20.0f,1.0f, 0.6f)));
			gc.fillOval(0+50*i, 50, 50, 50);
			
			//brighter colours (100% brightness and 100% saturation)
			gc.setColor(new Color(Color.HSBtoRGB(i/20.0f,1.0f, 1.0f)));
			gc.fillOval(0+50*i, 150, 50, 50);
			
			//paler: less saturation (60%)
			gc.setColor(new Color(Color.HSBtoRGB(i/20.0f,0.6f, 1.0f)));
			gc.fillOval(0+50*i, 250, 50, 50);
		}
		
	}
}