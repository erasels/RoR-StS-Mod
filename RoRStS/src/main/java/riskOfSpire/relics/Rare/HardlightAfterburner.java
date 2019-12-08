package riskOfSpire.relics.Rare;

import basemod.abstracts.CustomBottleRelic;
import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.actions.general.MakeTempEchoInHandAction;
import riskOfSpire.relics.Abstracts.StackableRelic;

import java.util.function.Predicate;

public class HardlightAfterburner extends StackableRelic implements CustomBottleRelic, CustomSavable<Integer> {
    public static final String ID = RiskOfSpire.makeID("HardlightAfterburner");

    private boolean cardSelected = true;
    public static AbstractCard card = null;

    public HardlightAfterburner() {
        super(ID, "HardlightAfterburner.png", RelicTier.RARE, LandingSound.HEAVY);
        if (AbstractDungeon.player != null && !AbstractDungeon.player.hasRelic(ID))
        {
            card = null;
        }
    }

    @Override
    public Predicate<AbstractCard> isOnCard() {
        return HardlightAfterburner::testCard;
    }

    private static boolean testCard(AbstractCard c)
    {
        return card != null && c.uuid.equals(card.uuid);
    }

    @Override
    public void onCardDraw(AbstractCard drawnCard) {
        if (card != null)
        {
            if (drawnCard.uuid.equals(card.uuid))
            {
                AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                AbstractDungeon.actionManager.addToTop(new MakeTempEchoInHandAction(drawnCard, relicStack, 1));
            }
        }
    }

    /*@Override
    public void onEnterRoom(AbstractRoom room) {
        updateCounter();
    }

    @Override
    public void onVictory() {
        updateCounter();
    }

    @Override
    public void onObtainCard(AbstractCard c) {
        updateCounter();
    }*/
    
    @Override
    public Integer onSave() {
        this.updateCounter(); //onSave occurs before the relic counter values are saved, so just update it here.
        return super.onSave();
    }

    private void updateCounter()
    {
        if (card != null)
        {
            this.counter = AbstractDungeon.player.masterDeck.group.indexOf(card);
            if (this.counter >= 0) //from 0 to infinity
            {
                this.counter *= -1; //from 0 to -infinity
                this.counter -= 2; //from -2 to -infinity
            }
        }
    }

    @Override
    public void setCounter(int counter) { //setCounter is used when loading.
        super.setCounter(counter);
        if (counter != -1)
        {
            int index = counter + 2;
            index *= -1;
            if (index >= 0 && index < AbstractDungeon.player.masterDeck.group.size()) {
                card = AbstractDungeon.player.masterDeck.group.get(index);
                this.updateDescriptionOnStack(false);
            }
        }
    }

    @Override
    public void onEquip() {
        cardSelected = false;
        card = null;
        CardGroup group = CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck);
        group.group.removeIf((c)->c.type!= AbstractCard.CardType.SKILL);
        if (group.size() > 0) //what the heck, shouldn't be able to obtain this without any skills.
        {
            if (AbstractDungeon.isScreenUp) {
                AbstractDungeon.dynamicBanner.hide();
                AbstractDungeon.overlayMenu.cancelButton.hide();
                AbstractDungeon.previousScreen = AbstractDungeon.screen;
            }
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;

            AbstractDungeon.gridSelectScreen.open(group, 1, DESCRIPTIONS[2] + name + ".", false, false, false, false);
        }
    }

    @Override
    public void onUnequip() {
        card = null;
    }

    @Override
    public void update() {
        super.update();
        if (!this.cardSelected && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            this.cardSelected = true;
            card = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            
            this.updateCounter();
            this.updateDescriptionOnStack(true);
        }
    }

    @Override
    public boolean canSpawn() {
        if (AbstractDungeon.player != null)
        {
            for (AbstractCard c : CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck).group)
            {
                if (c.type == AbstractCard.CardType.SKILL && c.rarity != AbstractCard.CardRarity.BASIC)
                    return true;
            }
        }
        return false;
    }

    @Override
    public String getUpdatedDescription() {
        if (AbstractDungeon.player != null && cardSelected && card != null)
        {
            return DESCRIPTIONS[3] + FontHelper.colorString(card.name, "y") + DESCRIPTIONS[4] + relicStack + (relicStack == 1 ? DESCRIPTIONS[5] : DESCRIPTIONS[6]);
        }
        else
        {
            return DESCRIPTIONS[0] + "1" + DESCRIPTIONS[1];
        }
    }


    public AbstractRelic makeCopy() {
        return new HardlightAfterburner();
    }
}
