package riskOfSpire.patches.powers;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import riskOfSpire.powers.interfaces.BetterOnLoseHpPower;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "damage"
)
public class BetterOnLoseHpPowerPatch {
    public static ExprEditor Instrument() {
        return new ExprEditor() {
            @Override
            public void edit(MethodCall m) throws CannotCompileException {
                if (m.getClassName().equals(AbstractPower.class.getName()) && m.getMethodName().equals("onLoseHp")) {
                    m.replace("{" +
                            "damageAmount = $proceed(damageAmount);" +
                            "$_ = " + BetterOnLoseHpPowerPatch.class.getName() + ".Do(info, p, damageAmount);" +
                            "}");
                }
            }
        };
    }

    @SuppressWarnings("unused")
    public static int Do(DamageInfo info, AbstractPower p, int damageAmount) {
        if (p instanceof BetterOnLoseHpPower && damageAmount > 0) {
            return ((BetterOnLoseHpPower) p).betterOnLoseHp(info, damageAmount);
        }
        return damageAmount;
    }
}