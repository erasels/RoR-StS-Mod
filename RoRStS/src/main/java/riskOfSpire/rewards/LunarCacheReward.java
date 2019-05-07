package riskOfSpire.rewards;

import basemod.abstracts.CustomReward;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.patches.RewardItemTypeEnumPatch;
import riskOfSpire.util.LunarCoinHelper;
import riskOfSpire.util.RiskOfRainRelicHelper;
import riskOfSpire.util.TextureLoader;

public class LunarCacheReward extends CustomReward {
    public static UIStrings UIStrings = CardCrawlGame.languagePack.getUIString(RiskOfSpire.makeID("LunarCacheReward"));
    private static final Texture ICON = TextureLoader.getTexture(RiskOfSpire.assetPath("images/ui/") + "LunarCoinIcon.png");
    private static final Texture CACHE_ICON = TextureLoader.getTexture(RiskOfSpire.assetPath("images/ui/") + "LunarCache_alt.png");
    private static final float LC_TEXT_X = 1135.0F * Settings.scale;
    private static final float LC_IMG_X = LC_TEXT_X - 66.0f * Settings.scale;
    private static final float LC_IMG_SIZE = (float) ICON.getWidth() * Settings.scale;

    private boolean canAfford = false;
    public LunarCacheReward() {
        this(0);
    }

    public LunarCacheReward(int lunarCoinCostModifier) {
        super(CACHE_ICON, UIStrings.TEXT[0], RewardItemTypeEnumPatch.LUNAR_CACHE);
        this.goldAmt = RiskOfRainRelicHelper.RiskOfRainRelicRng.random(LunarCoinHelper.MIN_COST, LunarCoinHelper.MAX_COST)+lunarCoinCostModifier;
        if(goldAmt<0) {
            goldAmt=0;
        }
    }

    @Override
    public boolean claimReward() {
        if (RiskOfSpire.lunarCoinAmount >= this.goldAmt) {
            LunarCoinHelper.manipLunarCoins(-goldAmt, true);
            //Add a new relic reward with a Lunar item
            RiskOfSpire.lCacheTrigger = true;
            return true;
        }
        return false;
    }

    @Override
    public void update() {
        super.update();
        canAfford = RiskOfSpire.lunarCoinAmount >= this.goldAmt;
        if (this.hb.hovered) {
            TipHelper.renderGenericTip(360.0F * Settings.scale, (float) InputHelper.mY, UIStrings.TEXT[0], UIStrings.TEXT[1]);
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);

        sb.setColor(Color.WHITE);
        sb.draw(ICON, LC_IMG_X+10, this.y - 2.0F * Settings.scale, LC_IMG_SIZE*0.66F, LC_IMG_SIZE*0.66F);
        Color c = Color.WHITE.cpy();
        if (this.goldAmt > RiskOfSpire.lunarCoinAmount) {
            c = Color.SALMON.cpy();
        }
        FontHelper.renderSmartText(sb, FontHelper.tipHeaderFont, Integer.toString(this.goldAmt), LC_TEXT_X, this.y + 30.0F * Settings.scale, 1000.0F * Settings.scale, 0.0F, c);
    }
}
