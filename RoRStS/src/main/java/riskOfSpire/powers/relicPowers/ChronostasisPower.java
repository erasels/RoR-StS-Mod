package riskOfSpire.powers.relicPowers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.powers.abstracts.RoRStSPower;
import riskOfSpire.relics.Uncommon.Chronobauble;

public class ChronostasisPower extends RoRStSPower implements CloneablePowerInterface {
    public static final String POWER_ID = RiskOfSpire.makeID("Chronostasis");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static final float DAMAGE_MODIFIER = 0.1f;

    public ChronostasisPower(AbstractCreature owner, int amt) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        isTurnBased = true;
        amount = amt;
        type = AbstractPower.PowerType.DEBUFF;
        setImage("84_Chronostasis.png", "32_Chronostasis.png");
        updateDescription();
    }

    @Override
    public void updateDescription() {
        Chronobauble cb = (Chronobauble)AbstractDungeon.player.getRelic(Chronobauble.ID);
        float dm;
        if(cb != null) {
            dm = cb.relicStack/10f;
        } else {
            dm = DAMAGE_MODIFIER;
        }
        description = DESCRIPTIONS[0] + MathUtils.round(dm*100f) + DESCRIPTIONS[1] +amount*10 + DESCRIPTIONS[2];
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        if (type != DamageInfo.DamageType.THORNS && type != DamageInfo.DamageType.HP_LOSS) {
            return damage * (1.0F + amount * DAMAGE_MODIFIER);
        }
        return damage;
    }

    @Override
    public void atEndOfRound() {
        decreasePower();
    }

    private void decreasePower() {
        if (amount < 2) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, this));
        } else {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(owner, owner, this, MathUtils.floor(amount/2f)));
            updateDescription();
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new ChronostasisPower(owner, amount);
    }
}