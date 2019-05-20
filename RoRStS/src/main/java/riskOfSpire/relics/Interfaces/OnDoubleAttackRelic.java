package riskOfSpire.relics.Interfaces;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

public interface OnDoubleAttackRelic {
    AbstractCard beforeDoubleAttack(AbstractCard card, UseCardAction action);
}
