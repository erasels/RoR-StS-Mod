package riskOfSpire.relics.Common;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.relics.Abstracts.StackableRelic;

public class SoldiersSyringe extends StackableRelic {
    public static final String ID = RiskOfSpire.makeID("SoldiersSyringe");
    private static final int CARD_AMT = 20;

    public SoldiersSyringe() {
        super(ID, "SoldiersSyringe.png", RelicTier.COMMON, LandingSound.CLINK);
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        if(c.type == CardType.ATTACK) {
            flash();
            manipCharge(1);
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + ATTACKS_TO_TRIGGER + DESCRIPTIONS[1] + REGEN_AMT + DESCRIPTIONS[2];
    }

    @Override
    public void onEquip() {
        startingCharges();
    }

    private void startingCharges()
    {
        setCounter(START_CHARGE);
    }

    private void manipCharge(int amt) {
        if (counter < 0) {
            counter = 0;
        }
        setCounter(counter + amt);

        if (counter >= ATTACKS_TO_TRIGGER) {
            flash();
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new RegenPower(AbstractDungeon.player, REGEN_AMT), REGEN_AMT));
            counter -= ATTACKS_TO_TRIGGER;
        }
    }

    public AbstractRelic makeCopy() {
        return new SoldiersSyringe();
    }
}