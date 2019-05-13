package riskOfSpire.relics.Common;

import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import org.apache.commons.lang3.math.NumberUtils;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.relics.Abstracts.StackableRelic;

public class CautiousSlug extends StackableRelic {
    public static final String ID = RiskOfSpire.makeID("CautiousSlug");

    public CautiousSlug() {
        super(ID, "CautiousSlug.png", RelicTier.COMMON, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + getVal() + DESCRIPTIONS[1] + getVal() + DESCRIPTIONS[2];
    }

    @Override
    public void onRest() {
        flash();
        if(counter<=0) {
            counter = 1;
        }else {
            counter++;
        }
        beginLongPulse();
    }

    @Override
    public void atBattleStart() {
        if(counter>0) {
            stopPulse();
            flash();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new RegenPower(AbstractDungeon.player, counter*getVal()), counter*getVal()));
            counter = -1;
        }
    }

    @Override
    public void onTrigger() {
        int tmp = NumberUtils.min(TempHPField.tempHp.get(AbstractDungeon.player), getVal());
        System.out.println("Healed for: " + tmp + " max was: " + TempHPField.tempHp.get(AbstractDungeon.player));
        if(tmp > 0) {
            flash();
            AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            AbstractDungeon.player.heal(tmp);
        }
    }

    public int getVal() {
        return relicStack;
    }

    public AbstractRelic makeCopy() {
        return new CautiousSlug();
    }
}
