package crux;

import java.util.LinkedList;;

/*
 * Desccription: Each symbol table has a list object. Then we recursively make symbol table,
 * 		somehow maintaining pointers to each object
 */
public class SymbolTable {
    
	//private LinkedList<SymbolTable> symTableList = null; 
	private LinkedList<Symbol> symTable = null;
	

	
	private SymbolTable parent;
	private int depth; 
	
	/*
	 * Description: This constructor gets in Parser. 
	 */
//    public SymbolTable()
//    {
//    	// root node has id zero
//    	this.id = 0;
//    	symTableList = new LinkedList<SymbolTable>();
//    	symTable = new LinkedList<Symbol>();
//    	initCruxFunctions();
//    }
	
    
    /*
     * Description: This gets called after the root table is setup 
     */
    public SymbolTable(int recursiveCounter)
    {
    	this.depth = recursiveCounter;    	
    	symTable = new LinkedList<Symbol>();
    	if(depth==0){
    		initCruxFunctions();
    	}
    	this.parent = null;
    }
    
    public SymbolTable getParent(){
		return parent;
    }
    
    public void setParent(SymbolTable parentTable){
    	this.parent = parentTable;
    }
    
    
    //Description: need to add global function variables 
    private void initCruxFunctions() {
		this.symTable.add(new Symbol("readInt"));
		this.symTable.add(new Symbol("readFloat"));
		this.symTable.add(new Symbol("printBool"));
		this.symTable.add(new Symbol("printInt"));
		this.symTable.add(new Symbol("printFloat"));
		this.symTable.add(new Symbol("println"));
	}
    

	public Symbol lookup(String name) throws SymbolNotFoundError
    {
		//Check local table
    	for(Symbol s : symTable){
    		if(s.name().equals(name)){
    			return s; 
    		}
    	}
		SymbolTable symTablePtr = this.parent;
		
        while(symTablePtr != null){
        	for(Symbol s : symTablePtr.symTable){
        		if(s.name().equals(name)){
        			return s; 
        		}
        	}
        	symTablePtr = symTablePtr.parent;
        }
        throw new SymbolNotFoundError(name);
    }
       
    public Symbol insert(String name) throws RedeclarationError
    {
    	// scan local list for a redeclaration of symbol with same name
    	for(Symbol s: this.symTable){
    		if(s.name().equals(name)){
    			throw new RedeclarationError(s);
    		}
    	}
    	// else it hasn't been declared. so make a new symbol and add it to
    	// the local list
    	Symbol tempSym = new Symbol(name);
    	this.symTable.add(tempSym);
    	return tempSym;
    	
    }
    
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        if (this.parent!=null)
            sb.append(parent.toString());
        
        String indent = new String();
        for (int i = 0; i < depth; i++) {
            indent += "  ";
        }
        
        for (Symbol s: symTable)
        {
            sb.append(indent + s.toString() + "\n");
        }
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
