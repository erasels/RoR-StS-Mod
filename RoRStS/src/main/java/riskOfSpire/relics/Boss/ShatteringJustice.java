package riskOfSpire.relics.Boss;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.relics.Abstracts.StackableRelic;

import java.util.ArrayList;

public class ShatteringJustice extends StackableRelic {
    public static final String ID = RiskOfSpire.makeID("ShatteringJustice");

    private static final int DEBUFF = 1;

    private ArrayList<AbstractCreature> triggeredList = new ArrayList<>();

    public ShatteringJustice() {
        super(ID, "ShatteringJustice.png", RelicTier.BOSS, LandingSound.HEAVY);
    }

    @Override
    public void atTurnStart() {
        triggeredList.clear();
        beginLongPulse();
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (info.type == DamageInfo.DamageType.NORMAL && !triggeredList.contains(target) && !target.isPlayer && damageAmount > 0)
        {
            this.flash();
            AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(target, this));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, AbstractDungeon.player, new VulnerablePower(target, relicStack * DEBUFF, false), relicStack * DEBUFF));

            triggeredList.add(target);

            //pulse control
            ArrayList<AbstractCreature> untargeted = new ArrayList<>(AbstractDungeon.getMonsters().monsters);

            untargeted.removeAll(triggeredList);
            if (untargeted.isEmpty())
            {
                stopPulse();
            }
            else
            {
                for (AbstractCreature m : untargeted) //if all creatures that haven't been targeted this turn are dead or escaped, turn off pulse
                {
                    if (!m.isDeadOrEscaped())
                        return;
                }
                stopPulse();
            }
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new ShatteringJustice();
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + relicStack * DEBUFF + DESCRIPTIONS[1];
    }
}
