package riskOfSpire.relics.Common;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.relics.Abstracts.StackableRelic;

public class Medkit extends StackableRelic {
    public static final String ID = RiskOfSpire.makeID("Medkit");

    public Medkit() {
        super(ID, "Medkit.png", RelicTier.COMMON, LandingSound.SOLID);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + relicStack + DESCRIPTIONS[1];
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type == DamageInfo.DamageType.NORMAL && (damageAmount > 0 || (info.output > 0 && AbstractDungeon.player.currentBlock == 0)) &&info.owner != AbstractDungeon.player) {
            flash();
            AbstractDungeon.actionManager.addToBottom(new AddTemporaryHPAction(AbstractDungeon.player, AbstractDungeon.player, relicStack));
        }
        return damageAmount;
    }


    public AbstractRelic makeCopy() {
        return new Medkit();
    }
}