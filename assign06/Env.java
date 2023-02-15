package assign6;

import assign6.lexer.*;
import assign6.parser.*;
import assign6.ast.*;
import java.util.*;

public class Env
{
    private Hashtable table;
    protected Env prev;
    
    public Env () 
    {
        this.table = new Hashtable();
    }

    public Env (Env n) 
    {
        table = new Hashtable();
        prev = n;
    }

    public void put(Token w, IdentifierNode id) 
    {
        table.put(w, id);
    }

    public IdentifierNode get(Token w)
    {
        if(w != null)
        {
            for (Env e = this; e != null; e = e.prev)
            {
                IdentifierNode found = (IdentifierNode)(e.table.get(w));
                if (found != null)
                {
                    return found;
                }     
            }
        }
        
        return null;
    }
}
