package riskOfSpire.actions.unique;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import riskOfSpire.powers.elites.OverloadingPower;

public class OverloadingAction extends AbstractGameAction {
    private static final float DUR = 0.25f;

    public OverloadingAction(AbstractCreature c) {
        this.setValues(c, c);
        this.actionType = ActionType.BLOCK;
        this.duration = DUR;
    }

    public void update() {
        if (!target.isDeadOrEscaped() && duration == DUR && TempHPField.tempHp.get(target) < target.maxHealth) {
            int maxTHP = target.maxHealth - TempHPField.tempHp.get(target);
            int tmp = Math.min(maxTHP, target.maxHealth* MathUtils.floor(OverloadingPower.OVERLOAD_PERCENTAGE));
            AbstractDungeon.actionManager.addToTop(new AddTemporaryHPAction(target, source, tmp));
        }

        this.tickDuration();
    }
}
