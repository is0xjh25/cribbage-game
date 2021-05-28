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

        Hand[] quad = tempSegment.segment.extractQuads();
        // if pairs exist, update the score
        if (quad.length > 0) {
            // add to score
            currentPlayer.setScore(currentPlayer.getScore() + 12);
            totalScore += 12;
            score = 12;
            flag = 4;

            // print to log
            // score,P1,7,2,pair2,[AD,AH]
//            String log = String.format("score,P%d,%d,%d,pair%d,%s", tempSegment.lastPlayer, currentPlayer.getScore(), score, flag, quad.toString());
//            logger.log(log);
            loggerHelper.logScore(tempSegment, currentPlayer.getScore(), score, Cribbage.ScoreType.PAIR4, Cribbage.canonical(quad[0]));
            return score;
        }



        Hand[] trip = tempSegment.segment.extractTrips();
        if (trip.length > 0) {
            // add to score
            currentPlayer.setScore(currentPlayer.getScore() + 6);
            totalScore += 6;
            score = 6;
            flag = 3;

            // print to log
            // score,P1,7,2,pair2,[AD,AH]
//            String log = String.format("score,P%d,%d,%d,pair%d,%s", tempSegment.lastPlayer, currentPlayer.getScore(), score, flag, Cribbage.canonical(trip[0]));
//            logger.log(log);
            loggerHelper.logScore(tempSegment, currentPlayer.getScore(), score, Cribbage.ScoreType.PAIR3, Cribbage.canonical(trip[0]));

//            int rank = trip[0].getFirst().getRankId();
//            for (int i = 0; i < cardList.size() - 1; i++) {
//                if (cardList.get(i).getRankId() == rank) {
//                    boolean remove = tempSegment.segment.remove(cardList.get(i));
//                    boolean remove2 = tempSegment.segment.remove(i);
//                }
//            }
//            if (cardList.get(0).getRankId() == cardList.get(1).getRankId()) {
//                // add to score
//                currentPlayer.setScore(currentPlayer.getScore() + 2);
//                score += 2;
//                flag = 2;
//
//                // print to log
//                // score,P1,7,2,pair2,[AD,AH]
//                String log = String.format("score,P%d,%d,%d,pair%d,%s", tempSegment.lastPlayer, currentPlayer.getScore(), score, flag, trip.toString());
//                logger.log(log);
//
//            }


        }

        for (Hand hand: trip) {
            ArrayList<Card> cards = hand.getCardList();
            for (Card card: cards) {
                tempSegment.segment.remove(card, false);
            }
        }

        Hand[] pair = tempSegment.segment.extractPairs();
        if (pair.length > 0) {
            int i;
            for (i = 0; i < pair.length; i++) {
                // add to score
                currentPlayer.setScore(currentPlayer.getScore() + 2);
                totalScore += 2;
                score = 2;
                flag = 2;

                // print to log
                // score,P1,7,2,pair2,[AD,AH]
//                String log = String.format("score,P%d,%d,%d,pair%d,%s", tempSegment.lastPlayer, currentPlayer.getScore(), score, flag, Cribbage.canonical(pair[i]));
//                logger.log(log);
                loggerHelper.logScore(tempSegment, currentPlayer.getScore(), score, Cribbage.ScoreType.PAIR2, Cribbage.canonical(pair[i]));
            }
        }

        return score;
    }
}