package riskOfSpire.patches.ForUsableRelics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

@SpirePatch(
        clz = AbstractRoom.class,
        method = "applyEndOfTurnRelics"
)
public class UsableRelicCooldown {
    @SpirePostfixPatch
    public static void updateCooldown(AbstractRoom __instance)
    {
        if (UsableRelicSlot.usableRelic.get(AbstractDungeon.player) != null)
        {
            UsableRelicSlot.usableRelic.get(AbstractDungeon.player).updateCooldown();
        }
    }
}
