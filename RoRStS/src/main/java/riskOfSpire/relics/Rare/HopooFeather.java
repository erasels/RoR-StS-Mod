package riskOfSpire.relics.Rare;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.powers.relicPowers.PlayerFlightPower;
import riskOfSpire.relics.Abstracts.StackableRelic;

public class HopooFeather extends StackableRelic {
    public static final String ID = RiskOfSpire.makeID("HopooFeather");

    public HopooFeather() {
        super(ID, "HopooFeather.png", RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + relicStack + DESCRIPTIONS[1];
    }

    @Override
    public void atPreBattle() {
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new PlayerFlightPower(AbstractDungeon.player, relicStack), relicStack));
    }

    public AbstractRelic makeCopy() {
        return new HopooFeather();
    }
}
