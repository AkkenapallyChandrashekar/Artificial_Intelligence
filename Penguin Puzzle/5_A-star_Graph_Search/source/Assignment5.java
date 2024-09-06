/**
 * 
 */
package assignment5;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * @author MYPC
 *
 */
public class Assignment5 {

	/**
	 * @param args
	 */
	
	public static Puzzle readPuzzle(String input) {
		Puzzle puzzle = new Puzzle();
		Scanner sc = null;
		try {
			File file = new File(input); // java.io.File
			sc = new Scanner(file); // java.util.Scanner
			String line;

			line = sc.nextLine(); // We are trying to fetch and read the first line from the input file
			String split[] = line.split(" ");// We are splitting the first line into two parts and converting
			int row = Integer.parseInt(split[0]);
			int col = Integer.parseInt(split[1]);

			char[][] tempGrid = new char[row][col];// Creating an dynamic character array with above dimension
														// provided in input file
			int penguin_row = 0;
			int [] penguPosition = new int[2];
			while (sc.hasNextLine()) { // We are trying to read each line from the input file and copy it to a string
				line = sc.nextLine().trim();//We are trying to trim any spaces in the line
				tempGrid[penguin_row] = line.toCharArray();//We are trying to convert the extracted line into character array and store it in the temporary puzzle grid row value
				int penguCol = line.indexOf('P');//While reading the puzzle data we are trying to identify the position of the penguin
				if(penguCol>-1) {//Once we get a column value which would be greater than -1 it will be entered into if loop
					penguPosition[0] = penguin_row;//We are assigning the row value to the penguinPosition array at index 0
					penguPosition[1] = penguCol;//We are assigning the col value to the penguinPosition array at index 1
					penguCol = -1;//Once P is found we are breaking the if condition
				}
				penguin_row++;//We are incrementing penguin row for each iteration to read each line in Input file.
			}
			puzzle = new Puzzle(row, col, tempGrid, penguPosition,"",0,0,"INITIAL", new ArrayList<String>());//We are passing the row, col and tempGrid to the puzz
		} catch (FileNotFoundException e)// Catching the file not found exception
		{
			e.printStackTrace();
		} finally {
			if (sc != null)//if the scanned value is null we are trying to close the scanner
				sc.close();
		}
		return puzzle;
	}

//This method helps use the write the puzzle, score and the path travelled by the penguin.
	public static void writePuzzle(Puzzle puzzle, String fileName) {
		try {
			String output = puzzle.stepsTaken + "\n" +  + puzzle.fishesEatenCount+"\n";//We are concatenating the path traveled by penguin and the fish eaten by penguin
			output = output + puzzle.getOutputFormat();//We are trying to concat the above string with puzzle array which we are retrieving as string with the help of getOutputFormat
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false));// We are trying to write the data into the file
			writer.write(output);//We are trying to write the output into the file.
			writer.close();
		} catch (IOException e) {
			System.out.print(e.getMessage());
		}
	}
	
	public Puzzle Astar(Puzzle initialPuzzle) {
		PriorityQueue<Puzzle> frontier = new PriorityQueue<Puzzle>(Collections.reverseOrder());//We are creating a priority queue of Puzzle class type.
		Puzzle tempPuzzle = initialPuzzle;// We are assigning the puzzle with initial values
		int initTotalFishInPuzzle =  initialPuzzle.findFishCount();
		frontier.add(initialPuzzle);//We are pushing the initial puzzle to frontier
		HashSet<String> visitedStates = new HashSet<String>();//This Hashset string stores the visited paths.
		while(!frontier.isEmpty()) {
			tempPuzzle = frontier.poll();
			//System.out.println(tempPuzzle.stepsTaken);
			if((tempPuzzle.fishesEatenCount >= initTotalFishInPuzzle)&&(tempPuzzle.isPenguEatenByBearOrShark()==false)) {//When the score is reached and penguin is still alive we are returning the puzzle
				return tempPuzzle;// We are returning the tempPuzzle in which we have reached the goal state
			}
			if(tempPuzzle.isPenguEatenByBearOrShark()) {//Even if the puzzle is eaten by bear or shark also we are continuing the loop
				continue;
			}
			ArrayList<Integer> availableDirections = tempPuzzle.generateValidDirectionToMove();//We are pushing all the available directions for the current penguin position				
			for(int eachDirection: availableDirections) {
				Puzzle newTempPuzzle = tempPuzzle.getClone();//We are creating a copy of temp puzzle to new temp puzzle object
				newTempPuzzle.newDirectionTaken(eachDirection);//We are trying to retrieve the direction to be travelled by penguin
				newTempPuzzle.glidePenguInTheGrid();//We are gliding the penguin in each direction in newTempPuzzle
				newTempPuzzle.heuristic = newTempPuzzle.calculateHeuristic();//Here we are calculating the heuristic value of the puzzle
				String stateKey = String.format("%d_%d_%s",tempPuzzle.penguPosition[0],tempPuzzle.penguPosition[1],newTempPuzzle.getVisitedKey());
				if(!visitedStates.contains(stateKey)) {	//It compares whether the state key is present in the visited states or not					
					frontier.add(newTempPuzzle); //pushing the newtemp puzzle to the frontier
				visitedStates.add(stateKey);//Adding the visited state key to the visisted states.
				}
			}
			
		}
		return tempPuzzle;// returning the temp puzzle
	}
	
	
	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
				String inputFilePath = args[0];//Reading the input file path
				String outputFilePath = args[1];//Reading the output file path
				Puzzle puzzle=readPuzzle(inputFilePath); // Reading the puzzle data
				puzzle = (new Assignment5()).Astar(puzzle);//Calling the A* function
				writePuzzle(puzzle,outputFilePath);//Writing the values to the output text file		

	}

}
