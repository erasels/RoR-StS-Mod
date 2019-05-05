package riskOfSpire.ui;

import basemod.TopPanelItem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.util.TextureLoader;

public class LunarCoinDisplay extends TopPanelItem {

    private static final float tipYpos = Settings.HEIGHT - (120.0f * Settings.scale);

    public static final String ID = RiskOfSpire.makeID("LunarCoinItem");

    private static final Texture ICON = TextureLoader.getTexture(RiskOfSpire.assetPath("images/ui/") + "LunarCoinIcon.png");
    public static UIStrings UIStrings = CardCrawlGame.languagePack.getUIString(ID);

    public LunarCoinDisplay() {
        super(ICON, ID);
    }

    @Override
    public boolean isClickable() {
        return false;
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont_N, Integer.toString(RiskOfSpire.lunarCoinAmount), this.x + (this.hb_w / 2), this.y + 16f * Settings.scale, Color.WHITE.cpy());

        if(this.getHitbox().hovered) {
            float xPos = this.x + (this.hb_w / 2);
            TipHelper.renderGenericTip(xPos, tipYpos, UIStrings.TEXT[0], UIStrings.TEXT[1]);
        }
    }

    @Override
    protected void onClick() {
        CardCrawlGame.sound.play("RELIC_DROP_MAGICAL");
    }
}