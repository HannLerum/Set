import java.security.SecureRandom;

//"Coding is like walking on coals: when you do it right everyone is impressed but the learning process sucks"

public class Deck {
	
	private Card[] deck;
	private int currentCard;
	private int NUMBER_OF_CARDS;
	private static final SecureRandom randomNumbers = new SecureRandom();
	
	Deck()
	{
		NUMBER_OF_CARDS = 3;
		deck = new Card[NUMBER_OF_CARDS];
		
	}
	
	Deck(int numOfVariables, int cardsInASet)
	{
		NUMBER_OF_CARDS = (int)Math.pow(cardsInASet,numOfVariables);
		deck = new Card[NUMBER_OF_CARDS];
		
		
	}
	
	public void shuffle()
	{
		
	}
	
	public void deal()
	{
		
	}
	
	public int numberOfCards()
	{
		return NUMBER_OF_CARDS;
	}
	

	
}
