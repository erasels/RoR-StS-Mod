package riskOfSpire.util.helpers;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.TreasureRoom;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.relics.Interfaces.BonusRorRelicChanceRelic;
import riskOfSpire.rewards.LunarCacheReward;
import riskOfSpire.rewards.LunarCoinReward;

import static riskOfSpire.RiskOfSpire.lunarCoinAmount;

public class LunarCoinHelper {
    public static float RANDOM_LUNAR_COIN_CHANCE = 0.04f;
    public static float RANDOM_LUNAR_COIN_CHANCE_BOSS = 0.15f;
    public static float RANDOM_LUNAR_CACHE_CHANCE = 0.25f;
    public static int BASE_LUNAR_COIN_AMT = 1;
    public static int HEART_ENTER_COIN_AMT = 5;
    public static int LUNAR_CACHE_BASE_COST = 1;

    public static void modifyCombatRewards(CombatRewardScreen cRS) {
        if (CardCrawlGame.isInARun()) {
            if (AbstractDungeon.getCurrRoom() instanceof MonsterRoom) {
                if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss) {
                    if (AbstractDungeon.miscRng.randomBoolean(RANDOM_LUNAR_COIN_CHANCE_BOSS * getDifficultyModifier())) {
                        cRS.rewards.add(new LunarCoinReward(BASE_LUNAR_COIN_AMT));
                    }
                } else if (AbstractDungeon.miscRng.randomBoolean(RANDOM_LUNAR_COIN_CHANCE * getDifficultyModifier())) {
                    cRS.rewards.add(new LunarCoinReward(BASE_LUNAR_COIN_AMT));
                }
            }

            if (AbstractDungeon.getCurrRoom() instanceof TreasureRoom) {
                float tmpChance = RANDOM_LUNAR_CACHE_CHANCE * getDifficultyModifier();
                for(AbstractRelic r : AbstractDungeon.player.relics) {
                    if(r instanceof BonusRorRelicChanceRelic) {
                        tmpChance = ((BonusRorRelicChanceRelic) r).lunarCacheChanceModifier(tmpChance);
                    }
                }
                if (AbstractDungeon.miscRng.randomBoolean(Math.min(tmpChance, 1.0f))) {
                    cRS.rewards.add(new LunarCacheReward());
                }
            }
        }
    }

    //TODO: See if I need to do the save and load this for this (increment later like RoRRelicHelper)

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

    private static float getDifficultyModifier() {
        return RiskOfSpire.DifficultyMeter.getDifficultyMod() + 1F;
    }
}
