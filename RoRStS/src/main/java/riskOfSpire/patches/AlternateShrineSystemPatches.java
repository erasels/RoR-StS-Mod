package riskOfSpire.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import javassist.CtBehavior;
import riskOfSpire.shrines.AbstractShrineEvent;
import riskOfSpire.util.helpers.RoRShrineHelper;

public class AlternateShrineSystemPatches {
    @SpirePatch(clz = ProceedButton.class, method = "update")
    public static class PostCombatRewardScreenShrine {
        @SpireInsertPatch(locator = Locator.class)
        public static SpireReturn Insert(ProceedButton __instance) {
            if(AbstractDungeon.eventRng.randomBoolean(RoRShrineHelper.getCurrentShrineChance())) {
                AbstractDungeon.currMapNode.room = new PostCombatShrineRoom(AbstractDungeon.currMapNode.room);
                AbstractDungeon.getCurrRoom().onPlayerEntry();
                AbstractDungeon.rs = AbstractDungeon.RenderScene.EVENT;
                AbstractDungeon.combatRewardScreen.clear();
                AbstractDungeon.previousScreen = null;
                AbstractDungeon.closeCurrentScreen();
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    private static class Locator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractDungeon.class, "closeCurrentScreen");
            int[] found = LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            return new int[]{found[2]};
        }
    }

    public static class PostCombatShrineRoom extends EventRoom {
        public AbstractRoom originalRoom;

        PostCombatShrineRoom(AbstractRoom originalRoom) { this.originalRoom = originalRoom; }

        public void onPlayerEntry() {
            AbstractDungeon.overlayMenu.proceedButton.hide();
            event = RoRShrineHelper.getShrines().getRandom(AbstractDungeon.eventRng).makeCopy();
            ((AbstractShrineEvent) event).originalRoom = originalRoom;
            event.onEnterRoom();
        }
    }
}
