package cribbage;

public abstract class ScoringStrategy {

    protected LoggerHelper loggerHelper = LoggerHelper.getInstance();

    protected Cribbage.Segment segmentScoring;

    protected IPlayer currentPlayer;

    public IPlayer getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(IPlayer currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Cribbage.Segment getSegment() {
        return segmentScoring;
    }

    public void setSegment(Cribbage.Segment segment) {
        this.segmentScoring = segment;
    }

    public abstract int getScore();

}
