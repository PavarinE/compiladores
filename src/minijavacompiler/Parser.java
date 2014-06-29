package minijavacompiler;

import java.util.concurrent.ExecutionException;

/**
 * @author Lucas Tomaz Heck
 */
public class Parser 
{
    private Scanner scan;
    private Token lToken;
    private SymbolTable globalST;
    private SymbolTable currentST;
    private Token last;
    
    public Parser(String inputFile)    
    {
        //Instancia o analisador lÃ©xico
        scan = new Scanner(inputFile);
        
        globalST = new SymbolTable<STEntry>();
        currentST = globalST;
    }
    
    /*
     * Método que inicia o processo de análise sintática do compilador
     */
    
    public void execute()
    {
        int result = 0;
        lToken = scan.nextToken();
        try
        {
           // program(); // metodo que inicializa as verificacoes de "matches" 
        }
        catch(Exception e)
        {
           // JOptionPane.showMessageDialog(null, "Falha ao compilar arquivo!\nVerifique seu código", "Mini Java Compiler",JOptionPane.INFORMATION_MESSAGE);
            result = 1;
        }
        //if (result == 0)
           // JOptionPane.showMessageDialog(null, "Arquivo compilado com sucesso", "Mini Java Compiler",JOptionPane.INFORMATION_MESSAGE); 
    }
    
    private void program() throws Exception
    {
        //mainClass();

        while (lToken.name == EnumToken.CLASS) 
            classList();

        match(EnumToken.EOF);
        
    }
    
    private void classList() throws Exception
    {
    	classDecl();
    }
    
    private void classDecl() throws Exception
    {
    	match(EnumToken.CLASS);
    	last = lToken;
    	match(EnumToken.ID);
    	if ( lToken.name == EnumToken.EXTENDS )
    	{
    		last = lToken;
    		match(EnumToken.EXTENDS);
    		match(EnumToken.ID);
    	}
    	classBody();
    	
    	
    }
    
    private void classBody() throws Exception
    {
    	match(EnumToken.LKEY);
    	SymbolTable temp = new SymbolTable();
    	temp.parent = currentST;
    	currentST = temp;
    	VarDeclList();
    	ConstructDeclList();
    	MethodDeclList();
    	match(EnumToken.RKEY);
    	globalST.add(new STEntry(currentST, new Token(EnumToken.ID), lToken.value));
    	currentST = currentST.parent;
    }
    
    private void VarDeclList() throws Exception {
        while (lToken.name == EnumToken.INTEGER || lToken.name == EnumToken.FLOAT || lToken.name ==  EnumToken.DOUBLE || lToken.name == EnumToken.ID ) 
        {
        	last = lToken;
        	VarDecl();
        } 
    }
    
    private void VarDecl() throws Exception
    {
    	match(EnumToken.TYPE);
    	if ( lToken.name == EnumToken.LBRACKET) 
    	{
    		match(EnumToken.LBRACKET);
    		match(EnumToken.RBRACKET);
    		STEntry entry = new STEntry(currentST, lToken, "" );
    		currentST.add(entry);
    		VarDeclOpt();
    		match(EnumToken.DOTEND);
    	}
    	else
    	{
    		match(EnumToken.ID);
    		VarDeclOpt();
    		match(EnumToken.DOTEND);
    	}
    	 	
    }
    
    private void VarDeclOpt() throws Exception
    {
    	while ( lToken.name == EnumToken.COMMA)
    	{
	    	match(EnumToken.COMMA);
	    	match(EnumToken.ID);
    	}
    	
    }
    
    private void Type() throws Exception
    {
    	switch ( lToken.name )
    	{
    	case ID:
    		match(EnumToken.ID);
    		break;
    	case INTEGER:
    		match(EnumToken.INTEGER);
    		break;
    	case DOUBLE:
    		match(EnumToken.DOUBLE);
    		break;
    	case STRING:
    		match(EnumToken.STRING);
    		break;
    	}
    }
    
    private void ConstructDecList() throws Exception
    {
    	while ( lToken.name == EnumToken.CONSTRUCTOR)
    	{
    		match(EnumToken.CONSTRUCTOR);
    		MethodBody();
    	}
    }
    
    private void MethodDeclList() throws Exception
    {
    	while ( lToken.name == EnumToken.INTEGER || lToken.name == EnumToken.DOUBLE || lToken.name == EnumToken.STRING || lToken.name == EnumToken.ID  )
    	{
    		last = lToken;
    		MethodDecl();
    	}
    }
    
    private void MethodDecl() throws Exception
    {
    	match(EnumToken.TYPE);
    	if ( lToken.name == EnumToken.LBRACKET)
    	{
    		match(EnumToken.LBRACKET);
    		match(EnumToken.RBRACKET);
    		STEntry entry = new STEntry(currentST, lToken, "" );
    		currentST.add(entry);
    		MethodBody();
    	}
    	else
    	{
        	match(EnumToken.ID);
        	MethodBody();
    	}
    }
    
    private void MethodBody() throws Exception
    {
    	match(EnumToken.LPARENTHESE);
    	ParamListOpt();
    	match(EnumToken.RPARENTHESE);
    	match(EnumToken.LBRACKET);
    	Statements();
    	match(EnumToken.RBRACKET);
    }
    
    private void ParamListOpt() throws Exception
    {
    	ParamList();
    }
    
    private void ParamList() throws Exception
    {
    	while ( lToken.name == EnumToken.INTEGER || lToken.name == EnumToken.DOUBLE || lToken.name == EnumToken.STRING || lToken.name == EnumToken.ID  )
    	{
    		match(EnumToken.COMMA);
    		Param();
    	}
    }
    
    private void Param() throws Exception
    {
    	match(EnumToken.TYPE);
    	if( lToken.name == EnumToken.LBRACKET)
    	{
    		match(EnumToken.LBRACKET);
    		match(EnumToken.RBRACKET);
    		STEntry entry = new STEntry(currentST, lToken, "" );
    		currentST.add(entry);
    	}
    	else
    	{
    		STEntry entry = new STEntry(currentST, lToken, "" );
    		currentST.add(entry);
    	}
    }
    
    private void Statements() throws Exception
    {
    	while ( lToken.name == EnumToken.PRINT || lToken.name == EnumToken.READ || lToken.name == EnumToken.RETURN || lToken.name == EnumToken.SUPER || lToken.name == EnumToken.IF || lToken.name == EnumToken.FOR || lToken.name == EnumToken.BREAK || lToken.name == EnumToken.DOTEND || lToken.name == EnumToken.ID )
    	{
    		Statement();
    	}
    }
    
    private void Statement() throws Exception
    {
    	switch ( lToken.name )
    	{
    	case PRINT:
    		PrintStat();
    		match(EnumToken.DOTEND);
    		break;
    	
    	case READ:
    		ReadStat();
    		match(EnumToken.DOTEND);
    		break;
    		
    	case RETURN:
    		ReturnStat();
    		match(EnumToken.DOTEND);
    		break;
    	
    	case SUPER:
    		SuperStat();
    		match(EnumToken.DOTEND);
    		break;
    		
    	case IF:
    		IfStat();
    		match(EnumToken.DOTEND);
    		break;
    		
    	case FOR:
    		ForStat();
    		match(EnumToken.DOTEND);
    		break;
    		
    	case BREAK:
    		match(EnumToken.BREAK);
    		match(EnumToken.DOTEND);
    		break;
    	
    	case DOTEND:
    		match(EnumToken.DOTEND);
    		break;
    		
    	}
    		
    		//verificar var declarition e attrStat
    }
    
    private void PrintStat() throws Exception
    {
    	match(EnumToken.PRINT);
    	Expression();
    }
    
    private void ReadtStat() throws Exception
    {
    	match(EnumToken.READ);
    	LValue();
    }
    
    private void ReturnStat() throws Exception
    {
    	match(EnumToken.RETURN);
    	Expression();
    }
    
    private void SuperStat() throws Exception
    {
    	match(EnumToken.SUPER);
    	match(EnumToken.LPARENTHESE);
    	ArgListOpt();
    	match(EnumToken.RPARENTHESE);
    }
    
    private void IfStat() throws Exception
    {
    	match(EnumToken.IF);
    	match(EnumToken.LPARENTHESE);
    	Expression();
    	match(EnumToken.RPARENTHESE);
    	match(EnumToken.LKEY);
    	Statements();
    	match(EnumToken.RKEY);
    	if ( lToken.name == EnumToken.ELSE)
    	{
    		match(EnumToken.ELSE);
    		match(EnumToken.LKEY);
    		Statements();
    		match(EnumToken.RKEY);
    	}
    }
    
    private void ForStat() throws Exception
    {
    	match(EnumToken.FOR);
    	match(EnumToken.LPARENTHESE);
    	AttribStatOpt();
    	match(EnumToken.DOTEND);
    	ExpressionOpt();
    	match(EnumToken.DOTEND);
    	AttribStatOpt();
    	match(EnumToken.RPARENTHESE);
    	match(EnumToken.LKEY);
    	Statements();
    	match(EnumToken.RKEY);
    }
    	
    private void AttribStatOpt() throws Exception
    {
    	if( )
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    private void advance()
    {
        lToken = scan.nextToken();
    }
    
    private void match(EnumToken cTokenName) throws Exception
    {
    	System.out.println (lToken.name + "==" + cTokenName);
        if (lToken.name == cTokenName)
        {
        	if ( cTokenName == EnumToken.ID)
        	{
        		STEntry entry = new STEntry(last, lToken.value);
        		currentST.add(entry);
        	}
            advance();
        }
        else if(lToken.name != cTokenName){
           // JOptionPane.showMessageDialog(null, "Erro na linha: " + lToken.line, "Mini Java Compiler - ERROR",JOptionPane.INFORMATION_MESSAGE);
            advance();
        }
            
        else 
        {            //Erro
           // JOptionPane.showMessageDialog(null, "Token inesperado", "Mini Java Compiler - ERROR",JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
}
