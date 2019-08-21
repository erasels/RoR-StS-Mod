package riskOfSpire.relics.Usable;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.powers.relicPowers.PlayerFlightPower;
import riskOfSpire.relics.Abstracts.UsableRelic;

import java.util.ArrayList;
import java.util.Arrays;

public class MilkyChrysalis extends UsableRelic {
    public static final String ID = RiskOfSpire.makeID("MilkyChrysalis");
    private static final int FLIGHT_AMT = 2;
    private static final int COOLDOWN = 15;

    public MilkyChrysalis() {
        super(ID, "MilkyChrysalis.png", RelicTier.RARE, LandingSound.FLAT);
        addPowerTips(new ArrayList<PowerTip>(Arrays.asList(new PowerTip(PlayerFlightPower.NAME, PlayerFlightPower.getDesc()))));
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + FLIGHT_AMT + DESCRIPTIONS[1] + getCooldownString();
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
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new PlayerFlightPower(AbstractDungeon.player, FLIGHT_AMT), FLIGHT_AMT));
            this.activateCooldown();
            this.stopPulse();
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new MilkyChrysalis();
    }
}
