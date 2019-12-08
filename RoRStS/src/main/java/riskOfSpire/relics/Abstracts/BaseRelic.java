package riskOfSpire.relics.Abstracts;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;

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
        for (AbstractRelic r : AbstractDungeon.player.relics) {
            if (r instanceof StackableRelic) {
                ((StackableRelic) r).onRelicGet(this);
            } else if (r instanceof UsableRelic) {
                ((UsableRelic) r).onRelicGet(this);
            }
        }
    }

    public void addPowerTips(ArrayList<PowerTip> pTs) {
        ArrayList<String[]> powerTips = new ArrayList<>();
        tips.forEach(pT -> powerTips.add(new String[]{pT.header, pT.body}));

        tips.clear();
        powerTips.forEach(s -> tips.add(new PowerTip(s[0], s[1])));
        tips.addAll(pTs);
        initializeTips();
    }

    public static void atb(AbstractGameAction a) {
        AbstractDungeon.actionManager.addToBottom(a);
    }

    public static void att(AbstractGameAction a) {
        AbstractDungeon.actionManager.addToTop(a);
    }
}
