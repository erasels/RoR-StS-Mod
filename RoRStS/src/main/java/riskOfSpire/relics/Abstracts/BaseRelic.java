package riskOfSpire.relics.Abstracts;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public abstract class BaseRelic extends AbstractRelic {
    public boolean isLunar = false;
    public boolean isTempHP = false;
    public boolean isCritical = false;

    public BaseRelic(String setId, String imgName, RelicTier tier, LandingSound sfx) {
        super(setId, imgName, tier, sfx);
    }

    public void onRelicGet(AbstractRelic r) {
    }

    public void notifyRelicGet() {
        for(AbstractRelic r : AbstractDungeon.player.relics) {
            if (r instanceof StackableRelic) {
                ((StackableRelic)r).onRelicGet(this);
            } else if(r instanceof UsableRelic) {
                ((UsableRelic)r).onRelicGet(this);
            }
        }
    }
}
