package riskOfSpire.patches.relics;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import riskOfSpire.relics.Interfaces.OnMonsterSpawn;

import java.util.ArrayList;

public class OnMonsterSpawnHook {
    @SpirePatch(
            clz = SpawnMonsterAction.class,
            method = "update"
    )
    public static class OnMonsterSpawnListener {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(SpawnMonsterAction __instance) {
            for(AbstractRelic r : AbstractDungeon.player.relics) {
                if (r instanceof OnMonsterSpawn) {
                    //Could use FieldAccessMatch on this.used = true; and use localvars but whatever
                    AbstractMonster m = (AbstractMonster)ReflectionHacks.getPrivate(__instance, SpawnMonsterAction.class, "m");
                    ((OnMonsterSpawn)r).onMonsterSpawn(m);
                }
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ModHelper.class, "isModEnabled");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
            }
        }
    }
}
