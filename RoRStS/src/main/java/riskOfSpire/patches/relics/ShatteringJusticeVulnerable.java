package riskOfSpire.patches.relics;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import riskOfSpire.relics.Boss.ShatteringJustice;

public class ShatteringJusticeVulnerable {
    @SpirePatch(
            clz = VulnerablePower.class,
            method = "atDamageReceive"
    )
    public static class StackBonus
    {
        @SpirePostfixPatch
        public static float JUSTICE(float out, VulnerablePower __instance, float base, DamageInfo.DamageType t)
        {
            if (!__instance.owner.isPlayer && out != base && AbstractDungeon.player.hasRelic(ShatteringJustice.ID))
            {
                float mult = out / base; //ex, 1.5, 1.75, idk
                mult -= 1; //now it's .5, .75, something like that
                mult *= __instance.amount; //now it's a much bigger number. 3 stacks at .5 per: this is 1.5.
                mult += 1;
                //250% more damage = 3.5x
                if (mult > 3.5f)
                    mult = 3.5f;
                return base * mult;
            }
            return out;
        }
    }

    @SpirePatch(
            clz = VulnerablePower.class,
            method = "updateDescription"
    )
    public static class MatchingDescription
    {
        @SpirePrefixPatch
        public static SpireReturn LOTSOFJUSTICE(VulnerablePower __instance)
        {
            if (!__instance.owner.isPlayer && AbstractDungeon.player.hasRelic(ShatteringJustice.ID))
            {
                float mult = __instance.atDamageReceive(1.0f, DamageInfo.DamageType.NORMAL); //will use altered calculation
                mult -= 1;
                mult *= 100;

                if (mult > 250)
                    mult = 250;

                if (__instance.amount == 1)
                    __instance.description = VulnerablePower.DESCRIPTIONS[0] + MathUtils.floor(mult) + VulnerablePower.DESCRIPTIONS[1] + __instance.amount + VulnerablePower.DESCRIPTIONS[2];
                else
                    __instance.description = VulnerablePower.DESCRIPTIONS[0] + MathUtils.floor(mult) + VulnerablePower.DESCRIPTIONS[1] + __instance.amount + VulnerablePower.DESCRIPTIONS[3];
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }
}
