package minijavacompiler;

/**
 * @author Lucas Tomaz Heck
 */
public class MiniJavaCompiler 
{
    public static void main(String[] args) 
    {
        Parser parser = new Parser("teste1.mj");
        parser.execute();
    }
}
