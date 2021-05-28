package cribbage;

import ch.aplu.jcardgame.Hand;

public class PlayPairStrategy extends ScoringStrategy{


    @Override
    public int getScore() {
        int score = 0;
        int flag = -1;

        Cribbage.Segment tempSegment = segmentScoring.copySegment();
        Cribbage.ScoreType scoreType = null;

        // Start to find pair for pair4
        while (tempSegment.segment.getNumberOfCards() > 1) {
            if (tempSegment.segment.getNumberOfCards() == 4) {
                Hand[] quads = tempSegment.segment.extractQuads();
                if (quads.length > 0) {
                    currentPlayer.setScore(currentPlayer.getScore() + 12);
                    score += 12;
                    flag = 4;
                    scoreType = Cribbage.ScoreType.PAIR4;
                    break;
                }
            } else if (tempSegment.segment.getNumberOfCards() == 3) {
                Hand[] trips = tempSegment.segment.extractTrips();
                if (trips.length > 0) {
                    currentPlayer.setScore(currentPlayer.getScore() + 6);
                    score += 6;
                    flag = 3;
                    scoreType = Cribbage.ScoreType.PAIR3;
                    break;
                }
            } else if (tempSegment.segment.getNumberOfCards() == 2) {
                Hand[] pair = tempSegment.segment.extractPairs();
                if (pair.length > 0) {
                    currentPlayer.setScore(currentPlayer.getScore() + 2);
                    score += 2;
                    flag = 2;
                    scoreType = Cribbage.ScoreType.PAIR2;
                    break;
                }
            }
            tempSegment.segment.removeFirst(false);
        }

        if (flag != -1 && scoreType != null) {
            loggerHelper.logScore(tempSegment, currentPlayer.getScore(), score, scoreType);
        }

        return score;
    }
}
