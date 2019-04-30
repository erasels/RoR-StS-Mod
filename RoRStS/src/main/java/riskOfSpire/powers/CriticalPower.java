package riskOfSpire.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.powers.abstracts.RoRStSPower;

public class CriticalPower extends RoRStSPower implements CloneablePowerInterface {
    public static final String POWER_ID = RiskOfSpire.makeID("Critical");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private boolean dontRemove;

    public CriticalPower(AbstractCreature owner, boolean dontRemove) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        isTurnBased = false;
        amount = -1;
        type = AbstractPower.PowerType.BUFF;
        this.dontRemove = dontRemove;
        setImage("84_Critical.png", "32_Critical.png");
        updateDescription();
    }

    @Override
    public float atDamageFinalGive(float damageAmount, DamageInfo.DamageType info) {
        if (info == DamageInfo.DamageType.NORMAL) {
            return (damageAmount * 2);
        }

        return damageAmount;
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK && !dontRemove && card.damage > 0) {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(owner, owner, this.ID));
        }
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public void stackPower(int i) {
    }

    @Override
    public AbstractPower makeCopy() {
        return new CriticalPower(owner, dontRemove);
    }
}