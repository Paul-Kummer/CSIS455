package intercode.lexer ;

public class Real extends Token // Should this extend Num???
{ 
    public final float value ;
    
    public Real(float v) 
    {     
        super(Tag.REAL); 
        value = v; 
    }

    public String toString() 
    { 
        return "" + value ; 
    }

    public float getValue()
    {
        return this.value;
    }
}
