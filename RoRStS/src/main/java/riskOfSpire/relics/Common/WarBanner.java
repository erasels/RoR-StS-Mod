package riskOfSpire.relics.Common;

import com.evacipated.cardcrawl.mod.stslib.relics.BetterOnSmithRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.relics.Abstracts.StackableRelic;

public class WarBanner extends StackableRelic implements BetterOnSmithRelic {
    public static final String ID = RiskOfSpire.makeID("WarBanner");
    private static final int STRENGTH_PER = 1;

    public WarBanner() {
        super(ID, "BundleOfFireworks.png", RelicTier.COMMON, LandingSound.FLAT);
        this.counter = 0;
    }

    @Override
    public void betterOnSmith(AbstractCard abstractCard) {
        this.flash();
        this.counter += STRENGTH_PER * relicStack;
    }

    @Override
    public void atBattleStart() {
        if (this.counter > 0)
        {
            this.flash();

            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, this.counter), this.counter));
            AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));

            this.counter = 0;
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + STRENGTH_PER * relicStack + DESCRIPTIONS[1];
    }

    public AbstractRelic makeCopy() {
        return new WarBanner();
    }
}