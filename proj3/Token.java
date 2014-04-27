package crux;


import crux.Token.Kind;

/* Description:      
 *				This class defines all possible types of Tokens for the crux language. Also some helper 
 *				methods have been implemented. 
 * 
 * */
public class Token {
	
	
	/*
	 * define enum type Kind, we are using enum because we need to represent a fixed set of constants. 
	 */
	public static enum Kind {
		AND("and"),
		OR("or"),
		NOT("not"),
		LET("let"),
		VAR("var"),
		ARRAY("array"),
		FUNC("func"),
		IF("if"),
		ELSE("else"),
		WHILE("while"),
		TRUE("true"),
		FALSE("false"),
		RETURN("return"),
		
		OPEN_PAREN("("),
		CLOSE_PAREN(")"),
		OPEN_BRACE("{"),
		CLOSE_BRACE("}"),
		OPEN_BRACKET("["),
		CLOSE_BRACKET("]"),
		ADD("+"),
		SUB("-"),
		MUL("*"),
		DIV("/"),
		GREATER_EQUAL(">="),
		LESSER_EQUAL("<="),
		NOT_EQUAL("!="),
		EQUAL("=="),
		GREATER_THAN(">"),
		LESS_THAN("<"),
		ASSIGN("="),
		COMMA(","),
		SEMICOLON(";"),
		COLON(":"),
		CALL("::"),
		
		
		
		IDENTIFIER(),
		INTEGER(),
		FLOAT(),
		ERROR(),
		EOF();
				
		private String default_lexeme;
		

		/*
		 * Constructor 
		 */
		Kind()
		{
			default_lexeme = "";
		}
		
		
		/*
		 * Constructor 
		 */
		Kind(String lexeme)
		{
			default_lexeme = lexeme;
		}
		
		
		/*
		 * Test for whether or not a Token instance contains a valid lexeme or is yet to be assigned.
		 */
		public boolean hasStaticLexeme()
		{
			return default_lexeme != null;
		}

	}
	
	private int lineNum;
	private int charPos;
	Kind kind;
	private String lexeme = "";
	
	// Added Variable
	private int value=0;
	private String type;

	/*
	 *  Token Constructor : Under Construction, used when an error occurs and there is no lexeme to pass in 
	 */
	private Token(int lineNum, int charPos)
	{
		this.lineNum = lineNum;
		this.charPos = charPos;
		
		// if we don't match anything, signal error
		//this.kind = Kind.ERROR;
		//this.lexeme = "No Lexeme Given";
	}
	
	
	
	/*
	 *  Token Constructor : Deals with Integers, and Floats, Identifiers 
	 */
	public Token(String type, String lexeme, int charPos, int lineNum)
	{
		this.lineNum = lineNum;
		this.charPos = charPos;
		this.lexeme = lexeme;
		
		if(lexeme == "integer"){
			this.kind = Kind.INTEGER;
			this.type = type;
			//System.out.println(kind + "(" + value +")"+ "(lineNum:" + lineNum +"," + "charPos:" + charPos + ")");
		}
		if(lexeme == "float"){
			this.kind = Kind.FLOAT;
			this.type = type;
			//System.out.println(kind + "(" + value +")"+ "(lineNum:" + lineNum +"," + "charPos:" + charPos + ")");
		}
		if(type.equals("identifier")){
			this.kind = Kind.IDENTIFIER;
			this.type = type;
			//System.out.println(kind + "(lineNum:" + lineNum +"," + "charPos:" + charPos + ")");
			
		}
		
	}
	
	/*
	 *  Token Constructor : Deals with errors
	 */
	public Token(int errChar, String lexeme, int charPos, int lineNum)
	{
		this.value = errChar; 
		this.lineNum = lineNum;
		this.charPos = charPos;
		this.lexeme = lexeme;
		
		if(lexeme == "ERROR"){
			this.kind = Kind.ERROR;
			//System.out.println(kind + " (Unexpected character: " + (char)value +")"+ "(lineNum:" + lineNum +"," + "charPos:" + charPos + ")");
		}
		
	}

	
	/*
	 *  Token Constructor : Under Construction, used we we have found a token based on maximal munch heuristics 
	 */
	public Token(String lexeme, int charPos, int lineNum)
	{
		this.lineNum = lineNum;
		this.charPos = charPos;
		this.lexeme = lexeme;
		
		//Based on the given lexeme determine and set the actual kind
		
		if(lexeme == "="){
			this.kind = Kind.ASSIGN;
			return;
		}
		// easy ones
		else if(lexeme == "("){
			this.kind = Kind.OPEN_PAREN;
			return;
		}
		else if(lexeme == ")"){
			this.kind = Kind.CLOSE_PAREN;
			return;
		}
		else if(lexeme == "{"){
			this.kind = Kind.OPEN_BRACE;
			return;
		}
		else if(lexeme == "}"){
			this.kind = Kind.CLOSE_BRACE;
			return;
		}
		else if(lexeme == "["){
			this.kind = Kind.OPEN_BRACKET;
			return;
		}
		else if(lexeme == "]"){
			this.kind = Kind.CLOSE_BRACKET;
			return;
		}
		
		else if(lexeme == "+"){
			this.kind = Kind.ADD;
			return;
		}
		else if(lexeme == "-"){
			this.kind = Kind.SUB;
			return;
		}
		else if(lexeme == "*"){
			this.kind = Kind.MUL;
			return;
		}
		else if(lexeme == "/"){
			this.kind = Kind.DIV;
			return;
		}
		
		else if(lexeme == ","){
			this.kind = Kind.COMMA;
			return;
		}
		
		else if(lexeme == ";"){
			this.kind = Kind.SEMICOLON;
			return;
		}
		// hard ones
		else if(lexeme == "<="){
			this.kind = Kind.LESSER_EQUAL;
			return;
		}
		else if(lexeme == "<"){
			this.kind = Kind.LESS_THAN;
			return;
		}
		else if(lexeme == "::"){
			this.kind = Kind.CALL;
			return;
		}
		else if(lexeme == ":"){
			this.kind = Kind.COLON;
			return;
		}
		else if(lexeme == ">="){
			this.kind = Kind.GREATER_EQUAL;
			return;
		}
		else if(lexeme == ">"){
			this.kind = Kind.GREATER_THAN;
			return;
		}
		else if(lexeme == "=="){
			this.kind = Kind.EQUAL;
			return;
		}
		else if(lexeme == "eof"){
			this.kind = Kind.EOF;
			return;
		}
		else if(lexeme == "!="){
			this.kind = Kind.NOT_EQUAL;
			return;
		}
		else if(lexeme == "and"){
			this.kind = Kind.AND;
			return;
		}
		else if(lexeme == "or"){
			this.kind = Kind.OR;
			return;
		}
		else if(lexeme == "not"){
			this.kind = Kind.NOT;
			return;
		}
		else if(lexeme == "let"){
			this.kind = Kind.LET;
			return;
		}
		else if(lexeme == "var"){
			this.kind = Kind.VAR;
			return;
		}
		else if(lexeme == "array"){
			this.kind = Kind.ARRAY;
			return;
		}
		else if(lexeme == "func"){
			this.kind = Kind.FUNC;
			return;
		}
		else if(lexeme == "if"){
			this.kind = Kind.IF;
			return;
		}
		else if(lexeme == "else"){
			this.kind = Kind.ELSE;
			return;
		}
		else if(lexeme == "while"){
			this.kind = Kind.WHILE;
		}
		else if(lexeme == "true"){
			this.kind = Kind.TRUE;
		}
		else if(lexeme == "false"){
			this.kind = Kind.FALSE;
		}
		else if(lexeme == "return"){
			this.kind = Kind.RETURN;
		}
		else{
			this.kind = Kind.ERROR;
			this.lexeme = "Unrecognized lexeme: " + lexeme;
		}
	}
	
	

	/*
	 *  get functions
	 */
	public int lineNumber()
	{
		return lineNum;
	}
	

	public int charPosition()
	{
		return charPos;
	}
	
	public String lexeme()
	{
		return lexeme;
	}
	public Token.Kind getKind(){
		return this.kind;
	}
	
	
	/*
	 *  Converts the Token information into nice compact form and ready to write to file.
	 */
	public String toString()
	{
		//System.out.println(kind + "(lineNum:" + this.lineNum +"," + "charPos:" + this.charPos + ")");
		if (this.kind == Kind.ERROR){
			return (kind + " (Unexpected character: " + (char)value +")"+ "(lineNum:" + lineNum +"," + "charPos:" + charPos + ")");
		}
		else if(this.kind == Kind.IDENTIFIER){
			return (kind + "(" + lexeme + ")" + "(lineNum:" + lineNum +"," + "charPos:" + charPos + ")");
		}
		else if(this.kind == Kind.INTEGER){
			return (kind + "(" + type + ")" + "(lineNum:" + lineNum +"," + "charPos:" + charPos + ")");
		}
		else if(this.kind == Kind.FLOAT){
			return (kind + "(" + type + ")" + "(lineNum:" + lineNum +"," + "charPos:" + charPos + ")");
		}
		return kind + "(lineNum:" + lineNum +"," + "charPos:" + charPos + ")";
	}



	public boolean is(Kind k) {
		
		// I suspect this is correct
		if(this.kind == k)
			return true; 
		else 
			return false;
	}
}
