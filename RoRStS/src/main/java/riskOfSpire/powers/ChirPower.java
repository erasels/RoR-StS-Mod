package riskOfSpire.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.powers.abstracts.RoRStSPower;

import java.util.ArrayList;

public class ChirPower extends RoRStSPower implements CloneablePowerInterface {
    public static final String POWER_ID = RiskOfSpire.makeID("Chir");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private ArrayList<AbstractCard> storedCards;

    private boolean naturalTrigger = false;

    public ChirPower(AbstractCreature owner, ArrayList<AbstractCard> storedCards) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        isTurnBased = false;
        amount = -1;
        type = AbstractPower.PowerType.BUFF;
        this.storedCards = storedCards;
        loadRegion("corruption");
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        storedCards.forEach(c->AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(c)));
        naturalTrigger = true;
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, this));
    }

    @Override
    public void onRemove() {
        if(!naturalTrigger) {
            storedCards.forEach(c -> AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(c, 1, true, true)));
        }
    }

    public void updateDescription() {
        StringBuilder sb = new StringBuilder();
        storedCards.forEach(c-> sb.append(c.name).append(", "));
        if(!storedCards.isEmpty()) {
            sb.delete(sb.lastIndexOf(","), sb.length() - 1);
        }
        this.description = DESCRIPTIONS[0] + FontHelper.colorString(sb.toString(), "b");
    }

    @Override
    public AbstractPower makeCopy() {
        return new ChirPower(owner, storedCards);
    }
}