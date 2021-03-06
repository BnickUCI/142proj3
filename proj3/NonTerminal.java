package crux;

import java.util.HashSet;
import java.util.Set;

// NonTerminal
//
//	Description: This class stores all the First Sets to each grammar 
//						rule in the crux language
// ====================================================================================
public enum NonTerminal {
    
    // TODO: mention that we are not modeling the empty string
    // TODO: mention that we are not doing a first set for every line in the grammar
    //       some lines have already been handled by the CruxScanner
    
    DESIGNATOR(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
            add(Token.Kind.IDENTIFIER);
        }}),
    TYPE(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	add(Token.Kind.IDENTIFIER);
            //throw new RuntimeException("implement this");
        }}),
    LITERAL(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	add(Token.Kind.INTEGER);
        	add(Token.Kind.FLOAT);
        	add(Token.Kind.TRUE);
        	add(Token.Kind.FALSE);
           // throw new RuntimeException("implement this");
        }}),
    CALL_EXPRESSION(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	add(Token.Kind.CALL);
           // throw new RuntimeException("implement this");
        }}),
    OP0(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	add(Token.Kind.GREATER_EQUAL);
        	add(Token.Kind.LESSER_EQUAL);
        	add(Token.Kind.NOT_EQUAL);
        	add(Token.Kind.EQUAL);
        	add(Token.Kind.GREATER_THAN);
        	add(Token.Kind.LESS_THAN);
           // throw new RuntimeException("implement this");
       }}),
    OP1(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	add(Token.Kind.ADD);
        	add(Token.Kind.SUB);
        	add(Token.Kind.OR);
          //  throw new RuntimeException("implement this");
       }}),
    OP2(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	add(Token.Kind.MUL);
        	add(Token.Kind.DIV);
        	add(Token.Kind.AND);
            //throw new RuntimeException("implement this");
       }}),
    EXPRESSION3(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	add(Token.Kind.NOT);
        	add(Token.Kind.OPEN_PAREN);
        	addAll(DESIGNATOR.firstSet());
        	addAll(CALL_EXPRESSION.firstSet());
        	addAll(LITERAL.firstSet());
           // throw new RuntimeException("implement this");
       }}),
    EXPRESSION2(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	addAll(EXPRESSION3.firstSet());
            //throw new RuntimeException("implement this");
        }}),
    EXPRESSION1(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	addAll(EXPRESSION2.firstSet());
           // throw new RuntimeException("implement this");
        }}),
    EXPRESSION0(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	addAll(EXPRESSION1.firstSet());
            //throw new RuntimeException("implement this");
        }}),
    EXPRESSION_LIST(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	addAll(EXPRESSION0.firstSet());
        	
            //throw new RuntimeException("implement this");
        }}),
    PARAMETER(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	add(Token.Kind.IDENTIFIER);
            //throw new RuntimeException("implement this");
        }}),
    PARAMETER_LIST(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	addAll(PARAMETER.firstSet());
            //throw new RuntimeException("implement this");
        }}),
    VARIABLE_DECLARATION(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	add(Token.Kind.VAR);
           // throw new RuntimeException("implement this");
        }}),
    ARRAY_DECLARATION(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	add(Token.Kind.ARRAY);
           // throw new RuntimeException("implement this");
        }}),
    FUNCTION_DEFINITION(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	add(Token.Kind.FUNC);
           // throw new RuntimeException("implement this");
        }}),
    DECLARATION(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	addAll(VARIABLE_DECLARATION.firstSet());
        	addAll(ARRAY_DECLARATION.firstSet());
        	addAll(FUNCTION_DEFINITION.firstSet());
            //throw new RuntimeException("implement this");
        }}),
    DECLARATION_LIST(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	addAll(DECLARATION.firstSet());
            //throw new RuntimeException("implement this");
        }}),    
    ASSIGNMENT_STATEMENT(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	add(Token.Kind.LET);
           // throw new RuntimeException("implement this");
        }}),
    CALL_STATEMENT(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	addAll(CALL_EXPRESSION.firstSet());
           // throw new RuntimeException("implement this");
        }}),
    IF_STATEMENT(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	add(Token.Kind.IF);
           // throw new RuntimeException("implement this");
        }}),
    WHILE_STATEMENT(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	add(Token.Kind.WHILE);
           // throw new RuntimeException("implement this");
        }}),
    RETURN_STATEMENT(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	add(Token.Kind.RETURN);
        //   throw new RuntimeException("implement this");
        }}),
    STATEMENT_BLOCK(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	add(Token.Kind.OPEN_BRACE);
          //  throw new RuntimeException("implement this");
        }}),
    STATEMENT(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	addAll(VARIABLE_DECLARATION.firstSet());
        	addAll(CALL_STATEMENT.firstSet());
        	addAll(ASSIGNMENT_STATEMENT.firstSet());
        	addAll(IF_STATEMENT.firstSet());
        	addAll(WHILE_STATEMENT.firstSet());
        	addAll(RETURN_STATEMENT.firstSet());
        	
          //  throw new RuntimeException("implement this");
        }}),
    STATEMENT_LIST(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	addAll(STATEMENT.firstSet());
           // throw new RuntimeException("implement this");
        }}),
    PROGRAM(new HashSet<Token.Kind>() {
        private static final long serialVersionUID = 1L;
        {
        	addAll(DECLARATION_LIST.firstSet());
           // throw new RuntimeException("implement this");
        }});
           
    public final HashSet<Token.Kind> firstSet = new HashSet<Token.Kind>();

    NonTerminal(HashSet<Token.Kind> t)
    {
        firstSet.addAll(t);
    }
    
    public final Set<Token.Kind> firstSet()
    {
        return firstSet;
    }
}
