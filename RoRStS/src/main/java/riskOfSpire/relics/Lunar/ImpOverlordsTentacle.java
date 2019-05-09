package riskOfSpire.relics.Lunar;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.cards.ImpCards.*;
import riskOfSpire.relics.Abstracts.StackableRelic;

public class ImpOverlordsTentacle extends StackableRelic {
    public static final String ID = RiskOfSpire.makeID("ImpOverlordsTentacle");

    public ImpOverlordsTentacle() {
        super(ID, "ImpOverlordsTentacle.png", RelicTier.SPECIAL, LandingSound.FLAT);
        this.tips.clear();
        this.tips.add(new PowerTip(name, description));
        this.tips.add(new PowerTip(DESCRIPTIONS[4], DESCRIPTIONS[5]));
        initializeTips();
    }

    @Override
    public String getUpdatedDescription() {
        if(relicStack>1) {
            return DESCRIPTIONS[1] + relicStack + DESCRIPTIONS[2] + relicStack + DESCRIPTIONS[3];
        }
        return DESCRIPTIONS[0];
    }

    public void onEnergyRecharge() {
        AbstractCard c;
        for(int i = 0; i<relicStack;i++) {
            switch (AbstractDungeon.cardRandomRng.random(0, 4)) {
                case 0:
                    c = new ImpAetu();
                    break;
                case 1:
                    c = new ImpBava();
                    break;
                case 2:
                    c = new ImpYggo();
                    break;
                case 3:
                    c = new ImpUgorn();
                    break;
                default:
                    c = new ImpChir();
                    break;

            }
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(c, false));
        }
        flash();
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

    @Override
    public void onEquip() {
        safeReduceMHS();
    }

    @Override
    public void onUnequip() {
        AbstractDungeon.player.masterHandSize += relicStack;
    }

    @Override
    public void onStack() {
        super.onStack();
        safeReduceMHS();
    }

    @Override
    public void onUnstack() {
        if (relicStack > 1) {
            AbstractDungeon.player.masterHandSize++;
        }
        super.onUnstack();
    }

    private void safeReduceMHS() {
        if (AbstractDungeon.player.masterHandSize > 0) {
            AbstractDungeon.player.masterHandSize--;
        }
    }

    public AbstractRelic makeCopy() {
        return new ImpOverlordsTentacle();
    }
}