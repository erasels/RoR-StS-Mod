package riskOfSpire.util.helpers;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import riskOfSpire.shrines.AbstractShrineEvent;
import riskOfSpire.util.WeightedList;

public class RoRShrineHelper {
    private static WeightedList<AbstractShrineEvent> rorShrines;
    private static final float BASE_SHRINE_CHANCE = 0.015f;

    public static float getCurrentShrineChance() {
        //can add shrine chance modifying relics hooks here
        return BASE_SHRINE_CHANCE* (AbstractDungeon.actNum+1);
    }

    public static WeightedList<AbstractShrineEvent> getShrines() {
        if(rorShrines != null && !rorShrines.isEmpty()) {
            return rorShrines;
        }
        return createShrineList();
    }

    private static WeightedList<AbstractShrineEvent> createShrineList() {
        rorShrines = new WeightedList<>();

        //TODO: Add all shrines once they're done

        return rorShrines;
    }
}
