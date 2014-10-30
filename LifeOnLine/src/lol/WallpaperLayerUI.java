package lol;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.plaf.LayerUI;

class WallpaperLayerUI extends LayerUI<JPanel> {
	
		Color BG = new Color(210,247,233);
	  @Override
	  public void paint(Graphics g, JComponent c) {
	    super.paint(g, c);

	    Image backgroundImage;
		try {
			backgroundImage = ImageIO.read(new File("C:\\Users\\owner\\LOL\\main\\LifeOnLine\\src\\resources\\background.jpg"));
			g.drawImage(backgroundImage, 0, 0, null);
		} catch (IOException e) {
		  System.out.println("image not loaded");
		}
	    
	   /* Graphics2D g2 = (Graphics2D) g.create();

	    int w = c.getWidth();
	    int h = c.getHeight();
	    g2.setComposite(AlphaComposite.getInstance(
	            AlphaComposite.SRC_OVER, .5f));
	    g2.setPaint(new GradientPaint(0, 0, Color.white, 0, h, BG));
	    g2.fillRect(0, 0, w, h);

	    g2.dispose();*/
	  }
	}