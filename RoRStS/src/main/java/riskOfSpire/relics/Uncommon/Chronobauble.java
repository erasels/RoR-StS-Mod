package riskOfSpire.relics.Uncommon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.powers.relicPowers.ChronostasisPower;
import riskOfSpire.relics.Abstracts.StackableRelic;

public class Chronobauble extends StackableRelic {
    public static final String ID = RiskOfSpire.makeID("Chronobauble");

    public Chronobauble() {
        super(ID, "Chronobauble.png", RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + relicStack*10 + DESCRIPTIONS[1];
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (info.type == DamageInfo.DamageType.NORMAL && !target.isPlayer && damageAmount > 0) {
            flash();
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, AbstractDungeon.player, new ChronostasisPower(target, relicStack), relicStack));
        }
    }

    public AbstractRelic makeCopy() {
        return new Chronobauble();
    }
}
