
public class Card {
  
  int cardsInASet;
  int numberOfVariables;
  int[] variables;
  boolean selected;
  
  public Card()
  {
    cardsInASet = 3;
    numberOfVariables = 4;
    variables = new int[numberOfVariables];
    for(int i =0; i<numberOfVariables; i++)
    {
      variables[i]=0;
    }
    selected = false;
  }
  
  public Card(int numberOfVariables,int numberOfCardstoaSet,int[] variables)
  {
    cardsInASet = numberOfCardstoaSet;
    numberOfVariables = numberOfVariables;
    this.variables = new int[numberOfVariables];
    for(int i =0; i<numberOfVariables; i++)
    {
      this.variables[i]=variables[i];
    }
    selected = false;
  }
  
  public boolean isSelected()
  {
    return selected;
  }
  
}
