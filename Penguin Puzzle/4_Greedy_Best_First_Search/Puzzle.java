package assignment3;

import java.util.ArrayList;
import java.util.Arrays;

//The direction class has a array of choices each choice in the array is the incremental or decremental value of row and column
//If the direction is 0 or 5 then it is an invalid move so we dont increment or decrement row and column
class Direction{
	public static int choices[][] = { { 0, 0 }, { 1, -1 }, { 1, 0 }, { 1, 1 }, { 0, -1 }, { 0, 0 }, { 0, 1 }, { -1, -1 },
			{ -1, 0 }, { -1, 1 } };
}
//The Puzzle is class which is created to store the puzzle and its size parameters. The Puzzle class object is also used to pass
//as a method variable under different circumstances.
public class Puzzle implements Comparable<Puzzle> {
	int row, col; //These variables are used to store the row and col dimensions of the puzzle
	char[][] ch;  //This character is used to store the puzzle elements
	int[] penguPosition; //This array is used to store the row and column location of penguin position index 0=row and 1=column
	int[] prevPenguPosition; //This variable is used to store penguin's previous position
	ArrayList<String> fishesEatenCoordinates; //This Array is used to store coordinates of fishes eaten by penguin
	String stepsTaken; //This string variable is used to store the path travelled by the penguin
	int recentDirection;//This integer variable store the recent direction in which penguin travelled.
	int fishesEatenCount;//This integer variable store the number of fish consumed by peguin
	String depthStatus;//This string variable store the information about the depth status
	Puzzle() { //This is a non parameterized constructor of puzzle class which instantiates the puzzle object with the default values.
		row = 0;
		col = 0;
		ch = new char[this.row][this.col];
		this.penguPosition= new int[2];
		this.penguPosition[0] = 0;
		this.penguPosition[1] = 0;
		this.prevPenguPosition = new int[2];
		this.prevPenguPosition[0] = 0;
		this.prevPenguPosition[1] = 0;
		this.fishesEatenCoordinates = new ArrayList<String>();
		this.stepsTaken = "";
		this.recentDirection = 0;
		this.fishesEatenCount = 0;
		this.depthStatus = "INITIAL";
	}

	Puzzle(int row, int col, char[][] ch, int[] penguPosition,String stepsTaken,int recentDirection, int fishesEatenCount,String depthStatus,ArrayList<String> fishesEatenCoordinates) {
		//This is a  parameterized constructor of puzzle class which instantiates the puzzle object with the parameter values.
		this.row = row;
		this.col = col;
		this.ch = new char[this.row][this.col];//
		for (int i = 0; i < this.row; i++) {
			for (int j = 0; j < this.col; j++) {
				this.ch[i][j] = ch[i][j];
			}
		}		
		this.penguPosition = new int[2];
		this.penguPosition[0] = penguPosition[0];
		this.penguPosition[1] = penguPosition[1];
		this.prevPenguPosition = new int[2];
		this.prevPenguPosition[0] = penguPosition[0];
		this.prevPenguPosition[1] = penguPosition[1];
		this.fishesEatenCoordinates = fishesEatenCoordinates;
		this.stepsTaken = stepsTaken;
		this.recentDirection = recentDirection;
		this.fishesEatenCount = 0;
		this.depthStatus = depthStatus;
	}

	public int getRow() {//This method is getter which helps me to return the Row value
		return row;
	}

	public void setRow(int row) {//This method helps to set the Row value
		this.row = row;
	}

	public int getCol() {//This method is getter which helps me to return the Col value
		return col;
	}

	public void setCol(int col) {//This method helps to set the Col value
		this.col = col;
	}

	public char[][] getCh() {//This method is getter which helps me to return the character array value
		return ch;
	}

	public void setCh(char[][] ch) {//This method helps to set the character array value
		this.ch = ch;
	}
	//The newDirectionTaken method helps to set the recent direction to the current move and concats the existing path travelled with the current path
	public void newDirectionTaken(int direction) {
		this.recentDirection = direction;
		this.stepsTaken += direction;
	}
	//The setPenguPosition method helps us to update the current penguin row and column position
	public void setPenguPosition(int[] penguPosition) {
		this.resetPreviousVisitedPenguPosition();
		this.penguPosition[0] = penguPosition[0];
		this.penguPosition[1] = penguPosition[1];
		this.ch[penguPosition[0]][penguPosition[1]] = 'P';
		
	}
	
	//The puzzle clone method helps us to clone or copy the existing puzzle object with the new puzzle object.
	public Puzzle getClone() {
		Puzzle tempPuzzle = new Puzzle();
		tempPuzzle.row = this.row;
		tempPuzzle.col = this.col;
		tempPuzzle.ch = new char[this.row][this.col];
		for (int i = 0; i < tempPuzzle.row; i++) {
			for (int j = 0; j < tempPuzzle.col; j++) {
				tempPuzzle.ch[i][j] = this.ch[i][j];
			}
		}
		tempPuzzle.penguPosition = new int[2];
		tempPuzzle.penguPosition[0] =this.penguPosition[0];
		tempPuzzle.penguPosition[1] = this.penguPosition[1];
		tempPuzzle.prevPenguPosition = new int[2];
		tempPuzzle.prevPenguPosition[0] = this.prevPenguPosition[0];
		tempPuzzle.prevPenguPosition[1] = this.prevPenguPosition[1];
		tempPuzzle.fishesEatenCoordinates.addAll(this.fishesEatenCoordinates);
		tempPuzzle.stepsTaken = this.stepsTaken;
		tempPuzzle.recentDirection = this.recentDirection;
		tempPuzzle.fishesEatenCount = this.fishesEatenCount;
		tempPuzzle.depthStatus = this.depthStatus;
		return tempPuzzle;
	}
//This method helps us to read each value of character array and store it in a String text
	public String getOutputFormat() {
		String text = ""; 
		for (int i = 0; i < this.getRow(); i++) {
			for (int j = 0; j < this.getCol(); j++) {
				if (this.getCh()[i][j] == 'R') {//Here in the puzzle we assumed R as penguin on snow cell and P as penguin position on empty cell
					this.getCh()[i][j] = 'P';  //Hence, we are trying to convert R back to P.
					text = text + this.getCh()[i][j];
				} else {
					text = text + this.getCh()[i][j];
				}
			}
			text = text + "\n"; // Each row is concatenated with the other rows and stored as String array
		}
		return text;
	}

	//This method helps to print the character array in the grid format and also the row and col values
	public String toString() {
		String st = "";
		int i = 0;
		while (i < this.row) {
			st += Arrays.toString(this.ch[i]);
			i++;
		}
		return String.format("%d %d \n%s", this.row, this.col, st);
	}
	
	// This method will generate the row and column increment or decrement values
	public RowColumn nextStep(int move) {
		int [] direction = Direction.choices[move];
		RowColumn movement = new RowColumn();
		movement.setRow(this.penguPosition[0]+direction[0]);
		movement.setCol(this.penguPosition[1]+direction[1]);
		return movement;
	}

	// The findFishCount method helps us to count of fish present in the puzzle
	// which is passed as a parameter using Puzzle object.
	public int findFishCount() {
		int fishcount = 0;
		for (int i = 0; i < this.getRow(); i++) {
			for (int j = 0; j < this.getCol(); j++) {
				if (this.getCh()[i][j] == '*') {
					fishcount = fishcount + 1;
				}
			}
		}
		return fishcount;
	}
	
	public boolean isNextStepAWall(RowColumn move) {//This method checks whether we have wall in the next step or not. If yes returns true else false
		if(this.ch[move.row][move.col] == '#') {
			return true;
		}
		return false;
	}
	
	public boolean isNextStepASnowCell(RowColumn move) {//This method checks whether we have snow cell in the next step or not. If yes returns true else false
		if(this.ch[move.row][move.col] == '0') {
			return true;
		}
		return false;
	}
	
	public boolean isNextStepAFishCell(RowColumn move) {//This method checks whether we have fish cell in the next step or not. If yes returns true else returns false
		if(this.ch[move.row][move.col] == '*') {
			return true;
		}
		return false;
	}
	
	public boolean isNextStepWithInGridWalls(RowColumn nextPenguMove) {//This method checks whether the next step is grid wall or not if yes returns false else returns true
		if(nextPenguMove.row<0 || nextPenguMove.row>=this.row) {//checking row conditions
			return false;
		}
		if(nextPenguMove.col<0 || nextPenguMove.col>=this.col) {//checking column conditions
			return false;
		}
		return true;
	}
	
	public boolean isPenguEatenByBearOrShark() {//This method checks whether the penguin is dead or alive in the given grid
		return "X".contains(""+this.ch[this.penguPosition[0]][this.penguPosition[1]]);
	}
	public boolean isPenguEatenByBearOrShark(RowColumn nextPenguMove) {//This method checks whether the current location is bear or shark
		return "U_S".contains(""+this.ch[nextPenguMove.row][nextPenguMove.col]);
	}
	
	// This method helps us to generate all the valid moves with respect to
		// penguin's current position
		// This method takes the penguin_position and puzzle object as input parameters
		public ArrayList<Integer> generateValidDirectionToMove() {
			ArrayList<Integer> validDirections = new ArrayList<Integer>();
			int allDirectionsLength = Direction.choices.length;//This store length of the all the possible moves
			RowColumn newStep = new RowColumn();
			for (int eachDirection = 0; eachDirection < allDirectionsLength; eachDirection++) {
				if("0|5".contains(""+eachDirection)) continue;//It checks whether the next direction if 0 or 5 if it is 0 or 5 it will skip
				newStep = this.nextStep(eachDirection);//else we will get the assign the next step to the new step variable.
				if (!isNextStepAWall(newStep)) {//This if will check whether the next step is wall or not if not wall then it will add the direction to valid moves
					validDirections.add(eachDirection);
				}
			}
			return validDirections;//returns all the valid directions
		}
	public void resetPreviousVisitedPenguPosition(){//This method will reset the previous value of the pengu position that is when the pengu is on a snow cell previously.
		char oldPositionSymbol = this.ch[this.penguPosition[0]][this.penguPosition[1]];//We are storing the penguPosition row and column value
		char oldPositionNewSymbol = ' ';
		if(oldPositionSymbol == 'R') {//If the penguin's previous position is on R i.e when pengu is on snow cell then we will update the previous path with snow cell again
			oldPositionNewSymbol = '0';
		}
		this.prevPenguPosition = new int[2];
		this.prevPenguPosition[0] = this.penguPosition[0];
		this.prevPenguPosition[1] = this.penguPosition[1];
		this.ch[this.penguPosition[0]][this.penguPosition[1]] = oldPositionNewSymbol;//If it is not a snow cell setting the previous element
	 }
	public void glidePenguInTheGrid(){//This method helps us to glide the pengu in the puzzle board with respect to the direction
		int directionToSlide = this.recentDirection;
		//RowColumn penguin_oldposition = new RowColumn();
		RowColumn newMove = new RowColumn();
		newMove = this.nextStep(directionToSlide);// The new move variable will store the direction of next move
		while (true) {
			if(!isNextStepWithInGridWalls(newMove)) {//If the next step is a grid wall we are going to break the loop or glide
				break;
			}
			if(isPenguEatenByBearOrShark(newMove)) {//If the next step consists of shark then we are going to set the value X
				this.ch[newMove.row][newMove.col] = 'X';
				this.resetPreviousVisitedPenguPosition();
				this.penguPosition[0] = newMove.row;
				this.penguPosition[1] = newMove.col;
				break;
			}else if(isNextStepAWall(newMove)) {//If the next step is a  wall we are going to break the loop or glide
				break;
			}else if(isNextStepASnowCell(newMove)) {//If the next is a snow cell then we are going to represent the position of penguin with R
				this.ch[newMove.row][newMove.col] = 'R';
				this.resetPreviousVisitedPenguPosition();
				this.penguPosition[0] = newMove.row;
				this.penguPosition[1] = newMove.col;
				break;
			}else if(isNextStepAFishCell(newMove)) {//If the next step consists of fish then we are going to increment the count of fish eaten
				this.fishesEatenCount++; //We are storing the number of fishes being eaten
				this.fishesEatenCoordinates.add(String.format("%d_%d",newMove.row,newMove.col));//here we are trying to store the coordinates of fishes eaten
				
			}
			this.ch[newMove.row][newMove.col] = 'P';
			this.resetPreviousVisitedPenguPosition();//We are reseting the penguin's previous position
			this.penguPosition[0] = newMove.row;//We are updating the penguin position with new row value
			this.penguPosition[1] = newMove.col;//We are updating the penguin position with new col value
			newMove = this.nextStep(directionToSlide);//newMove variable is stored with next step to glide
		}
	}
	public int heuristicFunction(Puzzle state2) {
	/*	if(this.isPenguEatenByBearOrShark() && this.fishesEatenCount < 20) {
			return -1;
		}
		if(state2.isPenguEatenByBearOrShark() && this.fishesEatenCount < 20) {
			return 1;
		}*/
		int value = Integer.compare(this.fishesEatenCount, state2.fishesEatenCount);//It compares the current fisheatencount with the state2 fish eaten count and if it is
		if(value != 0) {//not equal to zero then we are returning the difference between the current fisheatencount and state2 fish eaten count
			return this.fishesEatenCount-state2.fishesEatenCount;
		}
		//If it is equal to zero we are comparing the steps taken length and returning the value 
		value = Integer.compare(state2.stepsTaken.length(), this.stepsTaken.length());
		//System.out.println(String.format("%s %s %d %d",this.stepsTaken,state2.stepsTaken,this.fishesEatenCount,state2.fishesEatenCount));
		return value;
		
	}
	public int compareTo(Puzzle state2) {//The compareTo function is interenally called which helps to internally sort the Priority Queue 
		return heuristicFunction(state2);//The priority value is received and based on that priority value the priority queue is modified
    }
	public String getVisitedKey() {//This function helps us to return penguin position row and column and fish eaten in formatted string 
		return String.format("%d_%d_%s",this.penguPosition[0],this.penguPosition[1],this.fishesEatenCoordinates.toString());
	}
}
