package riskOfSpire.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.powers.abstracts.RoRStSPower;
import riskOfSpire.relics.Uncommon.HappiestMask;

public class SurroundedPower extends RoRStSPower implements CloneablePowerInterface {
    public static final String POWER_ID = RiskOfSpire.makeID("Surrounded");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private HappiestMask hM;

    public SurroundedPower(AbstractCreature owner, HappiestMask hM) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        type = AbstractPower.PowerType.BUFF;
        isTurnBased = false;
        this.hM = hM;
        updateDescription();
        loadRegion("surrounded");
    }

    @Override
    public float atDamageReceive(float dmg, DamageInfo.DamageType type) {
        if (hM != null && type == DamageInfo.DamageType.NORMAL) {
            return dmg * hM.getInc();
        }
        return dmg;
    }

    public float atDamageGive(float dmg, DamageInfo.DamageType type) {
        if (hM != null && type == DamageInfo.DamageType.NORMAL) {
            return dmg * hM.getDec();
        }
        return dmg;
    }

    public void updateDescription() {
        int tmp1 = MathUtils.round((hM.getInc()-1f)*100);
        int tmp2 = MathUtils.round((1f-hM.getDec())*100);
        this.description = DESCRIPTIONS[0] + tmp1 + DESCRIPTIONS[1] + tmp2 + DESCRIPTIONS[2];
    }

    @Override
    public AbstractPower makeCopy() {
        return new SurroundedPower(owner, hM);
    }
}