package riskOfSpire.patches.ForUsableRelics;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.OverlayMenu;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;

@SpirePatch(
        clz = OverlayMenu.class,
        method = "update"
)
public class UpdateUsableRelic {
    @SpireInsertPatch(
            locator = Locator.class
    )
    public static void OverlayMenuUpdate(OverlayMenu __instance)
    {
        if (UsableRelicSlot.usableRelic.get(AbstractDungeon.player) != null)
        {
            UsableRelicSlot.usableRelic.get(AbstractDungeon.player).normalUpdate();
        }
    }

    private static class Locator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(OverlayMenu.class, "updateBlackScreen");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
