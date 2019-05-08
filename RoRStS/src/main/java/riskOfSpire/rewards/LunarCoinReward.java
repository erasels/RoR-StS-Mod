package riskOfSpire.rewards;

import basemod.abstracts.CustomReward;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.patches.RewardItemTypeEnumPatch;
import riskOfSpire.util.LunarCoinHelper;
import riskOfSpire.util.TextureLoader;


public class LunarCoinReward extends CustomReward {
    private static final Texture ICON = TextureLoader.getTexture(RiskOfSpire.assetPath("images/ui/") + "LunarCoinReward.png");
    public static UIStrings UIStrings = CardCrawlGame.languagePack.getUIString(RiskOfSpire.makeID("LunarCoinReward"));

    public int amountOfCoins;

    public LunarCoinReward(){
        this(1);
    }

    public LunarCoinReward(int amount) {
        super(ICON, amount + amount == 1 ? UIStrings.TEXT[1]:UIStrings.TEXT[0], RewardItemTypeEnumPatch.LUNAR_COIN);
        this.amountOfCoins = amount;
    }

    @Override
    public boolean claimReward() {
        LunarCoinHelper.manipLunarCoins(amountOfCoins, true);
        return true;
    }
}