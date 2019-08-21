package riskOfSpire.actions.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.stream.Stream;

public class HitByBlazingEliteAction extends AbstractGameAction {

    private AbstractCard c;
    private int damage;

    public HitByBlazingEliteAction(AbstractCreature owner, AbstractCard c, int damage) {
        this.c = c;
        this.damage = damage;
        this.source = owner;
        this.actionType = ActionType.EXHAUST;
    }

    public void update() {
        AbstractPlayer p = AbstractDungeon.player;
        Stream.of(p.hand.group, p.discardPile.group, p.drawPile.group)
                .forEach(as ->
                        as.stream()
                                .filter(card -> card.cardID.equals(c.cardID))
                                .forEach(card -> {
                                    AbstractDungeon.actionManager.addToTop(new DamageAction(p, new DamageInfo(source, damage, DamageInfo.DamageType.THORNS), AttackEffect.FIRE, true));
                                    AbstractDungeon.actionManager.addToTop(new ExhaustSpecificCardAction(card, findCardGroup(as), true));
                                }
                                )
                );

        isDone = true;
    }

    private CardGroup findCardGroup(ArrayList<AbstractCard> as) {
        if(as.equals(AbstractDungeon.player.hand.group)) {
            return AbstractDungeon.player.hand;
        } else if (as.equals(AbstractDungeon.player.drawPile.group)) {
            return AbstractDungeon.player.drawPile;
        } else if (as.equals(AbstractDungeon.player.discardPile.group)) {
            return AbstractDungeon.player.exhaustPile;
        } else {
            return new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        }
    }
}