package riskOfSpire.util.helpers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.MinionPower;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.powers.elites.AbstractElitePower;
import riskOfSpire.powers.elites.BlazingPower;
import riskOfSpire.powers.elites.GlacialPower;
import riskOfSpire.powers.elites.OverloadingPower;

import static com.megacrit.cardcrawl.core.CardCrawlGame.psb;

public class EliteHelper {
    public static void SetElite(AbstractMonster m) {
        if (m.type == AbstractMonster.EnemyType.BOSS || m.hasPower(MinionPower.POWER_ID)) return;

        int r = AbstractDungeon.monsterHpRng.random(1, 100);
        if ((RiskOfSpire.DifficultyMeter.getDifficultyMod() == 0f && r <= (5 + MathUtils.floor(AbstractDungeon.floorNum * 0.1f))) ||
                (RiskOfSpire.DifficultyMeter.getDifficultyMod() > 0f && r <= 40 - (40 / Math.max((RiskOfSpire.DifficultyMeter.getDifficultyMod() / 100 + 1), 1)))) {
            m.increaseMaxHp(MathUtils.floor(m.maxHealth * 0.2f), false);
            r = r % 3;
            switch (r) {
                case 0:
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, null, new OverloadingPower(m)));
                    break;
                case 1:
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, null, new GlacialPower(m)));
                    break;
                default:
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, null, new BlazingPower(m)));
                    break;
            }
        }
    }

    public static void eliteColorChange(AbstractMonster m) {
        AbstractPower pow = null;
        for (AbstractPower p : m.powers) {
            if (p instanceof AbstractElitePower) {
                pow = p;
                break;
            }
        }

        if (pow != null) {
            if (pow.ID.equals(OverloadingPower.POWER_ID)) {
                m.tint.changeColor(new Color(1.0F, 1.0F, 0.3F, 1.0F));
            } else if (pow.ID.equals(GlacialPower.POWER_ID)) {
                psb.setShader(RiskOfSpire.GlacialShader);
            } else if (pow.ID.equals(BlazingPower.POWER_ID)) {
                m.tint.changeColor(new Color(1.0F, 0.3F, 0.3F, 1.0F));
            }
        }
    }

    public static void resetColorChange() {
        boolean triggered = false;
        if (psb.isDrawing()) {
            psb.end();
            triggered = true;
        }
        psb.setShader(null);
        //ShaderHelper.setShader(psb, ShaderHelper.Shader.DEFAULT);
        if (triggered) {
            psb.begin();
        }
    }
}
