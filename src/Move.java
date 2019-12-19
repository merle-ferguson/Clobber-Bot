
public class Move {
	String type = "";
	int vertexToBeRemoved = -1;
	int v1 = -1;
	int v2 = -1;
	
	public Move(int x){
		vertexToBeRemoved = x;
		type = "vertex";
	}
	
	public Move(int x, int y){
		v1 = x;
		v2 = y;
		type = "edge";
		
	}
	
	public String getType(){
		return type;
	}

	public int getIndex(){
		return vertexToBeRemoved;
	}
	
	public int getV1Index(){
		return v1;
	}
	
	public int getV2Index(){
		return v2;
	}
}

