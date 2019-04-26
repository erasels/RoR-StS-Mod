package riskOfSpire.relics.Common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AfterImagePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.actions.general.GuaranteedDamageRandomEnemyAction;
import riskOfSpire.relics.Abstracts.StackableRelic;

public class EnergyDrink extends StackableRelic {
    public static final String ID = RiskOfSpire.makeID("EnergyDrink");

    private boolean triggered = false;

    public EnergyDrink() {
        super(ID, "EnergyDrink.png", RelicTier.COMMON, LandingSound.SOLID);
        this.counter = -1;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + relicStack + DESCRIPTIONS[1];
    }

    @Override
    public void atBattleStart() {
        flash();
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new AfterImagePower(AbstractDungeon.player, 1),1));
        setCounter(relicStack);
    }

    @Override
    public void onPlayerEndTurn() {
        if(!triggered) {
            setCounter(relicStack - GameActionManager.turn);
            if (counter <= 0) {
                setCounter(-1);
            }

            if (GameActionManager.turn >= relicStack) {
                flash();
                AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, AfterImagePower.POWER_ID, 1));

                AbstractDungeon.player.getPower(AfterImagePower.POWER_ID).updateDescription();

                if (AbstractDungeon.player.getPower(AfterImagePower.POWER_ID).amount <= 0) {
                    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, AbstractDungeon.player.getPower(AfterImagePower.POWER_ID)));
                }
                triggered = true;
            }
        }
    }

    @Override
    public void onVictory() {
        triggered = false;
        setCounter(-1);
    }


    public AbstractRelic makeCopy() {
        return new EnergyDrink();
    }
}