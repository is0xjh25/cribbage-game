package cribbage;

import ch.aplu.jcardgame.Hand;

public class PlayRunStrategy extends ScoringStrategy{


    @Override
    public int getScore() {
        int score = 0;

        Cribbage.Segment tempSegment = segmentScoring.copySegment();
        int runNum = -1;

        while (tempSegment.segment.getNumberOfCards() > 2) {
            if (tempSegment.segment.getNumberOfCards() == 7) {
                Hand[] temp = tempSegment.segment.extractSequences(7);
                if (temp.length > 0) {
                    currentPlayer.setScore(currentPlayer.getScore() + 7);
                    score += 7;
                    runNum = 7;
                    break;
                }
            } else if (tempSegment.segment.getNumberOfCards() == 6) {
                Hand[] temp = tempSegment.segment.extractTrips();
                if (temp.length > 0) {
                    currentPlayer.setScore(currentPlayer.getScore() + 6);
                    score += 6;
                    runNum = 6;
                    break;
                }
            } else if (tempSegment.segment.getNumberOfCards() == 5) {
                Hand[] temp = tempSegment.segment.extractTrips();
                if (temp.length > 0) {
                    currentPlayer.setScore(currentPlayer.getScore() + 5);
                    score += 6;
                    runNum = 6;
                    break;
                }
            } else if (tempSegment.segment.getNumberOfCards() == 4) {
                Hand[] temp = tempSegment.segment.extractTrips();
                if (temp.length > 0) {
                    currentPlayer.setScore(currentPlayer.getScore() + 4);
                    score += 6;
                    runNum = 6;
                    break;
                }
            } else if (tempSegment.segment.getNumberOfCards() == 3) {
                Hand[] temp = tempSegment.segment.extractTrips();
                if (temp.length > 0) {
                    currentPlayer.setScore(currentPlayer.getScore() + 3);
                    score += 6;
                    runNum = 6;
                    break;
                }
            }
            tempSegment.segment.removeFirst(false);
        }

        if (runNum != -1) {
            String pairLog = String.format("score,P%d,%d,%d,run%d",tempSegment.lastPlayer, currentPlayer.getScore(), score, runNum);
            logger.log(pairLog);
        }

        return score;
    }
}
