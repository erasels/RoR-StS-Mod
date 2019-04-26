package riskOfSpire.actions.general;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class GuaranteedDamageRandomEnemyAction extends AbstractGameAction {
    private DamageInfo info;
    private boolean fast;

    public GuaranteedDamageRandomEnemyAction(AbstractCreature source, int damage, DamageInfo.DamageType damageType, AttackEffect effect, boolean fast)
    {
        this.source = source;
        this.amount = damage;
        this.damageType = damageType;
        this.attackEffect = effect;
        this.info = null;
        this.fast = fast;
    }
    public GuaranteedDamageRandomEnemyAction(DamageInfo info, AttackEffect effect, boolean fast)
    {
        this.source = info.owner;
        this.info = info;
        this.attackEffect = effect;
        this.fast = fast;
    }

    @Override
    public void update() {
        AbstractMonster target = AbstractDungeon.getRandomMonster();

        if (target != null && !target.isDeadOrEscaped() && target.currentHealth > 0)
        {
            if (this.info == null)
            {
                DamageInfo damageInfo = new DamageInfo(source, amount, damageType);

                damageInfo.applyPowers(damageInfo.owner, target);

                AbstractDungeon.actionManager.addToTop(new DamageAction(target, damageInfo, this.attackEffect, fast));
            }
            else
            {
                AbstractDungeon.actionManager.addToTop(new DamageAction(target, info, this.attackEffect, fast));
            }
        }

        this.isDone = true;
    }
}