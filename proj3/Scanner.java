package crux;


import java.io.IOException;
import java.io.Reader;

public class Scanner {

	// variables
	private int lineNum; 				 // current line count
	private int charPos;  				 // character offset for current line
	private int nextChar;				 // contains the next char (-1 == EOF)
	private Reader input;				 // Makes a copy of the FileReader object passed to the constructor
	private Token tempToken; 			// temp storage for a token object
	
	
	// Flags
	boolean tokenFoundFlag;
	boolean whiteSpaceFlag;
	boolean ptrAheadOneFlag; 
	boolean exitSMFlag;
	
	// Used in getNextToken
	int currentState; 
	int nextState;
	String tempString;
	
	
	/*
	 * Constructor: Initializes local variables
	 */
	Scanner(Reader reader){
		input = reader;
		lineNum = 1; 			// Starts at 1, to match project conventions
		charPos = 0; 
		nextChar = 0;
		
		// init variables
		tempToken = null;
		currentState = 0; 
		nextState = 0;
		tempString = "";
		
		// init Scanner Flags
		tokenFoundFlag = false;
		whiteSpaceFlag = false;
		ptrAheadOneFlag = false; 
		exitSMFlag = false;
		
		
	}// end of "Scanner"
	// =========================================================================================================
	
	
	
	//  getNextToken
	// 
	//  Description: This methods reads characters from the input file until it find a valid CRUX TOKEN, 
	// 						then returns the TOKEN.
	// ==========================================================================================================
	public Token getNextToken(){
		// Clear Flags from previous call 
		currentState = 0;
		nextState = 0;
		
		tokenFoundFlag = false;
		whiteSpaceFlag = false;
		exitSMFlag = false;
		tempString = "";
			
		while(!tokenFoundFlag){
			exitSMFlag = false;
			
			if(!ptrAheadOneFlag){
				nextChar = readChar();
			}
			else
				ptrAheadOneFlag = false;
			
			determineType();
			runStateMachine();
		}

		return tempToken;
	}// end of "getNextToken"
	// ==========================================================================================================
	
	
	
	//  runStateMachine
	//
	// 	Description: This method simulates the state machine to find a token
	//
	// ==========================================================================================================
	private void runStateMachine() {
	
		while(!tokenFoundFlag){
			if(exitSMFlag)
				break;
			
			switch(nextState){
			case 1: // letter
				letter();
				break;
			case 2:
				symbol();
				break;
			case 3:
				number();
				break;
			case 4:
				otherChar();
				break;
			}
		}

	}// end of "runStateMachine"
	// =========================================================================================================


	
	//  number
	//
	// 	Description: mini state machine, if the next character is a number it determines if that number
	//						is a float or integer token
	//
	// ==========================================================================================================
	private void number() {
		// Some preliminaries
		int tempCharPos = charPos;
		int x = nextChar;
		tempString = "";
		//tempString += (char)x;
		
		// So if we are here, we have a number so, crunch numbers until a . or non number
		while((x>= 48)&&(x <=57)){ // while x is an element of the set {0,1,2,3,4,5,6,7,8,9}
			// eat the nextNumber
			tempString += (char)x; 
			nextChar = readChar();
			x = nextChar; 
			
			if(x==46){ // if you encounter a dot while eating numbers we have a float
				// See what the next character is
				tempString += (char)x;
				nextChar = readChar();
				x = nextChar;
				// while there are still numbers consume symbols
				while((x>= 48)&&(x <=57)){
					tempString += (char)x; 
					nextChar = readChar();
					x = nextChar; 
				}
				// once the while loop breaks, we are one symbol ahead and we have a token.
				tempToken = new Token(tempString,"float", tempCharPos, lineNum);
				tokenFoundFlag = true;
				ptrAheadOneFlag = true;
				break;
			}
			// if we never enter the float block we have a number
			tempToken = new Token(tempString, "integer", tempCharPos, lineNum);
			tokenFoundFlag = true;
			ptrAheadOneFlag = true;
		}
	}// end of "number"
	// ==========================================================================================================



	//  symbol
	//
	// 	Description: mini state machine, used to tokenize symbols
	//
	// ==========================================================================================================
	private void symbol() {
		int tempCharPos = charPos;
		int x = nextChar;
		
//		if(x==40|x ==41|x==123|x==125|x==91|x==93|x==43|x==45|x==42|x==47|x==62|x==60|x==61|x==44|x==59|x==58){
//			nextState = 2; // see state diagram for reference
//		}
		
		if(x==40|x ==41|x==123|x==125|x==91|x==93|x==43|x==45|x==42|x==44|x ==59){
			switch(x){
			case 40:
				tempToken = new Token("(", charPos, lineNum);
				tokenFoundFlag = true;
				break;
			case 41:
				tempToken = new Token(")", charPos, lineNum);
				tokenFoundFlag = true;
				break;
			case 123:
				tempToken = new Token("{", charPos, lineNum);
				tokenFoundFlag = true;
				break;
			case 125:
				tempToken = new Token("}", charPos, lineNum);
				tokenFoundFlag = true;
				break;
			case 91:
				tempToken = new Token("[", charPos, lineNum);
				tokenFoundFlag = true;
				break;
			case 93:
				tempToken = new Token("]", charPos, lineNum);
				tokenFoundFlag = true;
				break;
			case 43:
				tempToken = new Token("+", charPos, lineNum);
				tokenFoundFlag = true;
				break;
			case 45:
				tempToken = new Token("-", charPos, lineNum);
				tokenFoundFlag = true;
				break;
			case 42:
				tempToken = new Token("*", charPos, lineNum);
				tokenFoundFlag = true;
				break;
			case 44:
				tempToken = new Token(",", charPos, lineNum);
				tokenFoundFlag = true;
				break;
			case 59:
				tempToken = new Token(";", charPos, lineNum);
				tokenFoundFlag = true;
				break;
			default:
				tempToken = new Token("identifier", tempString, tempCharPos, lineNum);
				tokenFoundFlag = true;
				break;		
			}
		}
		else if(x==47|x==62|x==60|x==61|x==58|x==33){
			switch(x){
			case 47: // potential comment or divide
				nextChar = readChar();
				x = nextChar; 
				if(x==47){ // we have a comment
					while(nextChar != 10 ){
						nextChar = readChar();
						if(nextChar == -1)
							break;
					}
					ptrAheadOneFlag = true;
					exitSMFlag = true;
				}
				else{
					tempToken = new Token("/", charPos-1, lineNum);
					tokenFoundFlag = true;
					ptrAheadOneFlag = true;
				}
				break;
			case 62: // > or >=  Greater than
				nextChar = readChar();
				x = nextChar;
				if(x==61){ // if we find an equal sign
					tempToken = new Token(">=", tempCharPos, lineNum);
					tokenFoundFlag = true;
				}
				else{ // not an equal sign
					tempToken = new Token(">", charPos-1, lineNum);
					tokenFoundFlag = true;
					ptrAheadOneFlag = true; 
				}
				break;
			case 60: // less than, less than or equal to <, <=
				nextChar = readChar();
				x = nextChar;
				if(x==61){ // if we find an equal sign
					tempToken = new Token("<=", tempCharPos, lineNum);
					tokenFoundFlag = true;
				}
				else{ // not an equal sign
					tempToken = new Token("<", charPos-1, lineNum);
					tokenFoundFlag = true;
					ptrAheadOneFlag = true; 
				}
				break;
				
			case 61: // == Asign or equal to, =
				nextChar = readChar();
				x = nextChar;
				// if we see another equal sign
				if(x==61){
					tempToken = new Token("==", tempCharPos, lineNum);
					tokenFoundFlag = true;
				}
				else{
					tempToken = new Token("=", charPos-1, lineNum);
					tokenFoundFlag = true;
					ptrAheadOneFlag = true; 
				}
				break;
				
			case 58: // colon or call--> :, ::
				nextChar = readChar();
				x = nextChar;
				// if we see another colon we have a call token
				if(x==58){
					tempToken = new Token("::", tempCharPos, lineNum);
					tokenFoundFlag = true;
				}
				else{
					tempToken = new Token(":", charPos-1, lineNum);
					tokenFoundFlag = true;
					ptrAheadOneFlag = true; 
				}
				break;
				
			case 33: // != , Not equal to
				nextChar = readChar();
				x = nextChar;
				// if we see another colon we have a call token
				if(x==61){
					tempToken = new Token("!=", tempCharPos, lineNum);
					tokenFoundFlag = true;
				}
				else{
					tempToken = new Token(x, "ERROR", charPos-1, lineNum);
					tokenFoundFlag = true;
					ptrAheadOneFlag = true; 
				}
				break;
				
			default:
				System.out.println("Error in symbol switch block");
				System.exit(-1);
				break;
			}
		}
		else
			System.out.println("Something went terribly wrong, the program shouldnt even be here.");
		
		
	}// end of "letter"
	// =========================================================================================================



	//  letter
	//
	// 	Description: mini state machine, used for abstraction and modulation 
	//
	// ==========================================================================================================
	private void letter() {
		int tempCharPos = charPos;
		int x = nextChar;
		tempString += (char)x;
		
		// uppercase letter, lowercase letter, underscore, or number 
		while((x >= 65 && x <= 90) | (x >= 97 && x <= 122) | (x == 95) | ((x >= 48) && (x <= 57))){
			nextChar = readChar();
			x = nextChar; 
			if((x >= 65 && x <= 90) | (x >= 97 && x <= 122) | (x == 95) | ((x >= 48) && (x <= 57)))
				tempString += (char)x;
		}
		ptrAheadOneFlag = true;
		
		// Check to see if the string read is all caps, which
		boolean tempFlag = false;
		for(int i = 0; i < tempString.length(); i++){
			if(!((tempString.charAt(i)>=65)&&(tempString.charAt(i)<=90))){
				tempFlag = true;
				break;
			}
		}
		if(!tempFlag){
			tempToken = new Token("identifier", tempString, tempCharPos, lineNum);
			tokenFoundFlag = true;
			return;
		}
		
		
		switch (tempString.toLowerCase()){
		case "and": 
			tempToken = new Token("and", tempCharPos, lineNum);
			tokenFoundFlag = true;
			break;
		case "or":
			tempToken = new Token("or", tempCharPos, lineNum);
			tokenFoundFlag = true;
			break;
		case "not":
			tempToken = new Token("not", tempCharPos, lineNum);
			tokenFoundFlag = true;
			break;
		case "let":
			tempToken = new Token("let", tempCharPos, lineNum);
			tokenFoundFlag = true;
			break;
		case "var":
			tempToken = new Token("var", tempCharPos, lineNum);
			tokenFoundFlag = true;
			break;
		case "array":
			tempToken = new Token("array", tempCharPos, lineNum);
			tokenFoundFlag = true;
			break;
		case "func":
			tempToken = new Token("func", tempCharPos, lineNum);
			tokenFoundFlag = true;
			break;
		case "if":
			tempToken = new Token("if", tempCharPos, lineNum);
			tokenFoundFlag = true;
			break;
		case "else":
			tempToken = new Token("else", tempCharPos, lineNum);
			tokenFoundFlag = true;
			break;
		case "while":
			tempToken = new Token("while", tempCharPos, lineNum);
			tokenFoundFlag = true;
			break;
		case "true":
			tempToken = new Token("true", tempCharPos, lineNum);
			tokenFoundFlag = true;
			break;
		case "false":
			tempToken = new Token("false", tempCharPos, lineNum);
			tokenFoundFlag = true;
			break;
		case "return":
			tempToken = new Token("return", tempCharPos, lineNum);
			tokenFoundFlag = true;
			break;
		default:
			tempToken = new Token("identifier", tempString, tempCharPos, lineNum);
			tokenFoundFlag = true;
			break;
		}
		
	}// end of "letter"
	// =========================================================================================================


	
	//  determineType
	//
	// 	Description: This method is a switch statement that determines what category the next character
	//
	// ==========================================================================================================
	private void determineType() {
		int x = nextChar; // data preservation 
		
		// is it a symbol?
		if(x==40|x ==41|x==123|x==125|x==91|x==93|x==43|x==45|x==42|x==47|x==62|x==60|x==61|x==44|x==59|x==58|x==33){
			nextState = 2; // see state diagram for reference
		}
		
		// is it a letter?  [a,z] + [A,Z] + _ + [0,9] after first letter 
		else if((x >= 65 && x <= 90) | (x >= 97 && x <= 122) | x == 95){
			nextState = 1;
		}
		
		// is it a number
		else if( (x >= 48) && (x <= 57)){
			nextState = 3;
		}
		else if(x==-1|x==10|x==32|x==9|x==13){ // eof, space, newline, tab 
			nextState = 4; 
		}
		else{
			tempToken = new Token(x,"ERROR",charPos,lineNum);
			tokenFoundFlag = true;
		}
	}
	// end of "determineType"
	// ==========================================================================================================



	//  getNextToken
	//
	// 	Description: This method is a switch statement that determines what category the symbol should fall under
	//
	// ==========================================================================================================
	private void otherChar() {
		int tempChar = nextChar; // for data integrity purposes.
		
		switch(tempChar){
			case -1: // end of file
				tempToken = new Token("eof",charPos, lineNum);
				tokenFoundFlag = true;
				break;
			case 32: // whiteSpace, ignore
				whiteSpaceFlag = true;
				exitSMFlag = true;
				break; 
			case 10: // newline character
				lineNum++;
				charPos = 0; // reset char counter
				exitSMFlag = true;
				break;
			case 13:
				exitSMFlag = true;
				break;
			case 9: // tab 
				exitSMFlag = true;
				break;
			default: // Character not an element of crux alphabet
				tempToken = new Token(tempChar,"ERROR",charPos,lineNum);
				break;
		}
		
	}// end "getNextToken"
	// ==========================================================================================================


	
	//  readChar
	//
	//  Description: This method reads the next character from the input file
	//
	// ==========================================================================================================
	private int readChar() {
		try {
			nextChar = input.read();			
		} catch (IOException e) {
			System.out.println("ERROR Reading next character...Scanner constructor");
			e.printStackTrace();
		}
		charPos++;
		//System.out.println( "TEST: charPos(" + charPos + ")" + "Character:" + (char)nextChar + "    int:" + nextChar);
		return nextChar;
	}
}
