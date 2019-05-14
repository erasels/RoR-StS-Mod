package riskOfSpire.relics.Uncommon;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.actions.unique.TargetedMissileAction;
import riskOfSpire.relics.Abstracts.StackableRelic;

public class AtGMissileMk1 extends StackableRelic {
    public static final String ID = RiskOfSpire.makeID("AtGMissileMk1");

    public static final int BASE_DMG = 1;

    public AtGMissileMk1() {
        super(ID, "AtGMissileMk1.png", RelicTier.UNCOMMON, LandingSound.HEAVY);
    }
    
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + getVal() + DESCRIPTIONS[1];
    }

    @Override
    public void onUseCard(AbstractCard c, UseCardAction uac) {
        if(!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            AbstractDungeon.actionManager.addToBottom(new TargetedMissileAction(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, Color.ORANGE.cpy(), 1, new DamageInfo(AbstractDungeon.player, getVal(), DamageInfo.DamageType.THORNS)));
        }
    }

    private int getVal() {
        return BASE_DMG+relicStack;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new AtGMissileMk1();
    }
}
