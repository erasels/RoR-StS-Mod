package riskOfSpire.relics.Common;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.relics.Abstracts.StackableRelic;

public class BackupMagazine extends StackableRelic {
    public static final String ID = RiskOfSpire.makeID("BackupMagazine");

    private boolean enabled;

    public BackupMagazine() {
        super(ID, "RustedKey.png", RelicTier.COMMON, LandingSound.FLAT);
    }

    @Override
    public void atBattleStartPreDraw() {
        this.enabled = true;
        this.beginLongPulse();

    }

    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        if (enabled && targetCard.type == AbstractCard.CardType.ATTACK)
        {
            enabled = false;
            stopPulse();
            flash();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(targetCard.makeStatEquivalentCopy(), relicStack));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + relicStack + (relicStack == 1 ? DESCRIPTIONS[1] : DESCRIPTIONS[2]);
    }

    public AbstractRelic makeCopy() {
        return new BackupMagazine();
    }
}