package riskOfSpire.patches.DifficultyMeter;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.util.helpers.EliteHelper;

import java.util.ArrayList;

public class UpgradeMonstersPatch {
    @SpirePatch(clz = AbstractMonster.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {String.class, String.class, int.class, float.class, float.class, float.class, float.class, String.class, float.class, float.class, boolean.class})
    public static class MonsterConstructorUpgrade {
        @SpireInsertPatch(locator = Locator.class)
        public static void patch(AbstractMonster __instance, String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY, boolean ignoreBlights) {
            if(__instance != null && CardCrawlGame.isInARun()) {
                RiskOfSpire.DifficultyMeter.PreUpgradeMonsterHealth(__instance);
            }
        }

        private static class Locator extends SpireInsertLocator {
            ArrayList<Matcher> Prerequisites = new ArrayList<>();

            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(
                        AbstractDungeon.class, "player");
                return LineFinder.findAllInOrder(ctMethodToPatch, Prerequisites, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = AbstractMonster.class, method = "useUniversalPreBattleAction")
    public static class ApplyElitePowers {
        @SpirePostfixPatch
        public static void patch(AbstractMonster __instance) {
            EliteHelper.SetElite(__instance);
        }
    }
    @SpirePatch(clz = AbstractMonster.class, method = "setHp", paramtypez = {
            int.class, int.class
    })
    public static class MonsterHpUpgrade {
        @SpireInsertPatch(locator = Locator.class)
        public static void patch(AbstractMonster __instance, int min, int max) {
            if(__instance != null && CardCrawlGame.isInARun()) {
                RiskOfSpire.DifficultyMeter.UpgradeMonsterHealth(__instance);
            }
        }

        private static class Locator extends SpireInsertLocator {
            ArrayList<Matcher> Prerequisites = new ArrayList<>();

            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(
                        AbstractMonster.class, "maxHealth");
                return LineFinder.findAllInOrder(ctMethodToPatch, Prerequisites, finalMatcher);
            }
        }
        /*....................../´¯/)
          ....................,/¯../
          .................../..../
          ............./´¯/'...'/´¯¯`·¸
          ........../'/.../..../......./¨¯\
          ........('(...´...´.... ¯~/'...')
          .........\.................'...../
          ..........''...\.......... _.·´
          ............\..............(
          ..............\.............\...*/
    }

    @SpirePatch(clz = AbstractMonster.class, method = "render")
    public static class RenderMonster {
        @SpirePrefixPatch
        public static void patch(AbstractMonster __instance, SpriteBatch sb) {
            EliteHelper.eliteColorChange(__instance);
        }

        //@SpireInsertPatch(locator = Locator.class)
        @SpirePostfixPatch
        public static void otherpatch(AbstractMonster __instance, SpriteBatch sb) {
            EliteHelper.resetColorChange();
        }

        private static class Locator extends SpireInsertLocator {
            ArrayList<Matcher> Prerequisites = new ArrayList<>();

            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(
                        AbstractMonster.class, "isDying");
                return LineFinder.findAllInOrder(ctMethodToPatch, Prerequisites, finalMatcher);
            }
        }
    }
}
