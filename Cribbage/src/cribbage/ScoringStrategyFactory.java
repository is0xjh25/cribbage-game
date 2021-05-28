package cribbage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/* SWEN-30006-Project2
    Created by Workshop16Team02, May 28th 2021
*/
public class ScoringStrategyFactory {
    private static final ScoringStrategyFactory scoringStrategyFactory = new ScoringStrategyFactory();

    private ScoringStrategy scoringStrategy = null;

    private ScoringStrategyFactory() {

    }

    public static ScoringStrategyFactory getInstance() {
        return scoringStrategyFactory;
    }

    // Get the composite strategy as required
    public ScoringStrategy getScoringStrategy(Cribbage.StrategyType type) {
        CompositeStrategy compositeStrategy = null;
        switch (type) {
            case PLAY:
                compositeStrategy = new CompositePlayStrategy();

                //  the order is matter here
                // compositeStrategy.addStrategy(new PlayTotalAndLastStrategy());
                compositeStrategy.addStrategy(new PlayRunStrategy());
                compositeStrategy.addStrategy(new PlayPairStrategy());
                break;
            case SHOW:
                compositeStrategy = new CompositeShowStrategy();

                // the order is matter here
                compositeStrategy.addStrategy(new ShowFifteenStrategy());
                compositeStrategy.addStrategy(new ShowRunStrategy());
                compositeStrategy.addStrategy(new ShowPairStrategy());
                compositeStrategy.addStrategy(new ShowFlushStrategy());
                compositeStrategy.addStrategy(new ShowJackStrategy());
                break;
        }

        scoringStrategy = compositeStrategy;
        return scoringStrategy;
    }
}
