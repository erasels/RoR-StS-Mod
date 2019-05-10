package riskOfSpire.util;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.TreasureRoom;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.rewards.LunarCacheReward;
import riskOfSpire.rewards.LunarCoinReward;

import static riskOfSpire.RiskOfSpire.lunarCoinAmount;

public class LunarCoinHelper {
    public static float RANDOM_LUNAR_COIN_CHANCE = 0.07f;
    public static float RANDOM_LUNAR_CACHE_CHANCE = 0.15f;
    public static int BASE_LUNAR_COIN_AMT = 1;
    public static int BOSS_MOD = 3;
    public static int RAND_MOD = 1;
    public static int HEART_ENTER_COIN_AMT = 5;

    public static int MIN_COST = 4;
    public static int MAX_COST = 10;
    public static int LUNAR_CACHE_BASE_COST = 1;

    public static void modifyCombatRewards(CombatRewardScreen cRS) {
        if (CardCrawlGame.isInARun()) {
            if (AbstractDungeon.getCurrRoom() instanceof MonsterRoom) {
                /*if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss) {
                    cRS.rewards.add(new LunarCoinReward(AbstractDungeon.miscRng.random(BOSS_MOD) + BASE_LUNAR_COIN_AMT));
                } else*/
                if (AbstractDungeon.miscRng.randomBoolean(RANDOM_LUNAR_COIN_CHANCE)) {
                    cRS.rewards.add(new LunarCoinReward(BASE_LUNAR_COIN_AMT));
                }
            }

            if (AbstractDungeon.getCurrRoom() instanceof TreasureRoom) {
                if (AbstractDungeon.miscRng.randomBoolean(RANDOM_LUNAR_CACHE_CHANCE)) {
                    cRS.rewards.add(new LunarCacheReward());
                }
            }
        }
    }

    public static void manipLunarCoins(int amt, boolean playSound) {
        if (playSound) {
            CardCrawlGame.sound.play("RELIC_DROP_MAGICAL");
        }
        lunarCoinAmount += amt;
        if (lunarCoinAmount < 0) {
            lunarCoinAmount = 0;
        }
        RiskOfSpire.lCD.flash();
    }
}
