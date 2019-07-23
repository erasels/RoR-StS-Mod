package riskOfSpire.relics.Boss;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.relics.Abstracts.StackableRelic;

public class TitanicKnurl extends StackableRelic {
    public static final String ID = RiskOfSpire.makeID("TitanicKnurl");
    private static final int MHP_INC = 10;
    private static final int HP_TICK = 1;

    public TitanicKnurl() {
        super(ID, "TitanicKnurl.png", RelicTier.BOSS, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + MHP_INC + DESCRIPTIONS[1] + getVal() + DESCRIPTIONS[2];
    }

    @Override
    public void atTurnStart() {
        flash();
        AbstractDungeon.actionManager.addToBottom(new HealAction(AbstractDungeon.player, AbstractDungeon.player, getVal()));
    }

    @Override
    public void onEquip() {
        super.onEquip();
        AbstractDungeon.player.increaseMaxHp(MHP_INC, true);
    }

    @Override
    public void onUnequip() {
        for (int i = 0; i < relicStack; i++) {
            AbstractDungeon.player.decreaseMaxHealth(MHP_INC);
        }
    }

    @Override
    public void onStack() {
        super.onStack();
        AbstractDungeon.player.increaseMaxHp(MHP_INC, true);
    }

    @Override
    public void onUnstack() {
        if (relicStack > 1) {
            AbstractDungeon.player.decreaseMaxHealth(MHP_INC);
        }
        super.onUnstack();
    }

    public int getVal() {
        return HP_TICK * relicStack;
    }

    public AbstractRelic makeCopy() {
        return new TitanicKnurl();
    }
}
