package riskOfSpire.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.EventHelper;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.relics.Common.RustyKey;

@SpirePatch(
        clz = EventHelper.class,
        method = "roll",
        paramtypez = { Random.class }
)
public class RustyKeyPatch {
    @SpirePostfixPatch
    public static EventHelper.RoomResult updateTreasureChance(EventHelper.RoomResult choice, Random rng)
    {
        if (choice == EventHelper.RoomResult.TREASURE)
        {
            //Treasure chance has been reset; update it.
            if (AbstractDungeon.player.hasRelic(RustyKey.ID))
            {
                AbstractRelic k = AbstractDungeon.player.getRelic(RustyKey.ID);
                if (k instanceof RustyKey)
                {
                    k.flash();
                    EventHelper.TREASURE_CHANCE += ((RustyKey) k).getBaseRate();
                }
            }
        }
        return choice;
    }
}
