package riskOfSpire.actions.general;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;

public class SetCostForTurnAction extends AbstractGameAction {
    private AbstractCard card;

    public SetCostForTurnAction(AbstractCard toSet, int value)
    {
        this.amount = value;
        this.card = toSet;
    }

    @Override
    public void update() {
        if (card.costForTurn >= 0 || card.cost >= 0)
        {
            for (AbstractCard c : GetAllInBattleInstances.get(card.uuid))
            {
                c.modifyCostForTurn(-c.costForTurn);
            }
        }
        this.isDone = true;
    }
}
