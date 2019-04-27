package riskOfSpire.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.helpers.FontHelper;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import riskOfSpire.relics.Abstracts.StackableRelic;

@SpirePatch(
        clz=FontHelper.class,
        method="initialize"
)
public class GenerateRelicStackFont
{
    public static ExprEditor Instrument()
    {
        return new ExprEditor() {
            @Override
            public void edit(FieldAccess f) throws CannotCompileException
            {
                if (f.isWriter() && f.getFieldName().equals("tipHeaderFont")) {
                    f.replace("{" +
                            StackableRelic.class.getName() + ".STACK_FONT = prepFont(" + StackableRelic.class.getName() + ".STACK_FONT_SIZE, false);" +
                            "$_ = $proceed($$);" +
                            "}");
                }
            }
        };
    }
}
