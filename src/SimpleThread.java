import java.util.ArrayList;

class SimpleThread extends Thread {
	private boolean wonState = false;
	int [][] matrix = null;
	private int threadLayer;
	MoveCalculator2 mc= null;
	ArrayList<SimpleThread> childrenThreads = new ArrayList<SimpleThread>();
	private boolean myTurn = false;
	private Move winningMove = null;

	public SimpleThread(int [][] m, int threadLayer, MoveCalculator2 moveCalc, boolean mt) {
		matrix = m;
		mc = moveCalc;
		myTurn = mt;
	}
	public void run() {
		if(threadLayer > 0){
			ArrayList<Move> moveList = mc.generateMoveList(matrix);
			for(Move m: moveList){
				int [][] tempMatrix = mc.makeMove(matrix, m);
				childrenThreads.add(new SimpleThread(tempMatrix, threadLayer - 1, mc,!myTurn));
			}

			for(SimpleThread st: childrenThreads){
				st.start();
			}
			if(myTurn){
				for(SimpleThread st: childrenThreads){
					wonState = wonState ||st.wonState();
				}

			}
			else{
				wonState = true;
				for(SimpleThread st: childrenThreads){
					wonState = wonState &&st.wonState();
				}
				if(childrenThreads.size() == 0){
					System.out.println("Probrem");
					wonState = mc.wonState(matrix, myTurn);
				}
			}
			if(wonState){
				
			}
		}
		else{
			wonState = mc.wonState(matrix, myTurn);
		}
		
		


	}
	public boolean wonState(){
		return wonState;
	}
/*
	public Move nextMove(){
		
	}
	public Move getMove(){
		
		
	}
*/
}