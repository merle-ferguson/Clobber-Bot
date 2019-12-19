import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;

public class Vertex extends JButton implements Values {

	private static final long serialVersionUID = 1L;
	Dimension size = new Dimension(VERTEXWIDTH - 5,VERTEXHEIGHT- 5);
	private Color color;
	private boolean selected = false;

	private ArrayList<Vertex> connectedList = new ArrayList<Vertex>();


	public Vertex() {
		super();

		setContentAreaFilled(false);
		Random rand = new Random();
		float r = rand.nextFloat();
		//float g = rand.nextFloat();
		float b = rand.nextFloat();
		color = new Color(r,0,b);


	}

	
	
	
	protected void paintComponent(Graphics g) {
			g.setColor(color);

			g.fillOval(0, 0, size.width, 
					size.height);
	}

	protected void paintBorder(Graphics g) {
	
		if(selected){
			g.setColor(color.GREEN);
			for(int x = 0; x< 3; x++){
				g.drawOval(0,0,size.width+x,size.height+x);
			}
		}
	}

	public void setCurSelected(boolean b){
		selected = b;
		repaint();
	}
	
	
	public void changeSelected(){
		selected = !selected;
		repaint();
	}
	
	public ArrayList<Vertex> getList(){
		return connectedList;
	}
	public void addConnected(Vertex v){
		connectedList.add(v);
		
	}
	


	
	
}