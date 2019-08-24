package riskOfSpire.powers.relicPowers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.actions.unique.BurningAction;
import riskOfSpire.powers.abstracts.RoRStSTwoAmountPower;

public class BurningPower extends RoRStSTwoAmountPower implements CloneablePowerInterface {
    public static final String POWER_ID = RiskOfSpire.makeID("Burning");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public BurningPower(AbstractCreature owner, int amount, int amount2) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.amount2 = amount2;
        type = AbstractPower.PowerType.DEBUFF;
        updateDescription();
        isTurnBased = true;
        loadRegion("explosive");
    }

    @Override
    public void updateDescription() {
        if (amount == 1) {
            this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2] + DESCRIPTIONS[3] + amount2 + DESCRIPTIONS[4];
        } else {
            this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + DESCRIPTIONS[3] + amount2 + DESCRIPTIONS[4];
        }
    }

    @Override
    public void atStartOfTurn() {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                flashWithoutSound();
                AbstractDungeon.actionManager.addToBottom(new BurningAction(owner, owner, amount2));
            }
        }

        if (this.amount == 0) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        } else {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, this, 1));
        }
    }

    @Override
    public void stackPower(int i) {
        super.stackPower(i);
        updateDescription();
    }

    @Override
    public AbstractPower makeCopy() {
        return new BurningPower(owner, amount, amount2);
    }
}