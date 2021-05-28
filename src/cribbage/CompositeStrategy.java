package cribbage;

import java.util.ArrayList;
import java.util.List;

/* SWEN-30006-Project2
    Created by Workshop16Team02, May 28th 2021
*/
public abstract class CompositeStrategy extends ScoringStrategy {
    protected List<ScoringStrategy> strategyList = new ArrayList<>();

    @Override
    public void setSegment(Cribbage.Segment segment) {
        this.segmentScoring = segment;
        for (ScoringStrategy scoringStrategy: strategyList) {
            scoringStrategy.setSegment(segment);
        }
    }

    @Override
    public void setCurrentPlayer(IPlayer currentPlayer) {
        this.currentPlayer = currentPlayer;
        for (ScoringStrategy scoringStrategy: strategyList) {
            scoringStrategy.setCurrentPlayer(currentPlayer);
        }
    }

    @Override
    public abstract int getScore();


    public void addStrategy(ScoringStrategy scoringStrategy) {
        strategyList.add(scoringStrategy);
    }
}
