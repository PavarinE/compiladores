package minijavacompiler;

import java.util.concurrent.ExecutionException;

import javax.swing.event.TreeSelectionEvent;

/**
 * @author Lucas Tomaz Heck
 */
public class Parser 
{
    private Scanner scan;
    private Token lToken;
    private SymbolTable<STEntry> globalST;
    private SymbolTable currentST;
    private Token last;
    private int lastType;
    
    public Parser(String inputFile)    
    {
        //Instancia o analisador lÃ©xico
    	
        scan = new Scanner(inputFile);
        
        globalST = new SymbolTable();
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
           program(); // metodo que inicializa as verificacoes de "matches" 
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

        
            classList();

        match(EnumToken.EOF);
        
    }
    
    private void classList() throws Exception
    {
    	while (lToken.name == EnumToken.CLASS) 
    	{
    		classDecl();
    	}
    }
    
    private void classDecl() throws Exception
    {
    	match(EnumToken.CLASS);
    	STEntry entry = new STEntry(lToken, " "); 
    	entry.className = lToken.value;
    	currentST = new SymbolTable();
    	if ( !globalST.add(entry))
    	{
    		System.out.println("Erro: Redefinição da classe " + lToken.value + " na linha " + lToken.line);//////////////////////////////////////////////////////
    	}
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
    	VarDeclList();
    	ConstructDeclList();
    	MethodDeclList();
    	match(EnumToken.RKEY);
    }
    
    private void VarDeclList() throws Exception {
        while (lToken.name == EnumToken.INTEGER || lToken.name == EnumToken.STRING || lToken.name ==  EnumToken.DOUBLE || lToken.name == EnumToken.ID ) 
        {
        	last = lToken;
        	VarDecl();
        } 
    }
    
    private void VarDecl() throws Exception
    {
    	Type();
    	
    	if ( lToken.name == EnumToken.LBRACKET) 
    	{
    		match(EnumToken.LBRACKET);
    		match(EnumToken.RBRACKET);
    		lToken.type = this.lastType;
    		STEntry ste = new STEntry(lToken, lToken.value);
    		if ( !currentST.add(ste) )
    		{
    			System.out.println("Erro: Redefinição de: " + lToken.value.toLowerCase() + " na linha " + lToken.line);
    		}
    		match(EnumToken.ID);

    		if( lToken.name == EnumToken.LPARENTHESE)
    		{
    			MethodBody();
    		}
    		else
    		{
        		VarDeclOpt();
        		match(EnumToken.DOTEND);
    		}
    		
    		
    	}
    	else
    	{
    		lToken.type = this.lastType;
    		STEntry ste = new STEntry(lToken, lToken.value);
    		if ( !currentST.add(ste) )
    		{
    			System.out.println("Erro: Redefinição de: " + lToken.value.toLowerCase() + " na linha " + lToken.line);
    		}
    		match(EnumToken.ID);
    		if( lToken.name == EnumToken.LPARENTHESE)
    		{
    			MethodBody();
    		}
    		else
    		{
	    		VarDeclOpt();
	    		match(EnumToken.DOTEND);
    		}
    	}
    	 	
    }
    
    private void VarDeclOpt() throws Exception
    {
    	while ( lToken.name == EnumToken.COMMA)
    	{
    		lToken.type = this.lastType;
	    	match(EnumToken.COMMA);
	    	STEntry ste = new STEntry(lToken, lToken.value);
    		if ( !currentST.add(ste) )
    		{
    			System.out.println("Erro: Redefinição de: " + lToken.value + " na linha " + lToken.line);
    		}
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
    		this.lastType = 1;
    		match(EnumToken.INTEGER);
    		break;
    	case DOUBLE:
    		this.lastType = 2;
    		match(EnumToken.DOUBLE);
    		break;
    	case STRING:
    		this.lastType = 3;
    		match(EnumToken.STRING);
    		break;
    	}
    }
    
    private void ConstructDeclList() throws Exception
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
    	Type();
    	if ( lToken.name == EnumToken.LBRACKET)
    	{
    		match(EnumToken.LBRACKET);
    		match(EnumToken.RBRACKET);
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
    	match(EnumToken.LKEY);
    	Statements();
    	match(EnumToken.RKEY);
    	currentST = currentST.parent;
    }
    
    private void ParamListOpt() throws Exception
    {
    	ParamList();
    }
    
    private void ParamList() throws Exception
    {
    	SymbolTable st = new SymbolTable();
    	st.parent = currentST;
    	currentST = st;
    	if (lToken.name == EnumToken.INTEGER || lToken.name == EnumToken.DOUBLE || lToken.name == EnumToken.STRING || lToken.name == EnumToken.ID)
    	{
    		Param();
    	}
    	while ( lToken.name == EnumToken.COMMA  )
    	{
    		match(EnumToken.COMMA);
    		Param();
    	}
    }
    
    private void Param() throws Exception
    {
    	Type();
    	if( lToken.name == EnumToken.LBRACKET)
    	{
    		match(EnumToken.LBRACKET);
    		match(EnumToken.RBRACKET);
    	}
    	lToken.type = this.lastType;
    	STEntry ste = new STEntry(lToken, lToken.value);
		if ( !currentST.add(ste) )
		{
			System.out.println("Erro: Redefinição do parametro " + lToken.value.toLowerCase() + " na linha " + lToken.line);
		}  
    	match(EnumToken.ID);
    }
    
    private void Statements() throws Exception
    {
    	
    	while ( lToken.name == EnumToken.PRINT || lToken.name == EnumToken.READ || lToken.name == EnumToken.RETURN || lToken.name == EnumToken.SUPER || lToken.name == EnumToken.IF || lToken.name == EnumToken.FOR || lToken.name == EnumToken.BREAK || lToken.name == EnumToken.DOTEND || lToken.name == EnumToken.ID || lToken.name == EnumToken.INTEGER || lToken.name == EnumToken.STRING || lToken.name == EnumToken.DOUBLE )
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
    		break;
    		
    	case FOR:
    		ForStat();
    		break;
    		
    	case BREAK:
    		match(EnumToken.BREAK);
    		match(EnumToken.DOTEND);
    		break;
    	
    	case DOTEND:
    		match(EnumToken.DOTEND);
    		break;
    		
    	case INTEGER:
    		VarDeclList();
    		break;
    	case STRING:
    		VarDeclList();
    		break;
    	case DOUBLE:
    		VarDeclList();
    		break;
    		
    	case ID:
    		String temp = new String(lToken.value);
    		match(EnumToken.ID);
    		if( lToken.name == EnumToken.LBRACKET)
    		{
    			match(EnumToken.LBRACKET);
    			if (lToken.name == EnumToken.RBRACKET )
    			{
    				match(EnumToken.RBRACKET);
    				match(EnumToken.ID);
    				VarDeclOpt();
    				match(EnumToken.DOTEND);
    				
    			}
    			else
    			{
    				if ( !currentST.symbols.containsKey(temp))
    				{
    					System.out.println("A variavel: " + temp.toLowerCase() + " não existe no escopo atual");
    				}
    				Expression();
    				match(EnumToken.RBRACKET);
    				LValueComp();
    				match(EnumToken.ASSIGN);
    				if ( lToken.name == EnumToken.NEW || lToken.name == EnumToken.INTEGER || lToken.name == EnumToken.DOUBLE || lToken.name == EnumToken.STRING || lToken.name == EnumToken.ID )
    				{
    					AllocExpression();
    				}
    				else
    				{
    					Expression();
    				}
    			}
    			
    		}
    		
    		if( lToken.name == EnumToken.DOT)
    		{
    			LValueComp();
    			match(EnumToken.ASSIGN);
    			Expression();
    			
    		}
    		
    		if ( lToken.name == EnumToken.ASSIGN)
    		{
    			if ( !currentST.symbols.containsKey(temp))
    			{
    				System.out.println("A variavel: " + temp.toLowerCase() + " não existe no escopo atual");
    			}
    			match(EnumToken.ASSIGN);
    			Expression();
    		}
    		
    		if ( lToken.name == EnumToken.ID) 
    		{
    			match(EnumToken.ID);
    			match(EnumToken.DOTEND);
    		}
    		
    	}
    		
    		//verificar var declarition e attrStat
    }
    
    private void PrintStat() throws Exception
    {
    	match(EnumToken.PRINT);
    	Expression();
    }
    
    private void ReadStat() throws Exception
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
    	if( lToken.name == EnumToken.ID)
    	{
    		LValue();
    		match(EnumToken.ASSIGN);
    		if( lToken.name == EnumToken.PLUS || lToken.name == EnumToken.MINUS  )
    		{
    			Expression();
    		}
    		else
    		{
    			AllocExpression();
    		}
    	}
    	
    }
    
    private void ExpressionOpt()  throws Exception
    {
    	if( lToken.name == EnumToken.PLUS || lToken.name == EnumToken.MINUS  )
    	{
    		Expression();	
    	}
    }
    
    private void Expression() throws Exception
    {
    	NumExpression();
    	if ( lToken.name == EnumToken.RELOP)
    	{
    		match(EnumToken.RELOP);
    		NumExpression();
    		
    	}
    }
    
    private void NumExpression() throws Exception
    {
    	Term();
    	if( lToken.name == EnumToken.PLUS )
    	{
    		match(EnumToken.PLUS);
    		Term();
    	}
    	else if( lToken.name == EnumToken.MINUS )
    	{
    		match(EnumToken.MINUS);
    		Term();
    	}

    }
    
    private void Term() throws Exception
    {
    	UnaryExpression();
    	if( lToken.name == EnumToken.TIMES)
    	{
    		match(EnumToken.TIMES);
    		UnaryExpression();
    	}
    	else if( lToken.name == EnumToken.DIVIDES )
    	{
    		match(EnumToken.DIVIDES);
    		UnaryExpression();
    	}
    	else if (lToken.name == EnumToken.MOD )
    	{
    		match(EnumToken.MOD);
    		UnaryExpression();
    	}
    	
    }
    
    private void UnaryExpression() throws Exception
    {
    	if(  lToken.name == EnumToken.PLUS )
    	{
    		match(EnumToken.PLUS);
    		Factor();
    	}
    	else if ( lToken.name == EnumToken.MINUS)
    	{
    		match(EnumToken.MINUS);
    		Factor();
    	}
    }
    
    private void Factor() throws Exception
    {
    	if (  lToken.name == EnumToken.TEXT )
    	{
    		match(EnumToken.TEXT);
    	}
        else if (  lToken.name == EnumToken.NUMBER )
    	{
    		match(EnumToken.NUMBER);
     	}
    	/*else if (  lToken.name == EnumToken.DOUBLE_LITERAL )
    	{
    		match(EnumToken.STRING);
    	}*/
    	else if (  lToken.name == EnumToken.ID )
    	{
    		if (!currentST.symbols.containsKey(lToken.value))
    		{
    			System.out.println("A variavel " + lToken.value.toLowerCase() + " não foi declarada no escopo atual");
    		}
    		LValue();
    	}
    	else if( lToken.name == EnumToken.LPARENTHESE)
    	{
    		match(EnumToken.LPARENTHESE);
    		Expression();
    		match(EnumToken.RPARENTHESE);
    	}
    }
    
    private void ArgListOpt() throws Exception
    {
    	if ( lToken.name == EnumToken.PLUS || lToken.name == EnumToken.MINUS )
    	{
	    	Expression();
	    	while ( lToken.name == EnumToken.COMMA  )
	    	{
	    		match(EnumToken.COMMA);
	    		Expression();
	    	}
    	}
    	
    }
    
    private void LValue() throws Exception
    {
    	match(EnumToken.ID);
    	if ( lToken.name == EnumToken.LBRACKET)
    	{
    		match(EnumToken.LBRACKET);
    		Expression();
    		match(EnumToken.RBRACKET);
    	}
    	LValueComp();
    		
    }
    
    private void LValueComp() throws Exception
    {
    	if ( lToken.name == EnumToken.DOT)
    	{
    		match(EnumToken.DOT);
    		match(EnumToken.ID);
    		if ( lToken.name == EnumToken.LBRACKET)
        	{
        		match(EnumToken.LBRACKET);
        		Expression();
        		match(EnumToken.RBRACKET);
        	}
    		LValueComp();
    	}
    }
    
    private void AllocExpression() throws Exception
    {
    	if( lToken.name == EnumToken.NEW )
    	{
    		match(EnumToken.NEW);
    		match(EnumToken.ID);
    		match(EnumToken.LPARENTHESE);
    		ArgListOpt();
    		match(EnumToken.RPARENTHESE);
    		
    	}
    	else
    	{
    		Type();
    		match(EnumToken.LBRACKET);
    		Expression();
    		match(EnumToken.RBRACKET);
    		
    	}
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    private void advance()
    {
        lToken = scan.nextToken();
    }
    
    private void match(EnumToken cTokenName) throws Exception
    {
    	//System.out.println (lToken.name + "==" + cTokenName);
        if (lToken.name == cTokenName)
        {
        	/*if ( cTokenName == EnumToken.ID)
        	{
        		STEntry entry = new STEntry(last, lToken.value);
        		currentST.add(entry);
        	}*/
        	System.out.println ("Linha "+lToken.line+" Match: " + cTokenName);
            advance();
        }
        else if(lToken.name != cTokenName){
           // JOptionPane.showMessageDialog(null, "Erro na linha: " + lToken.line, "Mini Java Compiler - ERROR",JOptionPane.INFORMATION_MESSAGE);
        	System.out.println ("Linha "+ lToken.line+" Esperado: " + cTokenName + " Encontrado: " + lToken.name);
            advance();
        }
            
        else 
        {            //Erro
           // JOptionPane.showMessageDialog(null, "Token inesperado", "Mini Java Compiler - ERROR",JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
}
