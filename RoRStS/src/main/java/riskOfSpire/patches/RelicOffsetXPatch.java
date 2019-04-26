package riskOfSpire.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import javassist.CtBehavior;

@SpirePatch(
        clz = AbstractRelic.class,
        method = "updateOffsetX"
)
public class RelicOffsetXPatch {
    public static float offsetX = 0.0f;

    @SpireInsertPatch(
        locator = Locator.class,
            localvars = { "target" }
    )
    public static void updatePublicOffset(float target)
    {
        offsetX = target;
    }

    private static class Locator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(MathHelper.class, "uiLerpSnap");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
