package riskOfSpire.patches.relics;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.relics.Abstracts.OnMonsterSpawn;

public class OnMonsterSpawnHook {
    @SpirePatch(
            clz = SpawnMonsterAction.class,
            method = "update"
    )
    public static class OnMonsterSpawnListener {
        public static void Postfix(SpawnMonsterAction __instance) {
            for(AbstractRelic r : AbstractDungeon.player.relics) {
                if (r instanceof OnMonsterSpawn) {
                    //Could use FieldAccessMatch on this.used = true; and use localvars but whatever
                    AbstractMonster m = (AbstractMonster)ReflectionHacks.getPrivate(__instance, AbstractMonster.class, "m");
                    ((OnMonsterSpawn)r).onMonsterSpawn(m);
                }
            }
        }
    }
}
