package riskOfSpire.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import riskOfSpire.RiskOfSpire;

public class GainDexPower extends AbstractPower {
    public static final String POWER_ID = RiskOfSpire.makeID("GainDex");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private boolean justApplied = true;

    public GainDexPower(AbstractCreature owner, int newAmount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = newAmount;
        this.type = PowerType.BUFF;
        updateDescription();
        loadRegion("shackle");
        if (this.amount >= 999)
            this.amount = 999;
        if (this.amount <= -999)
            this.amount = -999;
    }

    public void playApplyPowerSfx() { CardCrawlGame.sound.play("POWER_SHACKLE", 0.05F); }

    public void stackPower(int stackAmount) {
        fontScale = 8.0F;
        amount += stackAmount;
        if (amount == 0)
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(owner, owner, this));
        if (amount >= 999)
            amount = 999;
        if (amount <= -999)
            amount = -999;
    }

    public void reducePower(int reduceAmount) {
        fontScale = 8.0F;
        amount -= reduceAmount;
        if (amount == 0)
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(owner, owner, this));
        if (amount >= 999)
            amount = 999;
        if (amount <= -999)
            amount = -999;
    }

    public void updateDescription() { description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1]; }

    public void atEndOfTurn(boolean isPlayer) {
        if(justApplied) {
            justApplied = false;
            return;
        }
        flash();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, new DexterityPower(owner, amount), amount));
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, this));
    }
}
