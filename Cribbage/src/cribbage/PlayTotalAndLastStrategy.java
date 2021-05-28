package cribbage;

public class PlayTotalAndLastStrategy extends ScoringStrategy{
    @Override
    public int getScore() {
        int score = 0;
        int totalScore = 0;
        Cribbage.Segment tempSegment = segmentScoring.copySegment();

        String logMessage = "";
        if (Cribbage.total(tempSegment.segment) == 31) {
            currentPlayer.setScore(currentPlayer.getScore() + 2);
            score = 2;
            totalScore += 2;
            loggerHelper.logScore(tempSegment, currentPlayer.getScore(), score, Cribbage.ScoreType.THIRTYONE);
        } else if (Cribbage.total(tempSegment.segment) == 15) {
            currentPlayer.setScore(currentPlayer.getScore() + 2);
            score = 2;
            totalScore += 2;
            loggerHelper.logScore(tempSegment, currentPlayer.getScore(), score, Cribbage.ScoreType.FIFTEEN);
        }

        if (tempSegment.go == true && tempSegment.nonCard == true) {
            currentPlayer.setScore(currentPlayer.getScore() + 1);
            score = 1;
            totalScore += 1;
            loggerHelper.logScore(tempSegment, currentPlayer.getScore(), score, Cribbage.ScoreType.GO);
        }
        return score;
    }
}
