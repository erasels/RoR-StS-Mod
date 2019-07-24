package riskOfSpire.patches.relics.usableHooks;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import riskOfSpire.patches.ForUsableRelics.UsableRelicSlot;

import java.util.ArrayList;

@SpirePatch(clz = AbstractPlayer.class, method = "onVictory")
public class OnVictoryHook {
    @SpireInsertPatch(locator = Locator.class)
    public static void Insert(AbstractPlayer __instance) {
        if(UsableRelicSlot.usableRelic.get(__instance) != null) {
            UsableRelicSlot.usableRelic.get(__instance).onVictory();
        }
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "relics");
            return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
        }
    }
}
