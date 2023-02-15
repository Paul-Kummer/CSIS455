package intercode.ast;

import intercode.visitor.* ;
import intercode.lexer.*;
import intercode.ast.*;

public class ArrayAccessNode extends IdentifierNode
{
	public IdentifierNode id;
	public ArrayDimsNode index; //This is an ArrayDimsNode IE: [10][3] or just [10]
	public int indexVal = 0;
	public Boolean isArray = false;
	//public IdentifierNode returnVal = null; //This is already in IdentifierNode

	public ArrayAccessNode()
	{
		super();
	}

	public ArrayAccessNode (IdentifierNode id, ArrayDimsNode index)
	{
		super();
		//Node[] idArray = ((ArrayTypeNode)id.typeNode).array;
		//returnVal = idArray[index];
		if(index.size.value instanceof Num)
		{
			this.indexVal = ((Num)index.size.value).value;
		}
		else
		{
			this.indexVal = -1;
		}
		this.id = id;
		this.index = index;
		this.type = id.type;

		String temp = "" + id.lexeme + index.toString();

		this.w = new Word (temp, Tag.ARRAY);
	}

	
	public Boolean isValidIndex()
	{
		ArrayTypeNode currentArray = (ArrayTypeNode)this.id.typeNode.array;
		ArrayDimsNode attemptedIndex = index;
		int index;
		int arrSize;
		Boolean isValid = true; 

		while(attemptedIndex != null)
		{
			if(currentArray == null)
			{
				isValid = false;
				break;
			}
			
			if(attemptedIndex.size instanceof IdentifierNode)
			{
				//System.out.println("Is an Id: " + attemptedIndex.size);
				if(((IdentifierNode)attemptedIndex.size).storedVal.value instanceof Num)
				{
					index = ((Num)((IdentifierNode)attemptedIndex.size).storedVal.value).value;
					//System.out.println("Id stored val is num tok: " + index);
				}
				else
				{
					//System.out.println("default to -1");
					index = -1;
				}
			}
			else if(attemptedIndex.size.value instanceof Num)
			{
				index = ((Num)attemptedIndex.size.value).value;
			}
			else
			{
				//System.out.println("default to -1");
				index = -1;
			}

			//System.out.println("ArraySize: " + currentArray.size.value + " | " + index + " IndexRequest");
			if(index < currentArray.size.value && index >= 0)
			{
				isValid = true;
			}
			else
			{
				isValid = false;
				break;
			}

			//Advance to the next array dimension
			attemptedIndex = attemptedIndex.dim;
		
			if(currentArray.typeNode instanceof ArrayTypeNode)
			{
				isArray = true;
				currentArray = (ArrayTypeNode)currentArray.typeNode;
			}
			else
			{
				isArray = false;
				currentArray = null;
			}
			System.out.println("is the access dim an array?: " + (isArray?"true":"false"));
		}

		return isValid;
	}
	

	public String toString()
	{
		String returnStr = ((IdentifierNode)this.id).lexeme + ((ArrayDimsNode)this.index).toString();

		if(storedVal != null)
		{
			returnStr += " = ";
			if(storedVal instanceof NumNode)
			{
				returnStr += ((NumNode)this.storedVal).toString();
			}
			else if(storedVal instanceof RealNode)
			{
				returnStr += ((RealNode)this.storedVal).toString();
			}
			else if(storedVal instanceof BoolNode)
			{
				returnStr += ((BoolNode)this.storedVal).toString();
			}
			else if(storedVal instanceof IdentifierNode)
			{
				returnStr += ((IdentifierNode)this.storedVal).toString();
			}
			else if(storedVal instanceof ParenthesesNode)
			{
				returnStr += ((ParenthesesNode)this.storedVal).toString();
			}
			else if(storedVal instanceof BinaryNode)
			{
				returnStr += ((BinaryNode)this.storedVal).toString();
			}
			else if(storedVal instanceof ArrayAccessNode)
			{
				returnStr += ((ArrayAccessNode)this.storedVal).toString();
			}
		}

		return returnStr;
	}

	public void accept(ASTVisitor v) 
    {
        v.visit(this);
    }
}
