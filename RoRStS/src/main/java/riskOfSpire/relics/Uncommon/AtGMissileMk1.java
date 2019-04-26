package riskOfSpire.relics.Uncommon;

import com.evacipated.cardcrawl.mod.stslib.relics.OnReceivePowerRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BlurPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.actions.unique.MissileStrikeAction;
import riskOfSpire.relics.Abstracts.StackableRelic;
import riskOfSpire.vfx.combat.MissileStrikeEffect;

public class AtGMissileMk1 extends StackableRelic {
    public static final String ID = RiskOfSpire.makeID("AtGMissileMk1");

    public static final int BASE_DMG = 2;

    public AtGMissileMk1() {
        super(ID, "AtGMissileMk1.png", RelicTier.UNCOMMON, LandingSound.HEAVY);
    }
    
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + (BASE_DMG*relicStack) + DESCRIPTIONS[1];
    }

    @Override
    public void onUseCard(AbstractCard c, UseCardAction uac) {
        if(!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            AbstractDungeon.actionManager.addToBottom(new MissileStrikeAction(new DamageInfo(AbstractDungeon.player, BASE_DMG*relicStack, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new AtGMissileMk1();
    }
}
