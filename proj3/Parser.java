package crux;

// debug run configurations
//C:\Users\Nick\Desktop\CS_142_Compilers\CS142_Lab3\tests\tests\test04.crx

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Parser {
    public static String studentName = "Nicholas Bennett";
    public static String studentID = "76237804";
    public static String uciNetID = "nrbennet";
    
// SymbolTable Management ==========================
    private SymbolTable symbolTable;
    private int recursiveCounter; 
    private SymbolTable head; 
    private SymbolTable currentSymTable;
    
    // Semantic overhead
    private Token tempToken;
    private Symbol tempSymbol;
    
    
    private void initSymbolTable()
    {
    	// declare new symbol table object
        symbolTable = new SymbolTable(0);
        recursiveCounter++;
        head = symbolTable;
        currentSymTable = symbolTable;
    }
    
    private void enterScope()
    {
        // we need to add a new node to the list
    	// so declare a new symbol table
    	SymbolTable tempSymTable = new SymbolTable(recursiveCounter);
    	//System.out.println(" Enter test currentSymTable : " + tempSymTable.toString());
    
    	
    	recursiveCounter++;
    	// now add it to the list 
    	tempSymTable.setParent(currentSymTable);
    	//System.out.println(" Enter test currentSymTable : " + tempSymTable.toString());
//    	System.out.println(" Enter test currentSymTable : " + currentSymTable.toString());
    	//System.out.println("recursiveCounter: " + recursiveCounter);
    	currentSymTable = tempSymTable;
    //	System.out.println(" Enter test currentSymTable : " + currentSymTable.toString());
    }
    
    private void exitScope()
    {
        // just need to move to the next node up the list
    	// so follow parent node
//    	System.out.println("Exit test currentSymTable : " + currentSymTable.toString());
    	//System.out.println("recursiveCounter: " + recursiveCounter);
    	recursiveCounter--;
    	currentSymTable = currentSymTable.getParent(); 
    	
    	// I think garbage collection picks up the left behind node
    	
    }

    private Symbol tryResolveSymbol(Token ident)
    {
        assert(ident.is(Token.Kind.IDENTIFIER));
        String name = ident.lexeme();
        try {
//        	// redo 
//            return symbolTable.lookup(name);
        	return currentSymTable.lookup(name);
        } catch (SymbolNotFoundError e) {
            String message = reportResolveSymbolError(name, ident.lineNumber(), ident.charPosition());
            return new ErrorSymbol(message);
        }
    }

    private String reportResolveSymbolError(String name, int lineNum, int charPos)
    {
        String message = "ResolveSymbolError(" + lineNum + "," + charPos + ")[Could not find " + name + ".]";
        errorBuffer.append(message + "\n");
        // original
        //errorBuffer.append(symbolTable.toString() + "\n");
        errorBuffer.append(currentSymTable.toString() + "\n");
        return message;
    }

    private Symbol tryDeclareSymbol(Token ident)
    {
        assert(ident.is(Token.Kind.IDENTIFIER));
        String name = ident.lexeme();
        try {
            return currentSymTable.insert(name);
        } catch (RedeclarationError re) {
            String message = reportDeclareSymbolError(name, ident.lineNumber(), ident.charPosition());
            return new ErrorSymbol(message);
        }
    }

    private String reportDeclareSymbolError(String name, int lineNum, int charPos)
    {
        String message = "DeclareSymbolError(" + lineNum + "," + charPos + ")[" + name + " already exists.]";
        errorBuffer.append(message + "\n");
        errorBuffer.append(currentSymTable.toString() + "\n");
        return message;
    }    

// Helper Methods ==========================================

    private Token expectRetrieve(Token.Kind kind)
    {
        Token tok = currentToken;
        if (accept(kind))
            return tok;
        String errorMessage = reportSyntaxError(kind);
        throw new QuitParseException(errorMessage);
        //return ErrorToken(errorMessage);
    }
        
    private Token expectRetrieve(NonTerminal nt)
    {
        Token tok = currentToken;
        if (accept(nt))
            return tok;
        String errorMessage = reportSyntaxError(nt);
        throw new QuitParseException(errorMessage);
        //return ErrorToken(errorMessage);
    }
              
    // Methods brought in from the original parser
    //======================================================================================
    //======================================================================================
    //======================================================================================
	// Error Reporting 
    
	// Necessary Variables for Parser Constructor.
	private Scanner scanner;
	private Token currentToken;
	
	
    //  Parser Constructor
	// 
	//  Description:  Stores the passed Scanner object in a private variable
	// ==========================================================================================================
	public Parser(Scanner scanner)
	{		
		recursiveCounter = 0;
		this.scanner = scanner;
		currentToken = scanner.getNextToken();
		
		// Sematic check Overhead
		tempToken = null;// may screw things up. 
		tempSymbol = null;
		
	}// end of "Parser"
	// ==========================================================================================================
	private StringBuffer errorBuffer = new StringBuffer();

	/*private String reportSyntaxError(NonTerminal nt)
    {
        //String message = "SyntaxError(" + lineNumber() + "," + charPosition() + ")[Expected a token from " + nt.name() + " but got " + currentToken.kind() + ".]";
        errorBuffer.append(message + "\n");
        return message;
    }*/
    private String reportSyntaxError(Token.Kind kind)
	{
		String message = "SyntaxError(" + lineNumber() + "," + charPosition() + ")[Expected " + kind + " but got " + currentToken.getKind() + ".]";
		errorBuffer.append(message + "\n");
		return message;
	}
	
	private String reportSyntaxError(NonTerminal nt)
	{
		String message = "SyntaxError(" + lineNumber() + "," + charPosition() + ")[Expected " + nt + " but got " + currentToken.getKind() + ".]";
		errorBuffer.append(message + "\n");
		return message;
	}

	public String errorReport()
	{
		return errorBuffer.toString();
	}
	
	
	public boolean hasError()
	{
		return errorBuffer.length() != 0;
	}

	private class QuitParseException extends RuntimeException
	{
		private static final long serialVersionUID = 1L;
		public QuitParseException(String errorMessage) {
			super(errorMessage);
		}
	}

	private int lineNumber()
	{
		return currentToken.lineNumber();
	}

	private int charPosition()
	{
		return currentToken.charPosition();
	}
	
	// Helper Methods ==========================================================================================
		private boolean have(Token.Kind kind)
		{
			return currentToken.is(kind);
		}

		private boolean have(NonTerminal nt)
		{
			return nt.firstSet().contains(currentToken.getKind());
		}

		private boolean accept(Token.Kind kind)
		{
			if (have(kind)) {
				currentToken = scanner.getNextToken();
				return true;
			}
			return false;
		}    

		private boolean accept(NonTerminal nt)
		{
			if (have(nt)) {
				currentToken = scanner.getNextToken();
				return true;
			}
			return false;
		}

		private boolean expect(Token.Kind kind)
		{
			if (accept(kind))
				return true;
			String errorMessage = reportSyntaxError(kind);
			throw new QuitParseException(errorMessage);
			//return false;
		}

		private boolean expect(NonTerminal nt)
		{
			if (accept(nt))
				return true;
			String errorMessage = reportSyntaxError(nt);
			throw new QuitParseException(errorMessage);
			//return false;
		}
    
    //======================================================================================
    //======================================================================================
	//======================================================================================
    
    
// Parser ==========================================
    
    public void parse()
    {
        initSymbolTable();
        try {
            program();
        } catch (QuitParseException q) {
            errorBuffer.append("SyntaxError(" + lineNumber() + "," + charPosition() + ")");
            errorBuffer.append("[Could not complete parsing.]");
        }
    }

    public void program()
    {
    	
		if(have(NonTerminal.DECLARATION_LIST)){
			declarationList();
		}
		expectRetrieve(Token.Kind.EOF);
    }
    

	// literal := INTEGER | FLOAT | TRUE | FALSE .
	// -------------------------------------------------------------------
	public void literal()
	{
		if(have(Token.Kind.INTEGER)){
			expectRetrieve(Token.Kind.INTEGER);
		}
		else if(have(Token.Kind.FLOAT)){
			expectRetrieve(Token.Kind.FLOAT);
		}
		else if(have(Token.Kind.TRUE)){
			expectRetrieve(Token.Kind.TRUE);
		}
		else if(have(Token.Kind.FALSE)){
			expectRetrieve(Token.Kind.FALSE);
		}
		else
			System.out.println("Something wrong in the literal class");
	}

	
	// designator := IDENTIFIER { "[" expression0 "]" } .
	// -------------------------------------------------------------------
	public void designator()
	{
		// This is a test, not sure if this will work. Checks for
		// redefinition 
		tempToken = expectRetrieve(Token.Kind.IDENTIFIER);
		tempSymbol = tryResolveSymbol(tempToken); 
		//System.out.println("designator-->returned " + tempSymbol.toString());
		
		while (accept(Token.Kind.OPEN_BRACKET)) {
			have(NonTerminal.EXPRESSION0);
			expression0();
			expectRetrieve(Token.Kind.CLOSE_BRACKET);
		}
	}
	
	
	// type := IDENTIFIER. 
	// -------------------------------------------------------------------
	public void type()
	{
		tempToken = expectRetrieve(Token.Kind.IDENTIFIER);
		//tempSymbol = tryResolveSymbol(tempToken); 
		//System.out.println("designator-->returned " + tempSymbol.toString());
	}
	
	
	// op0 := ">=" | "<=" | "!=" | "==" | ">" | "<" 
	// -------------------------------------------------------------------
	public void op0()
	{
		if(have(Token.Kind.GREATER_EQUAL))
			expectRetrieve(Token.Kind.GREATER_EQUAL);
		else if(have(Token.Kind.LESSER_EQUAL))
			expectRetrieve(Token.Kind.LESSER_EQUAL);
		else if(have(Token.Kind.NOT_EQUAL))
			expectRetrieve(Token.Kind.NOT_EQUAL);
		else if(have(Token.Kind.EQUAL))
			expect(Token.Kind.EQUAL);
		else if(have(Token.Kind.GREATER_THAN))
			expectRetrieve(Token.Kind.GREATER_THAN);
		else if(have(Token.Kind.LESS_THAN))
			expectRetrieve(Token.Kind.LESS_THAN);
		else
			System.out.println("Something wrong in op0");
	}
	
	
	// op1 := "+" | "-" | "or" 
	// -------------------------------------------------------------------
	public void op1()
	{
		if(have(Token.Kind.ADD)){
			expectRetrieve(Token.Kind.ADD);
		}
		else if(have(Token.Kind.SUB)){
			expectRetrieve(Token.Kind.SUB);
		}
		if(have(Token.Kind.OR)){
			expectRetrieve(Token.Kind.OR);
		}
	}
	
	// op2 := "*" | "/" | "and" 
	// -------------------------------------------------------------------
	public void op2()
	{
		if(have(Token.Kind.MUL)){
			expectRetrieve(Token.Kind.MUL);
		}
		else if(have(Token.Kind.DIV)){
			expectRetrieve(Token.Kind.DIV);
		}
		if(have(Token.Kind.AND)){
			expectRetrieve(Token.Kind.AND);
		}
	}
	
	
	// expression0 := expression1 [op0 expression1].
	// -------------------------------------------------------------------
	public void expression0() {
		have(NonTerminal.EXPRESSION1);
		expression1();
		if(have(NonTerminal.OP0)){
			//accept(NonTerminal.OP0);
			op0();
			have(NonTerminal.EXPRESSION1);
			expression1();
		}
	}
	
	
	// expression1 := expression2 { op1 expression2 }.
	// -------------------------------------------------------------------
	public void expression1() {
		have(NonTerminal.EXPRESSION2);
		expression2();
		while(have(NonTerminal.OP1)){
			op1();
			have(NonTerminal.EXPRESSION2);
			expression2();
		}
	}
	
	
	// expression2 := expression3 { op2 expression3 }.
	// -------------------------------------------------------------------
	public void expression2() {
		have(NonTerminal.EXPRESSION3);
		expression3();
		while(have(NonTerminal.OP2)){
			op2();
			have(NonTerminal.EXPRESSION3);
			expression3();
		}
	}
	
	
	
	// expression3 := "not" expression3 | "(" expression0 ")" |
	//					designator | call-expression | literal
	// -------------------------------------------------------------------
	public void expression3() {
		
		if(have(Token.Kind.NOT)){
			expectRetrieve(Token.Kind.NOT);
			have(NonTerminal.EXPRESSION3); 
			expression3();
		}
		else if(have(Token.Kind.OPEN_PAREN)){
			expectRetrieve(Token.Kind.OPEN_PAREN);
			have(NonTerminal.EXPRESSION0);
			expression0();
			expectRetrieve(Token.Kind.CLOSE_PAREN);
		}
		else if(have(NonTerminal.DESIGNATOR)){
			
			designator();
		}
		else if(have(NonTerminal.CALL_EXPRESSION)){
			callExpression();
		}
		
		else if(have(NonTerminal.LITERAL)){
			literal();
		}
		else
			System.out.println("Something wrong in expression3");
	}
	
	
	
	// call-expression := "::" IDENTIFIER "(" expression-list ")". 
	// -------------------------------------------------------------------
	public void callExpression() {
		expectRetrieve(Token.Kind.CALL); 
		tempToken = expectRetrieve(Token.Kind.IDENTIFIER);
		tempSymbol = tryResolveSymbol(tempToken); 
		//System.out.println("callExpression-->returned " + tempSymbol.toString());
		
		expectRetrieve(Token.Kind.OPEN_PAREN);
		have(NonTerminal.EXPRESSION_LIST);
		expressionList();
		expectRetrieve(Token.Kind.CLOSE_PAREN);
	}
	
	
	
	// expression-list := [expression0 {"," expression0}].
	// -------------------------------------------------------------------
	public void expressionList() {
		if(have(NonTerminal.EXPRESSION0)){
			expression0();
			while(accept(Token.Kind.COMMA)){
				have(NonTerminal.EXPRESSION0);
				expression0();
			}
		}
	}
	
	
	
	// parameter := IDENTIFIER ":" type 
	// -------------------------------------------------------------------
	public void parameter() {
		tempToken = expectRetrieve(Token.Kind.IDENTIFIER);
		//tempSymbol = tryResolveSymbol(tempToken);  // original
		tempSymbol = tryDeclareSymbol(tempToken);
		//System.out.println("parameter-->returned " + tempSymbol.toString());
		expectRetrieve(Token.Kind.COLON);
		have(NonTerminal.TYPE);
		type();
	}
	
	// parameter-list := [parameter {"," parameter}].
	// -------------------------------------------------------------------
	public void parameterList() {
		if(have(NonTerminal.PARAMETER)){
			//expect(NonTerminal.PARAMETER);
			parameter();
			while(accept(Token.Kind.COMMA)){
				have(NonTerminal.PARAMETER);
				parameter();
			}
		}
	}
	
	
	// variable-declaration := "var" IDENTIFIER ":" type ";"
	// -------------------------------------------------------------------
	public void variableDeclaration(){
		expectRetrieve(Token.Kind.VAR);
		tempToken = expectRetrieve(Token.Kind.IDENTIFIER);
		tempSymbol = tryDeclareSymbol(tempToken); 
		//System.out.println("variableDeclaration-->returned " + tempSymbol.toString());
		expectRetrieve(Token.Kind.COLON);
		have(NonTerminal.TYPE);
		type();
		expectRetrieve(Token.Kind.SEMICOLON);
	}
	
	// array-declaration := "array" IDENTIFIER ":" type ";"
	// -------------------------------------------------------------------
	public void arrayDeclaration() {

		expectRetrieve(Token.Kind.ARRAY);
		tempToken = expectRetrieve(Token.Kind.IDENTIFIER);
		tempSymbol = tryResolveSymbol(tempToken); 
		//System.out.println("designator-->returned " + tempSymbol.toString());
		expectRetrieve(Token.Kind.COLON);
		have(NonTerminal.TYPE);
		type();
		expectRetrieve(Token.Kind.OPEN_BRACKET);
		expectRetrieve(Token.Kind.INTEGER);
		expectRetrieve(Token.Kind.CLOSE_BRACKET);
		
		while(accept(Token.Kind.OPEN_BRACKET)){
			expectRetrieve(Token.Kind.INTEGER);
			expectRetrieve(Token.Kind.CLOSE_BRACKET);
		}
		
		expectRetrieve(Token.Kind.SEMICOLON);
	}
	
	// function-definition := "func" IDENTIFIER "(" parameter-list ")"
	//									":" type statement-block .
	// -------------------------------------------------------------------
	public void functionDefinition() {
		expectRetrieve(Token.Kind.FUNC);
		
		// I think enterscope needs to return the currentToken
		//System.out.println("Before enter currentSymTable : " + currentSymTable.toString());
		//enterScope(); // experimental positioning
		
		
		//System.out.println("After Exit test currentSymTable : " + currentSymTable.toString());
		tempToken = expectRetrieve(Token.Kind.IDENTIFIER);
		tempSymbol = tryDeclareSymbol(tempToken); 
		//System.out.println("func-->returned " + tempSymbol.toString());
		expectRetrieve(Token.Kind.OPEN_PAREN);
		have(NonTerminal.PARAMETER_LIST); // changed from expect to have
		enterScope();
		parameterList();
		expectRetrieve(Token.Kind.CLOSE_PAREN);
		expectRetrieve(Token.Kind.COLON);
		have(NonTerminal.TYPE);
		type();
		//expect(NonTerminal.STATEMENT_BLOCK); // original
		have(NonTerminal.STATEMENT_BLOCK);
		statementBlock();
		exitScope();
	}
	
	// declaration := variable-declaration | array-declaration | function-definition
	// -------------------------------------------------------------------
	public void declaration() {
		
		if(have(NonTerminal.VARIABLE_DECLARATION)){
			//expect(NonTerminal.VARIABLE_DECLARATION);
			variableDeclaration();
		}
		else if(have(NonTerminal.ARRAY_DECLARATION)){
			//expect(NonTerminal.ARRAY_DECLARATION);
			arrayDeclaration();
		}
		else if(have(NonTerminal.FUNCTION_DEFINITION)){
			//expect(NonTerminal.FUNCTION_DEFINITION);
			functionDefinition();
		}
		else
			System.out.println("Something wrong in the declaration class");
	}
	
	
	// declaration-list := { declaration }. 
	// -------------------------------------------------------------------
	public void declarationList(){	
		while(have(NonTerminal.DECLARATION)){
			declaration();
		}
	}
	
	
	// assignment-statement := "let" designator "=" expression0 ";"
	// -------------------------------------------------------------------
	public void assignmentStatement() {
		expectRetrieve(Token.Kind.LET);
		have(NonTerminal.DESIGNATOR);
		designator();
		expectRetrieve(Token.Kind.ASSIGN);
		have(NonTerminal.EXPRESSION0);
		expression0();
		expectRetrieve(Token.Kind.SEMICOLON);
	}
	
	// call-statement := call-expression ";"
	// -------------------------------------------------------------------
	public void callStatement() {
		have(NonTerminal.CALL_EXPRESSION);
		callExpression();
		expectRetrieve(Token.Kind.SEMICOLON);
	}
	// if-statement := "if" expression0 statement-block ["else" statement-block ].
	// -------------------------------------------------------------------
	public void ifStatement() {
		expectRetrieve(Token.Kind.IF);
		have(NonTerminal.EXPRESSION0);
		expression0();
		have(NonTerminal.STATEMENT_BLOCK);
		enterScope(); // testing
		statementBlock();
		exitScope(); // testing 
		if(have(Token.Kind.ELSE)){
			expectRetrieve(Token.Kind.ELSE);
			have(NonTerminal.STATEMENT_BLOCK);
			enterScope(); // testing
			statementBlock();
			exitScope(); // testing
		}
	}
	
	
	// while-statement := "while" expression0 statement-block .
	// -------------------------------------------------------------------
	public void whileStatement() {
		expectRetrieve(Token.Kind.WHILE);
		have(NonTerminal.EXPRESSION0);
		expression0();
		have(NonTerminal.STATEMENT_BLOCK);
		enterScope();
		statementBlock();
		exitScope();
	}
	
	
	
	// return-statement := "return" expression0 ";" . 
	// -------------------------------------------------------------------
	public void returnStatement() {
		expectRetrieve(Token.Kind.RETURN);
		have(NonTerminal.EXPRESSION0);
		expression0();
		expectRetrieve(Token.Kind.SEMICOLON);
	}

	
	// statement := variable-declaration | call-statement | assignment-statement| 
	//					if-statement | while-statement| return-statement .
	// -------------------------------------------------------------------
	public void statement() {
		if(have(NonTerminal.VARIABLE_DECLARATION)){
			//expect(NonTerminal.VARIABLE_DECLARATION);
			variableDeclaration();
		}
		else if(have(NonTerminal.CALL_STATEMENT)){
			//expect(NonTerminal.CALL_STATEMENT);
			callStatement();
		}
		else if(have(NonTerminal.ASSIGNMENT_STATEMENT)){
			//expect(NonTerminal.ASSIGNMENT_STATEMENT);
			assignmentStatement();
		}
		else if(have(NonTerminal.IF_STATEMENT)){
			//expect(NonTerminal.IF_STATEMENT);
			ifStatement();
		}
		else if(have(NonTerminal.WHILE_STATEMENT)){
			//expect(NonTerminal.WHILE_STATEMENT);
			whileStatement();
		}
		else if(have(NonTerminal.RETURN_STATEMENT)){
			//expect(NonTerminal.RETURN_STATEMENT);
			returnStatement();
		}
		else
			System.out.println("Something wrong in the statement class");
	}
	
	// statement-list := { statement }.
	// -------------------------------------------------------------------
	public void statementList() {
		while(have(NonTerminal.STATEMENT)){
			statement();
		}
	}
	
	// statement-block := "{" statement-list "}" .
	// -------------------------------------------------------------------
	public void statementBlock() {	
		//if we are here we are in a new scope
		//enterScope(); // Wrong spot maybe
		expectRetrieve(Token.Kind.OPEN_BRACE);
		have(NonTerminal.STATEMENT_LIST);
		statementList();
		expectRetrieve(Token.Kind.CLOSE_BRACE);
		//exitScope(); //Wrong spot maybe
	}
}
