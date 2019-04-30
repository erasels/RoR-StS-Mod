package riskOfSpire.actions.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;

public class ZapRandomEnemyAction extends AbstractGameAction
{
    private DamageInfo info;

    public ZapRandomEnemyAction(DamageInfo info) {
        this.info = info;
        this.actionType = AbstractGameAction.ActionType.DAMAGE;
    }

    public void update() {
        AbstractCreature m = AbstractDungeon.getRandomMonster();
        if (m != null) {
            float speedTime = Settings.FAST_MODE ? 0.0F : 0.1F;

            AbstractDungeon.actionManager.addToTop(new DamageAction(m, this.info, AbstractGameAction.AttackEffect.NONE, true));
            AbstractDungeon.actionManager.addToTop(new VFXAction(new LightningEffect(m.drawX, m.drawY), speedTime));
            AbstractDungeon.actionManager.addToTop(new SFXAction("ORB_LIGHTNING_EVOKE"));
        }

        this.isDone = true;
    }
}