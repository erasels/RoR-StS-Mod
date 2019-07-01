package riskOfSpire.patches.DifficultyMeter;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import riskOfSpire.RiskOfSpire;

import java.util.ArrayList;


@SpirePatch(clz = AbstractDungeon.class, method = "render")
public class RenderMeterPatch {
    @SpireInsertPatch(locator = Locator.class)
    public static void patch(AbstractDungeon __instance, SpriteBatch sb) {
        RiskOfSpire.DifficultyMeter.render(sb);
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractRoom.class, "render");
            return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
        }
    }

}