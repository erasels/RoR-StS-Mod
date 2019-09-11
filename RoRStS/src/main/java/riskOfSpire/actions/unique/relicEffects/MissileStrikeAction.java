package riskOfSpire.actions.unique.relicEffects;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import riskOfSpire.vfx.combat.MissileStrikeEffect;

public class MissileStrikeAction extends AbstractGameAction {
    private DamageInfo info;
    private static final float DURATION = 0.1F;
    private String soundKey;
    private Color c;

    public MissileStrikeAction(DamageInfo info, AttackEffect effect) {
        this.info = info;
        this.setValues(AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng), info);
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = effect;
        this.duration = DURATION;
    }

    public MissileStrikeAction(AbstractMonster target, DamageInfo info, AttackEffect effect, Color c, String soundKey) {
        this.info = info;
        this.setValues(target, info);
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = effect;
        this.duration = DURATION;
        this.c = c.cpy();
        this.soundKey = soundKey;
    }

    public MissileStrikeAction(DamageInfo info, AttackEffect effect, Color c, String soundKey) {
        this(AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng), info, effect, c, soundKey);
    }

    public void update() {
        if (this.shouldCancelAction()) {
            this.isDone = true;
        } else {
            if (this.duration == DURATION) {
                if(c !=null && soundKey != null) {
                    AbstractDungeon.effectsQueue.add(new MissileStrikeEffect(target.hb.cX, target.hb.cY, c));
                } else {
                    AbstractDungeon.effectsQueue.add(new MissileStrikeEffect(target.hb.cX, target.hb.cY, Color.ORANGE, soundKey));
                }
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
