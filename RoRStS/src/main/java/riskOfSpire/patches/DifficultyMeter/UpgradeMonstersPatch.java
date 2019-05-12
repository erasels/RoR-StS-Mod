package riskOfSpire.patches.DifficultyMeter;


import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import riskOfSpire.RiskOfSpire;

import java.util.ArrayList;

public class UpgradeMonstersPatch {
    @SpirePatch(clz = AbstractMonster.class, method = "setHp", paramtypez = {
            int.class, int.class
    })
    public static class MonsterUpgrade {
        @SpireInsertPatch(locator = Locator.class)
        public static void patch(AbstractMonster __instance, int min, int max) {
            RiskOfSpire.DifficultyMeter.UpgradeMonster(__instance);
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
}
