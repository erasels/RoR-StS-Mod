package riskOfSpire.actions.general;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class PlayerLoseMaxHPAction extends AbstractGameAction {
    public PlayerLoseMaxHPAction(int amount) {
        this.amount = amount;
        this.actionType = ActionType.DAMAGE;
    }

    public void update() {
        AbstractDungeon.player.decreaseMaxHealth(amount);
        isDone = true;
    }
}

