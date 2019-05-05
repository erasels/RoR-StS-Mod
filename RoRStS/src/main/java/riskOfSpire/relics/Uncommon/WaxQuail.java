package riskOfSpire.relics.Uncommon;

import com.evacipated.cardcrawl.mod.stslib.relics.OnReceivePowerRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BlurPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.relics.Abstracts.StackableRelic;

public class WaxQuail extends StackableRelic implements OnReceivePowerRelic {
    public static final String ID = RiskOfSpire.makeID("WaxQuail");
    public WaxQuail() {
        super(ID, "Infusion.png", RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public boolean onReceivePower(AbstractPower abstractPower, AbstractCreature abstractCreature) {
        if(abstractPower.ID.equals(DexterityPower.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new BlurPower(AbstractDungeon.player, relicStack), relicStack));
        }
        return true;
    }
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + relicStack + DESCRIPTIONS[1];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new WaxQuail();
    }
}
