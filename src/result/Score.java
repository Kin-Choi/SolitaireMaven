package result;

import cards.Mode;

public class Score {

    private int score;

    public Score(Mode mode) {
        if (mode.equals(Mode.VEGAS)) {
            this.score = -50;
        } else {
            this.score = 0;
        }
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }



}
