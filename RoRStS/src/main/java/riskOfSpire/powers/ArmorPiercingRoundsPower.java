package riskOfSpire.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.actions.unique.DumbApplyPowerAction;
import riskOfSpire.powers.abstracts.RoRStSPower;
import riskOfSpire.relics.Common.ArmorPiercingRounds;

public class ArmorPiercingRoundsPower extends RoRStSPower implements CloneablePowerInterface, InvisiblePower {
    public static final String POWER_ID = RiskOfSpire.makeID("ArmorPiercingRounds");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public ArmorPiercingRoundsPower(AbstractCreature owner) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        isTurnBased = false;
        amount = -1;
        type = AbstractPower.PowerType.BUFF;
        loadRegion("lockon");
        updateDescription();
    }

    @Override
    public float atDamageFinalReceive(float damageAmount, DamageInfo.DamageType info) {
        ArmorPiercingRounds arp = (ArmorPiercingRounds) AbstractDungeon.player.getRelic(ArmorPiercingRounds.ID);
        if(arp != null && info == DamageInfo.DamageType.NORMAL) {
            return damageAmount + arp.relicStack;
        }

        return damageAmount;
    }

    @Override
    public void onRemove() {
        AbstractDungeon.actionManager.addToTop(new DumbApplyPowerAction(owner, AbstractDungeon.player, makeCopy(), -1, true));
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public AbstractPower makeCopy() {
        return new ArmorPiercingRoundsPower(owner);
    }
}