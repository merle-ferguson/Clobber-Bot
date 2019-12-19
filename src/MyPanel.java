import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.swing.JPanel;


public class MyPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	public ArrayList<Line2D> lines = new ArrayList<Line2D>();
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		//System.out.println("Main Panel is Repainted");
		for(Line2D l : lines){
		    Graphics2D g2 = (Graphics2D) g;
		    g2.draw(l);
		}

	}
	public void addEdge(Line2D l){
		lines.add(l);
		repaint();
	}
	
	public void removeEdge(Line2D l){
		lines.remove(l);
		repaint();
	}
}
