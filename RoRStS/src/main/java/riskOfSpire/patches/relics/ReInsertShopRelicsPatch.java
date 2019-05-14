package riskOfSpire.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.shop.ShopScreen;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import org.apache.commons.lang3.math.NumberUtils;
import riskOfSpire.relics.Abstracts.BaseRelic;

import java.util.ArrayList;

@SpirePatch(clz = ShopScreen.class, method = "initRelics")
public class ReInsertShopRelicsPatch {
    @SpireInsertPatch(locator = Locator.class, localvars = {"tempRelic"})
    public static void Insert(ShopScreen __instance, @ByRef AbstractRelic[] tempRelic) {
        if (tempRelic[0] instanceof BaseRelic) {
            if (tempRelic[0].tier != null) {
                ArrayList<String> tmp;
                switch (tempRelic[0].tier) {
                    case COMMON:
                        tmp = AbstractDungeon.commonRelicPool;
                        break;
                    case UNCOMMON:
                        tmp = AbstractDungeon.uncommonRelicPool;
                        break;
                    case RARE:
                        tmp = AbstractDungeon.rareRelicPool;
                        break;
                    default:
                        //Basically Shop
                        tmp = AbstractDungeon.shopRelicPool;
                }
                //Shuffle it into the last five relics (10 if not taken or seen for first time) of the end because otherwise it would always spawn after seeing it once.
                int listBound = NumberUtils.min(tmp.size(), AbstractDungeon.player.hasRelic(tempRelic[0].relicId) ? 5 : 10);
                if (listBound > 0) {
                    int bound = tmp.size() - (AbstractDungeon.relicRng.random(listBound) + 1);
                    String newEnd = tmp.get(bound);
                    tmp.set(bound, tempRelic[0].relicId);
                    tmp.add(newEnd);
                } else {
                    tmp.add(tempRelic[0].relicId);
                }
            }
        }
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.NewExprMatcher("com.megacrit.cardcrawl.shop.StoreRelic");
            return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
        }
    }
}