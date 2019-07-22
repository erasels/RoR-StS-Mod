package riskOfSpire.relics.Rare;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.relics.Abstracts.StackableRelic;

public class Brainstalks extends StackableRelic {
    public static final String ID = RiskOfSpire.makeID("Brainstalks");
    private static final int AMT = 4;

    public Brainstalks() {
        super(ID, "Brainstalks.png", RelicTier.RARE, LandingSound.MAGICAL);
        setCounter(0);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + getVal() + DESCRIPTIONS[1];
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (counter > 0) {
            flash();
            setCounter(counter - 1);
            int tmp;
            if (card.freeToPlayOnce) {
                tmp = 0;
            } else if (card.cost == -1) {
                tmp = card.energyOnUse;
            } else {
                tmp = card.costForTurn;
            }

            if (tmp > 0) {
                AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(1));
            }
        }
    }

    @Override
    public void onMonsterDeath(AbstractMonster m) {
        if (m.currentHealth == 0 && !m.hasPower(MinionPower.POWER_ID)) {
            flash();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            setCounter(counter + getVal());
        }
    }

    public AbstractRelic makeCopy() {
        return new Brainstalks();
    }

    public int getVal() {
        return AMT * relicStack;
    }
}
