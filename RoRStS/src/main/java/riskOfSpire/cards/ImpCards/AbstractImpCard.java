package riskOfSpire.cards.ImpCards;

import com.megacrit.cardcrawl.actions.utility.ExhaustAllEtherealAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import riskOfSpire.cards.AbstractDefaultCard;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;

public abstract class AbstractImpCard extends AbstractDefaultCard {
    public AbstractImpCard(final String id, final String img, final int cost, final CardType type, final CardColor color, final CardRarity rarity, final CardTarget target) {
        super(id, languagePack.getCardStrings(id).NAME, img, cost, languagePack.getCardStrings(id).DESCRIPTION, type, color, rarity, target);
    }

    @Override
    public void triggerOnEndOfPlayerTurn() {
        AbstractDungeon.actionManager.addToTop(new ExhaustAllEtherealAction());
    }
}