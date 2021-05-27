package cribbage;

import ch.aplu.jcardgame.Card;

public class CompositeShowStrategy extends CompositeStrategy{
    @Override
    public int getScore() {
        Cribbage.Segment tempSegment = segmentScoring.copySegment();
        Card starterCard = tempSegment.segment.getLast();
        tempSegment.segment.removeLast(false);
//        String showLog = String.format("show,p%d,%s+%s",tempSegment.lastPlayer,Cribbage.canonical(starterCard),Cribbage.canonical(tempSegment.segment));
//        logger.log(showLog);

        loggerHelper.logShow(tempSegment,starterCard);
        int score = 0;
        for (ScoringStrategy scoringStrategy: strategyList) {
            score += scoringStrategy.getScore();
        }
        return score;
    }
}
