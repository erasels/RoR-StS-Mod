package riskOfSpire.relics.Usable;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.actions.unique.TargetedMissileAction;
import riskOfSpire.relics.Abstracts.UsableRelic;

public class DisposableMissileLauncher extends UsableRelic {
    public static final String ID = RiskOfSpire.makeID("DisposableMissileLauncher");

    private static final int DAMAGE_PER = 4;
    private static final int MISSILE_COUNT = 10;

    private static final int COOLDOWN = 12;

    public DisposableMissileLauncher() {
        super(ID, "DisposableMissileLauncher.png", RelicTier.UNCOMMON, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + DAMAGE_PER + DESCRIPTIONS[1] + MISSILE_COUNT + DESCRIPTIONS[2] + getCooldownString();
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
        if (this.counter == 0)
        {
            this.flash();

            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            DamageInfo info = new DamageInfo(AbstractDungeon.player, DAMAGE_PER, DamageInfo.DamageType.THORNS);

            AbstractDungeon.actionManager.addToBottom(new TargetedMissileAction(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, Color.ORANGE.cpy(), MISSILE_COUNT, info));

            /*for (int i = 0; i < MISSILE_COUNT; ++i)
            {
                AbstractDungeon.actionManager.addToBottom(new GuaranteedDamageRandomEnemyAction(info, AbstractGameAction.AttackEffect.FIRE, true, true));
            }*/

            this.activateCooldown();
            this.stopPulse();
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new DisposableMissileLauncher();
    }
}
