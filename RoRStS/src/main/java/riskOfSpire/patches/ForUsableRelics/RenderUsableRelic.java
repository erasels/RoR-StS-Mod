package riskOfSpire.patches.ForUsableRelics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CtBehavior;

import java.util.ArrayList;

public class RenderUsableRelic {
    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "renderRelics"
    )
    public static class renderInTopPanel
    {
        @SpirePostfixPatch
        public static void outOfCombatRender(AbstractPlayer __instance, SpriteBatch sb)
        {
            if (AbstractDungeon.getCurrRoom() == null || AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT)
            {
                if (UsableRelicSlot.usableRelic.get(__instance) != null)
                {
                    UsableRelicSlot.usableRelic.get(__instance).normalRender(sb);
                }
            }
        }
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "render"
    )
    public static class renderNextToHealth
    {
        @SpireInsertPatch (
                locator = Locator.class
        )
        public static void inCombatRender(AbstractPlayer __instance, SpriteBatch sb)
        {
            if (UsableRelicSlot.usableRelic.get(__instance) != null)
            {
                UsableRelicSlot.usableRelic.get(__instance).normalRender(sb);
            }
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ArrayList.class, "isEmpty");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
