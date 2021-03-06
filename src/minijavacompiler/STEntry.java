package minijavacompiler;

/**
 * @author facom
 */
public class STEntry 
{
    public Token token;
    public String lexeme;
    public boolean reserved;
    public SymbolTable st;
    public String className;
    {}
    
    public STEntry(Token tok, String lex)
    {
        token = tok;
        lexeme = lex;
        reserved = false;
        
        //double var = 2.e+24;
    }
    
    public STEntry(SymbolTable<STEntry> st , String className)
    {
    	this.st = st;
    	this.className = className;
    }
    
    
    public STEntry(Token tok, String lex, boolean res)
    {
        token = tok;
        lexeme = lex;
        reserved = res;
    }
}
