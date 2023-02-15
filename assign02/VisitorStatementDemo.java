/*
Design and implement a Visitor Pattern program in Java. Assignment 02 is an individual assignment.

    The main purpose of this programming assignment is to implement a Java program in order to traverse an AST of Assignmentstatement such as a = b + c - d
    Create a class, VisitorStatementDemo.java, which includes the main method just like VisitorPatternDemo.java in advance2 from the second video
    Create an interface, StatementVisitor.java, which includes visit methods without implementation
    Create an interface, Node.java, which includes an accept method without implementation
    Create the following Java classes, which implement Node.java
        CompilationUnit.java
        AssignmentStatement.java
        IdentifierNode.java
        AdditionExpression.java
        SubtractionExpression.java
    You have to make sure all visit methods from StatementVisitor.java should use the above five nodes as a parameter
    CompilationUnit.java should be similar to Computer.java in advance2
    Create AssignmentStatementVisitor.java, which implements StatementVisitor.java
    Each visit method in AssignmentStatementVisitor.java should print like its parameter type name
    Whenever your visitor visits its subclasses (i.e. child nodes), add four dots (i.e. ....) before its parameter type name


Output

CompilationUnit

....AssignmentStatement

........IdentifierNode a

........AdditionExpression +

............IdentifierNode b

............SubtractionExpression -

................IdentifierNode c

................IdentifierNode d
*/

public class VisitorStatementDemo
{
	public static void main(String[] args)
	{
		Node root = new CompilationUnit();
		root.accept(new AssignmentStatementVisitor());
	}
}