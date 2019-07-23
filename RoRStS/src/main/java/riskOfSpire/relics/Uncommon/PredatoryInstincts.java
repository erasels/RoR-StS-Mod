package riskOfSpire.relics.Uncommon;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.relics.Abstracts.StackableRelic;
import riskOfSpire.relics.Common.LensMakersGlasses;
import riskOfSpire.relics.Common.SoldiersSyringe;
import riskOfSpire.relics.Interfaces.ModifyCritDamageRelic;
import riskOfSpire.relics.Interfaces.OnDoubleAttackRelic;

public class PredatoryInstincts extends StackableRelic implements ModifyCritDamageRelic, OnDoubleAttackRelic {
    public static final String ID = RiskOfSpire.makeID("PredatoryInstincts");

    public PredatoryInstincts() {
        super(ID, "PredatoryInstincts.png", RelicTier.UNCOMMON, LandingSound.FLAT);
        isCritical = true;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + relicStack + DESCRIPTIONS[1];
    }

    @Override
    public void afterCrit() {
        flash();
        SoldiersSyringe sS = (SoldiersSyringe) AbstractDungeon.player.getRelic(SoldiersSyringe.ID);
        if(sS != null && sS.counter > 1) {
            sS.decreaseCharge(relicStack);
        }
    }

    @Override
    public AbstractCard beforeDoubleAttack(AbstractCard card, UseCardAction action) {
        flash();
        LensMakersGlasses lMG = (LensMakersGlasses) AbstractDungeon.player.getRelic(LensMakersGlasses.ID);
        if(lMG != null && lMG.counter > 1) {
            lMG.decreaseCharge(relicStack);
        }
        return card;
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player.hasRelic(LensMakersGlasses.ID) && AbstractDungeon.player.hasRelic(SoldiersSyringe.ID);
    }

    public AbstractRelic makeCopy() {
        return new PredatoryInstincts();
    }
}
