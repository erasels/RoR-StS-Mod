package riskOfSpire.relics.Uncommon;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.actions.general.SpawnTolerantDamageAllEnemiesAction;
import riskOfSpire.relics.Abstracts.StackableRelic;

public class WillOWisp extends StackableRelic
{
    public static final String ID = RiskOfSpire.makeID("WillOWisp");
    private static final float HEALTH_PERCENT = 0.2f;

    public WillOWisp()
    {
        super(ID, "WillOWisp.png", RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + MathUtils.round(getVal()*100) + DESCRIPTIONS[1];
    }

    @Override
    public void onMonsterDeath(AbstractMonster m) {
        if (m.currentHealth == 0) { //idk gremlin horn does it so I will too
            if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(m, this));
                AbstractDungeon.actionManager.addToTop(new SpawnTolerantDamageAllEnemiesAction(AbstractDungeon.player, MathUtils.floor((float)m.maxHealth*getVal()), true, false, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE, false));
                this.flash();
            }
        }
    }

    public float getVal() {
        return HEALTH_PERCENT*relicStack;
    }

    public AbstractRelic makeCopy()
    {
        return new WillOWisp();
    }
}
