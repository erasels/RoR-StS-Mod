package riskOfSpire.relics.Uncommon;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.relics.Abstracts.StackableRelic;
import riskOfSpire.relics.Interfaces.ModifyCritDamageRelic;
import riskOfSpire.util.RiskOfRainRelicHelper;

public class HarvesterScythe extends StackableRelic implements ModifyCritDamageRelic {
    public static final String ID = RiskOfSpire.makeID("HarvesterScythe");
    private static final int BASE_HEAL = 3;

    public HarvesterScythe() {
        super(ID, "HarvesterScythe.png", RelicTier.UNCOMMON, LandingSound.SOLID);
        isCritical = true;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + (relicStack*100) + DESCRIPTIONS[1] + getVal() + DESCRIPTIONS[2];
    }

    @Override
    public float modifyCrit(float critMod) {
        return critMod + relicStack;
    }

    @Override
    public void afterCrit() {
        AbstractDungeon.actionManager.addToTop(new HealAction(AbstractDungeon.player, AbstractDungeon.player, getVal()));
    }

    @Override
    public boolean canSpawn() {
        return RiskOfRainRelicHelper.hasCritRelic();
    }

    public int getVal() {
        return BASE_HEAL + (relicStack * 2);
    }

    public AbstractRelic makeCopy() {
        return new HarvesterScythe();
    }
}
