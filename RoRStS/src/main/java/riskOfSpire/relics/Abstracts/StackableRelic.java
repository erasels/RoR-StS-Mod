package riskOfSpire.relics.Abstracts;

import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.patches.RelicOffsetXPatch;

public abstract class StackableRelic extends AbstractRelic implements CustomSavable<Integer> {
    private static final int START_CHARGE = 1;
    public int relicStack = 1;

    public StackableRelic(String setId, String imgName, RelicTier tier, LandingSound sfx) {
        super(setId, "", tier, sfx);

        imgUrl = imgName;

        if (img == null || outlineImg == null) {
            img = ImageMaster.loadImage(RiskOfSpire.assetPath("images/relics/" + imgName));
            largeImg = ImageMaster.loadImage(RiskOfSpire.assetPath("images/largeRelics/" + imgName));
            outlineImg = ImageMaster.loadImage(RiskOfSpire.assetPath("images/relics/outline/" + imgName));
        }
    }

    //For mods that use CustomRelics
    public StackableRelic(String setId, Texture img, Texture outline, RelicTier tier, LandingSound sfx) {
        super(setId, "", tier, sfx);
        img.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        outline.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        this.img = img;
        largeImg = img;
        outlineImg = outline;
    }

    //TODO: Implement dynamic and easy description changing

    public void onRelicGet(AbstractRelic r) {
    }

    private void startingCharges()
    {
        setCounter(START_CHARGE);
    }

    private void manipCharge(int amt) {
        setCounter(counter + amt);
    }

    @Override
    public void renderCounter(SpriteBatch sb, boolean inTopPanel) {
        if (this.counter > -1) {
            if (inTopPanel) {
                FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelInfoFont, Integer.toString(this.counter), RelicOffsetXPatch.offsetX + this.currentX + 30.0F * Settings.scale, this.currentY - 7.0F * Settings.scale, Color.WHITE);
                if (this.relicStack > 0) { //Could also do if >1 but ror always shows amount so whatever
                    FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelInfoFont, Integer.toString(this.relicStack), RelicOffsetXPatch.offsetX + this.currentX + 30.0F * Settings.scale, this.currentY + 7.0F * Settings.scale, Color.WHITE);
                }
            } else {
                FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelInfoFont, Integer.toString(this.counter), this.currentX + 30.0F * Settings.scale, this.currentY - 7.0F * Settings.scale, Color.WHITE);
                if (this.relicStack > 0) {
                    FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelInfoFont, Integer.toString(this.relicStack), this.currentX + 30.0F * Settings.scale, this.currentY + 7.0F * Settings.scale, Color.WHITE);
                }
            }
        }
    }
}