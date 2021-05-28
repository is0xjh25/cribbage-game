package cribbage;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;

/* SWEN-30006-Project2
    Created by Workshop16Team02, May 28th 2021
*/
public class ShowFlushStrategy extends ScoringStrategy{
    @Override
    public int getScore() {
        int score = 0;

        // copy a temporary segment to avoid risky direct modification to the original segment
        Cribbage.Segment tempSegment = segmentScoring.copySegment();
        ArrayList<Card> cardList = tempSegment.segment.getCardList();

        // Find flush4
        Card starterCard = tempSegment.segment.getLast();
        tempSegment.segment.remove(starterCard, false);
        Cribbage.Suit flushSuit = null;
        for (Cribbage.Suit suit : Cribbage.Suit.values()) {
            int amount = tempSegment.segment.getNumberOfCardsWithSuit(suit);
            if (amount == 4) {
                score += 4;
                currentPlayer.setScore(currentPlayer.getScore() + 4);
                flushSuit = suit;
                break;
            }

        }

        // Find flush5
        if (score == 4) {
            tempSegment.segment.insert(starterCard, false);
            int flushIncludingStarter = tempSegment.segment.getNumberOfCardsWithSuit(flushSuit);
            if (flushIncludingStarter == 5) {
                score += 1;
                currentPlayer.setScore(currentPlayer.getScore() + 1);
            } else {
                tempSegment.segment.removeLast(false);
            }
        }

        if (score != 0) {
            if (score == 4) {
                loggerHelper.logScore(tempSegment, currentPlayer.getScore(), score, Cribbage.ScoreType.FLUSH4, Cribbage.canonical(tempSegment.segment));
            } else {
                loggerHelper.logScore(tempSegment, currentPlayer.getScore(), score, Cribbage.ScoreType.FLUSH5, Cribbage.canonical(tempSegment.segment));
            }

        }
        return score;

    }
}