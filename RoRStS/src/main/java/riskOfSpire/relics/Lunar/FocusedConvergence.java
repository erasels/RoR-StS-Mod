package riskOfSpire.relics.Lunar;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.relics.Abstracts.StackableRelic;

import java.util.ArrayList;
import java.util.Iterator;

public class FocusedConvergence extends StackableRelic {
    public static final String ID = RiskOfSpire.makeID("FocusedConvergence");
    private static final float REDUCTION = 0.25f;
    private static final float INCREASE = 0.25f;

    private ArrayList<AbstractMonster> antiScumList = new ArrayList<>();


    public FocusedConvergence() {
        super(ID, "BrittleCrown.png", RelicTier.SPECIAL, LandingSound.MAGICAL);
        isLunar = true;
    }

    public void atBattleStart() {
        boolean effect = true;

        for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters)
        {
            if (m.type == AbstractMonster.EnemyType.BOSS)
            {
                if (effect)
                {
                    effect = false;

                    this.flash();
                    this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                }

                m.currentHealth -= m.maxHealth * getReduction();
                m.healthBarUpdatedEvent();

                for (DamageInfo damage : m.damage)
                {
                    int oldBase = damage.base;
                    damage.base *= getIncrease();

                    RiskOfSpire.logger.info("Increased damage of boss from " + oldBase + " to " + damage.base + ".");
                }

                m.createIntent();
            }
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + MathUtils.round(getReduction()*100) + DESCRIPTIONS[1] + MathUtils.round((getIncrease() - 1) * 100) + DESCRIPTIONS[2];
    }

    @Override
    public void onVictory() {
        antiScumList.clear();
    }

    public float getReduction() {
        return (float) (1 - (Math.pow(0.8f, this.relicStack)));
    }
    public float getIncrease() {
        return (float) (Math.pow(1.25, this.relicStack));
    }

    public AbstractRelic makeCopy() {
        return new FocusedConvergence();
    }
}