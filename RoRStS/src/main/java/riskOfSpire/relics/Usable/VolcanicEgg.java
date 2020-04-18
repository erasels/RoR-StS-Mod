package riskOfSpire.relics.Usable;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.FlameBarrierPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.relics.Abstracts.UsableRelic;

public class VolcanicEgg extends UsableRelic {
    public static final String ID = RiskOfSpire.makeID("VolcanicEgg");

    private static final int REFLECT = 8;

    private static final int COOLDOWN = 6;

    public VolcanicEgg() {
        super(ID, "DisposableMissileLauncher.png", RelicTier.UNCOMMON, LandingSound.SOLID);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + REFLECT + DESCRIPTIONS[1] + getCooldownString();
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

            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FlameBarrierPower(AbstractDungeon.player, REFLECT), REFLECT));

            this.activateCooldown();
            this.stopPulse();
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new VolcanicEgg();
    }
}