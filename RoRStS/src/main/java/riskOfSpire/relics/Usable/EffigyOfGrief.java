package riskOfSpire.relics.Usable;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.SlowPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.relics.Abstracts.UsableRelic;

public class EffigyOfGrief extends UsableRelic {
    public static final String ID = RiskOfSpire.makeID("EffigyOfGrief");
    private static final int DEBUFF = 1;
    private static final int COOLDOWN = 0;

    public EffigyOfGrief() {
        super(ID, "EffigyOfGrief.png", RelicTier.SPECIAL, LandingSound.FLAT);

        setCounter(-2);
    }

    @Override
    public boolean isUsable() {
        return false;
    }

    @Override
    public void atTurnStart() {
        if (this.counter == -1)
        {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
            {
                if (!m.isDeadOrEscaped())
                {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, AbstractDungeon.player, new VulnerablePower(m, DEBUFF, false), DEBUFF, true));
                }
            }
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new VulnerablePower(AbstractDungeon.player, DEBUFF, false), DEBUFF, true));
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
            {
                if (!m.isDeadOrEscaped())
                {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, AbstractDungeon.player, new SlowPower(m, DEBUFF), DEBUFF, true));
                }
            }
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SlowPower(AbstractDungeon.player, DEBUFF), DEBUFF, true));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public int getBaseCooldown() {
        return COOLDOWN;
    }

    @Override
    public void onRightClick() {
        if (this.counter != -1)
        {
            setCounter(-1);
        }
        else
        {
            setCounter(-2);
        }
    }

    @Override
    public void setCounter(int counter) {
        super.setCounter(counter);
        if (this.counter == -1)
        {
            this.beginLongPulse();
        }
        else
        {
            this.stopPulse();
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new EffigyOfGrief();
    }
}
