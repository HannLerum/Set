
public class Card {
  
  int cardsInASet;
  int[] variables;
  boolean selected;
  boolean initialized;
  
  public Card()
  {
	  initialized = false;
	  cardsInASet = 3;
	  int numberOfVariables = 4;
	  variables = new int[numberOfVariables];
	  for(int i =0; i<numberOfVariables; i++)
	  {
		  variables[i]=0;
	  }
	  selected = false;
	  initialized = true;
  }
  
  public Card(int numberOfVariables,int numberOfCardstoaSet,int[] variables)
  {
	  initialized = false;
	  cardsInASet = numberOfCardstoaSet;
	  this.variables = new int[numberOfVariables];
	  for(int i =0; i<numberOfVariables; i++)
	  {
		  if(variables[i]<cardsInASet)
		  {
			  this.variables[i]=variables[i];
		  }
		  else
			  throw new IllegalArgumentException("variable is out of bounds");
	  }
	  selected = false;
	  initialized = true;
  }
  
  public boolean isSelected()
  {
	  if(initialized)
	  {
		  return selected;
	  }
	  else
		  throw new IllegalArgumentException("card is not initialized");
  }
  
  public int getVariable(int i)
  {
	  if(initialized)
	  {
		  return variables[i];
	  }
	  else
		  throw new IllegalArgumentException("card is not initialized");
	  
  }
  
}
