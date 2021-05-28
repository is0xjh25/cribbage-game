package cribbage;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;

public class ShowPairStrategy extends ScoringStrategy {

    @Override
    public int getScore() {
        int score = 0;
        int totalScore = 0;
        int flag = -1;

        // copy a temporary segment to avoid risky direct modification to the original segment
        Cribbage.Segment tempSegment = segmentScoring.copySegment();
        ArrayList<Card> cardList = tempSegment.segment.getCardList();

        // check if the show contains a quad, triple, or pair

        // Find pair4
        Hand[] quad = tempSegment.segment.extractQuads();
        if (quad.length > 0) {
            // add to score
            currentPlayer.setScore(currentPlayer.getScore() + 12);
            totalScore += 12;
            score = 12;
            flag = 4;
            loggerHelper.logScore(tempSegment, currentPlayer.getScore(), score, Cribbage.ScoreType.PAIR4, Cribbage.canonical(quad[0]));
            return totalScore;
        }

        // Find pair3
        Hand[] trip = tempSegment.segment.extractTrips();
        if (trip.length > 0) {
            // add to score
            currentPlayer.setScore(currentPlayer.getScore() + 6);
            totalScore += 6;
            score = 6;
            flag = 3;
            loggerHelper.logScore(tempSegment, currentPlayer.getScore(), score, Cribbage.ScoreType.PAIR3, Cribbage.canonical(trip[0]));
        }

        for (Hand hand: trip) {
            ArrayList<Card> cards = hand.getCardList();
            for (Card card: cards) {
                tempSegment.segment.remove(card, false);
            }
        }

        // Find pair2
        Hand[] pair = tempSegment.segment.extractPairs();
        if (pair.length > 0) {
            int i;
            for (i = 0; i < pair.length; i++) {
                // add to score
                currentPlayer.setScore(currentPlayer.getScore() + 2);
                totalScore += 2;
                score = 2;
                flag = 2;
                loggerHelper.logScore(tempSegment, currentPlayer.getScore(), score, Cribbage.ScoreType.PAIR2, Cribbage.canonical(pair[i]));
            }
        }

        return totalScore;
    }
}