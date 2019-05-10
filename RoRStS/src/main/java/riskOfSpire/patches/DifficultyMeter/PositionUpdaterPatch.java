package riskOfSpire.patches.DifficultyMeter;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.OverlayMenu;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import riskOfSpire.RiskOfSpire;

import java.util.ArrayList;


@SpirePatch(clz = OverlayMenu.class, method = "update")
public class PositionUpdaterPatch {
    @SpireInsertPatch(locator = Locator.class)
    public static void patch(OverlayMenu __instance) {
        RiskOfSpire.DifficultyMeter.updatePositions();
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(
                    OverlayMenu.class, "bottomBgPanel");
            return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
        }
    }
}