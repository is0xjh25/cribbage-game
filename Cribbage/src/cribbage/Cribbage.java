package cribbage;

// Cribbage.java

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;

import java.awt.Color;
import java.awt.Font;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLOutput;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Cribbage extends CardGame {
	static Cribbage cribbage;  // Provide access to singleton


	static LoggerHelper loggerHelper;

	public enum Suit {
		CLUBS, DIAMONDS, HEARTS, SPADES
	}

	public enum StrategyType {
		PLAY, SHOW
	}

	public enum Rank {
		// Order of cards is tied to card images
		ACE(1,1), KING(13,10), QUEEN(12,10), JACK(11,10), TEN(10,10), NINE(9,9), EIGHT(8,8), SEVEN(7,7), SIX(6,6), FIVE(5,5), FOUR(4,4), THREE(3,3), TWO(2,2);
		public final int order;
		public final int value;
		Rank(int order, int value) {
			this.order = order;
			this.value = value;
		}
	}

	public enum ScoreType {
		FIFTEEN("fifteen"), THIRTYONE("thirtyone"), GO("go"), RUN3("run3"), RUN4("run4"), RUN5("run5"), RUN6("run6"), RUN7("run7"), PAIR2("pair2"), PAIR3("pair3"), PAIR4("pair4"),
		FLUSH4("flush4"), FLUSH5("flush5"), JACK("jack"), STARTER("starter");
		public final String scoreType;
		ScoreType(String scoreType) {
			this.scoreType = scoreType;
		}
	}

	static int cardValue(Card c) { return ((Cribbage.Rank) c.getRank()).value; }

	/*
	Canonical String representations of Suit, Rank, Card, and Hand
	*/


	// Change cannonical function to static, could be called outside cribbage function
	static String canonical(Suit s) { return s.toString().substring(0, 1); }

	static String canonical(Rank r) {
		switch (r) {
			case ACE:case KING:case QUEEN:case JACK:case TEN:
				return r.toString().substring(0, 1);
			default:
				return String.valueOf(r.value);
		}
	}

    static String canonical(Card c) { return canonical((Rank) c.getRank()) + canonical((Suit) c.getSuit()); }

    static String canonical(Hand h) {
		Hand h1 = new Hand(deck); // Clone to sort without changing the original hand
		for (Card C: h.getCardList()) h1.insert(C.getSuit(), C.getRank(), false);
		h1.sort(Hand.SortType.POINTPRIORITY, false);
		return "[" + h1.getCardList().stream().map(x -> canonical(x)).collect(Collectors.joining(",")) + "]";
    }

	static class MyCardValues implements Deck.CardValues { // Need to generate a unique value for every card
		public int[] values(Enum suit) {  // Returns the value for each card in the suit
			return Stream.of(Rank.values()).mapToInt(r -> (((Rank) r).order-1)*(Suit.values().length)+suit.ordinal()).toArray();
		}
	}

	static Random random;

	public static <T extends Enum<?>> T randomEnum(Class<T> clazz){
      int x = random.nextInt(clazz.getEnumConstants().length);
      return clazz.getEnumConstants()[x];
  }

	static boolean ANIMATE;

	void transfer(Card c, Hand h) {
		if (ANIMATE) {
			c.transfer(h, true);
		} else {
			c.removeFromHand(true);
			h.insert(c, true);
		}
  }
  
  private void dealingOut(Hand pack, Hand[] hands) {
	  for (int i = 0; i < nStartCards; i++) {
		  for (int j=0; j < nPlayers; j++) {
			  Card dealt = randomCard(pack);
			  dealt.setVerso(false);  // Show the face
			  transfer(dealt, hands[j]);
		  }
	  }
  }

	static int SEED;

	public static Card randomCard(Hand hand){
      int x = random.nextInt(hand.getNumberOfCards());
      return hand.get(x);
  }

  private final String version = "0.1";
  static public final int nPlayers = 2;
  public final int nStartCards = 6;
  public final int nDiscards = 2;
  private final int handWidth = 400;
  private final int cribWidth = 150;
  private final int segmentWidth = 180;
  public static final Deck deck = new Deck(Suit.values(), Rank.values(), "cover", new MyCardValues());
  private final Location[] handLocations = {
			  new Location(360, 75),
			  new Location(360, 625)
	  };
  private final Location[] scoreLocations = {
			  new Location(590, 25),
			  new Location(590, 675)
	  };
  private final Location[] segmentLocations = {  // need at most three as 3x31=93 > 2x4x10=80
			new Location(150, 350),
			new Location(400, 350),
			new Location(650, 350)
	};
  private final Location starterLocation = new Location(50, 625);
  private final Location cribLocation = new Location(700, 625);
  private final Location seedLocation = new Location(5, 25);
  // private final TargetArea cribTarget = new TargetArea(cribLocation, CardOrientation.NORTH, 1, true);
  private final Actor[] scoreActors = {null, null}; //, null, null };
  private final Location textLocation = new Location(350, 450);
  private final Hand[] hands = new Hand[nPlayers];
  private Hand starter;
  private Hand crib;
  private final Hand[] handsBeforePlay = new Hand[nPlayers];

  public static void setStatus(String string) { cribbage.setStatusText(string); }

static private final IPlayer[] players = new IPlayer[nPlayers];
private final int[] scores = new int[nPlayers];

final Font normalFont = new Font("Serif", Font.BOLD, 24);
final Font bigFont = new Font("Serif", Font.BOLD, 36);

private void initScore() {
	 for (int i = 0; i < nPlayers; i++) {
		 scores[i] = 0;
		 scoreActors[i] = new TextActor("0", Color.WHITE, bgColor, bigFont);
		 addActor(scoreActors[i], scoreLocations[i]);
	 }
  }

private void updateScore(int player) {
	removeActor(scoreActors[player]);
	scores[player] = players[player].getScore();
	scoreActors[player] = new TextActor(String.valueOf(scores[player]), Color.WHITE, bgColor, bigFont);
	addActor(scoreActors[player], scoreLocations[player]);
}

private void deal(Hand pack, Hand[] hands) {
	for (int i = 0; i < nPlayers; i++) {
		hands[i] = new Hand(deck);
		// players[i] = (1 == i ? new HumanPlayer() : new RandomPlayer());
		players[i].setId(i);
		players[i].startSegment(deck, hands[i]);
	}
	RowLayout[] layouts = new RowLayout[nPlayers];
	for (int i = 0; i < nPlayers; i++)
	{
		layouts[i] = new RowLayout(handLocations[i], handWidth);
		layouts[i].setRotationAngle(0);
		// layouts[i].setStepDelay(10);
		hands[i].setView(this, layouts[i]);
		hands[i].draw();
	}
	layouts[0].setStepDelay(0);

	dealingOut(pack, hands);
	for (int i = 0; i < nPlayers; i++) {
		hands[i].sort(Hand.SortType.POINTPRIORITY, true);
		loggerHelper.logDeal(hands[i], i);

	}
	layouts[0].setStepDelay(0);
}

private void discardToCrib() {
	crib = new Hand(deck);
	RowLayout layout = new RowLayout(cribLocation, cribWidth);
	layout.setRotationAngle(0);
	crib.setView(this, layout);
	// crib.setTargetArea(cribTarget);
	crib.draw();
	for (int j = 0; j < nPlayers; j++) {
		IPlayer player = players[j];
		Hand hand = new Hand(deck);
		for (int i = 0; i < nDiscards; i++) {
			Card discard = player.discard();
			Card recordDiscard = discard.clone();
			transfer(discard, crib);
			hand.insert(recordDiscard, false);
		}
		loggerHelper.logDiscard(hand, j);

		crib.sort(Hand.SortType.POINTPRIORITY, true);
	}

	for (int j = 0; j < nPlayers; j++) {
		handsBeforePlay[j] = new Hand(deck);
		for (int i = 0; i<hands[j].getNumberOfCards();i++) {
			handsBeforePlay[j].insert(hands[j].get(i).getCardNumber(),false);
		}
	}

}

private void starter(Hand pack) {
	starter = new Hand(deck);  // if starter is a Jack, the dealer gets 2 points
	RowLayout layout = new RowLayout(starterLocation, 0);
	layout.setRotationAngle(0);
	starter.setView(this, layout);
	starter.draw();
	Card dealt = randomCard(pack);
	dealt.setVerso(false);
	transfer(dealt, starter);
	loggerHelper.loggerStarter(dealt);
	Cribbage.Rank rank = (Cribbage.Rank) dealt.getRank();
	if (rank.order == 11) {
		IPlayer dealer = players[1];
		dealer.setScore(dealer.getScore() + 1);
		Segment segment = new Segment();
		segment.lastPlayer = 1;
		Hand handTemp = new Hand(Cribbage.deck);
		handTemp.insert(dealt.getCardNumber(),false);
		loggerHelper.logScore(segment,dealer.getScore(),2, ScoreType.STARTER, canonical(handTemp));
	}
}

public static int total(Hand hand) {
	int total = 0;
	for (Card c: hand.getCardList()) total += cardValue(c);
	return total;
}

class Segment {
		Hand segment;
		boolean go;
		int lastPlayer;
		boolean newSegment;
		boolean nonCard;

		void reset(final List<Hand> segments) {
			segment = new Hand(deck);
			segment.setView(Cribbage.this, new RowLayout(segmentLocations[segments.size()], segmentWidth));
			segment.draw();
			go = false;        // No-one has said "go" yet
			lastPlayer = -1;   // No-one has played a card yet in this segment
			newSegment = false;  // Not ready for new segment yet
			nonCard = false;
		}

		Segment copySegment() {
			Segment segment_copy = new Segment();
			Hand hand_copy = new Hand(deck);
			ArrayList<Card> cards = this.segment.getCardList();
			for (Card card : cards) {
				hand_copy.insert(card.getCardNumber(), false);
			}
			segment_copy.segment = hand_copy;
			segment_copy.go = this.go;
			segment_copy.lastPlayer = this.lastPlayer;
			segment_copy.newSegment = this.newSegment;
			return segment_copy;
		}
}

private void play() {
	final int thirtyone = 31;
	final int fifteen = 15;
	List<Hand> segments = new ArrayList<>();
	int currentPlayer = 0; // Player 1 is dealer
	Segment s = new Segment();
	s.reset(segments);
	while (!(players[0].emptyHand() && players[1].emptyHand())) {
	 	// System.out.println("segments.size() = " + segments.size());
		Card nextCard = players[currentPlayer].lay(thirtyone-total(s.segment));
		if (nextCard == null) {
			if (s.go) {
				// Another "go" after previous one with no intervening cards
				// lastPlayer gets 1 point for a "go"
				IPlayer player = players[s.lastPlayer];
				player.setScore(player.getScore() + 1);
				loggerHelper.logScore(s,player.getScore(),1,ScoreType.GO);
				s.nonCard = true;
				s.newSegment = true;
			} else {
				// currentPlayer says "go"
				s.go = true;
			}
			currentPlayer = (currentPlayer+1) % 2;
		} else {
			s.lastPlayer = currentPlayer; // last Player to play a card in this segment
			transfer(nextCard, s.segment);
			 loggerHelper.logPlay(s,s.lastPlayer,nextCard);
			if (total(s.segment) == thirtyone) {
				// lastPlayer gets 2 points for a 31
				IPlayer player = players[s.lastPlayer];
				player.setScore(player.getScore() + 2);
				loggerHelper.logScore(s,player.getScore(),2, ScoreType.THIRTYONE);
				s.newSegment = true;
				currentPlayer = (currentPlayer+1) % 2;
			} else {
				if (total(s.segment) == fifteen) {
					IPlayer player = players[s.lastPlayer];
					player.setScore(player.getScore() + 2);
					loggerHelper.logScore(s,player.getScore(),2,ScoreType.FIFTEEN);
				}
				// if total(segment) == 15, lastPlayer gets 2 points for a 15
				if (!s.go) { // if it is "go" then same player gets another turn
					currentPlayer = (currentPlayer+1) % 2;
				}
			}
		}

		if (players[0].emptyHand() && players[1].emptyHand()) {
			IPlayer player = players[s.lastPlayer];
			player.setScore(player.getScore() + 1);
			loggerHelper.logScore(s,player.getScore(),1,ScoreType.GO);
		}

		if (s.lastPlayer != -1 && nextCard != null) {
			ScoringStrategy scoringStrategy = ScoringStrategyFactory.getInstance().getScoringStrategy(StrategyType.PLAY);
			scoringStrategy.setSegment(s);
			scoringStrategy.setCurrentPlayer(players[s.lastPlayer]);
			scoringStrategy.getScore();
			updateScore(s.lastPlayer);
		}

		if (s.newSegment) {
			segments.add(s.segment);
			s.reset(segments);
		}
	}
}

void showHandsCrib() {
	ScoringStrategy scoringStrategy = ScoringStrategyFactory.getInstance().getScoringStrategy(StrategyType.SHOW);

	// score player 0 (non dealer)
	Segment segment0 = new Segment();
	handsBeforePlay[0].insert(starter,false);
	segment0.segment = handsBeforePlay[0];
	segment0.lastPlayer = 0;
	segment0.go = false;
	segment0.newSegment = false;
	scoringStrategy.setSegment(segment0);
	scoringStrategy.setCurrentPlayer(players[segment0.lastPlayer]);

	scoringStrategy.getScore();

	// score player 1 (dealer)
	Segment segment1 = new Segment();
	handsBeforePlay[1].insert(starter,false);
	segment1.segment = handsBeforePlay[1];
	segment1.lastPlayer = 1;
	segment1.go = false;
	segment1.newSegment = false;
	scoringStrategy.setSegment(segment1);
	scoringStrategy.setCurrentPlayer(players[segment1.lastPlayer]);
	scoringStrategy.getScore();
	// score crib (for dealer)
	Segment crib_segment = new Segment();
	crib.insert(starter,false);
	crib_segment.segment = crib;
	crib_segment.lastPlayer = 1;
	crib_segment.go = false;
	crib_segment.newSegment = false;
	scoringStrategy.setSegment(crib_segment);
	scoringStrategy.setCurrentPlayer(players[crib_segment.lastPlayer]);
	scoringStrategy.getScore();


	updateScore(1);
	updateScore(0);

}

  public Cribbage()
  {
    super(850, 700, 30);
    cribbage = this;
    setTitle("Cribbage (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
    setStatusText("Initializing...");
    initScore();

	  Hand pack = deck.toHand(false);
	  RowLayout layout = new RowLayout(starterLocation, 0);
	  layout.setRotationAngle(0);
	  pack.setView(this, layout);
	  pack.setVerso(true);
	  pack.draw();
	  addActor(new TextActor("Seed: " + SEED, Color.BLACK, bgColor, normalFont), seedLocation);

	  /* Play the round */
	  deal(pack, hands);
	  discardToCrib();
	  starter(pack);
	  play();
	  showHandsCrib();

    addActor(new Actor("sprites/gameover.gif"), textLocation);
    setStatusText("Game over.");
    refresh();
  }

  public static void main(String[] args)
		  throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
		  	InstantiationException, IllegalAccessException {
	  /* Handle Properties */
	  // System.out.println("Working Directory = " + System.getProperty("user.dir"));
//	  logger = Logger.getInstance();

	  loggerHelper = LoggerHelper.getInstance();

	  Properties cribbageProperties = new Properties();
	  // Default properties
	  cribbageProperties.setProperty("Animate", "true");
	  cribbageProperties.setProperty("Player0", "cribbage.RandomPlayer");
	  cribbageProperties.setProperty("Player1", "cribbage.HumanPlayer");

	  // Read properties
	  try (FileReader inStream = new FileReader("cribbage.properties")) {
		  cribbageProperties.load(inStream);
	  }

	  // Control Graphics
	  ANIMATE = Boolean.parseBoolean(cribbageProperties.getProperty("Animate"));

	  // Control Randomisation
	  /* Read the first argument and save it as a seed if it exists */
	  if (args.length > 0 ) { // Use arg seed - overrides property
		  SEED = Integer.parseInt(args[0]);
	  } else { // No arg
		  String seedProp = cribbageProperties.getProperty("Seed");  //Seed property
		  if (seedProp != null) { // Use property seed
			  SEED = Integer.parseInt(seedProp);
		  } else { // and no property
			  SEED = new Random().nextInt(); // so randomise
		  }
	  }
	  random = new Random(SEED);

	  // Control Player Types
	  Class<?> clazz;
	  clazz = Class.forName(cribbageProperties.getProperty("Player0"));
	  players[0] = (IPlayer) clazz.getConstructor().newInstance();
	  clazz = Class.forName(cribbageProperties.getProperty("Player1"));
	  players[1] = (IPlayer) clazz.getConstructor().newInstance();


	  loggerHelper.logSeed(SEED);
	  loggerHelper.logplayer(0, cribbageProperties.getProperty("Player0"));
	  loggerHelper.logplayer(1, cribbageProperties.getProperty("Player1"));


	  new Cribbage();
  }



}













