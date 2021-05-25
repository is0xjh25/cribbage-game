package cribbage;

public class PlayTotalAndLastStrategy extends ScoringStrategy{
    @Override
    public int getScore() {
        int score = 0;
        Cribbage.Segment tempSegment = segmentScoring.copySegment();

        String logMessage = "";
        if (Cribbage.total(tempSegment.segment) == 31) {
            currentPlayer.setScore(currentPlayer.getScore() + 2);
            score += 2;
            logMessage = String.format("score,P%d,%d,%d,thirtyone",tempSegment.lastPlayer, currentPlayer.getScore(), score);
            logger.log(logMessage);
        } else if (Cribbage.total(tempSegment.segment) == 15) {
            currentPlayer.setScore(currentPlayer.getScore() + 2);
            score += 2;
            logMessage = String.format("score,P%d,%d,%d,fifteen",tempSegment.lastPlayer, currentPlayer.getScore(), score);
            logger.log(logMessage);
        } else if (tempSegment.go == true && tempSegment.newSegment == true) {
            currentPlayer.setScore(currentPlayer.getScore() + 1);
            score += 1;
            logMessage = String.format("score,P%d,%d,%d,go",tempSegment.lastPlayer, currentPlayer.getScore(), score);
            logger.log(logMessage);
        }
        return score;
    }
}