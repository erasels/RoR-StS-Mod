package riskOfSpire.rewards;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpireSuper;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Circlet;
import com.megacrit.cardcrawl.rewards.RewardItem;
import riskOfSpire.RiskOfSpire;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

//Mostly copied from Hubris, with some adaptations.
public class LinkedRewardItem extends RewardItem
{
    public static UIStrings linkedUIStrings = CardCrawlGame.languagePack.getUIString(RiskOfSpire.makeID("LinkedRewardItem"));

    public List<RewardItem> linkedRewards = new ArrayList<>();

    public boolean claimed = false;

    private static final boolean NO_PEEKING = false;

    public LinkedRewardItem(RewardItem original)
    {
        type = original.type;
        outlineImg = original.outlineImg;
        img = original.img;
        goldAmt = original.goldAmt;
        bonusGold = original.bonusGold;
        text = original.text;
        relic = original.relic;
        potion = original.potion;
        cards = original.cards;
        //effects
        //isBoss
        hb = original.hb;
        y = original.y;
        flashTimer = original.flashTimer;
        isDone = original.isDone;
        ignoreReward = original.ignoreReward;
        redText = original.redText;
    }

    public LinkedRewardItem(LinkedRewardItem setLink, AbstractRelic relic)
    {
        super(relic);

        addRelicLink(setLink);
    }

    public void addRelicLink(LinkedRewardItem setLink)
    {
        if (!linkedRewards.contains(setLink)) {
            linkedRewards.add(setLink);
        }
        if (!setLink.linkedRewards.contains(this)) {
            setLink.linkedRewards.add(this);
        }
    }

    private boolean isFirst()
    {
        int thisIndexOf = AbstractDungeon.combatRewardScreen.rewards.indexOf(this);
        for (RewardItem link : linkedRewards) {
            if (AbstractDungeon.combatRewardScreen.rewards.indexOf(link) < thisIndexOf) {
                return claimed;
            }
        }
        return true;
    }

    @Override
    public boolean claimReward()
    {
        boolean claimedReward = super.claimReward();
        this.claimed = claimedReward || (this.type == RewardType.CARD && NO_PEEKING); //When you click on reward, if it's card or claimed successfully, remove others.
        if (this.claimed)
        {
            if (!this.ignoreReward)
            {
                for (RewardItem link : linkedRewards) {
                    link.isDone = true;
                    link.ignoreReward = true;
                    if (link.type == RewardType.CARD) //Card rewards aren't removed properly, so it's a relic now. IT'S A RELIC, OK.
                    {
                        link.type = RewardType.RELIC;
                        link.relic = new Circlet();
                        link.move(-1000.0f); //poof, it's gone.
                    }
                }
            }
        }
        return claimedReward;
    }

    @Override
    public void update()
    {
        super.update();

        if (isFirst()) {
            redText = false;
            for (RewardItem link : linkedRewards) {
                link.redText = false;
            }
        }

        for (RewardItem link : linkedRewards) {
            if (hb.hovered) {
                link.redText = true;
            }
            if (!AbstractDungeon.combatRewardScreen.rewards.contains(link) && !this.claimed) //the linked item has vanished but not because this got claimed
            {
                this.isDone = true;
                this.ignoreReward = true;
            }
        }
    }

    @Override
    public void render(SpriteBatch sb)
    {
        super.render(sb);

        if (!linkedRewards.isEmpty()) {
            if (hb.hovered) {
                // Make TipHelper think we haven't tried to render a tip this frame
                try {
                    Field f = TipHelper.class.getDeclaredField("renderedTipThisFrame");
                    f.setAccessible(true);
                    f.setBoolean(null, false);
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    e.printStackTrace();
                }

                ArrayList<PowerTip> tips = new ArrayList<>();

                if (relic != null) {
                    tips.addAll(relic.tips);
                }

                for (RewardItem link : linkedRewards) {
                    if (link.relic != null) {
                        tips.add(new PowerTip(TEXT[7], TEXT[8] + FontHelper.colorString(link.relic.name, "y") + TEXT[9]));
                    }
                    else if (link.type == RewardType.CARD)
                    {
                        tips.add(new PowerTip(TEXT[7], TEXT[8] + linkedUIStrings.TEXT[0] + TEXT[9]));
                    }
                }
                TipHelper.queuePowerTips(360.0f * Settings.scale, InputHelper.mY + 50.0f * Settings.scale, tips);
            }

            if (!isFirst()) {
                renderRelicLink(sb);
            }
        }

        hb.render(sb);
    }

    @SpireOverride
    protected void renderRelicLink(SpriteBatch sb)
    {
        SpireSuper.call(sb);
    }
}