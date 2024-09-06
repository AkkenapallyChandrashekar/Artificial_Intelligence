/**
 * 
 */
package assignment3;

import java.io.*;
import java.util.*;

import java.util.Arrays;
import java.util.Random;
import java.lang.*;
import java.nio.file.Files;
import java.nio.file.*;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.charset.StandardCharsets;

import assignment3.RowColumn;
import assignment3.Puzzle;

/**
 * @author Chandrashekar Akkenapally
 *
 */

public class Assignment3 {
	/**
	 * @param args
	 */


	// This function helps us to read the puzzle data from the input text file.
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
			puzzle = new Puzzle(row, col, tempGrid, penguPosition,"",0,0,"INITIAL");//We are passing the row, col and tempGrid to the puzz
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

//The boundedDFS function is being used in the iterative deepening.
	public Puzzle boundedDFS(Puzzle initialPuzzle,int depthLimit) {
		boolean limit_hit = false;//The limit_hit variable is used to check whether depth limit is reached or not
		Stack<Puzzle> frontier = new Stack<Puzzle>();//We are creating a stack of Puzzle class type.
		Puzzle tempPuzzle = initialPuzzle;// We are assigning the puzzle with initial values
		frontier.push(initialPuzzle);//We are pushing the initial puzzle to frontier
		while(!frontier.isEmpty()) {
			tempPuzzle = frontier.pop();
			//System.out.println(tempPuzzle.stepsTaken);
			if(tempPuzzle.stepsTaken.length() == depthLimit) {//If path length is equal to depth limit we are going to test it or else we will continue the loop
				if(tempPuzzle.fishesEatenCount >= 16) {//When the score is reached we are updating the depth status as the solution is found at the given depth
					tempPuzzle.depthStatus = String.format("FOUND_SOLUTION_AT_DEPTH_%d", depthLimit);
					return tempPuzzle;// We are returning the tempPuzzle in which we have reached the goal state
				}
				if(tempPuzzle.isPenguEatenByBearOrShark()) {//We will continue the loop even if the pengu is eaten by bear or shark
					continue;
				}
				ArrayList<Integer> availableDirections = tempPuzzle.generateValidDirectionToMove();//Here we are retreiving the available valid directions
				if(availableDirections.size()>0) {//Whenever at a given location we dont have any moves then it means we have reached the depth limit
					limit_hit = true;
				}
			} else {//If the path length is not equal to depth limit we are not going to test is we are going to increase the depth limit
				if(tempPuzzle.isPenguEatenByBearOrShark()) {//Even if the puzzle is eaten by bear or shark also we are continuing the loop
					continue;
				}
				ArrayList<Integer> availableDirections = tempPuzzle.generateValidDirectionToMove();//We are pushing all the available directions for the current penguin position
				for(int eachDirection: availableDirections) {
					Puzzle newTempPuzzle = tempPuzzle.getClone();//We are creating a copy of temp puzzle to new temp puzzle object
					newTempPuzzle.newDirectionTaken(eachDirection);
					newTempPuzzle.glidePenguInTheGrid();//We are gliding the penguin in each direction in newTempPuzzle
					frontier.push(newTempPuzzle); //pushing the newtemp puzzle to the frontier
//					System.out.println(String.format("eachDirection:%d",eachDirection));
//					System.out.println(newTempPuzzle.getOutputFormat());
				}
			}
		}
		if(limit_hit) {//if the goal state is not reached but depth limit is hit then we are updating the depth status
			tempPuzzle.depthStatus = "HAS_SOLUTIONS_AT_GREATER_DEPTHS";
		} else {// If the goal state is not reached at give depth limit and  if there are no_plans_exist
			tempPuzzle.depthStatus = "NO_PLANS_EXIST";
		}
		return tempPuzzle;// returning the temp puzzle
	}
	
	//This method helps us to implement the iterative deepening depth first search
	public Puzzle IDDFS(Puzzle initialPuzzle) {
		int depth = 1;//Setting the initial depth status to 1
		Puzzle res = new Puzzle();//creating a new puzzle object res
		do {
			res = boundedDFS(initialPuzzle, depth);//We are sending the initial puzzle and depth value to the method
			if(res.depthStatus.equalsIgnoreCase(String.format("FOUND_SOLUTION_AT_DEPTH_%d", depth))) {//if the solution is found at depth limit d then we are returning puzzle object
				return res;
			}
			depth++;//Incrementing the depth value if goal state is not reached
		}while(!res.depthStatus.equalsIgnoreCase("NO_PLANS_EXIST"));//Even if the depth limit is hit and no more plans exists then we are breaking the do while loop
			return res;
	}
	public static void main(String[] args) {
		String inputFilePath = args[0];//Reading the input file path
		String outputFilePath = args[1];//Reading the output file path
		Puzzle puzzle=readPuzzle(inputFilePath); // Reading the puzzle data
		
		puzzle = new Assignment3().IDDFS(puzzle);//We are creating the class object and method of the class simultaneously
		writePuzzle(puzzle,outputFilePath);//Writing the values to the output text file		
	}

}

