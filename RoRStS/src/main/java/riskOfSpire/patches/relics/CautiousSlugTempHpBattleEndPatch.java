package riskOfSpire.patches.relics;

import com.evacipated.cardcrawl.mod.stslib.patches.tempHp.BattleEnd;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import riskOfSpire.relics.Common.CautiousSlug;

@SpirePatch(
        clz = BattleEnd.class,
        method = "Prefix"
)
public class CautiousSlugTempHpBattleEndPatch {
    public static void Prefix() {
        CautiousSlug cs = (CautiousSlug) AbstractDungeon.player.getRelic(CautiousSlug.ID);
        if(cs != null) {
            cs.onTrigger();
        }
    }
}