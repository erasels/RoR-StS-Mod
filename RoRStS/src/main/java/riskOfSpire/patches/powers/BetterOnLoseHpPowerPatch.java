package riskOfSpire.patches.powers;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import riskOfSpire.powers.interfaces.BetterOnLoseHpPower;
import riskOfSpire.powers.interfaces.PostOnLoseHpPower;

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

    @SpireInsertPatch(locator = PostPowerFinalLocator.class, localvars = {"damageAmount"})
    public static void Insert(AbstractPlayer __instance, DamageInfo DI, @ByRef int[] damageAmount) {
        for (AbstractPower p : AbstractDungeon.player.powers) {
            if (p instanceof PostOnLoseHpPower && damageAmount[0] > 0) {
                damageAmount[0] = ((PostOnLoseHpPower) p).postOnLoseHp(DI, damageAmount[0]);
            }
        }
    }

    private static class PostPowerFinalLocator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "relics");
            return new int[]{LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher)[4]};

        }
    }
}