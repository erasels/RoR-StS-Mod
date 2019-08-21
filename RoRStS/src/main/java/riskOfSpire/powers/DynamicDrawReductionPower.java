package riskOfSpire.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.powers.abstracts.RoRStSTwoAmountPower;

public class DynamicDrawReductionPower extends RoRStSTwoAmountPower implements CloneablePowerInterface, NonStackablePower {
    public static final String POWER_ID = RiskOfSpire.makeID("DynamicDrawReduction");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private boolean justApplied = true;

    public DynamicDrawReductionPower(AbstractCreature owner, int amount, int amount2) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.amount2 = amount2;
        type = AbstractPower.PowerType.DEBUFF;
        isTurnBased = true;
        updateDescription();
        loadRegion("lessdraw");
    }

    public void onInitialApplication() { AbstractDungeon.player.gameHandSize -= amount2; }

    @Override
    public boolean isStackable(AbstractPower p) {
        return amount2 == ((DynamicDrawReductionPower)p).amount2;
    }

    public void atEndOfRound() {
        if (this.justApplied) {
            this.justApplied = false;
            return;
        }
        AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, this, 1));
    }

    public void onRemove() { AbstractDungeon.player.gameHandSize += amount2; }

    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + (amount==1?DESCRIPTIONS[1]:DESCRIPTIONS[2]) + amount2 + (amount2==1?DESCRIPTIONS[3]:DESCRIPTIONS[4]);
    }

    @Override
    public AbstractPower makeCopy() {
        return new DynamicDrawReductionPower(owner, amount, amount2);
    }
}