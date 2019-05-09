package riskOfSpire.actions.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import riskOfSpire.powers.ChirPower;

public class ChirAction extends AbstractGameAction {
    private AbstractPlayer p;
    private int amount;
    private String text;

    public ChirAction(int amount, String text) {
        p = AbstractDungeon.player;
        actionType = ActionType.CARD_MANIPULATION;
        duration = Settings.ACTION_DUR_MED;
        this.amount = amount;
        this.text = text;
    }

    public void update() {
        CardGroup tmp;
        if (this.duration == Settings.ACTION_DUR_MED) {
            tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard c : this.p.drawPile.group) {
                tmp.addToRandomSpot(c);
            }
            tmp.sortByRarity(false);

            if (tmp.isEmpty()) {
                this.isDone = true;
                return;
            }
            if(tmp.size() <= amount) {
                tmp.group.forEach(c -> p.drawPile.removeCard(c));
                AbstractDungeon.actionManager.addToBottom(new DumbApplyPowerAction(p, p, new ChirPower(p, tmp.group)));
                this.isDone = true;
                return;
            }

            AbstractDungeon.gridSelectScreen.open(tmp, amount, text, false);
            tickDuration();
            return;
        }
        if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                c.unhover();
                p.drawPile.removeCard(c);
            }
            AbstractDungeon.actionManager.addToBottom(new DumbApplyPowerAction(p, p, new ChirPower(p, AbstractDungeon.gridSelectScreen.selectedCards)));
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
        tickDuration();
    }
}
