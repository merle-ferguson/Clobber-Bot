import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.io.IOException;

import javax.swing.*;

public class Screen extends JFrame implements ActionListener,MouseListener,Values{
	int width;
	int height;
	MyPanel mainPanel = null;
	Vertex selected = null;



	JButton remove = null;
	JButton computerMove = null;

	JTextArea vertCounter = new JTextArea("Vertices: 0");
	JTextArea edgeCounter = new JTextArea("Edges: 0");

	ArrayList<Edge> edges = new ArrayList<Edge>();
	ArrayList<Vertex> vertices = new ArrayList<Vertex>();
	ArrayList<Line2D> lines = new ArrayList<Line2D>();

	ArrayList<Point> vertexScreenLocations = new ArrayList<Point>();

	boolean removeIsSelected = false;

	public MoveCalculator mc = null;
	public MoveCalculator2 mc2 = null;




	public Screen() throws IOException { 
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		mainPanel = new MyPanel();
		mainPanel.setLayout(null);
		add(mainPanel);
		mainPanel.addMouseListener(this);

		remove = new JButton("Play");
		mainPanel.add(remove);
		remove.addActionListener(this);
		remove.setBounds(750, 10, 100, 50);

		mainPanel.add(vertCounter);
		vertCounter.setBounds(750, 60, 100, 50);

		mainPanel.add(edgeCounter);
		edgeCounter.setBounds(850, 60, 100, 50);

		computerMove = new JButton("Computer");
		mainPanel.add(computerMove);
		computerMove.addActionListener(this);
		computerMove.setBounds(850, 10, 100, 50);

		mc2 = new MoveCalculator2();

		mc2.buildWinningArray();
		//mc.printArray();
		mc = new MoveCalculator(mc2);

		makeGraph();

	}


	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof Vertex){
			Vertex v = (Vertex)e.getSource();
			if(removeIsSelected){
				removeVertex(v);
				mainPanel.repaint();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(vertices.size() > 0)
					computerMakeMove();

			}
			else{

				if(selected!=null && !selected.equals(v)){
					addLine(v , selected);
				}
				else{
					changeSelected(v);
				}
			}
		}

		if(e.getSource() == remove){
			if(removeIsSelected){
				removeIsSelected = false;
				remove.setText("Play");
			}
			else{
				removeIsSelected = true;
				remove.setText("Playing");
			}
		}

		if(e.getSource() == computerMove){
			if(vertices.size() > 0)
				computerMakeMove();
			setSelectedNull();
			mainPanel.repaint();
		}
	}

	public void mousePressed(MouseEvent e){		
		setSelectedNull();


		Point p = MouseInfo.getPointerInfo().getLocation();
		p.setLocation(p.getX(), p.getY() + WEIRDYOFFSET);

		if(e.getButton() == MouseEvent.BUTTON1){
			boolean lineFound = false;
			if(removeIsSelected){
				int boxX = p.x - HIT_BOX_SIZE / 2;
				int boxY = p.y - HIT_BOX_SIZE / 2;

				int width = HIT_BOX_SIZE;
				int height = HIT_BOX_SIZE;

				for(Line2D l : lines){
					if (l.intersects(boxX, boxY, width, height)) {
						lineFound = true;
						removeLine(l);

						break;
					}		
				}

				if(lineFound){
					mainPanel.repaint();
					//System.out.println("Repaint");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						//System.out.println("Hello");
					}
					computerMakeMove();
				}

			}

			if(!lineFound&&!removeIsSelected)
			{
				addVertex((int)(p.x + 0.5), (int)(p.y+ 0.5));
				//System.out.println((int)(p.x + 0.5)+" "+ (int)(p.y+ 0.5));
			}

		}

	}

	public void removeVertex(Vertex v){
		ArrayList<Edge> removeEs = new ArrayList<Edge>();

		for(Edge e: edges){
			if(v.equals(e.getV1())||v.equals(e.getV2())){
				removeEs.add(e);
			}
		}

		removeLines(removeEs);
		vertices.remove(v);
		mainPanel.remove(v);

//		if(vertices.size() == 0){
//			try {
//				mc2.writeToFile();
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//		}
		updateText();

	}

	public void removeLine(Line2D l){
		edges.remove(findEdgeGivenLine(l));
		lines.remove(l);
		mainPanel.removeEdge(l);
		updateText();
	}

	public void removeLines(ArrayList<Edge> es){

		for(Edge e:es){
			edges.remove(e);
			lines.remove(e.getLine());
			mainPanel.removeEdge(e.getLine());
		}
		updateText();
	}

	public void removeEdge(Edge e){
		edges.remove(e);
		lines.remove(e.getLine());
		mainPanel.removeEdge(e.getLine());
		updateText();
	}

	public Edge findEdgeGivenLine(Line2D l){
		for(Edge e :edges){
			if(e.getLine().equals(l)){
				return e;
			}
		}
		return null;
	}

	public Edge findEdgeGivenVertices(Vertex a, Vertex b){
		for(Edge e: edges){
			if((e.getV1() == a && e.getV2() == b) || (e.getV2() ==a && e.getV1() == b))
			{
				return e;
			}
		}
		return null;

	}

	public void changeSelected(Vertex v){
		if(selected!=null){
			selected.changeSelected();
		}
		v.changeSelected();
		selected = v;
	}

	public void setSelectedNull(){

		if(selected!=null){
			selected.setCurSelected(false);
			selected = null;
		}
	}

	public void addLine(Vertex v1, Vertex v2){
		Line2D l = new Line2D.Double(v1.getX()  + VERTEXWIDTH/2, v1.getY()+ VERTEXHEIGHT/2, v2.getX()+ VERTEXWIDTH/2,v2.getY() + VERTEXHEIGHT/2);
		mainPanel.addEdge(l);
		lines.add(l);
		Edge edge = new Edge(v1, v2, l);
		edges.add(edge);

		setSelectedNull();


		updateText();
		mainPanel.repaint();


	}

	public void addVertex(int x, int y){
		Vertex v = new Vertex();
		mainPanel.add(v);
		v.setBounds(x - VERTEXWIDTH/2,y - VERTEXHEIGHT/2, VERTEXWIDTH, VERTEXHEIGHT);
		v.addActionListener(this);
		vertices.add(v);
		updateText();
	}

	public void computerMakeMove(){

		Move m = null;
		//		if(edges.size() < 4){
		//			System.out.println("Calculated Algorithm");
		//			m = mc2.makeNextMove(edges, vertices);
		//			
		//		}
		//		else if(vertices.size() + (int)(edges.size()/2.0 + 0.5) > 15){
		//			System.out.println("Las Vegas Algorithm");
		//			m = mc.makeNextMove(edges, vertices);
		//		}
		//		else{
		//			System.out.println("Calculated Algorithm");
		//			m = mc2.makeNextMove(edges, vertices);
		//
		//		}
		m = mc2.makeNextMove(edges, vertices);

		if(m.getType().equals("vertex")){
			removeVertex(vertices.get(m.getIndex()));
		}
		else{
			Edge e = null;

			Vertex c = vertices.get(m.getV1Index());
			Vertex d = vertices.get(m.getV2Index());

			for(Edge f: edges){
				Vertex a = f.getV1();
				Vertex b = f.getV2();
				if((a.equals(c) && b.equals(d)) || (a.equals(d) && b.equals(c)))
					e = f;
			}
			removeEdge(e);
		}
	}

	public void updateText(){
		vertCounter.setText("Vertices: "+vertices.size());

		edgeCounter.setText("Edges: "+edges.size());
	}

	public void makeGraph(){
		Random gen = new Random();
		int numVertices = gen.nextInt(20) + 10;

		int numEdges = gen.nextInt(numVertices *(numVertices - 1)/5);

		//Adds vertices to random locations
		for(int x = 0; x < numVertices; x++){	
			int xVal = gen.nextInt(SCREEN_WIDTH - 50) + 50;
			int yVal = gen.nextInt(SCREEN_HEIGHT - 150) + 150;


			while(!isClearLocation(xVal, yVal))
			{
				//System.out.println("Made it har"+ " " + x);
				xVal = gen.nextInt(SCREEN_WIDTH - 50) + 50;
				yVal = gen.nextInt(SCREEN_HEIGHT - 150) + 150;
			}
			addVertex(xVal,yVal);
			vertexScreenLocations.add(new Point(xVal, yVal));
		}

		ArrayList<Vertex> color1 = new ArrayList<Vertex>();
		ArrayList<Vertex> color2 = new ArrayList<Vertex>();

		for(int x = 0; x < vertices.size(); x++){
			if(x >= vertices.size()/2)
				color1.add(vertices.get(x));
			else
				color2.add(vertices.get(x));
		}



		//System.out.println("Made it banana");
		for(int x = 0; x < numEdges; x++){
			int a = gen.nextInt(color1.size());
			int b = gen.nextInt(color2.size());
			while(null !=findEdgeGivenVertices(color1.get(a),color2.get(b))){
				//System.out.println("Vertices "+vertices.size());
				//System.out.println(numEdges);


				a = gen.nextInt(color1.size());
				b = gen.nextInt(color2.size());

			}


			addLine(color1.get(a),color2.get(b));
		}

	}

	public boolean isClearLocation(int x, int y){
		for(int i = 0; i< vertexScreenLocations.size(); i++){
			if(Math.abs(x - vertexScreenLocations.get(i).getX()) <= 5 || Math.abs(y - vertexScreenLocations.get(i).getY()) <= 5){
				//System.out.println(x+" "+vertexScreenLocations.get(i).getX()+" "+y+" "+ vertexScreenLocations.get(i).getY());
				return false;
			}
		}
		return true;
	}

	
		
		
		
		

	

	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
}
