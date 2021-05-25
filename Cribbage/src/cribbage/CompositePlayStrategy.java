package cribbage;

import ch.aplu.jcardgame.Card;

public class CompositePlayStrategy extends CompositeStrategy{
    @Override
    public int getScore() {
        int score = 0;
        for (ScoringStrategy scoringStrategy: strategyList) {
            score += scoringStrategy.getScore();
        }
        return score;
    }
}
