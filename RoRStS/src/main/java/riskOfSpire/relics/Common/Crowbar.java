package riskOfSpire.relics.Common;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.relics.Abstracts.StackableRelic;
import riskOfSpire.relics.Interfaces.ModifyDamageRelic;

public class Crowbar extends StackableRelic implements ModifyDamageRelic {
    public static final String ID = RiskOfSpire.makeID("Crowbar");
    private static final float DINC = 0.25f;

    public Crowbar() {
        super(ID, "Crowbar.png", RelicTier.COMMON, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + MathUtils.round(DINC*100f) + DESCRIPTIONS[1];
    }

    @Override
    public int calculateCardDamageRelic(AbstractCard card, AbstractMonster target, int damage) {
        if(target.currentHealth == target.maxHealth) {
            flash();
            return MathUtils.round((float)damage*getVal());
        }
        return damage;
    }

    public float getVal() {
        return 1f+ (DINC*relicStack);
    }

    public AbstractRelic makeCopy() {
        return new Crowbar();
    }
}
