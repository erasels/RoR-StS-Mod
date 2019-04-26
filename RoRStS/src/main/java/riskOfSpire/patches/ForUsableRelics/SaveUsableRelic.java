package riskOfSpire.patches.ForUsableRelics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;

@SpirePatch(
        clz = SaveFile.class,
        method = SpirePatch.CONSTRUCTOR,
        paramtypez = {
                SaveFile.SaveType.class
        }
)
public class SaveUsableRelic {
    @SpirePostfixPatch
    public static void SaveUsable(SaveFile __instance, SaveFile.SaveType saveType)
    {
        if (UsableRelicSlot.usableRelic.get(AbstractDungeon.player) != null)
        {
            __instance.relics.add(UsableRelicSlot.usableRelic.get(AbstractDungeon.player).relicId);
            __instance.relic_counters.add(UsableRelicSlot.usableRelic.get(AbstractDungeon.player).counter);
        }
    }
}