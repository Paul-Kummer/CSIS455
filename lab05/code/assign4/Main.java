/*Setting the CLASSPATH
export CLASSPATH=/home/remote/kummerpa/Documents/CSIS455_compilers/assign04

export PATH=$PATH:/home/remote/kummerpa/Documents/CSIS455_compilers/assign04/assign4
*/

package assign4 ;

import java.io.*;

import assign4.lexer.* ;
import assign4.parser.* ;
import assign4.pretty.* ;
    
public class Main 
{
    public static void main (String[] args) throws IOException, FileNotFoundException
    {
        Lexer lexer = new Lexer() ;
        Parser parser = new Parser(lexer) ;
        PrettyPrinter pretty = new PrettyPrinter(parser) ;
    }
}
