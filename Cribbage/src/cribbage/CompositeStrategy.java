package cribbage;

import java.util.ArrayList;
import java.util.List;

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
