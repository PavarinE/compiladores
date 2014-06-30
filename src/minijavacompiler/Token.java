package minijavacompiler;

/**
 * @author Lucas Tomaz Heck
 */
public class Token 
{
    public EnumToken name;
    public EnumToken attribute;
    public SymbolTable tsPtr;
    public int type;
    public String value;
    public int line; 
    
    public Token(EnumToken name)
    {
        this.name = name;
        attribute = EnumToken.UNDEF;
        tsPtr = null;
        this.line = 0;
    }
    
    public Token ( EnumToken name, EnumToken attr,  SymbolTable ts)
    {
    	this.name = name;
    	this.attribute = attr;
    	this.tsPtr = ts;
    	
    }
    
    public Token(EnumToken name, EnumToken attr)
    {
        this.name = name;
        attribute = attr;
        tsPtr = null;
    }
}
