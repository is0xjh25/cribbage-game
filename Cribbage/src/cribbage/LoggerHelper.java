package cribbage;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

public class LoggerHelper {
    private Logger logger = Logger.getInstance();

    private static final LoggerHelper loggerHelper = new LoggerHelper();

    private LoggerHelper() {

    }


    public static LoggerHelper getInstance() {
        return loggerHelper;
    }

    public void logScore(Cribbage.Segment tempSegment, int newScore, int score, Cribbage.ScoreType scoreType) {
        String logMessage = String.format("score,P%d,%d,%d,%s",tempSegment.lastPlayer, newScore, score, scoreType.scoreType);
        logger.log(logMessage);
    }


    public void logScore(Cribbage.Segment tempSegment, int newScore, int score, Cribbage.ScoreType scoreType, String hand) {
        String logMessage = String.format("score,P%d,%d,%d,%s,%s",tempSegment.lastPlayer, newScore, score, scoreType.scoreType, hand);
        logger.log(logMessage);
    }


    public void logDiscard(Hand hand, int currentPlayer) {
        String logMessage = String.format("discard,P%d,%s", currentPlayer, Cribbage.canonical(hand));
        logger.log(logMessage);
    }

    public void logDeal(Hand hand, int currentPlayer) {
        String logMessage = String.format("deal,P%d,%s", currentPlayer, Cribbage.canonical(hand));
        logger.log(logMessage);
    }


    public void logSeed(int seed) {
        String logMessage = String.format("seed,%d", seed);
        logger.log(logMessage);
    }

    public void logplayer(int playerId, String playerType) {
        String logMessage = String.format("%s,P%d", playerType, playerId);
        logger.log(logMessage);
    }


//    public void logScoreShow(Cribbage.Segment tempSegment, int newScore, int score, Cribbage.ScoreType scoreType) {
//        String logMessage = String.format("score,P%d,%d,%d,%s,%s",tempSegment.lastPlayer, newScore, score, scoreType.scoreType, Cribbage.canonical(tempSegment.segment));
//        logger.log(logMessage);
//    }


    public void logShow(Cribbage.Segment tempSegment, Card starterCard) {
        String logMessage = String.format("show,P%d,%s+%s",tempSegment.lastPlayer,Cribbage.canonical(starterCard),Cribbage.canonical(tempSegment.segment));
        logger.log(logMessage);
    }

    public void loggerStarter(Card card) {
        String logMessage = String.format("starter,%s", Cribbage.canonical(card));
        logger.log(logMessage);
    }


    public void logPlay(Cribbage.Segment segment, int currentPlayer, Card nextCard) {
        String logMessage = String.format("play,P%d,%d,%s", currentPlayer, Cribbage.total(segment.segment), Cribbage.canonical(nextCard));
        logger.log(logMessage);
    }
}
