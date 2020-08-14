package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

	public static String delims = " \t*+-/(/)\\[\\]";
			
    /**
     * Populates the vars list with simple variables, and arrays lists with arrays
     * in the expression. For every variable (simple or array), a SINGLE instance is created 
     * and stored, even if it appears more than once in the expression.
     * At this time, values for all variables and all array items are set to
     * zero - they will be loaded from a file in the loadVariableValues method.
     * 
     * @param expr The expression
     * @param vars The variables array list - already created by the caller
     * @param arrays The arrays array list - already created by the caller
     */
    public static void makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
    	/** COMPLETE THIS METHOD **/
    	/** DO NOT create new vars and arrays - they are already created before being sent in
    	 ** to this method - you just need to fill them in.
    	 *
    	 *
    	 *
    	 *
    	 *
    	 *
    	 **/
    	StringTokenizer tokens = new StringTokenizer(expr, delims);
    	String[] tokensArray = new String[tokens.countTokens()];
    	
    	int i = 0;
    	while(tokens.hasMoreTokens())
    	{
    		tokensArray[i] = tokens.nextToken();
    		i++;
    	}
    	

    	for (int k = 0; k < tokensArray.length; k++)
    	{
    		String tempToken = tokensArray[k];
    		if(!tempToken.equals("") && !(tempToken.charAt(0) >= '0' && tempToken.charAt(0) <= '9')) // Not empty and not constant
    		{
    			k = expr.indexOf(tempToken, k) + tempToken.length();
    		}
    		if(k < expr.length() && expr.charAt(k) == '[' && !arrays.contains(new Array(tempToken))) // array/inside length/not already created
    		{
    			arrays.add(new Array(tempToken));   
    		}
    		else if(!vars.contains(new Variable(tempToken))) //not already created
    		{
    			vars.add(new Variable(tempToken));
    		}
    		else
    		{
    			continue;
    		}
    	}    	
    }
 
    
    /**
     * Loads values for variables and arrays in the expression
     * 
     * @param sc Scanner for values input
     * @throws IOException If there is a problem with the input 
     * @param vars The variables array list, previously populated by makeVariableLists
     * @param arrays The arrays array list - previously populated by makeVariableLists
     */
    public static void loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays) 
    throws IOException {
        while (sc.hasNextLine()) {
            StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
            int numTokens = st.countTokens();
            String tok = st.nextToken();
            Variable var = new Variable(tok);
            Array arr = new Array(tok);
            int vari = vars.indexOf(var);
            int arri = arrays.indexOf(arr);
            if (vari == -1 && arri == -1) {
            	continue;
            }
            int num = Integer.parseInt(st.nextToken());
            if (numTokens == 2) { // scalar symbol
                vars.get(vari).value = num;
            } else { // array symbol
            	arr = arrays.get(arri);
            	arr.values = new int[num];
                // following are (index,val) pairs
                while (st.hasMoreTokens()) {
                    tok = st.nextToken();
                    StringTokenizer stt = new StringTokenizer(tok," (,)");
                    int index = Integer.parseInt(stt.nextToken());
                    int val = Integer.parseInt(stt.nextToken());
                    arr.values[index] = val;              
                }
            }
        }
    }
    
    private static float mathOperator(String a, float x, float y)
    {
    	if(a.equals("*"))
    	{
    		return x * y;
    	}
    	else if(a.equals("/"))
    	{
    		if(x == 0)
    		{
    			throw new UnsupportedOperationException("Undefined");
    		}
    		return y / x;
    	}
    	else if(a.equals("+"))
    	{
    		return x + y;
    	}
    	else if(a.equals("-"))
    	{
    		return y - x;
    	}
    	
    	return 0;
    }
     private static boolean priority(String x, String y)
     {
    	 if(y.equals("(") || y.equals("") || y.equals("[") || y.equals("]"))
    	 {
    		 return false;
    	 }
    	 if(x.equals("/") || x.equals("*") || x.equals("+") || x.equals("-"))
    	 {
    		 return false;
    	 }
    	 else 
    	 {
    		 return true;
    	 } 
     }
     
     

	 public static boolean constantVal(String check) {
		 try 
		 {
			 int number = Integer.parseInt(check);
		 }
		 catch(NumberFormatException nfe) 
		 {
			 return false;
		 }
		 return true;
	 }
    /**
     * Evaluates the expression.
     * 
     * @param vars The variables array list, with values for all variables in the expression
     * @param arrays The arrays array list, with values for all array items
     * @return Result of evaluation
     */
    public static float evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays)
    {
    	/** COMPLETE THIS METHOD **/
    	// following line just a placeholder for compilation
    	String[] tokensArray = expr.split("(?<=["+delims+"])|(?=["+delims+"])");
    	Stack<Float> variables = new Stack<Float>();
		Stack<String> operators = new Stack<String>();
		variables.clear();
		operators.clear();
    	
    	
    	for(int k = 0; k < tokensArray.length; k++)
    	{
    		String currenttoken = tokensArray[k];
    		if(currenttoken.equals(" ")) // space
    		{
    			continue;
    		}
    		
    		if(constantVal(currenttoken)) // Constant
    		{
    			variables.push(Float.parseFloat(currenttoken));
    		}
    		
    		else if(currenttoken.equals("(") || currenttoken.equals("["))
    		{
    			operators.push(currenttoken);
    			//evaluate(expr.substring(k), vars, arrays);
    		}
    		else if(currenttoken.equals(")"))
    		{
    			while(!(operators.peek().equals("("))) 
    			{
    				if(operators.peek().equals("["))
    				{
    					operators.pop();
    				}
    				else
    				{
    					variables.push(mathOperator(operators.pop(), variables.pop(), variables.pop()));
    				}
    			}
    			if(!operators.isEmpty())
    			{
    				if(operators.peek().equals("("))
    				{
    					operators.pop();
    				}
    			}
    		}
    		
    		else if(currenttoken.equals("]"))
    		{
    			while(!(operators.peek().equals("[")))
    			{
    				variables.push(mathOperator(operators.pop(), variables.pop(), variables.pop()));
    			}
    			if(!operators.isEmpty())
    			{
    				if(operators.peek().equals("["))
    				{
    					operators.pop();
    				}
    			}
    			
    			String arrayName = operators.peek();
    			int c = arrays.indexOf(new Array(arrayName));
    			if(c > -1)
    			{
    				operators.pop();
        			Array arr1 = arrays.get(c);
        			int arr1c = (int)Math.round(variables.pop());
        			float value = arr1.values[arr1c];
        			variables.push(value);
    			}
    		}
    		
    		else if(currenttoken.equals("+") || currenttoken.equals("-") || currenttoken.equals("*") || currenttoken.equals("/"))
    		{
    			while(!operators.isEmpty() && priority(currenttoken, operators.peek()))
    			{
    				variables.push(mathOperator(operators.pop(), variables.pop(), variables.pop()));
    			}
    			operators.push(currenttoken);
    		}
    		else if(arrays.contains(new Array(currenttoken)))
    		{
    			operators.push(currenttoken);
    		}
    		else
    		{
    			Variable var = new Variable(currenttoken);
    			if(vars.contains(var))
    			{
    				int varIndex = vars.indexOf(var);
    				float value = vars.get(varIndex).value;
    				variables.push(value);
    			}
    		} 			
    	}
    	while(!operators.isEmpty() && variables.size() > 1)
    	{
    		variables.push(mathOperator(operators.pop(), variables.pop(), variables.pop()));
    	}
    	return (variables.pop());
    }
    
}
