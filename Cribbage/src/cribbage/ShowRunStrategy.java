package cribbage;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

public class ShowRunStrategy extends ScoringStrategy{


    @Override
    public int getScore() {

        int tot_score = 0;
        Cribbage.Segment tempSegment = segmentScoring.copySegment();
        Hand[] runs;

        runs = tempSegment.segment.extractSequences(5);
        if (runs.length > 0) {
            for (Hand run : runs) {
                if (checkRun(run) != true) {
                    currentPlayer.setScore(currentPlayer.getScore() + 5);
                    tot_score += 5;
                    //score,P1,10,3,run3,[JS,QC,KC]
                    String log = String.format("score,P%d,%d,%d,run5,%s", segmentScoring.lastPlayer, currentPlayer.getScore(), 5, Cribbage.canonical(run));
                    logger.log(log);
                }
            }
            return tot_score;
        }

        runs = tempSegment.segment.extractSequences(4);
        if (runs.length > 0) {
            for (Hand run : runs) {
                if (checkRun(run) != true) {
                    currentPlayer.setScore(currentPlayer.getScore() + 4);
                    tot_score += 5;
                    String log = String.format("score,P%d,%d,%d,run4,%s", segmentScoring.lastPlayer, currentPlayer.getScore(), 4, Cribbage.canonical(run));
                    logger.log(log);
                }
            }
            return tot_score;
        }

        runs = tempSegment.segment.extractSequences(3);
        if (runs.length > 0) {
            for (Hand run : runs) {
                if (checkRun(run) != true) {
                    currentPlayer.setScore(currentPlayer.getScore() + 3);
                    tot_score += 3;
                    //score,P1,10,3,run3,[JS,QC,KC]
                    String log = String.format("score,P%d,%d,%d,run3,%s", segmentScoring.lastPlayer, currentPlayer.getScore(), 3, Cribbage.canonical(run));
                    logger.log(log);
                }
            }
            return tot_score;
        }

        return tot_score;
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