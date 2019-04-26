package riskOfSpire.relics.Usable;

import com.megacrit.cardcrawl.actions.defect.SeekAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.relics.Abstracts.UsableRelic;

public class RadarScanner extends UsableRelic {
    public static final String ID = RiskOfSpire.makeID("RadarScanner");
    private static final int ADD = 1;
    private static final int COOLDOWN = 5;

    public RadarScanner() {
        super(ID, "RadarScanner.png", RelicTier.UNCOMMON, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + getFinalCooldown() + DESCRIPTIONS[1];
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
        if (this.counter == 0)
        {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new SeekAction(ADD));
            this.activateCooldown();
            this.stopPulse();
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new RadarScanner();
    }
}
