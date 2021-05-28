package cribbage;

import ch.aplu.jcardgame.Card;

/* SWEN-30006-Project2
    Created by Workshop16Team02, May 28th 2021
*/
public class CompositeShowStrategy extends CompositeStrategy{
    @Override
    public int getScore() {
        Cribbage.Segment tempSegment = segmentScoring.copySegment();
        Card starterCard = tempSegment.segment.getLast();
        tempSegment.segment.removeLast(false);

        loggerHelper.logShow(tempSegment,starterCard);
        int score = 0;
        for (ScoringStrategy scoringStrategy: strategyList) {
            score += scoringStrategy.getScore();
        }
        return score;
    }
}
