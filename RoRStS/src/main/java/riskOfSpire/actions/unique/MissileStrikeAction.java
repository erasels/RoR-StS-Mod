package riskOfSpire.actions.unique;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import riskOfSpire.vfx.combat.MissileStrikeEffect;

public class MissileStrikeAction extends AbstractGameAction {
    private DamageInfo info;
    private static final float DURATION = 0.1F;

    public MissileStrikeAction(DamageInfo info, AttackEffect effect) {
        this.info = info;
        this.setValues(AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng), info);
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = effect;
        this.duration = DURATION;
    }

    public void update() {
        if (this.shouldCancelAction()) {
            this.isDone = true;
        } else {
            if (this.duration == DURATION) {
                AbstractDungeon.effectsQueue.add(new MissileStrikeEffect(target.hb.cX, target.hb.cY, Color.ORANGE));
                target.damageFlash = true;
                target.damageFlashFrames = 4;
                AbstractDungeon.effectList.add(new FlashAtkImgEffect(target.hb.cX, target.hb.cY, attackEffect));
            }

            this.tickDuration();
            if (isDone) {
                target.tint.color = Color.RED.cpy();
                target.tint.changeColor(Color.WHITE.cpy());

                target.damage(info);
                if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                    AbstractDungeon.actionManager.clearPostCombatActions();
                }

            }

        }
    }
}
