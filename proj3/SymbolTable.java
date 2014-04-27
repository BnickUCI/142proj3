package crux;

import java.util.LinkedList;

public class SymbolTable {
    
	// declare a list object to store all 
	private LinkedList<Symbol> block = null;
	public int id;
	
	
    public SymbolTable(int id)
    {
    	this.id = id;
		block = new LinkedList<Symbol>();
    }
    
    public Symbol lookup(String name) throws SymbolNotFoundError
    {
    	// for each element in the symbol table, compare
        for(Symbol s: block){
        	if(s.name().equals(name)){
        		return s;
        	}
        }
        throw new SymbolNotFoundError(name);
    }
       
    
    // Description: so this just checks then adds it to the list
    public Symbol insert(String name) throws RedeclarationError
    {
        for(Symbol s:block){
           	if(s.name().equals(name)){
        		throw new RedeclarationError(s);
        	}
        }
        
        // if it doesn't exist in the table, make one and add it in
        boolean check = false;
        Symbol sym = new Symbol(name);
        check = block.add(sym);
        
        if(!check){
        	System.out.println("Something super worng");
        }
        return sym;
    }
    
    
    
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
//        if (/*I have a parent table*/)
//            sb.append(parent.toString());
//        
//        String indent = new String();
//        for (int i = 0; i < depth; i++) {
//            indent += "  ";
//        }
//        
//        for (/*Every symbol, s, in this table*/)
//        {
//            sb.append(indent + s.toString() + "\n");
//        }
        return sb.toString();
    }
}

class SymbolNotFoundError extends Error
{
    private static final long serialVersionUID = 1L;
    private String name;
    
    SymbolNotFoundError(String name)
    {
        this.name = name;
    }
    
    public String name()
    {
        return name;
    }
}

class RedeclarationError extends Error
{
    private static final long serialVersionUID = 1L;

    public RedeclarationError(Symbol sym)
    {
        super("Symbol " + sym + " being redeclared.");
    }
}
