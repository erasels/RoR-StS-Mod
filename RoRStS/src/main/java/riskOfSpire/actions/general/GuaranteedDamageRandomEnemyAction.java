package riskOfSpire.actions.general;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.vfx.combat.MissileStrikeEffect;

import java.util.ArrayList;
import java.util.Arrays;

public class GuaranteedDamageRandomEnemyAction extends AbstractGameAction {
    private DamageInfo info;
    private boolean fast;
    private boolean rainbowFirework;

    public GuaranteedDamageRandomEnemyAction(AbstractCreature source, int damage, DamageInfo.DamageType damageType, AttackEffect effect, boolean fast) {
        this.source = source;
        this.amount = damage;
        this.damageType = damageType;
        this.attackEffect = effect;
        this.info = null;
        this.fast = fast;
        this.rainbowFirework = false;
    }

    public GuaranteedDamageRandomEnemyAction(DamageInfo info, AttackEffect effect, boolean fast, boolean rainbowFirework) {
        this.source = info.owner;
        this.info = info;
        this.attackEffect = effect;
        this.fast = fast;
        this.rainbowFirework = rainbowFirework;
    }

    public GuaranteedDamageRandomEnemyAction(DamageInfo info, AttackEffect effect, boolean fast) {
        this.source = info.owner;
        this.info = info;
        this.attackEffect = effect;
        this.fast = fast;
        this.rainbowFirework = false;
    }

    @Override
    public void update() {
        AbstractMonster target = AbstractDungeon.getRandomMonster();

        if (target != null && !target.isDeadOrEscaped() && target.currentHealth > 0) {
            if (this.info == null) {
                DamageInfo damageInfo = new DamageInfo(source, amount, damageType);

                damageInfo.applyPowers(damageInfo.owner, target);
                if(rainbowFirework) {
                    AbstractDungeon.effectsQueue.add(new MissileStrikeEffect(target.hb.cX, target.hb.cY, RiskOfSpire.COLORS.get(MathUtils.random(RiskOfSpire.COLORS.size()-1))));
                }
                AbstractDungeon.actionManager.addToTop(new DamageAction(target, damageInfo, this.attackEffect, fast));
            } else {
                if(rainbowFirework) {
                    AbstractDungeon.effectsQueue.add(new MissileStrikeEffect(target.hb.cX, target.hb.cY, RiskOfSpire.COLORS.get(MathUtils.random(RiskOfSpire.COLORS.size()-1))));
                }
                AbstractDungeon.actionManager.addToTop(new DamageAction(target, info, this.attackEffect, fast));
            }
        }

        this.isDone = true;
    }
}