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

        for (int i = 0; i < cardList.size() - 1; i++) {
            if (cardList.get(i).getRankId() == 11 && cardList.get(i).getSuitId() == cardList.get(-1).getSuitId()) {
                currentPlayer.setScore(currentPlayer.getScore() + 1);
                String log = String.format("score,P%d,%d,%d,jack,[%s]", segmentScoring.lastPlayer, currentPlayer.getScore(), 1, Cribbage.canonical(cardList.get(i)));
                logger.log(log);
                score = 1;
                break;
            }
        }
        return score;
    }
}