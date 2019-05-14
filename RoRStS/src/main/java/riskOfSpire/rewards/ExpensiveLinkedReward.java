package riskOfSpire.rewards;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.util.RiskOfRainRelicHelper;

public class ExpensiveLinkedReward extends LinkedRewardItem {
    private static final float GOLD_TEXT_X = 1135.0F * Settings.scale;
    private static final float GOLD_IMG_X = GOLD_TEXT_X - 66.0f * Settings.scale;
    private static final float GOLD_IMG_SIZE = (float) ImageMaster.UI_GOLD.getWidth() * Settings.scale;
    //This class is not meant to be used on Gold rewards. As such, base class gold variable will be used to store gold cost. because i'm lazy xd
    //Also using it for a card reward will probably not work well.
    private boolean canAfford = false;

    public ExpensiveLinkedReward(LinkedRewardItem setLink, AbstractRelic reward) {
        super(setLink, reward);
        float modifier = 0f; //Adjust this value
        if(RiskOfSpire.difficultyCostSetting) {
            modifier = RiskOfRainRelicHelper.RiskOfRainRelicRng.random(0.66F, 1.33F) + ((RiskOfSpire.DifficultyMeter.getDifficultyMod()*(RiskOfSpire.DifficultyMeter.getDifficulty()/100F))*1F); //Adjust this value
        } else {
            modifier = RiskOfRainRelicHelper.RiskOfRainRelicRng.random(0.66F, 1.33F);
        }
        this.goldAmt = MathUtils.round(((float) relic.getPrice() * modifier)/2F);
    }

    @Override
    public boolean claimReward() {
        if (this.ignoreReward) {
            return true;
        }
        if (AbstractDungeon.player.gold >= this.goldAmt) {
            if (super.claimReward()) {
                AbstractDungeon.player.loseGold(this.goldAmt);
                return true;
            }
            //This will have some problems if you tried to use this for a card reward, also.
        }
        return false;
    }

    @Override
    public void update() {
        super.update();
        canAfford = AbstractDungeon.player.gold >= this.goldAmt;
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);

        sb.setColor(Color.WHITE);
        sb.draw(ImageMaster.UI_GOLD, GOLD_IMG_X, this.y - 9.0F * Settings.scale, GOLD_IMG_SIZE, GOLD_IMG_SIZE);
        Color c = Color.WHITE.cpy();
        if (this.goldAmt > AbstractDungeon.player.gold) {
            c = Color.SALMON.cpy();
        }
        FontHelper.renderSmartText(sb, FontHelper.tipHeaderFont, Integer.toString(this.goldAmt), GOLD_TEXT_X, this.y + 30.0F * Settings.scale, 1000.0F * Settings.scale, 0.0F, c);
    }
}
