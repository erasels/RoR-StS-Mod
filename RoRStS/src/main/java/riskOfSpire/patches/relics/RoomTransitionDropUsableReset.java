package riskOfSpire.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import riskOfSpire.util.helpers.RiskOfRainRelicHelper;

@SpirePatch(clz = AbstractDungeon.class, method = "nextRoomTransition", paramtypez = {SaveFile.class})
public class RoomTransitionDropUsableReset {
    public static void Postfix(AbstractDungeon __instance, SaveFile sf) {
        if(RiskOfRainRelicHelper.dropUsable && !CardCrawlGame.loadingSave) {
            RiskOfRainRelicHelper.dropUsable = false;
        }
    }
}
