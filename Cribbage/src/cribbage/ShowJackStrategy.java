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
            Cribbage.Rank rank = (Cribbage.Rank) cardList.get(i).getRank();
            if (rank.order == 11 && cardList.get(i).getSuitId() == cardList.get(cardList.size() - 1).getSuitId()) {
                currentPlayer.setScore(currentPlayer.getScore() + 1);
                score = 1;
//                String log = String.format("score,P%d,%d,%d,jack,[%s]", segmentScoring.lastPlayer, currentPlayer.getScore(), 1, Cribbage.canonical(cardList.get(i)));
//                logger.log(log);
                String cardLog = String.format("[%s]", Cribbage.canonical(cardList.get(i)));
                loggerHelper.logScore(tempSegment, currentPlayer.getScore(), score, Cribbage.ScoreType.JACK, cardLog);

                // ServiceFactory.getLoggerHelper()
                score = 1;
                break;
            }
        }
        return score;
    }
}
//ServiceFactory:
//private Lo str;
//private LoggerHelper log;