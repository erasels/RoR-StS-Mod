package riskOfSpire.relics.Usable;

import com.megacrit.cardcrawl.actions.defect.SeekAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.relics.Abstracts.UsableRelic;

public class RadarScanner extends UsableRelic {
    public static final String ID = RiskOfSpire.makeID("RadarScanner");
    private static final int ADD = 1;

    public RadarScanner() {
        super(ID, "RadarScanner.png", RelicTier.UNCOMMON, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onRightClickInCombat() {
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new SeekAction(ADD));
        this.counter = 5;
        this.stopPulse();
    }

    @Override
    public AbstractRelic makeCopy() {
        return new RadarScanner();
    }
}
