package cribbage;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

/* SWEN-30006-Project2
    Created by Workshop16Team02, May 28th 2021
*/
public class ShowRunStrategy extends ScoringStrategy{

    @Override
    public int getScore() {

        int tot_score = 0;
        Cribbage.Segment tempSegment = segmentScoring.copySegment();
        Hand[] runs;
        int score = 0;

        // figure run5
        runs = tempSegment.segment.extractSequences(5);
        if (runs.length > 0) {
            for (Hand run : runs) {
                if (checkRun(run) != true) {
                    currentPlayer.setScore(currentPlayer.getScore() + 5);
                    tot_score += 5;
                    score = 5;
                    loggerHelper.logScore(tempSegment, currentPlayer.getScore(), score, Cribbage.ScoreType.RUN5, Cribbage.canonical(run));
                    return tot_score;
                } else {
                    Hand runCopy = new Hand(Cribbage.deck);
                    for (Card card: run.getCardList()) {
                        Cribbage.Rank rank = (Cribbage.Rank) card.getRank();
                        if (rank.order != 1){
                            runCopy.insert(card.getCardNumber(),false);
                        }
                    }
                    Hand[] run4 = runCopy.extractSequences(4);
                    if (run4.length > 0) {
                        currentPlayer.setScore(currentPlayer.getScore() + 4);
                        tot_score += 4;
                        score = 4;
                        loggerHelper.logScore(tempSegment, currentPlayer.getScore(), score, Cribbage.ScoreType.RUN3, Cribbage.canonical(run4[0]));
                    }
                }
            }

        }

        // find run4
        runs = tempSegment.segment.extractSequences(4);
        if (runs.length > 0) {
            for (Hand run : runs) {

                if (checkRun(run) != true) {
                    currentPlayer.setScore(currentPlayer.getScore() + 4);
                    tot_score += 5;
                    score = 5;
                    loggerHelper.logScore(tempSegment, currentPlayer.getScore(), score, Cribbage.ScoreType.RUN4, Cribbage.canonical(run));
                    // return tot_score;
                } else {
                    Hand runCopy = new Hand(Cribbage.deck);
                    for (Card card: run.getCardList()) {
                        Cribbage.Rank rank = (Cribbage.Rank) card.getRank();
                        if (rank.order != 1){
                            runCopy.insert(card.getCardNumber(),false);
                        }
                    }
                    Hand[] run3 = runCopy.extractSequences(3);
                    if (run3.length > 0) {
                        currentPlayer.setScore(currentPlayer.getScore() + 3);
                        tot_score += 3;
                        score = 3;
                        loggerHelper.logScore(tempSegment, currentPlayer.getScore(), score, Cribbage.ScoreType.RUN3, Cribbage.canonical(run3[0]));
                    }
                }
            }
            return tot_score;
        }

        // find run3
        runs = tempSegment.segment.extractSequences(3);
        if (runs.length > 0) {
            for (Hand run : runs) {
                if (checkRun(run) != true) {
                    currentPlayer.setScore(currentPlayer.getScore() + 3);
                    tot_score += 3;
                    score = 3;
                    loggerHelper.logScore(tempSegment, currentPlayer.getScore(), score, Cribbage.ScoreType.RUN3, Cribbage.canonical(run));
                }
            }
            return tot_score;
        }
        return tot_score;
    }


    // check if a valid run
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