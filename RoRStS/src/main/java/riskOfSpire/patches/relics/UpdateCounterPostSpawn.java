package riskOfSpire.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.CtBehavior;
import riskOfSpire.util.RiskOfRainRelicHelper;

@SpirePatch(
        clz= AbstractRoom.class,
        method="update"
)
public class UpdateCounterPostSpawn
{
    @SpireInsertPatch(
            locator=Locator.class
    )
    public static void Insert(AbstractRoom __instance)
    {
        //This occurs post-reward generation, post-save.
        RiskOfRainRelicHelper.updateCounter();
    }

    private static class Locator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "loading_post_combat");
            int[] found = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            return new int[]{found[found.length - 1]};
        }
    }
}