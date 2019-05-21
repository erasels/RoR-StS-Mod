package riskOfSpire.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.relics.Interfaces.OnPlayerBlockBrokenRelic;

@SpirePatch(
        clz = AbstractCreature.class,
        method = "brokeBlock"
)
public class PlayerBreakBlockHook {
    @SpirePrefixPatch
    public static void Prefix(AbstractCreature __instance) {
        if(__instance.isPlayer) {
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                if(r instanceof OnPlayerBlockBrokenRelic) {
                    ((OnPlayerBlockBrokenRelic) r).onPlayerBlockBroken();
                }
            }
        }
    }
}
