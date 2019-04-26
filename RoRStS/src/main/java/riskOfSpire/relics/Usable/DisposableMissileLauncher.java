package riskOfSpire.relics.Usable;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.actions.general.GuaranteedDamageRandomEnemyAction;
import riskOfSpire.relics.Abstracts.UsableRelic;

public class DisposableMissileLauncher extends UsableRelic {
    public static final String ID = RiskOfSpire.makeID("DisposableMissileLauncher");

    private static final int DAMAGE_PER = 5;
    private static final int MISSILE_COUNT = 10;

    private static final int COOLDOWN = 15;

    public DisposableMissileLauncher() {
        super(ID, "DisposableMissileLauncher.png", RelicTier.UNCOMMON, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + getFinalCooldown() + DESCRIPTIONS[1];
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

            for (int i = 0; i < MISSILE_COUNT; ++i)
            {
                AbstractDungeon.actionManager.addToBottom(new GuaranteedDamageRandomEnemyAction(info, AbstractGameAction.AttackEffect.FIRE, true, true));
            }

            this.activateCooldown();
            this.stopPulse();
        }
    }

    @Override
    public AbstractRelic makeCopy() {
        return new DisposableMissileLauncher();
    }
}
