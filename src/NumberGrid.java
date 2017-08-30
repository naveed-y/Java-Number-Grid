package p2_NumberGrid;

import java.util.*;

public class NumberGrid {

	public static final Scanner sc = new Scanner(System.in); // This scanner is instantiated here and called throughout the program using NumberGrid.sc
	
	private static void displayMenu(){ // Displays menu options
	
		System.out.println("\nOperations");
		System.out.println("display           dis           assign cell       as");
		System.out.println("fill              f             number            n");
		System.out.println("add cells         a             subtract cells    s");
		System.out.println("multiply cells    m             divide cells      d");
		System.out.println("add rows          ar            subtract rows     sr");
		System.out.println("multiply rows     mr            divide rows       dr");
		System.out.println("add columns       ac            subtract columns  sc");
		System.out.println("multiply  columns  mc            divide columns    dc");
		System.out.println("insert row        ir            insert column     ic");
		System.out.println("delete row        delr          delete column     delc");
		System.out.println("quit              q");

	}
	
	public static void main(String[] args) {
		
	
		Grid grid = new Grid(); // Create the grid that all the information is stored on
		while(true){
			
			displayMenu(); // Display menu until user quits
			System.out.print("-> "); // Prompts user to enter something
			String in = sc.nextLine(); // Scans the next token of the input each time the loop occurs
			
			
			// The switch case below takes in the user's input and triggers the correct method operation depending on what the input is.
			// .. Most of the methods are located inside the other classes, so we call otherClass.someMethod() to trigger them. Some of the
			// cases have parameters, these parameters just specify which arithmetic operation is to be performed in the member function.
			switch(in){
				case "dis":
					grid.displayGrid();
					continue;
				case "as":
					grid.assignCell();
					continue;
				case "f":
					grid.fill(0);
					continue;
				case "n":
					grid.fill(1);
					continue;
				case "a":
					grid.cellArithmetic('a');
					continue;
				case "s":
					grid.cellArithmetic('s');
					continue;
				case "m":
					grid.cellArithmetic('m');
					continue;
				case "d":
					grid.cellArithmetic('d');
					continue;
				case "ar":
					grid.rowArithmetic('a');
					continue;
				case "sr":
					grid.rowArithmetic('s');
					continue;
				case "mr":
					grid.rowArithmetic('m');
					continue;
				case "dr":
					grid.rowArithmetic('d');
					continue;
				case "ac":
					grid.colArithmetic('a');
					continue;
				case "sc":
					grid.colArithmetic('s');
					continue;
				case "mc":
					grid.colArithmetic('m');
					continue;
				case "dc":
					grid.colArithmetic('d');
					continue;
				case "ir":
					grid.insertRow();
					continue;
				case "ic":
					grid.insertCol();
					continue;
				case "delr":
					grid.deleteRow();
					continue;
				case "delc":
					grid.deleteCol();
					continue;
				case "q":
					sc.close(); // Close the scanner and exit program
					System.out.println("Powering off...");
					return;				
			}
			
			// If no switch case runs, the following error message is displayed and the loop runs again
			System.out.println("\nInput not recognized, please enter one of the inputs below.");
			
		}
	}
}

class Grid{
	
	private Node head;
	private int numR, numC, printWidth;
	
	Grid(){
		numR = 10; // Default values for number of rows/columns/print width are here
		numC = 6;
		printWidth = 10;
		
		head = new Node(); // The head node is used to access the grid in all functions in this program
		
		Node temp = head; // Records which row we are on
		Node temp2; // Records which col we are on
		Node prev = null; // Saves the previous row so we can weave the grid properly
		Node firstRow; // Only used to link the bottom row back to the top row
		
		// This nested for loop uses four nodes to traverse through a 2-D grid of nodes, and links each node to its adjacent right/down nodes
		// The nodes in the last row are circled back to the nodes in the first and the nodes in the last column are circled back to the nodes in the first
		for (int r=0; r<numR; r++){
			
			temp.down = new Node(); // 'temp' stays in row 0
			temp = temp.down; // 'temp' only moves down and creates a 1 dimensional linked list
			temp2 = temp; // 'temp2' only moves right and creates a 1 dimensional linked list rooted at temp
			
			for (int c=0; c<numC-1; c++){
				
				temp2.right = new Node();
				temp2 = temp2.right;
				//temp2.value.changeValue("-1");
				
				// This if statement runs after the first loop
				if (prev != null){ // Prev is always rooted at col 0 of the row above temp2
					prev = prev.right; // iterate prev right alongside temp2 and link each row above .down onto temp2
					prev.down = temp2;
				}
				if (c == numC-2){ // If last col, link temp2 back to first element in the same row
					temp2.right = temp;
				}
			}
			
			// After the last row is built, the following if statement is run
			if (r == numR-1){ // If last row, link all elements in row back to their corresponding elements in the first row
				temp2 = temp; // temp is rooted at the beginning of the last row linked list
				firstRow = head.down; // firstRow stays in row 0 of the grid so that the last row can be linked to firstRow
				for (int c=0; c<numC; c++){ // Iterate .right, and connect each of the elements in the last row .down to the first row.
					temp2.down = firstRow;
					temp2 = temp2.right;
					firstRow = firstRow.right;
				}
			}
			prev = temp; // After each iteration, we set prev to temp before moving temp down the list. This essentially saves the link to the row above
		}
	}
	
	// Print a grid of custom cell width, number of rows, and number of columns to the console
	void displayGrid(){ 
		
		String gridString = "";
		Node temp = head; // temp stays in col 0
		Node temp2 = temp; // temp2 moves across each row
		
		// Iterate from -1 to numR/numC so we can have information on the sides of our display grid
		for (int r=-1; r<numR; r++){
			
			for (int c=-1; c<numC; c++){
				
				if (r== -1){ // This if statement handles the unused space in the top left of the grid
					if (c == -1){ 
						gridString += String.format("%1$-" + printWidth + "s", "");
					}
					else{ // This is the info row, which contains column 1, 2 3, etc...
						gridString += String.format("%1$-" + printWidth + "s", "col " + c);
					}
				}
				
				else{
					if (c == -1){ // This is the info column, which contains row 1, 2 3, etc...
						gridString += String.format("%1$-" + printWidth + "s", "row " + r);
					}
					else{
						if (temp2.value.toString().equals("null")){ // If the string is null (i.e. a new Node) print an empty cell
							gridString += String.format("%1$-" + printWidth + "s", "");
						}
						else{
							String s = temp2.value.toString(); // Use toString to print the value's string 
						
							// This concat statement adds the string to gridString, but if it is longer than 9 characters, it shortens before displaying.
							gridString += String.format("%1$-" + printWidth + "s", s.length() > 9 ? s.substring(0, 9) : s);

						}
						
						//gridString += String.format("%1$-" + printWidth + "s", temp2.value.toString().equals("null") ? "" : temp2.value.toString().substring(0,10));
						temp2 = temp2.right;
					}
				}
			}
			temp = temp.down; // After first row and column are complete, start iterating temp and temp2 through the grid for the values of each cell
			temp2 = temp;
			
			
			gridString += "\n";
		}
		System.out.print(gridString);
	}
	
	// When the program asks a user to change a cell's value, this method checks if the input is valid
	String validValue(){
		
		System.out.print("with value: ");
		while(true){
			String val = NumberGrid.sc.nextLine();
			if (val.charAt(0) == '"'){ // Strings must start with ("), so anything following (") is a valid String
				return val;
			}
			try{ // If the input isn't a string, we must make sure it is a valid Double. we try to parse double...
				Double intVal = Double.parseDouble(val);
				return intVal.toString();
			}
			catch (NumberFormatException e){ // ... but we catch Exception if the user enters something like '123123A'
			    System.out.print("Enter a string starting with \" or enter a number: ");

			}
		}	
	}
	
	// When the program asks a user to enter a row, column, or both a row and a column, this function
	// .. makes sure the inputs are valid integers and in the range of the grid. The function can be called with 'r'
	// .. to just check row, 'c' to just check column, or 'b' for both
	int[] validRowCol(int numR, int numC, char rowColOrBoth){
		
		int[] returnInt = new int[2]; // The return value is a row/col pair in a 2-element array
		
		if (rowColOrBoth == 'r' || rowColOrBoth == 'b'){
			System.out.print("row: ");
			while(true){
				
				
				while(!NumberGrid.sc.hasNextInt()){ // Continue looping until user enters an integer input
					System.out.print("Please enter a valid number: ");
					NumberGrid.sc.next();
				}
				
				returnInt[0] = NumberGrid.sc.nextInt(); // Save int to array[0] here
				NumberGrid.sc.nextLine(); // Clear the Scanner buffer
				if (returnInt[0] >= numR || returnInt[0] < 0){ // Print a warning message if input is still out of range
					System.out.print("Row out of range. Enter a number between 0-" + (numR-1) + ": ");
					continue;
				}
				break;
			}
		}
		
		// Same code here but rewritten for column,
		if (rowColOrBoth == 'c' || rowColOrBoth == 'b'){
			System.out.print("column: "); // Prompt user for a column
			while(true){
				
				
				while(!NumberGrid.sc.hasNextInt()){ 
					System.out.print("Please enter a valid number: ");
					NumberGrid.sc.next();
				}
				
				
				returnInt[1] = NumberGrid.sc.nextInt(); // Save to array[1] this time
				NumberGrid.sc.nextLine();
				if (returnInt[1] >= numC || returnInt[1] < 0){ 
					System.out.print("Column out of range. Enter a number between 0-" + (numC-1) + ": ");
					continue;
				}
				break;
			}
		}
		return returnInt;
	}
	
	
	// When the programs asks a user for two row-column pairs, (like in the number/fill operations
	// .. this method makes sure the second row/col-pair is "southeast" of the first.
	int[] fromRCtoRCValidation(int numR, int numC){
		int[] returnInt = new int[4]; // returns a four element array {row1, col1, row2, col2}
		int[] oneRCpair; // Used within program to save two element array from the above function 
		
		while(true){ // Loop continuously until the user enters two pairs of row/col coordinates that are valid and the second pair is after the first
			System.out.println("From"); 
			oneRCpair = Arrays.copyOf(validRowCol(numR,numC, 'b'), 2); // Call the above function
			returnInt[0] = oneRCpair[0];
			returnInt[1] = oneRCpair[1];
			
			System.out.println("To");
			oneRCpair = Arrays.copyOf(validRowCol(numR,numC, 'b'), 2);
			if (returnInt[0] > oneRCpair[0] || returnInt[1] > oneRCpair[1]){
				System.out.println("Second row and column indeces must be greater than the first");
				continue;
			}
			else{
				returnInt[2] = oneRCpair[0];
				returnInt[3] = oneRCpair[1];
				break;
			}
			
		}
		
		
		return returnInt;
	}
	
	// This function asks a user to enter a row/col-pair and a value, and then updates the corresponding
	// .. node with the new input
	void assignCell(){

		int[] rcArray = Arrays.copyOf(validRowCol(numR,numC, 'b'), 2); // Acquire a valid r/c pair
		int row = rcArray[0], col = rcArray[1]; // Give each value in the r/c pair its own variable
		String val = validValue(); // Also acquire a valid value input
		
		
		Node temp = head.down; // Create a temp and iterate it to the specified r/c index
		
		for (int c = 0; c<col; c++){
			temp = temp.right;
		}
		for (int r = 0; r<row; r++){
			temp = temp.down;
		}
		
		temp.value.changeValue(val); // Use member function in Value class to change the instance's value appropriately
	}

	// This function fills a subgrid between two row/col-pairs with either a user input or a counter
	// .. (i.e. each cell is assigned values 1,2,3,4,etc..)
	void fill(int fillOrNumber){ // Used for both fill and number options. Input 0 for fill, 1 for number
		
		int[] rcArray = Arrays.copyOf(fromRCtoRCValidation(numR,numC), 4); // Get two valid r/c pairs
		int row1 = rcArray[0], col1 = rcArray[1], row2 = rcArray[2], col2 = rcArray[3]; // Give them unique variables
		
		Double ctr = 0.0; // Used if user wants to number cells
		String val = ""; // Used if user want to fill cells
		
		if (fillOrNumber == 0){ // If user wants to fill...
			val = validValue(); // Also acquire a valid value input

		}
		
		// Iterate temp to  row1/col1 index
		Node temp = head.down;
		Node temp2;
		
		for (int r=0; r<row1; r++){
			temp = temp.down;
		}
		for (int c=0; c<col1; c++){
			temp = temp.right;
		}
		
		// Iterate across the row then down from from row1/col1 to row2/col2
		for (int r=row1; r<=row2; r++){
			temp2 = temp;
			for (int c=col1; c<=col2; c++){
				if (fillOrNumber == 0){ // If user wants to fill, changeValue to val
					temp2.value.changeValue(val);
				}
				else{
					temp2.value.changeValue("" + ctr++);  // If user wants to number, changeValue to the ctr then increment the ctr
				}
				temp2 = temp2.right;
			}
			temp = temp.down;
		}
		
	}

	// This function can perform addition, subtraction, multiplication, or division of two cells
	// .. and can designate "resultant" cell which stores the value of the operation
	void cellArithmetic(char whichOp){
		
		
		//Acquire three valid r/c pairs, iterate each temp node to its corresponding r/c index on the grid, then perform the operation
		Node temp, temp2, temp3;
				
		System.out.println("First cell");
		int[] rcArray = Arrays.copyOf(validRowCol(numR,numC, 'b'), 2);
		int row1 = rcArray[0], col1 = rcArray[1];
		
		System.out.println("Second cell");
		rcArray = Arrays.copyOf(validRowCol(numR,numC, 'b'), 2);
		int row2 = rcArray[0], col2 = rcArray[1];
		
		System.out.println("Destination cell");
		rcArray = Arrays.copyOf(validRowCol(numR,numC, 'b'), 2);
		int row3 = rcArray[0], col3 = rcArray[1];
		
		temp = head.down;
		temp2 = head.down;
		temp3 = head.down;
		
		for (int r=0; r<row1; r++){
			temp = temp.down;
		}
		for (int c=0; c<col1; c++){
			temp = temp.right;
		}
		for (int r=0; r<row2; r++){
			temp2 = temp2.down;
		}
		for (int c=0; c<col2; c++){
			temp2 = temp2.right;
		}
		for (int r=0; r<row3; r++){
			temp3 = temp3.down;
		}
		for (int c=0; c<col3; c++){
			temp3 = temp3.right;
		}
			
		// Use input value 'whichOp' to specify which arithmetic operation is performed. Similar code is reproduced in other member functions below
		temp3.value.changeValue(temp.value.valMath(temp2.value, whichOp));
	}
	
	// This function also performs arithmetic, but applies the chosen operation to every cell in a desired row
	void rowArithmetic(char whichOp){
		
		// Acquire three valid row indeces
		System.out.println("First row");
		int row1 = validRowCol(numR,numC, 'r')[0];
		
		System.out.println("Second row");
		int row2 = validRowCol(numR,numC, 'r')[0];
		
		System.out.println("Destination row");
		int row3 = validRowCol(numR,numC, 'r')[0];
		
		// Iterate each temp to its corresponding row (col 0)
		Node temp = head.down, temp2 = head.down, temp3 = head.down;
		
		for (int r=0; r<row1; r++){
			temp = temp.down;
		}
		for (int r=0; r<row2; r++){
			temp2 = temp2.down;
		}
		for (int r=0; r<row3; r++){
			temp3 = temp3.down;
		}
		
		// Start iterating across the row and performing the specified operation each time.
		for (int c=0; c<numC; c++){
			temp3.value.changeValue(temp.value.valMath(temp2.value, whichOp)); // Operate on temp1 and temp2 and store the value in temp3
			temp = temp.right;
			temp2 = temp2.right;
			temp3 = temp3.right;
		}
		
	}
	
	// This function also performs arithmetic, but applies the chosen operation to every cell in a desired col
	void colArithmetic(char whichOp){
		
		// Same as rowArithmetic, but we get column indices, iterate right to starting node, and then iterate down and perform operation 
		System.out.println("First col");
		int col1 = validRowCol(numR,numC, 'c')[1];
		
		System.out.println("Second col");
		int col2 = validRowCol(numR,numC, 'c')[1];
		
		System.out.println("Destination col");
		int col3 = validRowCol(numR,numC, 'c')[1];
		
		Node temp = head.down, temp2 = head.down, temp3 = head.down;
		
		for (int r=0; r<col1; r++){
			temp = temp.right;
		}
		for (int r=0; r<col2; r++){
			temp2 = temp2.right;
		}
		for (int r=0; r<col3; r++){
			temp3 = temp3.right;
		}
		
		for (int r=0; r<numR; r++){
			temp3.value.changeValue(temp.value.valMath(temp2.value, whichOp));
			temp = temp.down;
			temp2 = temp2.down;
			temp3 = temp3.down;
		}
		
	}

	// This function inserts a new row at a desired row index. If the user wants a new row at the end, they
	// .. can input an index 1 higher than the number of rows in the grid
	void insertRow(){
		
		// Acquire a valid row index
		System.out.println("Insert");
		int row = validRowCol(numR+1, numC, 'r')[0]; // We allow the user to 1 higher than the number of rows
		
		// temp iterates across the row above the row we're inserting to
		// first saves the first node in the row
		// newNode develops into a new linked list which will be pushed into the grid
		// prevNode saves the node directly to the left of newNode so that it can link it .right to newNode
		Node temp = head.down, first = null, newNode, prevNode = null;
		
		// If the user wants to insert at the first row, we have to send temp to the last row
		if (row == 0){
			for(int ctr=1; ctr<numR; ctr++){
				temp = temp.down;
			}
		}
		else{
			for (int r=0; r<row-1; r++){
				temp = temp.down;
			}
		}
		
		// At this point, temp is one row above the row we want to insert to
		
		for (int c=0; c<numC; c++){
			newNode = new Node(); // Build newNodes for the new row
			
			if (prevNode != null){ // After first loop, link the newNode from the previous loop .right to this newNode
				prevNode.right = newNode;
			}
			
			newNode.down = temp.down; // i.e row1->row2 becomes row1->newRow->row2
			temp.down = newNode;
			
			// Save the first node in the new row 
			if (c == 0){
				if (row == 0){ // IMPORTANT: if we insert to row 0, head.down must be reassigned to point to the new row!!!
					head.down = newNode;
				}
				first = newNode;
			}
			temp = temp.right; // Iterate right across the row
			prevNode = newNode;
		}
		prevNode.right = first;
		numR++; // After inserting the finished row, we increment numR
		
	}
	
	
	// Same as above method but for columns
	void insertCol(){
		System.out.println("Insert");
		int col = validRowCol(numR, numC+1, 'c')[1];
		
		Node temp = head.down, first = null, newNode, prevNode = null;
		if(col == 0){
			for(int c=0; c<numC-1; c++){
				temp = temp.right;
			}
		}
		else{
			for (int c=0; c<col-1; c++){
				temp = temp.right;
			}
		}
		
		for (int r=0; r<numR; r++){
			newNode = new Node();
			
			if (prevNode != null){
				prevNode.down = newNode;
			}
			newNode.right = temp.right;
			temp.right = newNode;
			
			if (r == 0){
				if (col == 0){
					head.down = newNode;
				}
				first = newNode;
			}
			
			temp = temp.down;
			prevNode = newNode;
		}
		prevNode.down = first;
		numC++;
		
	}

	// This function deletes an entire row of cells from the grid and compresses the rest of the grid.
	// .. The grid cannot be reduced any further than 1x1
	void deleteRow(){
		
		// We do not reduce below 1 row
		if (numR == 1){
			System.out.println("Cannot delete any more rows..");
			return;
		}
		
		// Acquire a valid row index
		System.out.println("Delete");
		int row = validRowCol(numR, numC, 'r')[0];
		
		
		Node temp = head.down;
		
		// Iterate temp down to 1 row above the row we want to delete
		if (row == 0){ // If we want to delete row 0, iterate temp down to the last row
			for (int r=0; r<numR-1; r++){
				temp = temp.down;
			}
		}
		else{
			for (int r=0; r<row-1; r++){
				temp = temp.down;
			}
		}
		
		for (int c=0; c<numC; c++){ // Iterate across the row
			temp.down = temp.down.down; // Simply mark temp.down to temp.down.down and let Java take care of the garbage collection
			// This action changes a linked list from row1->row2->row3 to row1->row3
			
			// If deleting first row, make sure to reassign head.down
			if (row == 0 && c == 0){
				head.down = temp.down;
			}
			temp = temp.right;
		}
		numR--; // Decrement num rows
		
	}
	
	// Same as above method but for columns
	void deleteCol(){
		
		if (numC == 1){
			System.out.println("Cannot delete any more columns..");
			return;
		}
		
		System.out.println("Delete");
		int col = validRowCol(numR, numC, 'c')[1];
		
		
		Node temp = head.down;
		
		if (col == 0){
			for (int c=0; c<numC-1; c++){
				temp = temp.right;
			}
		}
		else{
			for (int c=0; c<col-1; c++){
				temp = temp.right;
			}
		}
		
		for (int r=0; r<numR; r++){
			temp.right = temp.right.right;
			
			if (col == 0 && r == 0){
				head.down = temp.right;
			}
			temp = temp.down;
		}
		numC--;
		
	}
	
} // End of Grid Class

class Node{
	
	// Node has two Node fields, right and down, and one Value class field
	Node right;
	Node down;
	Value value;
	
	// Instantiate the value field when instantiating the Node class. Nothing else needs to be done.
	Node(){
		value = new Value();
	}
	
}

class Value{
	
	// Value has three fields, a String "sval", a double "dval", and an integer "tag"
	private String sval;
	private Double dval;
	private int tag; // 1 = String, 0 = Double, -1 = Invalid
	
	// Initially, every Value is tagged as a string, with a sval of null and a dval of 0
	Value(){
		sval = null;
		dval = 0.0;
		tag = 1; // tagged String	
	}
	
	// Returns a string representation of any instance of Value based on the tag field
	public String toString(){
		String s = "";
		
		if (tag == 1){ // Print sval if the tag specifies a String
			s += sval;
		}
		else if (tag == 0){
			
			if (dval > 999999999.0){ // If dval is too large to display, just print the largest printable number
				s += 999999999;
			}
			else{ // Otherwise just print the number
				s += dval;
			}
			
			// The following IMPROVES READABILITY by chopping trailing 0's off of the string if the string contains a double
			while (s.substring(s.length()-1).equals("0")){
				s = s.substring(0, s.length()-1);
			}
			// After clearing trailing 0's, we want to check if the last character in the string is a ".". If so, remove that as well
			if (s.substring(s.length()-1).equals(".")){
				s = s.substring(0, s.length()-1);
			}
		}
		else{ // Else, the tag specifies invalid value
			System.out.println("Invalid Value tag detected, cannot create String...");
			//return null;
		}
		return s;
	}
	
	// This method replaces the fields of a Value object with the fields of another Value object
	void changeValue(Value val){ 
		if (val.tag == -1){ // Do nothing if invalid tag
			return;
		}
		else if (val.tag == 0){ // If integer, replace dval and set tag to integer
			this.tag = 0;
			this.dval = val.dval;
		}
		else if (val.tag == 1){ // If String, replace sval and set tag to String
			this.tag = 1;
			this.sval = val.sval;
			
		} 
		else{ // Just in case, if we get some unintended tag, we have an error message for that.
			System.out.println("Invalid Value tag. tag field must be -1, 0, or 1...");
		}
		
	}
	
	// This method takes in an input, determines if that input is a String or a parse-able double, and replaces the fields in value with the input
	void changeValue(String input){ 
		
		if (this.tag == -1){
			// Do not perform any changes if the resultant Value's tag is invalid
			return;
		}
		if (input.charAt(0) == '"'){ // If input is a String
				tag = 1; // tag as String
				sval = input.substring(1); // Pass in the String input, after slicing the first (") symbol out 
			}
		else{
			tag = 0; // If integer, tag as such
			dval = Double.parseDouble(input); // parseDouble() to extract the double from a String 
		}
	}
	
	
	// Returns if instance of Value is a double 
	private boolean isDouble(){
		return tag == 0;
	}
	
	// Performs addition, subtraction, multiplication, or division of two values, and the result of the operation is stores in a new instance of Value
	// The first value is the first in the operation (i.e. this.val - otherVal.val). The operation is specified by either 'a', 's', 'm', or 'd' in the
	// Declaration line.
	Value valMath(Value otherVal, char operation){
		Value resultantValue = new Value();
		if( !(this.isDouble() && otherVal.isDouble()) ){
			resultantValue.tag = -1; // Tag as Invalid if both operands are not Doubles
			//System.out.println("Cannot perform math on non-Double cells...");
		}
		else{ // Perform arithmetic
			resultantValue.tag = 0; // Tag as Double
			
			switch(operation){ // Perform the operation using +, -, *, or / based on the input parameter
			case 'a':
				resultantValue.dval = this.dval + otherVal.dval;
				break;
			case 's':
				resultantValue.dval = this.dval - otherVal.dval;
				break;
			case 'm':
				resultantValue.dval = this.dval * otherVal.dval;
				break;
			case 'd': // If we're doing division, we have to check if dividing by 0. 
				if (otherVal.dval == 0){ //If we are, the resultantValue's tag is set to 0 we do not divide 
					System.out.println("Cannot divide by 0...");
					resultantValue.tag = -1;
				}
				resultantValue.dval = this.dval / otherVal.dval;
				break;
			}
		}
		return resultantValue;
	}
	
}


