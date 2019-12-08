package riskOfSpire.relics.Rare;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.actions.general.ReduceUsableCooldownAction;
import riskOfSpire.relics.Abstracts.StackableRelic;

public class SoulboundCatalyst extends StackableRelic {
    public static final String ID = RiskOfSpire.makeID("SoulboundCatalyst");

    public SoulboundCatalyst() {
        super(ID, "SoulboundCatalyst.png", RelicTier.RARE, LandingSound.SOLID);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + getVal() + DESCRIPTIONS[1];
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if ((info.owner != null) && (info.type != DamageInfo.DamageType.HP_LOSS) && (info.type != DamageInfo.DamageType.THORNS) && (damageAmount > 0 || (info.output > 0 && AbstractDungeon.player.currentBlock == 0))) {
            AbstractDungeon.actionManager.addToTop(new ReduceUsableCooldownAction(AbstractDungeon.player, getVal(), true));
        }
        return damageAmount;
    }

    public int getVal() {
        return relicStack + 1;
    }

    public AbstractRelic makeCopy() {
        return new SoulboundCatalyst();
    }
}