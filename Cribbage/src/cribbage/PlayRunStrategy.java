package cribbage;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

/* SWEN-30006-Project2
    Created by Workshop16Team02, May 28th 2021
*/
public class PlayRunStrategy extends ScoringStrategy{


    @Override
    public int getScore() {
        int score = 0;

        Cribbage.Segment tempSegment = segmentScoring.copySegment();
        int runNum = -1;
        Cribbage.ScoreType scoreType = null;

        // Start to find Run start from run7
        while (tempSegment.segment.getNumberOfCards() > 2) {
            if (tempSegment.segment.getNumberOfCards() == 7) {
                Hand[] temp = tempSegment.segment.extractSequences(7);
                if (temp.length > 0 && checkRun(temp[0])!=true) {
                    currentPlayer.setScore(currentPlayer.getScore() + 7);
                    score += 7;
                    runNum = 7;
                    scoreType = Cribbage.ScoreType.RUN7;
                    break;
                }
            } else if (tempSegment.segment.getNumberOfCards() == 6) {
                Hand[] temp = tempSegment.segment.extractSequences(6);
                if (temp.length > 0 && checkRun(temp[0])!=true) {
                    currentPlayer.setScore(currentPlayer.getScore() + 6);
                    score += 6;
                    runNum = 6;
                    scoreType = Cribbage.ScoreType.RUN6;
                    break;
                }
            } else if (tempSegment.segment.getNumberOfCards() == 5) {
                Hand[] temp = tempSegment.segment.extractSequences(5);
                if (temp.length > 0 && checkRun(temp[0])!=true) {
                    currentPlayer.setScore(currentPlayer.getScore() + 5);
                    score += 5;
                    runNum = 5;
                    scoreType = Cribbage.ScoreType.RUN5;
                    break;
                }
            } else if (tempSegment.segment.getNumberOfCards() == 4) {
                Hand[] temp = tempSegment.segment.extractSequences(4);
                if (temp.length > 0 && checkRun(temp[0])!=true) {
                    currentPlayer.setScore(currentPlayer.getScore() + 4);
                    score += 4;
                    runNum = 4;
                    scoreType = Cribbage.ScoreType.RUN4;
                    break;
                }
            } else if (tempSegment.segment.getNumberOfCards() == 3) {
                Hand[] temp = tempSegment.segment.extractSequences(3);
                if (temp.length > 0 && checkRun(temp[0])!=true) {
                    currentPlayer.setScore(currentPlayer.getScore() + 3);
                    score += 3;
                    runNum = 3;
                    scoreType = Cribbage.ScoreType.RUN3;
                    break;
                }
            }
            tempSegment.segment.removeFirst(false);
        }

        if (runNum != -1 && scoreType != null) {
            loggerHelper.logScore(tempSegment, currentPlayer.getScore(), score, scoreType);
        }

        return score;
    }

    private boolean checkRun(Hand run) {
        boolean K = false;
        boolean A = false;
        for (Card i : run.getCardList()) {
            if (((Cribbage.Rank)i.getRank()).order == 13) {
                K = true;
            } else if (((Cribbage.Rank)i.getRank()).order == 1) {
                A = true;
            }
        }
        return K == true && A == true;
    }
}
