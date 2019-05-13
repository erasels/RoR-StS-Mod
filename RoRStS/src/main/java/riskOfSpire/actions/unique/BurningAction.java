package riskOfSpire.actions.unique;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class BurningAction extends AbstractGameAction {
    private static final float DURATION = 0.33F;

    public BurningAction(AbstractCreature target, AbstractCreature source, int amount) {
        setValues(target, source, amount);
        this.actionType = AbstractGameAction.ActionType.DAMAGE;
        this.duration = DURATION;
    }

    public void update() {
        if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
            this.isDone = true;
            return;
        }
        if ((duration == DURATION) && (target.currentHealth > 0)) {
            target.damageFlash = true;
            target.damageFlashFrames = 4;
            AbstractDungeon.effectList.add(new ExplosionSmallEffect(target.hb.cX, target.hb.cY));
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(target.hb.cX, target.hb.cY, AttackEffect.NONE));
        }
        tickDuration();
        if (this.isDone) {
            if (target.currentHealth > 0) {
                target.tint.color = Color.ORANGE.cpy();
                target.tint.changeColor(Color.FIREBRICK.cpy());
                target.damage(new DamageInfo(source, amount, DamageInfo.DamageType.HP_LOSS));
                target.loseBlock(amount);

            }
            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
            AbstractDungeon.actionManager.addToTop(new WaitAction(0.05F));
        }
    }
}