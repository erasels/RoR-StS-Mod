package riskOfSpire.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import javassist.CtBehavior;
import riskOfSpire.relics.Interfaces.OnFinalHealRelic;

@SpirePatch(
        clz = AbstractCreature.class,
        method = "heal",
        paramtypez = { int.class, boolean.class }
)
public class FinalHealHook {
    @SpireInsertPatch(
            locator = Locator.class,
            localvars = { "healAmount" }
    )
    public static void Insert(AbstractCreature __instance, int baseHeal, boolean showEffect, @ByRef int[] healAmount)
    {
        if (__instance.isPlayer)
        {
            for (AbstractRelic r : AbstractDungeon.player.relics)
            {
                if (r instanceof OnFinalHealRelic)
                {
                    healAmount[0] = ((OnFinalHealRelic) r).onFinalHeal(healAmount[0]);
                }
            }
        }
    }

    private static class Locator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractCreature.class, "currentHealth");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
