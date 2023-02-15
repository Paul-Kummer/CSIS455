import java.util.*;

public class ASTVisitor
{
	int level = 0;

	public void visit(CompilationUnit n)
	{
		System.out.print("Compilation Unit:\n");

		level++;
		Node left;
		Node right;
		Node operation;

		Queue<Token> newToks = new LinkedList<Token>();
		Token first = n.toks.poll();
		Token op = n.toks.peek();		

		if(op != null)
		{
			right = new CompilationUnit(n.toks);

			switch(first.tag)
			{
				case Tag.NUM: //the token is a number
					left = new LiteralNode(first.toString());
					break;
				case Tag.ID: //the token is a word
					left = new LiteralNode(first.toString());
					break;
				case (int) '(':
					while((char)n.toks.peek().tag != ')' &&  n.toks.peek() != null && (char)first.tag != ')')
					{
						newToks.add(n.toks.poll());
					}
					n.toks.poll(); //remove the ')' token
					left = new CompilationUnit(newToks);
					op = n.toks.peek();
					break;
				default:
					left = new LiteralNode(first.toString());
					break;
			}

			switch((char)op.tag)
			{	
				case '=':
					operation = new AssignmentNode(left, right);
					break;
				case '+':
					operation = new AdditionNode(left, right);
					break;
				case '-':
					operation = new SubtractionNode(left, right);
					break;
				case '*':
					operation = new MultiplicationNode(left, right);
					break;
				case '/':
					operation = new DivisionNode(left, right);
					break;
				case '%':
					operation = new ModuloNode(left, right);
					break;
				default:
					operation = left;
					break;		
			}
			n.toks.poll(); //remove the operation symbol
			operation.accept(this);
		}

		else
		{
			switch(first.tag)
			{
				case Tag.NUM: //the token is a number
					left = new LiteralNode(first.toString());
					dots();
					left.accept(this);
					break;
				case Tag.ID: //the token is a word
					dots();
					left = new LiteralNode(first.toString());
					left.accept(this);
					break;
				default:
					if(first.tag != Tag.EOF)
					{
						dots();
						left = new LiteralNode(first.toString());
						left.accept(this);
					}
					break;
			}
		}
		level--;
	}
	
	public void visit(AssignmentNode n)
	{
		dots();
		System.out.print("Assignment:\n");


		level++;
		dots();
		n.left.accept(this);
		level--;

		level++;
		dots();
		n.printNode();
		level--;

		level++;
		dots();
		n.right.accept(this);
		level--;
	}
	
	public void visit (AdditionNode n)
	{
		dots();
		System.out.print("Addition:\n");

		level++;
		dots();
		n.left.accept(this);
		level--;

		level++;
		dots();
		n.printNode();
		level--;

		level++;
		dots();
		n.right.accept(this);
		level--;
	}
	
	public void visit (SubtractionNode n)
	{
		dots();
		System.out.print("Subtraction:\n");

		level++;
		dots();
		n.left.accept(this);
		level--;

		level++;
		dots();
		n.printNode();
		level--;

		level++;
		dots();
		n.right.accept(this);
		level--;
	}
	
	public void visit (MultiplicationNode n)
	{
		dots();
		System.out.print("Multiplication:\n");

		level++;
		dots();
		n.left.accept(this);
		level--;

		level++;
		dots();
		n.printNode();
		level--;

		level++;
		dots();
		n.right.accept(this);
		level--;
	}

	public void visit (DivisionNode n)
	{
		dots();
		System.out.print("Division:\n");

		level++;
		dots();
		n.left.accept(this);
		level--;

		level++;
		dots();
		n.printNode();
		level--;

		level++;
		dots();
		n.right.accept(this);
		level--;
	}

	public void visit (ModuloNode n)
	{
		dots();
		System.out.print("Modulo:\n");

		level++;
		dots();
		n.left.accept(this);
		level--;

		level++;
		dots();
		n.printNode();
		level--;

		level++;
		dots();
		n.right.accept(this);
		level--;
	}
	
	public void visit(LiteralNode n)
	{
		level++;
		dots();;
		n.accept(this);
		level--;
	}

	public void visit(Node n)
	{
		level++;
		dots();
		n.accept(this);
		level--;
	}

	private void dots()
	{
		System.out.print(new String(new char[level*4]).replace('\0', '.'));
	}
}