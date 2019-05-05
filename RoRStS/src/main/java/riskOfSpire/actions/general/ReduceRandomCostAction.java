package riskOfSpire.actions.general;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GetAllInBattleInstances;

import java.util.ArrayList;

public class ReduceRandomCostAction extends AbstractGameAction {
    private int reduction;

    public ReduceRandomCostAction(int numCards, int reduction)
    {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.reduction = reduction;
        this.amount = numCards;
    }

    @Override
    public void update() {
        if (amount > 0) {
            ArrayList<AbstractCard> betterOptions = new ArrayList<>();
            ArrayList<AbstractCard> worseOptions = new ArrayList<>();

            for (AbstractCard c : AbstractDungeon.player.hand.group)
            {
                if (c.costForTurn > 0) {
                    betterOptions.add(c);
                } else if (c.cost > 0) {
                    worseOptions.add(c);
                }
            }

            if (betterOptions.size() >= amount)
            {
                for (int i = 0; i < amount; i++)
                {
                    AbstractCard c = betterOptions.remove(AbstractDungeon.cardRandomRng.random(betterOptions.size() - 1));

                    reduceCost(c);
                }
            }
            else //amount > betterOptions.size
            {
                int extraCount = this.amount - betterOptions.size();
                for (AbstractCard c : betterOptions)
                {
                    reduceCost(c);
                }

                if (worseOptions.size() > extraCount)
                {
                    for (int i = 0; i < extraCount; i++)
                    {
                        AbstractCard c = worseOptions.remove(AbstractDungeon.cardRandomRng.random(worseOptions.size() - 1));

                        reduceCost(c);
                    }
                }
                else if (worseOptions.size() > 0) //worse options size = or < extraCount
                {
                    for (AbstractCard c : worseOptions)
                    {
                        reduceCost(c);
                    }
                }
            }
        }

        this.isDone = true;
    }

    private void reduceCost(AbstractCard c)
    {
        for (AbstractCard copy : GetAllInBattleInstances.get(c.uuid))
        {
            copy.modifyCostForCombat(-reduction);
            if (AbstractDungeon.player.hand.contains(copy))
                copy.superFlash();
        }
    }
}
