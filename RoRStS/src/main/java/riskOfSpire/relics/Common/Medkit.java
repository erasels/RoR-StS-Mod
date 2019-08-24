package riskOfSpire.relics.Common;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.relics.Abstracts.StackableRelic;

public class Medkit extends StackableRelic {
    public static final String ID = RiskOfSpire.makeID("Medkit");

    private static final int DAMAGE_GATE = 5;

    public Medkit() {
        super(ID, "Medkit.png", RelicTier.COMMON, LandingSound.SOLID);
        isTempHP = true;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + DAMAGE_GATE + DESCRIPTIONS[1] + relicStack + DESCRIPTIONS[2];
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type == DamageInfo.DamageType.NORMAL && (damageAmount > DAMAGE_GATE || (info.output > DAMAGE_GATE && AbstractDungeon.player.currentBlock == 0)) &&info.owner != AbstractDungeon.player) {
            flash();
            AbstractDungeon.actionManager.addToBottom(new AddTemporaryHPAction(AbstractDungeon.player, AbstractDungeon.player, relicStack));
        }
        return damageAmount;
    }


    public AbstractRelic makeCopy() {
        return new Medkit();
    }
}