package cribbage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ScoringStrategyFactory {
    private static final ScoringStrategyFactory scoringStrategyFactory = new ScoringStrategyFactory();

    private ScoringStrategy scoringStrategy = null;

    private ScoringStrategyFactory() {

    }

    public static ScoringStrategyFactory getInstance() {
        return scoringStrategyFactory;
    }

    public ScoringStrategy getScoringStrategy(Cribbage.StrategyType type) {
        CompositeStrategy compositeStrategy = null;
        switch (type) {
            case PLAY:
                compositeStrategy = new CompositePlayStrategy();

                //  the order is matter here
                compositeStrategy.addStrategy(new PlayPairStrategy());
                compositeStrategy.addStrategy(new PlayRunStrategy());
                compositeStrategy.addStrategy(new PlayTotalAndLastStrategy());
                break;
            case SHOW:
                compositeStrategy = new CompositeShowStrategy();

                // the order is matter here
                compositeStrategy.addStrategy(new ShowFlushStrategy());
                compositeStrategy.addStrategy(new ShowJackStrategy());
                compositeStrategy.addStrategy(new ShowPairStrategy());
                compositeStrategy.addStrategy(new ShowRunStrategy());
                break;
        }

        scoringStrategy = compositeStrategy;
        return scoringStrategy;
    }
}
