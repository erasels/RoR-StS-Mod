package riskOfSpire.actions.unique;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;

import java.util.ArrayList;

public class RandomLightningStrikesAction extends AbstractGameAction {
    private DamageInfo info;
    private static final float DURATION = 0.01F;
    private static final float POST_ATTACK_WAIT_DUR = 0.2F;
    private int numTimes;

    private ArrayList<AbstractMonster> possibleTargets;

    public RandomLightningStrikesAction(ArrayList<AbstractMonster> possibleTargets, DamageInfo info, int numTimes, AttackEffect attackEffect) {
        this.info = info;
        this.actionType = AbstractGameAction.ActionType.DAMAGE;
        this.possibleTargets = new ArrayList<>(possibleTargets);
        this.attackEffect = attackEffect;
        this.duration = DURATION;
        this.numTimes = numTimes;
    }

    public void update() {
        if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
            AbstractDungeon.actionManager.clearPostCombatActions();
            this.isDone = true;
            return;
        }

        if (target == null || target.isDeadOrEscaped()) {
            possibleTargets.removeIf(AbstractCreature::isDeadOrEscaped);
            if(!possibleTargets.isEmpty()) {
            target = possibleTargets.get(AbstractDungeon.cardRandomRng.random(possibleTargets.size() - 1));
            } else {
                isDone = true;
            }
        }

        if(!isDone) {
            target.damageFlash = true;
            target.damageFlashFrames = 4;
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(target.hb.cX, target.hb.cY, attackEffect));
            AbstractDungeon.effectList.add(new LightningEffect(target.drawX, target.drawY));
            CardCrawlGame.sound.play("ORB_LIGHTNING_EVOKE", 0.1F);

            info.applyPowers(info.owner, target);
            target.damage(info);
            if ((this.numTimes > 1)) {
                this.numTimes -= 1;
                AbstractDungeon.actionManager.addToTop(new RandomLightningStrikesAction(possibleTargets, info, numTimes, attackEffect));
            }
            //AbstractDungeon.actionManager.addToTop(new WaitAction(POST_ATTACK_WAIT_DUR));

            isDone = true;
        }
    }
}

