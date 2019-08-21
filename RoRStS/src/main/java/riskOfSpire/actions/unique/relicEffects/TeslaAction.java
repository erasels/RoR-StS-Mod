package riskOfSpire.actions.unique.relicEffects;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class TeslaAction extends AbstractGameAction {
    public TeslaAction(int amount)
    {
        this.actionType = ActionType.DAMAGE;
        setValues(null, AbstractDungeon.player, amount);
    }

    @Override
    public void update() {
        DamageInfo info = new DamageInfo(this.source, amount, DamageInfo.DamageType.THORNS);

        for (int i = 0; i < amount; ++i)
        {
            AbstractDungeon.actionManager.addToTop(new ZapRandomEnemyAction(info));
        }

        this.isDone = true;
    }
}
