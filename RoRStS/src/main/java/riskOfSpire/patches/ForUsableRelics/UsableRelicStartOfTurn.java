package riskOfSpire.patches.ForUsableRelics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "applyStartOfTurnRelics"
)
public class UsableRelicStartOfTurn {
    @SpirePostfixPatch
    public static void onTurnStart(AbstractPlayer __instance)
    {
        if (UsableRelicSlot.usableRelic.get(AbstractDungeon.player) != null)
        {
            UsableRelicSlot.usableRelic.get(AbstractDungeon.player).atTurnStart();
        }
    }
}
