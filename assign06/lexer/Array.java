package assign6.lexer ;

public class Array extends Type 
{
    public int size = 0;
	public Type type;

	public static final Array Int   	= new Array(Type.Int);
	public static final Array Float  	= new Array(Type.Float);
	public static final Array Char   	= new Array(Type.Char);
	public static final Array Boolean	= new Array(Type.Boolean);
	public static final Array Bool		= new Array(Type.Bool);
	//public static final Array Arr		= new Array(Array.Arr);
    
    public Array() 
    {     
		super();
    }

    public Array(Type type) 
    {     
        this.type = type;
    }

	public Array(String s, int tag, int w)
	{
		super(s, tag, w);
		this.type = new Type(s, tag, w);
	}

    public String toString() 
    { 
        return this.type + "[]"; 
    }

	public void printNode () 
    {
        System.out.println("Array: " + this.type) ;
    }
}
