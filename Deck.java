import java.security.SecureRandom;

//"Coding is like walking on coals: when you do it right everyone is impressed but the learning process sucks" Riki Sambori

public class Deck {
	
	private Card[] deck;
	private int currentCard;
	private int NUMBER_OF_CARDS;
	private static final SecureRandom randomNumbers = new SecureRandom();
	
	Deck()
	{
		this(4,3);
	}
	
	Deck(int numOfVariables, int cardsInASet)
	{
		initialize(numOfVariables, cardsInASet);
	}
	
	/*
	 * 
	 * @param numOfVariables
	 * @param cardsInASet
	 * @param variables
	 * this array should be size [numOfVariables][cardsInASet]
	 */
	Deck(int numOfVariables, int cardsInASet, int[][]variables)// a deck with a certain size and specifically defined variables, not just the default first few. 
	{
		if(variables.length!=numOfVariables)
		{
			throw new IllegalArgumentException("variables array is not the expected size");//TODO later make this go to a separate thingy so they can pick out a few variables
		}
		for(int i  = 0; i<variables.length; i++)
		{
			if(/*variables[i]!=null &&*/ variables[i].length!=cardsInASet)
			{
				throw new IllegalArgumentException("variables array is not the expected size");
			}
		}
		
		NUMBER_OF_CARDS = (int)Math.pow(cardsInASet,numOfVariables);
		deck = new Card[NUMBER_OF_CARDS];
		
		for(int i = 0 ; i<NUMBER_OF_CARDS ; i++)
		{
			int[] v = new int[numOfVariables];
			for(int j = 0; j<numOfVariables;j++)
			{
				v[j] = variables[j][(i/(int)Math.pow(cardsInASet,j))%cardsInASet];
			}
			deck[i] = new Card(numOfVariables,cardsInASet,v);
		}
		
	}
	
	private void initialize(int numOfVariables, int cardsInASet)
	{
		NUMBER_OF_CARDS = (int)Math.pow(cardsInASet,numOfVariables); // cardsInASet raised to numOfVariables
		deck = new Card[NUMBER_OF_CARDS];
		
		for(int i = 0 ; i<NUMBER_OF_CARDS ; i++)
		{
			int[] variables = new int[numOfVariables];
			for(int j = 0; j<numOfVariables; j++)
			{
				variables[j]= (i/(int)Math.pow(cardsInASet,j))%cardsInASet;
				
				//if the variable is the number of tokens on the card
				if(j==Card.NUMBER)
				{
					variables[j]++;
				}
			}
			deck[i] = new Card(numOfVariables,cardsInASet,variables);
		}
		
		currentCard = 0;
	}
	
	public void shuffle()
	{
		currentCard = 0;
		
		for(int first = 0; first < deck.length; first++)
		{
			int second = randomNumbers.nextInt(NUMBER_OF_CARDS);
			
			Card temp = deck[first];
			deck[first] = deck[second];
			deck[second] = temp;
		}
	}
	
	public Card deal()
	{
		if(currentCard < deck.length)
		{
			return deck[currentCard++];
		}
		else
		{
			return null;
		}
	}
	
	public boolean cardsRemaining()
	{
		return(currentCard < deck.length);
	}
	
	public int numberOfCards()
	{
		return NUMBER_OF_CARDS;
	}
	

	
}
