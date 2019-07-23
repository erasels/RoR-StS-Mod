package riskOfSpire.relics.Common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.actions.general.GuaranteedDamageRandomEnemyAction;
import riskOfSpire.relics.Abstracts.StackableRelic;

public class BundleOfFireworks extends StackableRelic {
    public static final String ID = RiskOfSpire.makeID("BundleOfFireworks");
    private static final int DAMAGE_PER = 2;
    private static final int DAMAGE_COUNT = 3;

    public BundleOfFireworks() {
        super(ID, "BundleOfFireworks.png", RelicTier.COMMON, LandingSound.SOLID);
        this.counter = 0;
    }

    @Override
    public void onChestOpen(boolean bossChest) {
        this.flash();
        this.counter += DAMAGE_COUNT * relicStack;
    }

    @Override
    public void onRelicGet(AbstractRelic r){
        //if((!(r instanceof StackableRelic) && !AbstractDungeon.player.hasRelic(r.relicId)) || (r instanceof StackableRelic && ((StackableRelic) r).relicStack == 1)) {
        if (!CardCrawlGame.loadingSave) {
            this.flash();
            this.counter += DAMAGE_COUNT * relicStack;
        }
    }

    @Override
    public void atBattleStart() {
        if (this.counter > 0)
        {
            this.flash();

            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            DamageInfo info = new DamageInfo(AbstractDungeon.player, DAMAGE_PER, DamageInfo.DamageType.THORNS);

            for (int i = 0; i < this.counter; ++i) //Hooray for not having to do recursive action.
            {
                //Maybe get really dumb and add an actual firework vfx for each firework, so it can look as dumb as it does in ror?
                //Already done ;) - Gk
                AbstractDungeon.actionManager.addToBottom(new GuaranteedDamageRandomEnemyAction(info, AbstractGameAction.AttackEffect.FIRE, true, true));
            }

            this.counter = 0;
        }
    }

    @Override
    public String getUpdatedDescription() {
        //Whenever you open a chest, deal 3 damage to a random enemy x times at the start of the next combat.
        //I think displaying final amount is good for this one.
        return DESCRIPTIONS[0] + DAMAGE_PER + DESCRIPTIONS[1] + DAMAGE_COUNT * relicStack + DESCRIPTIONS[2];
    }

    public AbstractRelic makeCopy() {
        return new BundleOfFireworks();
    }
}