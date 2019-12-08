package riskOfSpire.relics.Usable;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.actions.general.TargetAction;
import riskOfSpire.actions.unique.relicEffects.MissileStrikeAction;
import riskOfSpire.relics.Abstracts.UsableRelic;
import riskOfSpire.relics.Interfaces.TargetingRelic;

public class TheCrowdfunder extends UsableRelic implements TargetingRelic {
    public static final String ID = RiskOfSpire.makeID("TheCrowdfunder");

    private static final int DAMAGE_PER = 4;
    private static final int MISSILE_COUNT = 5;
    private static final int GOLD_COST = 35;
    private boolean targeting;

    private static final int COOLDOWN = 0;

    public TheCrowdfunder() {
        super(ID, "TheCrowdfunder.png", RelicTier.COMMON, LandingSound.HEAVY);
        this.counter = -1;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + GOLD_COST + DESCRIPTIONS[1] + DAMAGE_PER + DESCRIPTIONS[2] + MISSILE_COUNT + DESCRIPTIONS[3] + getCooldownString();
    }

    @Override
    public boolean isUsable() {
        return true;
    }

    @Override
    public int getBaseCooldown() {
        return COOLDOWN;
    }

    @Override
    public void onRightClickInCombat() {
        if (AbstractDungeon.player.gold >= GOLD_COST && !targeting) {
            targeting = true;
            new TargetAction(this, false);
        }
    }

    @Override
    public void targetAction(AbstractCreature c, boolean isPlayer) {
        targeting = false;
        if (AbstractDungeon.player.gold >= GOLD_COST && !isPlayer) {
            this.flash();
            AbstractDungeon.player.loseGold(GOLD_COST);
            if (AbstractDungeon.player.gold < GOLD_COST) {
                stopPulse();
            } else {
                beginLongPulse();
            }

            DamageInfo info = new DamageInfo(AbstractDungeon.player, DAMAGE_PER, DamageInfo.DamageType.THORNS);
            for (int i = 0; i < MISSILE_COUNT; i++) {
                AbstractDungeon.actionManager.addToBottom(new MissileStrikeAction((AbstractMonster) c, info, AbstractGameAction.AttackEffect.NONE, Color.GOLD.cpy(), "GOLD_GAIN"));
            }
        }
    }

    @Override
    public void onTargetingCancelled() {
        targeting = false;
    }

    @Override
    public void update() {
        super.update();
        if (CardCrawlGame.isInARun()) //no compendium crash.
        {
            if (!pulse && AbstractDungeon.player.gold >= GOLD_COST) {
                beginLongPulse();
            } else if (pulse && AbstractDungeon.player.gold < GOLD_COST) {
                stopPulse();
            }
        }
    }



    @Override
    public AbstractRelic makeCopy() {
        return new TheCrowdfunder();
    }
}
