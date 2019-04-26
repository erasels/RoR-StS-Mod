package riskOfSpire.actions.general;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class RaisePlayerMaxHPAction extends AbstractGameAction {
    private boolean showEffect;

    public RaisePlayerMaxHPAction(int amount, boolean showEffect)
    {
        this.amount = amount;
        this.target = AbstractDungeon.player;
        this.showEffect = showEffect;
    }

    @Override
    public void update() {
        AbstractDungeon.player.increaseMaxHp(amount, showEffect);
        this.isDone = true;
    }
}
