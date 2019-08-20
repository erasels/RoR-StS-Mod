package riskOfSpire.util.helpers;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import riskOfSpire.shrines.AbstractShrineEvent;
import riskOfSpire.shrines.BloodShrine;
import riskOfSpire.shrines.ThreeDPrinter;
import riskOfSpire.util.WeightedList;

public class RoRShrineHelper {
    private static WeightedList<AbstractShrineEvent> rorShrines;
    private static final float BASE_SHRINE_CHANCE = 0.1f;
    private static final float ELITE_BONUS_CHANCE = 0.1f;
    private static final float MAX_BASESHRINE_CHANCE = 0.75f;
    private static final float MISS_CHANCE_INCREASE = 0.05f;

    public static int shrineSpawnMiss = 0;

    public static float getCurrentShrineChance(boolean elite) {
        //can add shrine chance modifying relics hooks here
        float calcChance = BASE_SHRINE_CHANCE + ((float)AbstractDungeon.floorNum * 0.002f) + (elite?ELITE_BONUS_CHANCE:0);
        calcChance += MISS_CHANCE_INCREASE*shrineSpawnMiss;
        return Math.min(calcChance, MAX_BASESHRINE_CHANCE);
    }

    public static float getCurrentShrineChance() {
        return getCurrentShrineChance(false);
    }

    public static WeightedList<AbstractShrineEvent> getShrines() {
        if(rorShrines != null && !rorShrines.isEmpty()) {
            return rorShrines;
        }
        return createShrineList();
    }

    public static AbstractShrineEvent getRandomShrine() {
        WeightedList<AbstractShrineEvent> tmp = getShrines().returnSubList(AbstractShrineEvent::canSpawn);
        return tmp.getRandom(AbstractDungeon.eventRng).makeCopy();
    }

    private static WeightedList<AbstractShrineEvent> createShrineList() {
        rorShrines = new WeightedList<>();

        //TODO: Add all shrines once they're done
        rorShrines.add(new BloodShrine(), WeightedList.WEIGHT_COMMON);
        rorShrines.add(new ThreeDPrinter(), WeightedList.WEIGHT_COMMON);

        return rorShrines;
    }
}
