package cribbage;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;

public class ShowFlushStrategy extends ScoringStrategy{
    @Override
    public int getScore() {
        int score = 0;

        // copy a temporary segment to avoid risky direct modification to the original segment
        Cribbage.Segment tempSegment = segmentScoring.copySegment();
        ArrayList<Card> cardList = tempSegment.segment.getCardList();

        // extract the suit of the first card
        Card starterCard = tempSegment.segment.getLast();
        tempSegment.segment.remove(starterCard, false);

        Cribbage.Suit flushSuit = null;
        for (Cribbage.Suit suit : Cribbage.Suit.values()) {
            int amount = tempSegment.segment.getNumberOfCardsWithSuit(suit);
            if (amount == 4) {
                score += 4;
                currentPlayer.setScore(currentPlayer.getScore() + 4);
                flushSuit = suit;
            }
            break;
        }

        if (score == 4) {
            tempSegment.segment.insert(starterCard, false);
            int flushIncludingStarter = tempSegment.segment.getNumberOfCardsWithSuit(flushSuit);
            if (flushIncludingStarter == 5) {
                score += 1;
                currentPlayer.setScore(currentPlayer.getScore() + 1);
            } else {
                tempSegment.segment.getLast();
            }
        }


        if (score != 0) {
            String log = String.format("score,P%d,%d,%d,flush%d,%s",tempSegment.lastPlayer, currentPlayer.getScore(),score, score, Cribbage.canonical(tempSegment.segment));
            logger.log(log);
        }
        return score;

//        // check whether flush exists
//        int flush = tempSegment.segment.getNumberOfCardsWithSuit(suit);
//
//        if (flush == 4) {
//            currentPlayer.setScore(currentPlayer.getScore() + 4);
//            score += 4;
//        }
//
//        // if flush exists, check whether flush5 exists
//        Enum starterSuit = cardList.get(-1).getSuit();
//        if (score == 4 && suit == starterSuit) {
//            currentPlayer.setScore(currentPlayer.getScore() + 1);
//            score += 1;
//        }
//
//        // print log
//        if (score != 0) {
//            // no reference, follow score,P1,15,1,jack,[JC]
//            String log =
//                    String.format("score,P%d,%d,%d,flush%d,", segmentScoring.lastPlayer, currentPlayer.getScore(), score, score, cardList.toString());
//        }
    }
}