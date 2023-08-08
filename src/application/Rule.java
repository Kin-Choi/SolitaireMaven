package application;

import cards.Mode;

public class Rule {
    private final int
            startScore,
            wasteToWorkingStack,
            wasteToSuitStack,
            WorkingStackToSuitStack,
            turnOverWorkingStackCard,
            SuitStackToWorkingStack,
            resetWaste,
            loseScore;

    public Rule(Mode mode) {
        if (mode.equals(Mode.VEGAS)) {
            this.startScore=-50;
            this.wasteToWorkingStack=5;
            this.wasteToSuitStack=5;
            this.WorkingStackToSuitStack=5;
            this.turnOverWorkingStackCard=5;
            this.SuitStackToWorkingStack=15;
            this.resetWaste=50;
            this.loseScore=-100;
        } else {
            this.startScore=0;
            this.wasteToWorkingStack=5;
            this.wasteToSuitStack=10;
            this.WorkingStackToSuitStack=10;
            this.turnOverWorkingStackCard=5;
            this.SuitStackToWorkingStack=15;
            this.resetWaste=50;
            this.loseScore=-50;
        }
    }

    public int getStartScore() {
        return startScore;
    }

    public int getWasteToWorkingStack() {
        return wasteToWorkingStack;
    }

    public int getWasteToSuitStack() {
        return wasteToSuitStack;
    }

    public int getWorkingStackToSuitStack() {
        return WorkingStackToSuitStack;
    }

    public int getTurnOverWorkingStackCard() {
        return turnOverWorkingStackCard;
    }

    public int getSuitStackToWorkingStack() {
        return SuitStackToWorkingStack;
    }

    public int getResetWaste() {
        return resetWaste;
    }

    public int getLoseScore() {
        return loseScore;
    }
}
