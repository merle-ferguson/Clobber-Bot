import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.Random;

import javax.swing.JButton;

public class Edge{

	Line2D line = null;
	Vertex a = null;
	Vertex b = null;
	
	
	public Edge(Vertex x, Vertex y, Line2D l) {
		super();

		line = l;
		a = x;
		b = y;
	}
	
	public Edge(Vertex x, Vertex y) {
		super();

	
		a = x;
		b = y;
	}
	
	
	public Line2D getLine(){
		return line;
	}

	public Vertex getV1()
	{
		return a;
	}
	public Vertex getV2()
	{
		return b;
	}
	
}
