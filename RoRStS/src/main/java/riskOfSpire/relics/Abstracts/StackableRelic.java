package riskOfSpire.relics.Abstracts;

import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.patches.RelicOffsetXPatch;

public abstract class StackableRelic extends AbstractRelic implements CustomSavable<Integer> {
    private static final int START_CHARGE = 1;
    public int relicStack = START_CHARGE;

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

    //TODO: Write own Patch taht prevents the getting of new relics (Will still trigger onEquip, kill me)
    @Override
    public void instantObtain(AbstractPlayer p, int slot, boolean callOnEquip) {
        if(p.hasRelic(this.relicId)) {
            ((StackableRelic)p.getRelic(this.relicId)).onStack();
            this.isDone = true;
            this.isObtained = true;
        } else {
            super.instantObtain(p, slot, callOnEquip);
        }
    }

    @Override
    public void instantObtain() {
        if(AbstractDungeon.player.hasRelic(this.relicId)) {
            ((StackableRelic)AbstractDungeon.player.getRelic(this.relicId)).onStack();
            this.isDone = true;
            this.isObtained = true;
        } else {
            super.instantObtain();
        }
    }

    @Override
    public void obtain() {
        if(AbstractDungeon.player.hasRelic(this.relicId)) {
            ((StackableRelic)AbstractDungeon.player.getRelic(this.relicId)).onStack();
            this.isDone = true;
            this.isObtained = true;
        } else {
            super.obtain();
        }
    }

    public void onStack() {
        this.relicStack++;
    }

    public void onRelicGet(AbstractRelic r) { }

    @Override
    public void renderCounter(SpriteBatch sb, boolean inTopPanel) {
        if (this.counter > -1) {
            if (inTopPanel) {
                FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelInfoFont, Integer.toString(this.counter), RelicOffsetXPatch.offsetX + this.currentX + 30.0F * Settings.scale, this.currentY - 7.0F * Settings.scale, Color.WHITE);
                if (this.relicStack > 0) { //Could also do if >1 but ror always shows amount so whatever
                    FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelInfoFont, Integer.toString(this.relicStack), RelicOffsetXPatch.offsetX + this.currentX + 30.0F * Settings.scale, this.currentY + 28.0F * Settings.scale, Color.WHITE);
                }
            } else {
                FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelInfoFont, Integer.toString(this.counter), this.currentX + 30.0F * Settings.scale, this.currentY - 7.0F * Settings.scale, Color.WHITE);
                if (this.relicStack > 0) {
                    FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelInfoFont, Integer.toString(this.relicStack), this.currentX + 30.0F * Settings.scale, this.currentY + 28.0F * Settings.scale, Color.WHITE);
                }
            }
        }
    }

    @Override
    public Integer onSave() {
        return relicStack;
    }

    @Override
    public void onLoad(Integer integer) {
        if (integer != null) {
            relicStack = integer;
        } else {
            relicStack = START_CHARGE;
        }
    }
}