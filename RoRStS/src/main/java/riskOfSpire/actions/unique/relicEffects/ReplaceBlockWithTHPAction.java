package riskOfSpire.actions.unique.relicEffects;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ReplaceBlockWithTHPAction extends AbstractGameAction {
    private static final float DUR = 0.25f;

    public ReplaceBlockWithTHPAction(AbstractCreature target, AbstractCreature source) {
        this.setValues(target, source);
        this.actionType = ActionType.BLOCK;
        this.duration = DUR;
    }

    public void update() {
        if (!target.isDeadOrEscaped() && duration == DUR && target.currentBlock > 0) {
            int tmp = target.currentBlock;
            target.loseBlock(tmp, false);
            AbstractDungeon.actionManager.addToTop(new AddTemporaryHPAction(target, source, tmp));
        }

        this.tickDuration();
    }
}
