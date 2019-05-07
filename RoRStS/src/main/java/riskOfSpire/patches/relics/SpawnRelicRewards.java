package riskOfSpire.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import javassist.CtBehavior;
import riskOfSpire.util.LunarCoinHelper;
import riskOfSpire.util.RiskOfRainRelicHelper;

@SpirePatch(
        clz = CombatRewardScreen.class,
        method = "setupItemReward",
        paramtypez = {}
)
public class SpawnRelicRewards {
    @SpireInsertPatch (
            locator = Locator.class
    )
    public static void modifyRewards(CombatRewardScreen __instance)
    {
        RiskOfRainRelicHelper.modifyCombatRewards(__instance);
        LunarCoinHelper.modifyCombatRewards(__instance);
    }

    private static class Locator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(CombatRewardScreen.class, "positionRewards");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}