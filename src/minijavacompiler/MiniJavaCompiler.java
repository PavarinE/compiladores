package minijavacompiler;

/**
 * @author Lucas Tomaz Heck
 */
public class MiniJavaCompiler 
{
    public static void main(String[] args) 
    {
        Scanner scanner = new Scanner("teste1.mj");
        Token tok = new Token(EnumToken.UNDEF);
        tok.getClass();
        int i=0;
        do
        {
            if ((i%10) == 0) {
                System.out.println();
            }
            tok = scanner.nextToken();
            if (tok.name != EnumToken.EOF)
                if (tok.name != EnumToken.RELOP)
                    System.out.print(tok.name+" ");
                else
                    System.out.print(tok.attribute+" ");
            i++;
        } while (tok.name != EnumToken.EOF);
    }
}
