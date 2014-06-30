/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package minijavacompiler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.StringCharacterIterator;

/**
 * @author bianca
 */
public class Scanner 
{
    private static String input;
    private StringCharacterIterator inputIt;
    public int line;

    
    public Scanner(String inputString)
    {
    	this.line = 1;
      /*  File inputFile = new File(inputFileName);       
        
        try
        {
            FileReader fr = new FileReader(inputFile);
            
            int size = (int)inputFile.length();            
            char[] buffer = new char[size];
        
            fr.read(buffer, 0, size);*/
            
            input = new String(inputString);
            
            inputIt = new StringCharacterIterator(input);
            //System.out.println ("ComeÁando a leitura do arquivo: " + inputFileName);
            
       /* }
        catch(FileNotFoundException e)
        {
            System.err.println("Arquivo n„o encontrado");
        }
        catch(IOException e)
        {
            System.err.println("Erro na leitura do arquivo");
        }*/
    }
    
    public Token nextToken()
    {
        Token tok = new Token(EnumToken.UNDEF);        
        
        int state = 0;
        int begin = 0, end = 0;
        String lexema;        

        if (inputIt.getIndex() >= inputIt.getEndIndex())            
            tok.name = EnumToken.EOF;
        else
        {
            while (state != -1)
            {       
                //System.out.println("caracter:"+inputIt.current());
                switch (state)
                {
                    case 0:
                        if (inputIt.current() == '<')
                            state = 1;
                        else if (inputIt.current() == '=')
                            state = 5;
                        else if (inputIt.current() == '>')
                            state = 6;
                        else if (Character.isLetter(inputIt.current()) || (inputIt.current() == '_')) {
                            state = 9;
                            begin = inputIt.getIndex();
                        } else if (Character.isDigit(inputIt.current()))
                            state = 11;
                        else if (inputIt.current() == ' ' || inputIt.current() == '\r')
                            state = 20;
                        else if (inputIt.current() == ';')
                            state = 21;
                        else if (inputIt.current() == '(')
                            state = 22;
                        else if (inputIt.current() == ')')
                            state = 23;
                        else if (inputIt.current() == '[')
                            state = 24;
                        else if (inputIt.current() == ']')
                            state = 25;
                        else if (inputIt.current() == '{')
                            state = 26;
                        else if (inputIt.current() == '}')
                            state = 27;
                        else if (inputIt.current() == ';')
                            state = 21;
                        else if (inputIt.current() == '/')
                            state = 29;
                        else if (inputIt.current() == '"')
                            state = 30;
                        else if (inputIt.current() == '+' || inputIt.current() == '-' || inputIt.current() == '*' || inputIt.current() == '%' ) {
                        	  if (inputIt.current() == '+') {
                                  tok.name = EnumToken.PLUS;
                              } else if (inputIt.current() == '-') {
                                  tok.name = EnumToken.MINUS;
                              } else if (inputIt.current() == '*') {
                                  tok.name = EnumToken.TIMES;
                              } else if (inputIt.current() == '/') {
                                  tok.name = EnumToken.DIVIDES;
                              } else if (inputIt.current() == '%') {
                                  tok.name = EnumToken.MOD;
                              }
                            state = -1;
                        } else if (inputIt.current() == '.') {
                            tok.name = EnumToken.DOT;
                            state = -1;
                        } else if (inputIt.current() == ',') {
                            tok.name = EnumToken.COMMA;
                            state = -1;
                        } else if (inputIt.current() == '!') {
                            if (inputIt.getIndex()+1>= inputIt.getEndIndex()) {
                                System.err.print("Token n„o identificado na linha " + this.line);
                                state = -1;
                            } else {
                                inputIt.next();
                                if (inputIt.current() == '=') {
                                    tok.name = EnumToken.RELOP;
                                    tok.attribute = EnumToken.NE;
                                    state = -1;
                                } else {
                                    System.err.print("Token n„o identificado na linha " + this.line);
                                    state = -1;
                                }
                            }
                        } else if ((inputIt.getIndex()+1) >= inputIt.getEndIndex()) {
                            tok.name = EnumToken.EOF;
                            state = -1;
                        } else
                            System.err.print("Token n„o identificado na linha " + this.line);
                        inputIt.next();
                    break;
                    case 1:
                        if (inputIt.current() == '=')
                            state = 2;
                        else if (inputIt.current() == '>')
                            state = 3;
                        else { 
                            state = 4;
                            inputIt.previous();
                        }
                        inputIt.next();
                    break;
                    case 2:
                        tok.name = EnumToken.RELOP;
                        tok.attribute = EnumToken.LE;
                        state = -1;
                    break;
                    case 4:
                        tok.name = EnumToken.RELOP;
                        tok.attribute = EnumToken.LT;
                        state = -1;
                    break;
                    case 5:
                        if (inputIt.current() == '=') {
                            state = 28;
                        } else {
                            tok.name = EnumToken.ASSIGN;
                            inputIt.previous();
                            state = -1;
                        }
                        inputIt.next();
                    break;
                    case 6:
                        if (inputIt.current() == '=')
                            state = 7;
                        else {
                            state = 8;
                            inputIt.previous();
                        }
                        inputIt.next();
                    break;
                    case 7:
                        tok.name = EnumToken.RELOP;
                        tok.attribute = EnumToken.GE;
                        state = -1;
                    break;
                    case 8:
                        tok.name = EnumToken.RELOP;
                        tok.attribute = EnumToken.GT;
                        state = -1;
                    break;
                    case 9:
                        while ((Character.isLetterOrDigit(inputIt.current())) || (inputIt.current() == '_'))
                            inputIt.next();
                        end = (inputIt.getIndex());
                        lexema = new String(input.substring(begin, end));
                        String text = lexema.toUpperCase();
                        switch (text) {
                            case "IF":
                                tok.name = EnumToken.IF;
                                break;
                            case "THEN":
                                tok.name = EnumToken.THEN;
                                break;
                            case "ELSE":
                                tok.name = EnumToken.ELSE;
                                break;
                            case "INT":
                                tok.name = EnumToken.INTEGER;
                                break;
                            case "DOUBLE":
                                tok.name = EnumToken.DOUBLE;
                                break;
                            case "STRING":
                                tok.name = EnumToken.STRING;
                                break;
                            case "CONSTRUCTOR":
                                tok.name = EnumToken.CONSTRUCTOR;
                                break;
                            case "CLASS":
                                tok.name = EnumToken.CLASS;
                                break;
                            case "EXTENDS":
                                tok.name = EnumToken.EXTENDS;
                                break;
                            case "NEW":
                                tok.name = EnumToken.NEW;
                                break;
                            case "MAIN":
                                tok.name = EnumToken.MAIN;
                                break;
                            case "FOR":
                                tok.name = EnumToken.FOR;
                                break;
                            case "RETURN":
                                tok.name = EnumToken.RETURN;
                                break;
                            case "PRINT":
                                tok.name = EnumToken.PRINT;
                                break;
                            case "READ":
                                tok.name = EnumToken.READ;
                                break;
                            case "SUPER":
                                tok.name = EnumToken.SUPER;
                                break;
                            case "WHILE":
                                tok.name = EnumToken.WHILE;
                                break;
                            case "DO":
                                tok.name = EnumToken.DO;
                                break;
                            case "SWITCH":
                                tok.name = EnumToken.SWITCH;
                                break;
                            case "CASE":
                                tok.name = EnumToken.CASE;
                                break;
                            case "BREAK":
                                tok.name = EnumToken.BREAK;
                                break;
                            default:
                                tok.name = EnumToken.ID;
                                tok.value = text;
                                break;
                        }
                        state = -1;
                    break;
                    case 11:
                        while (Character.isDigit(inputIt.current()))
                            inputIt.next();
                        tok.name = EnumToken.NUMBER;
                        state = -1;
                    break;
                    case 20:
                        while (inputIt.current() == ' ' || inputIt.current() == '\r' || inputIt.current() == '\n' || inputIt.current() == '\t') {
                        	  if (inputIt.current() == '\n') {
                                  this.line++;
                              }
                            if (inputIt.getIndex()+1>= inputIt.getEndIndex()) {
                                tok.name = EnumToken.EOF;
                                state = -1;
                            }
                            inputIt.next();
                        }
                        if (state!=-1)
                            state = 0;
                    break;
                    case 21:
                        tok.name = EnumToken.DOTEND;
                        state = -1;
                    break;
                    case 22:
                        tok.name = EnumToken.LPARENTHESE;
                        state = -1;
                    break;
                    case 23:
                        tok.name = EnumToken.RPARENTHESE;
                        state = -1;
                    break;
                    case 24:
                        tok.name = EnumToken.LBRACKET;
                        state = -1;
                    break;
                    case 25:
                        tok.name = EnumToken.RBRACKET;
                        state = -1;
                    break;
                    case 26:
                        tok.name = EnumToken.LKEY;
                        state = -1;
                    break;
                    case 27:
                        tok.name = EnumToken.RKEY;
                        state = -1;
                    break;
                    case 28:
                        tok.name = EnumToken.RELOP;
                        tok.attribute = EnumToken.EQ;
                        state = -1;
                    break;
                    case 29:
                        if ((inputIt.getIndex())==inputIt.getEndIndex()) {
                                    state = -1;
                                    tok.name = EnumToken.DIVIDES;
                        } else {
                            if (inputIt.current() == '/') {
                                while (inputIt.current() != '\r' && state==29) {
                                    if (inputIt.getIndex()+1==inputIt.getEndIndex()) {
                                        tok.name = EnumToken.EOF;
                                        state = -1;
                                    } else {
                                        inputIt.next();
                                    }
                                }
                                if (state!=-1)
                                    state = 0;
                            } else if (inputIt.current() == '*') {
                                while (state == 29) {
                                    if ((inputIt.getIndex()+1)==inputIt.getEndIndex()) {
                                        System.err.print("Coment√°rio sem fim!");
                                        tok.name = EnumToken.EOF;
                                        state = -1;
                                    } else {
                                        inputIt.next();
                                        if (inputIt.current() == '*') {
                                            if ((inputIt.getIndex()+1) < inputIt.getEndIndex()) {
                                                if (inputIt.next() == '/') {
                                                    inputIt.next();
                                                    state = 0;
                                                } else {
                                                    inputIt.previous();
                                                }
                                            } else {
                                                System.err.print("Coment√°rio sem fim!");
                                                tok.name = EnumToken.EOF;
                                                state = -1;
                                            }
                                        }
                                    }
                                }
                            } else {
                                tok.name = EnumToken.DIVIDES;
                                state = -1;
                            }
                        }
                    break;
                    case 30:
                        while (state==30 && inputIt.current() != '"') {
                            if (inputIt.getIndex()+1 == inputIt.getEndIndex()) {
                                System.err.print("Cadeia de caracter sem fim!");
                                tok.name = EnumToken.EOF;
                                state = -1;
                            } else {
                                inputIt.next();
                            }
                        }
                        if (inputIt.getIndex()+1 != inputIt.getEndIndex()) {
                            inputIt.next();
                            tok.name = EnumToken.TEXT;
                            state = -1;
                        }
                    break;
                    default:
                        System.err.println(inputIt.current());
                    break;

                    
                    /*case 0:
                        if (inputIt.current() == '<')
                            state = 1;
                        else if (inputIt.current() == '=')
                            state = 5;
                        else if (inputIt.current() == '>')
                            state = 6;
                        else if (Character.isLetter(inputIt.current()))
                            state = 9;//10
                        else if (Character.isDigit(inputIt.current()))
                            state = 11; //13       
                        else if (Character.isWhitespace(inputIt.current()))
                            state = 20;
                        else
                        {
                            System.err.println("S√≠mbolo desconhecido.");
                            state = -1;
                        }

                        begin = inputIt.getIndex();
                        inputIt.next();

                        break;
                    case 1:
                        if (inputIt.current() == '=')
                            state = 2;
                        else if (inputIt.current() == '>')
                            state = 3;
                        else
                            state = 4;

                        inputIt.next();                           

                        break;
                    case 2:
                        tok.name = EnumToken.RELOP;
                        tok.attribute = EnumToken.LE;
                        state = -1;

                        break;
                    case 3:
                        tok.name = EnumToken.RELOP;
                        tok.attribute = EnumToken.NE;
                        state = -1;

                        break;
                    case 4:
                        tok.name = EnumToken.RELOP;
                        tok.attribute = EnumToken.LT;
                        state = -1;

                        break;
                    case 5:
                        tok.name = EnumToken.RELOP;
                        tok.attribute = EnumToken.EQ;
                        state = -1;

                        break;
                    case 6:
                        if (inputIt.current() == '=')                        
                            state = 7;                            
                        else
                            state = 8;

                        inputIt.next();                           

                        break;
                    case 7:
                        tok.name = EnumToken.RELOP;
                        tok.attribute = EnumToken.GE;
                        state = -1;

                        break;
                    case 8:
                        if (inputIt.current() != StringCharacterIterator.DONE)
                            inputIt.previous();

                        tok.name = EnumToken.RELOP;
                        tok.attribute = EnumToken.GT;
                        state = -1;

                        break;

                    case 9:
                        while (Character.isLetterOrDigit(inputIt.current()))
                            inputIt.next();                   

                        state = 10;
                        inputIt.next();

                        break;

                    case 10:
                        //Verificar se √© identificador ou palavra reservada
                        if (inputIt.current() != StringCharacterIterator.DONE)
                            inputIt.previous();
                        end = inputIt.getIndex();
                        lexema = new String(input.substring(begin, end));

                        if (lexema.equals("if"))                    
                            tok.name = EnumToken.IF;
                        else if (lexema.equals("else"))
                            tok.name = EnumToken.ELSE;
                        else if (lexema.equals("then"))
                            tok.name = EnumToken.THEN;
                        else
                            tok.name = EnumToken.ID;
                        
                        state = -1;

                        break;
                    case 11:
                        while (Character.isDigit(inputIt.current()))
                            inputIt.next();                    

                        if (inputIt.current() == '.')
                            state = 12; //14
                        else if (inputIt.current() == 'E')
                            state = 14; //16
                        else
                            state = 18; //20

                        inputIt.next();

                        break;
                    case 12:
                        //inputIt.next();
                        if (Character.isDigit(inputIt.current()))
                        {
                            state = 13; //15
                            inputIt.next();
                        }
                        else
                        {
                            System.out.println("Erro no reconhecimento de float");
                            state = -1;
                        }                
                        

                        break;
                    case 13:
                        while (Character.isDigit(inputIt.current()))
                            inputIt.next();                    

                        if (inputIt.current() == 'E')
                            state = 14; //16
                        else
                            state = 19; //21
                        
                        inputIt.next();

                        break;
                    case 14:
                        if (inputIt.current() == '+' || inputIt.current() == '-')
                            state = 15; //17
                        else if (Character.isDigit(inputIt.current()))
                            state = 16; //18
                        else
                        {
                            System.out.println("Erro no reconhecimento de float");
                            state = -1;
                        }                        

                        inputIt.next();

                        break;
                    case 15:
                        if (Character.isDigit(inputIt.current()))
                            state = 16;//18
                        else
                        {
                            System.out.println("Erro no reconhecimento de float");
                            state = -1;
                        }

                        inputIt.next();

                        break;
                    case 16:
                        while (Character.isDigit(inputIt.current()))
                            inputIt.next();                    

                        state = 17;
                        inputIt.next();

                        break;
                    case 17:
                        if (inputIt.current() != StringCharacterIterator.DONE)
                            inputIt.previous();
                        
                        end = inputIt.getIndex();                    
                        lexema = new String(input.substring(begin, end));

                        tok.name = EnumToken.NUMBER;
                        tok.attribute = EnumToken.FLOAT;
                        
                        state = -1;

                        break;
                    case 18:
                        if (inputIt.current() != StringCharacterIterator.DONE)
                            inputIt.previous();
                        
                        end = inputIt.getIndex();                    
                        lexema = new String(input.substring(begin, end));

                        tok.name = EnumToken.NUMBER;
                        tok.attribute = EnumToken.INTEGER;                        
                        
                        state = -1;

                        break;
                    case 19:
                        if (inputIt.current() != StringCharacterIterator.DONE)
                            inputIt.previous();
                        
                        end = inputIt.getIndex();                    
                        lexema = new String(input.substring(begin, end));

                        tok.name = EnumToken.NUMBER;
                        tok.attribute = EnumToken.FLOAT;
                        
                        state = -1;

                        break;
                    case 20:
                        //Consome espa√ßos em branco e volta para o estado inicial
                        while (Character.isWhitespace(inputIt.current()))
                            inputIt.next();

                        state = 0;
                */}// Fim switch
            }// Fim while
        }// Fim else
        tok.line = this.line;
        return tok;
    }
}