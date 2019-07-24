package riskOfSpire.patches.relics.usableHooks;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import riskOfSpire.patches.ForUsableRelics.UsableRelicSlot;

@SpirePatch(clz = AbstractPlayer.class, method = "applyPreCombatLogic")
public class AtPreBattleHook {
    public static void Postfix(AbstractPlayer __instance) {
        if(UsableRelicSlot.usableRelic.get(__instance) != null) {
            UsableRelicSlot.usableRelic.get(__instance).atPreBattle();
        }
    }
}
