package cribbage;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;

public class ShowJackStrategy extends ScoringStrategy {
    @Override
    public int getScore() {
        int score = 0;
        Cribbage.Segment tempSegment = segmentScoring.copySegment();
        ArrayList<Card> cardList = tempSegment.segment.getCardList();

        // Find if there is jack score
        for (int i = 0; i < cardList.size() - 1; i++) {
            Cribbage.Rank rank = (Cribbage.Rank) cardList.get(i).getRank();
            if (rank.order == 11 && cardList.get(i).getSuitId() == cardList.get(cardList.size() - 1).getSuitId()) {
                currentPlayer.setScore(currentPlayer.getScore() + 1);
                score = 1;
                Hand handTemp = new Hand(Cribbage.deck);
                handTemp.insert(cardList.get(i).getCardNumber(),false);
                loggerHelper.logScore(tempSegment, currentPlayer.getScore(), score, Cribbage.ScoreType.JACK, Cribbage.canonical(handTemp));
                score = 1;
                break;
            }
        }
        return score;
    }
}