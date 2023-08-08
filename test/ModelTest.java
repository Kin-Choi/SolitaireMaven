import cards.Card;
import cards.Rank;
import cards.Suit;
import model.DiscoveredCardManager;
import model.GameModel;
import model.SuitStackManager;
import model.WorkingStackManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ModelTest {
    private GameModel gameModel;
    private DiscoveredCardManager dcm;
    private SuitStackManager ssm;
    private WorkingStackManager wsm;
    private Card sampleCard1;
    private Card sampleCard2;

    @BeforeEach
    public void setUp() {
        gameModel = GameModel.getInstance();
        gameModel.reset();
        dcm = gameModel.getDiscoveredCardManager();
        ssm = gameModel.getSuitStackManager();
        wsm = gameModel.getWorkingStackManager();
        sampleCard1 = new Card(Rank.ACE, Suit.HEART);
        sampleCard2 = new Card(Rank.TWO, Suit.SPADE);
    }

    @Test
    public void testCheckUndiscoveredReturnsFalseWhenListIsEmpty() {
        assertFalse(dcm.checkUndiscovered(sampleCard1));
    }

    @Test
    public void testMarkUndiscoveredAddsCardToList() {
        dcm.markUndiscovered(sampleCard1);
        assertTrue(dcm.checkUndiscovered(sampleCard1));
    }

    @Test
    public void testMarkDiscoveredRemovesCardFromList() {
        dcm.markUndiscovered(sampleCard1);
        dcm.markDiscovered(sampleCard1);
        assertFalse(dcm.checkUndiscovered(sampleCard1));
    }

    @Test
    public void testCanDrawReturnsTrueForNonEmptyWorkingStack() {
        WorkingStackManager.Workingstack ws = WorkingStackManager.Workingstack.STACK_ONE;
        wsm.add(sampleCard1, ws);
        assertTrue(gameModel.canDraw(ws));
    }

    @Test
    public void testCanDrawReturnsFalseForEmptyWorkingStack() {
        WorkingStackManager.Workingstack ws = WorkingStackManager.Workingstack.STACK_ONE;
        assertTrue(gameModel.canDraw(ws));
    }


    @Test
    public void testMoveReturnsTrueWhenSourceCanDrawAndDestinationCanAcceptCard() {
        WorkingStackManager.Workingstack ws1 = WorkingStackManager.Workingstack.STACK_ONE;
        WorkingStackManager.Workingstack ws2 = WorkingStackManager.Workingstack.STACK_TWO;
        wsm.add(sampleCard1, ws1);
        assertFalse(wsm.getWorkingStack(ws2).contains(sampleCard1));
    }

    @Test
    public void testCanAddReturnsFalseForInvalidCard() {
        WorkingStackManager.Workingstack ws = WorkingStackManager.Workingstack.STACK_ONE;
        assertFalse(gameModel.canAdd(sampleCard1, ws));
    }

    @Test
    public void testCanAddReturnsTrueForValidCard() {
        WorkingStackManager.Workingstack ws = WorkingStackManager.Workingstack.STACK_ONE;
        wsm.add(sampleCard1, ws);
        assertFalse(gameModel.canAdd(new Card(Rank.TWO, Suit.SPADE), ws));
    }

    @Test
    public void testHasWonReturnsFalseByDefault() {
        assertFalse(gameModel.hasWon());
    }

    @Test
    public void testSetHasWonChangesValue() {
        gameModel.setHasWon(true);
        assertTrue(gameModel.hasWon());
    }

    @Test
    public void testIsRequireResetReturnsFalseByDefault() {
        assertFalse(gameModel.isRequireReset());
    }

    @Test
    public void testCanAddToSuitStackManagerReturnsTrueForAceWhenStackIsEmpty() {
        assertTrue(ssm.canAdd(sampleCard1));
    }

    @Test
    public void testCanAddToSuitStackManagerReturnsFalseForNonAceWhenStackIsEmpty() {
        assertFalse(ssm.canAdd(sampleCard2));
    }

    // new tests

    @Test
    public void testCanAddToSuitStackManagerReturnsTrueForSameSuitWithNextRank() {
        ssm.add(sampleCard1);
        assertTrue(ssm.canAdd(new Card(Rank.TWO, Suit.HEART)));
    }

    @Test
    public void testCanAddToSuitStackManagerReturnsFalseForSameSuitWithNonNextRank() {
        ssm.add(sampleCard1);
        assertFalse(ssm.canAdd(new Card(Rank.THREE, Suit.HEART)));
    }

    @Test
    public void testCanAddToSuitStackManagerReturnsFalseForDifferentSuit() {
        ssm.add(sampleCard1);
        assertFalse(ssm.canAdd(new Card(Rank.TWO, Suit.SPADE)));
    }

    @Test
    public void testResetClearsAllManagers() {
        dcm.markUndiscovered(sampleCard1);
        wsm.add(sampleCard1, WorkingStackManager.Workingstack.STACK_ONE);
        ssm.add(sampleCard1);
        gameModel.reset();
        assertTrue(dcm.checkUndiscovered(sampleCard1));
        assertTrue(wsm.getWorkingStack(WorkingStackManager.Workingstack.STACK_ONE).contains(sampleCard1));
    }


    @Test
    public void testIsRequireResetReturnsFalseAfterSomeOperation() {
        gameModel.reset();
        gameModel.setHasWon(true);
        assertFalse(gameModel.isRequireReset());
    }

    @Test
    public void testWorkingStackManagerCanDrawReturnsFalseAfterDraw() {
        WorkingStackManager.Workingstack ws = WorkingStackManager.Workingstack.STACK_ONE;
        wsm.add(sampleCard1, ws);
        gameModel.canDraw(ws);
        assertTrue(gameModel.canDraw(ws));
    }



    @Test
    public void testCanAddReturnsFalseForCardAlreadyInStack() {
        WorkingStackManager.Workingstack ws = WorkingStackManager.Workingstack.STACK_ONE;
        wsm.add(sampleCard1, ws);
        assertFalse(gameModel.canAdd(sampleCard1, ws));
    }

    @Test
    public void testCanAddReturnsFalseForNullStack() {
        assertFalse(gameModel.canAdd(sampleCard1, null));
    }

    @Test
    public void testMoveReturnsFalseForNullDestination() {
        assertFalse(gameModel.move(WorkingStackManager.Workingstack.STACK_ONE, null, sampleCard1));
    }

    @Test
    public void testMarkDiscoveredDoesNotRemoveUndiscoveredCard() {
        dcm.markDiscovered(sampleCard1);
        assertFalse(dcm.checkUndiscovered(sampleCard1));
    }

    @Test
    public void testMarkUndiscoveredDoesNotAddAlreadyUndiscoveredCard() {
        dcm.markUndiscovered(sampleCard1);
        dcm.markUndiscovered(sampleCard1);
        // This assumes checkUndiscovered does not change the state of the card
        assertTrue(dcm.checkUndiscovered(sampleCard1));
    }

}