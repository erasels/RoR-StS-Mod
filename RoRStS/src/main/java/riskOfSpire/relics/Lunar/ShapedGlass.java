package riskOfSpire.relics.Lunar;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.relics.Abstracts.StackableRelic;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ShapedGlass extends StackableRelic {
    public static final String ID = RiskOfSpire.makeID("ShapedGlass");
    private ArrayList<Integer> mhpDecreases = new ArrayList<>();

    private static DecimalFormat val = new DecimalFormat("#.#");
    private static final float HEALTH_PERC = 0.5f;
    private static final float DAMAGE_PERC = 100f;

    public ShapedGlass() {
        super(ID, "ShapedGlass.png", RelicTier.SPECIAL, LandingSound.CLINK);
        isLunar = true;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + val.format((1-Math.pow(HEALTH_PERC, relicStack))*100) + DESCRIPTIONS[1] + MathUtils.round(DAMAGE_PERC*relicStack) + DESCRIPTIONS[2];
    }

    @Override
    public float atDamageModify(float damage, AbstractCard c) {
        if(c.damageTypeForTurn == DamageInfo.DamageType.NORMAL) {
            damage *= (1+relicStack);
        }
        return damage;
    }

    @Override
    public void onEquip() {
        decrease();
    }

    @Override
    public void onUnequip() {
        fullIncrease();
    }

    @Override
    public void onStack() {
        super.onStack();
        decrease();
    }

    @Override
    public void onUnstack() {
        if (relicStack > 1) {
            increase();
        }
        super.onUnstack();
    }

    private void decrease() {
        mhpDecreases.add(MathUtils.round((float)AbstractDungeon.player.maxHealth/2f));
        AbstractDungeon.player.decreaseMaxHealth(mhpDecreases.get(mhpDecreases.size()-1));
    }

    //Savescumming with mango or other HP increase POG
    private void increase() {
        if(!mhpDecreases.isEmpty()) {
            AbstractDungeon.player.increaseMaxHp(mhpDecreases.get(mhpDecreases.size() - 1), false);
        } else {
            AbstractDungeon.player.increaseMaxHp(AbstractDungeon.player.maxHealth, false);
        }
    }

    private void fullIncrease() {
        if(!mhpDecreases.isEmpty()) {
                mhpDecreases.forEach(i -> AbstractDungeon.player.increaseMaxHp(i, false));
        } else {
            for(int i = 0; i<relicStack;i++) {
                AbstractDungeon.player.increaseMaxHp(AbstractDungeon.player.maxHealth, false);
            }
        }
    }

    public AbstractRelic makeCopy() {
        return new ShapedGlass();
    }
}