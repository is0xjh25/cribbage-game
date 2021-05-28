package cribbage;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;

/* SWEN-30006-Project2
    Created by Workshop16Team02, May 28th 2021
*/
public class ShowFifteenStrategy extends ScoringStrategy{
    @Override
    public int getScore() {
        int score = 0;
        int totalScore = 0;

        // copy a temporary segment to avoid risky direct modification to the original segment
        Cribbage.Segment tempSegment = segmentScoring.copySegment();

        // Start to figure which combination could sum to 15
        ArrayList<Hand> allCombination = new ArrayList<>();
        ArrayList<Card> combination = new ArrayList<>();
        tempSegment.segment.sort(Hand.SortType.POINTPRIORITY, false);
        ArrayList<Card> allCard = tempSegment.segment.getCardList();
        dfs(allCard,combination,0,allCard.size(),allCombination);
        for (Hand hand: allCombination) {
            int sum = calculateSum(hand);
            if (sum == 15) {
                // score,P0,2,2,fifteen,[7C,8H]
                totalScore+=2;
                score = 2;
                currentPlayer.setScore(currentPlayer.getScore()+2);
                loggerHelper.logScore(tempSegment, currentPlayer.getScore(), score, Cribbage.ScoreType.FIFTEEN, Cribbage.canonical(hand));
            }
        }


        return totalScore;
    }

    // Calculate sum of a hand
    private int calculateSum(Hand hand) {
        int sum = 0;
        for (Card card: hand.getCardList()) {
            Cribbage.Rank rank = (Cribbage.Rank) card.getRank();
            sum += rank.value;
        }
        return sum;
    }

    // Figure out all combination
    private void dfs(ArrayList<Card> allCard, ArrayList<Card> combination, int begin, int len, ArrayList<Hand> allCombination) {
        if (combination.size() != 0) {
            Hand newCombination = new Hand(Cribbage.deck);
            for (Card card: combination) {
                newCombination.insert(card.getCardNumber(), false);
            }
            allCombination.add(newCombination);
        }

        for (int i = begin; i< len; i++) {
            combination.add(allCard.get(i));
            dfs(allCard, combination, i+1,allCard.size(),allCombination);
            combination.remove(combination.size()-1);
        }
    }
}
