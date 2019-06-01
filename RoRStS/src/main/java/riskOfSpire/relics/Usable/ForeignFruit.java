package riskOfSpire.relics.Usable;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.actions.unique.ReplaceBlockWithTHPAction;
import riskOfSpire.relics.Abstracts.UsableRelic;

public class ForeignFruit extends UsableRelic {
    public static final String ID = RiskOfSpire.makeID("ForeignFruit");
    private static final int COOLDOWN = 10;

    public ForeignFruit() {
        super(ID, "ForeignFruit.png", RelicTier.COMMON, LandingSound.SOLID);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + getCooldownString();
    }

    @Override
    public boolean isUsable() {
        return true;
    }

    @Override
    public int getBaseCooldown() {
        return COOLDOWN;
    }

    @Override
    public void onRightClickInCombat() {
        if (this.counter == 0 && AbstractDungeon.player.currentBlock>0) {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new ReplaceBlockWithTHPAction(AbstractDungeon.player, AbstractDungeon.player));
            this.activateCooldown();
            this.stopPulse();
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new ForeignFruit();
    }
}
