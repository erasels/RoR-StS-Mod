package riskOfSpire.actions.general;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import riskOfSpire.patches.ForUsableRelics.UsableRelicSlot;
import riskOfSpire.relics.Abstracts.UsableRelic;

public class ReduceUsableCooldownAction extends AbstractGameAction {
    private int cdAmt;
    private boolean flash;

    public ReduceUsableCooldownAction(AbstractCreature source, int cdAmt, boolean flash) {
        this.source = source;
        this.cdAmt = cdAmt;
        this.flash = flash;
    }

    @Override
    public void update() {
        UsableRelic r = UsableRelicSlot.usableRelic.get(AbstractDungeon.player);
        if (r != null) {
            r.reduceCooldown(cdAmt);
            if (flash) {
                r.flash();
            }
        }
        isDone = true;
    }
}
