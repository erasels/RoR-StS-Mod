package riskOfSpire.actions.unique;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import riskOfSpire.vfx.combat.TargetedMissileEffect;

import java.util.ArrayList;

public class TargetedMissileAction extends AbstractGameAction {
    private DamageInfo info;

    private float x;
    private float y;
    private Color color;

    private static final float MAX_DURATION = 10.0f; //if this matters, there was probably a problem.

    private ArrayList<TargetedMissileEffect> missileEffects = new ArrayList<>();

    public TargetedMissileAction(float x, float y, Color c, int amount, DamageInfo info) {
        this.info = info;
        setValues(null, info.owner, amount);
        this.actionType = ActionType.DAMAGE;
        this.duration = MAX_DURATION;
        this.color = c;
        this.x = x;
        this.y = y;
    }
    public TargetedMissileAction(float x, float y, Color c, AbstractCreature target, int amount, DamageInfo info) {
        this.info = info;
        setValues(target, info.owner, amount);
        this.actionType = ActionType.DAMAGE;
        this.duration = MAX_DURATION;
        this.color = c;
        this.x = x;
        this.y = y;
    }

    public void update() {
        if (this.amount-- > 0)
        {
            if (target == null)
            {
                AbstractCreature t = AbstractDungeon.getRandomMonster();
                if (t != null)
                {
                    CardCrawlGame.sound.play("CARD_EXHAUST", 0.1F);
                    TargetedMissileEffect e = new TargetedMissileEffect(x, y, t, info, color);
                    AbstractDungeon.effectsQueue.add(e);
                    missileEffects.add(e);
                }
            }
            else
            {
                if (!target.isDeadOrEscaped())
                {
                    CardCrawlGame.sound.play("CARD_EXHAUST", 0.1F);
                    TargetedMissileEffect e = new TargetedMissileEffect(x, y, target, info, color);
                    AbstractDungeon.effectsQueue.add(e);
                    missileEffects.add(e);
                }
            }
        }
        else
        {
            missileEffects.removeIf((e)->{
                if (e.isDone && e.target != null)
                {
                    e.target.damage(e.dmg);
                }
                return e.isDone;
            });
            if (missileEffects.size() == 0)
            {
                this.isDone = true;

                if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                    AbstractDungeon.actionManager.clearPostCombatActions();
                }
            }
            this.tickDuration(); //For backup.
        }
    }
}