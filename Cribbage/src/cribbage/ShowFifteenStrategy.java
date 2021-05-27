package cribbage;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;

public class ShowFifteenStrategy extends ScoringStrategy{
    @Override
    public int getScore() {
        int score = 0;

        // copy a temporary segment to avoid risky direct modification to the original segment
        Cribbage.Segment tempSegment = segmentScoring.copySegment();



        ArrayList<Hand> allCombination = new ArrayList<>();
        ArrayList<Card> combination = new ArrayList<>();
        tempSegment.segment.sort(Hand.SortType.POINTPRIORITY, false);
        ArrayList<Card> allCard = tempSegment.segment.getCardList();
        dfs(allCard,combination,0,allCard.size(),allCombination);
//        System.out.println();
        // allCombination.sort();
        for (Hand hand: allCombination) {
            int sum = calculateSum(hand);
            if (sum == 15) {
                // score,P0,2,2,fifteen,[7C,8H]
                score+=2;
                currentPlayer.setScore(currentPlayer.getScore()+2);
//                String log = String.format("score,p%d,%d,%d,fifteen,%s",segmentScoring.lastPlayer,currentPlayer.getScore(),2,Cribbage.canonical(hand));
//
//                logger.log(log);
                loggerHelper.logScore(tempSegment, currentPlayer.getScore(), score, Cribbage.ScoreType.FIFTEEN, Cribbage.canonical(hand));
            }
        }


        return score;
    }


    private int calculateSum(Hand hand) {
        int sum = 0;
        for (Card card: hand.getCardList()) {
            // System.out.println(Cribbage.canonical(card));
            Cribbage.Rank rank = (Cribbage.Rank) card.getRank();
            // System.out.println(rank.value);
            sum += rank.value;
        }
        return sum;
    }

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
