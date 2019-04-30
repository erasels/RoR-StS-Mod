package riskOfSpire.relics.Common;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.EventHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.relics.Abstracts.StackableRelic;
import riskOfSpire.util.RiskOfRainRelicHelper;

public class RustyKey extends StackableRelic {
    public static final String ID = RiskOfSpire.makeID("RustyKey");

    private static final float RARITY_SCALE = 0.95f; //How much odds are increased of getting higher rarity relics in chests.
    private static final float CHEST_RATE_BOOST = 0.05f;

    public RustyKey() {
        super(ID, "BundleOfFireworks.png", RelicTier.COMMON, LandingSound.CLINK);
    }

    public void onEquip() {
        EventHelper.TREASURE_CHANCE += CHEST_RATE_BOOST;
    }

    @Override
    public void onStack() {
        super.onStack();
        EventHelper.TREASURE_CHANCE += CHEST_RATE_BOOST;
    }

    public float getBaseRate() {
        return CHEST_RATE_BOOST * relicStack;
    }

    private float getRarityModifier() {
        float rate = 1.0f;
        for (int i = 0; i < relicStack; ++i)
        {
            rate *= RARITY_SCALE;
        }
        return rate; //I didn't want to cast from double after using math.pow xd
    }

    @Override
    public void onChestOpen(boolean bossChest) {
        if (!bossChest)
        {
            this.flash();
            AbstractDungeon.getCurrRoom().rewards.add(new RewardItem(RiskOfRainRelicHelper.getRandomRelic(false, true, getRarityModifier())));
        }
    }

    @Override
    public String getUpdatedDescription() {
        //Chests contain an additional Risk of Rain based relic. You are x% more likely to find treasure in ? rooms.
        return DESCRIPTIONS[0] + (int)(getBaseRate() * 100) + DESCRIPTIONS[1];
    }

    public AbstractRelic makeCopy() {
        return new RustyKey();
    }
}