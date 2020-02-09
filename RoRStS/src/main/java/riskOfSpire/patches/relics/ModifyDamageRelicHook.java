package riskOfSpire.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import riskOfSpire.relics.Interfaces.ModifyDamageRelic;

@SpirePatch(clz = AbstractCard.class, method = "calculateCardDamage")
public class ModifyDamageRelicHook {
    private static boolean firstMatch = true;

    public static ExprEditor Instrument() {
        return new ExprEditor() {
            @Override
            public void edit(MethodCall m) throws CannotCompileException {
                if (m.getClassName().equals(AbstractRelic.class.getName()) && m.getMethodName().equals("atDamageModify")) {
                    if(firstMatch) {
                        firstMatch = false;
                        m.replace("{" +
                                "tmp = $proceed(tmp, this);" +
                                "$_ = " + ModifyDamageRelicHook.class.getName() + ".Do($0, tmp, this, mo);" +
                                "}");
                    } else {
                        m.replace("{" +
                                "tmp[i] = $proceed(tmp[i], this);" +
                                "$_ = " + ModifyDamageRelicHook.class.getName() + ".Do($0, tmp[i], this, mo);" +
                                "}");
                    }
                }
            }
        };
    }

    @SuppressWarnings("unused")
    public static float Do(AbstractRelic r, float damageAmount, AbstractCard c, AbstractMonster m) {
        if (r instanceof ModifyDamageRelic && m != null) {
            return ((ModifyDamageRelic) r).calculateCardDamageRelic(damageAmount, c, m);
        }
        return damageAmount;
    }
}