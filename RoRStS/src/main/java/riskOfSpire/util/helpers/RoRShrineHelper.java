package riskOfSpire.util.helpers;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import riskOfSpire.shrines.AbstractShrineEvent;
import riskOfSpire.shrines.BloodShrine;
import riskOfSpire.shrines.ThreeDPrinter;
import riskOfSpire.util.WeightedList;

public class RoRShrineHelper {
    private static WeightedList<AbstractShrineEvent> rorShrines;
    private static final float BASE_SHRINE_CHANCE = 0.05f;
    private static final float ELITE_BONUS_CHANCE = 0.05f;
    private static final float MAX_SHRINE_CHANCE = 0.25f;

    public static float getCurrentShrineChance(boolean elite) {
        //can add shrine chance modifying relics hooks here
        float calcChance = BASE_SHRINE_CHANCE * AbstractDungeon.actNum;
        if(elite) {
            calcChance += ELITE_BONUS_CHANCE;
        }
        return Math.min(calcChance, MAX_SHRINE_CHANCE);
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
