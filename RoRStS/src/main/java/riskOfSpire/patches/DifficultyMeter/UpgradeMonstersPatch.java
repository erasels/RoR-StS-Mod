package riskOfSpire.patches.DifficultyMeter;


import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import riskOfSpire.RiskOfSpire;

public class UpgradeMonstersPatch {
    @SpirePatch(clz = MonsterRoom.class, method = "onPlayerEntry")
    public static class NormalMonsters {
        @SpirePostfixPatch
        public static void patch(MonsterRoom __instance) {
            for (AbstractMonster m : __instance.monsters.monsters) {
                if (!m.isDeadOrEscaped()) {
                    RiskOfSpire.DifficultyMeter.onBattleStart(m);
                }
            }
        }
    }

    @SpirePatch(clz = MonsterRoomElite.class, method = "onPlayerEntry")
    public static class EliteMonsters {
        @SpirePostfixPatch
        public static void patch(MonsterRoomElite __instance) {
            for (AbstractMonster m : __instance.monsters.monsters) {
                if (!m.isDeadOrEscaped()) {
                    RiskOfSpire.DifficultyMeter.onBattleStart(m);
                }
            }
        }
    }

    @SpirePatch(clz = MonsterRoomBoss.class, method = "onPlayerEntry")
    public static class BossMonsters {
        @SpirePostfixPatch
        public static void patch(MonsterRoomBoss __instance) {
            for (AbstractMonster m : __instance.monsters.monsters) {
                if (!m.isDeadOrEscaped()) {
                    RiskOfSpire.DifficultyMeter.onBattleStart(m);
                }
            }
        }
    }
}
