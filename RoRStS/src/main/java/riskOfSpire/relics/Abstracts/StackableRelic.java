package riskOfSpire.relics.Abstracts;

import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.patches.RelicOffsetXPatch;

import java.lang.reflect.Type;
import java.util.ArrayList;

public abstract class StackableRelic extends BaseRelic implements CustomSavable<Integer> {
    private static final int START_CHARGE = 1;
    public int relicStack = START_CHARGE;

    public static final float STACK_FONT_SIZE = 20;
    public static BitmapFont STACK_FONT;

    public StackableRelic(String setId, String imgName, RelicTier tier, LandingSound sfx) {
        super(setId, "", tier, sfx);

        imgUrl = imgName;

        if (img == null || outlineImg == null) {
            img = ImageMaster.loadImage(RiskOfSpire.assetPath("images/relics/" + imgName));
            largeImg = ImageMaster.loadImage(RiskOfSpire.assetPath("images/relicsBig/" + imgName));
            outlineImg = ImageMaster.loadImage(RiskOfSpire.assetPath("images/relics/outline/" + imgName));
        }

        updateDescriptionOnStack(false); //Description is constructed in super before stack value is set to 1, so values will be incorrect if this is not done.
    }

    @Override
    public void loadLargeImg()
    {
        if (this.largeImg == null) {
            largeImg = ImageMaster.loadImage(RiskOfSpire.assetPath("images/relicsBig/" + imgUrl));
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

        updateDescriptionOnStack(false); //Description is constructed in super before stack value is set to 1, so values will be incorrect if this is not done.
    }

    @Override
    public void instantObtain(AbstractPlayer p, int slot, boolean callOnEquip) {
        if (p.hasRelic(this.relicId)) {
            ((StackableRelic) p.getRelic(this.relicId)).onStack();
            this.isDone = true;
            this.isObtained = true;
            if (AbstractDungeon.getCurrRoom() != null) //just in case.
                AbstractDungeon.getCurrRoom().relics.remove(this);
        } else {
            super.instantObtain(p, slot, callOnEquip);
            updateDescriptionOnStack(false);
        }
    }

    @Override
    public void instantObtain() {
        if (AbstractDungeon.player.hasRelic(this.relicId)) {
            ((StackableRelic) AbstractDungeon.player.getRelic(this.relicId)).onStack();
            this.isDone = true;
            this.isObtained = true;
            if (AbstractDungeon.getCurrRoom() != null) //just in case.
                AbstractDungeon.getCurrRoom().relics.remove(this);
        } else {
            super.instantObtain();
            updateDescriptionOnStack(false);
        }
    }

    @Override
    public void obtain() {
        if (AbstractDungeon.player.hasRelic(this.relicId)) {
            ((StackableRelic) AbstractDungeon.player.getRelic(this.relicId)).onStack();
            this.isDone = true;
            this.isObtained = true;
            if (AbstractDungeon.getCurrRoom() != null) //just in case.
                AbstractDungeon.getCurrRoom().relics.remove(this);
        } else {
            super.obtain();
            updateDescriptionOnStack(false);
        }
    }

    public void onStack() {
        this.relicStack++;
        updateDescriptionOnStack(true);
        notifyRelicGet();
    }

    protected void updateDescriptionOnStack(boolean flash) {
        this.description = this.getUpdatedDescription();
        ArrayList<PowerTip> tmp = new ArrayList<>();
        this.tips.forEach(pT -> {
            if (!pT.header.equals(this.name) && !pT.header.startsWith(RiskOfSpire.makeID("@RECOLOR@"))) tmp.add(pT);
        });
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.tips.addAll(tmp);
        this.initializeTips();

        if (flash)
            flash();
    }

    public void onUnstack() {
        this.relicStack--;
        if(relicStack < 1) {
            AbstractDungeon.player.loseRelic(this.relicId);
        } else {
            updateDescriptionOnStack(true);
        }
    }

    @Override
    protected void initializeTips() {
        super.initializeTips();

        if (tips.size() > 0 && tips.get(0).header.toLowerCase().equals(name.toLowerCase())) {
            tips.get(0).header = RiskOfSpire.makeID("@RECOLOR@") + getColorChar() + tips.get(0).header;
        }
    }

    public char getColorChar()
    {
        switch (this.tier)
        {
            case COMMON:
                return 'w';
            case RARE:
            case BOSS:
                return 'r';
            case SPECIAL:
                return 'b';
            case UNCOMMON:
                return 'g';
            default:
                return 'y';
        }
    }

    @Override
    public int getPrice() {
        switch(this.tier) {
            case COMMON:
                return 50;
            case UNCOMMON:
                return 100;
            case RARE:
            case SHOP:
                return 200;
            case SPECIAL:
                return 400;
            case BOSS:
            default:
                return 999;
        }
    }

    @Override
    public void renderCounter(SpriteBatch sb, boolean inTopPanel) {
        if (inTopPanel) {
            if (counter > -1) {
                FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelInfoFont, Integer.toString(this.counter), RelicOffsetXPatch.offsetX + this.currentX + 30.0F * Settings.scale, this.currentY - 7.0F * Settings.scale, Color.WHITE);
            }
            if (this.relicStack > 0) { //Could also do if >1 but ror always shows amount so whatever
                FontHelper.renderFontRightTopAligned(sb, STACK_FONT, "x" + this.relicStack, RelicOffsetXPatch.offsetX + this.currentX + 30.0F * Settings.scale, this.currentY + 28.0F * Settings.scale, Color.WHITE);
            }
        } else {
            if (counter > -1) {
                FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelInfoFont, Integer.toString(this.counter), this.currentX + 30.0F * Settings.scale, this.currentY - 7.0F * Settings.scale, Color.WHITE);
            }
            if (this.relicStack > 0) {
                FontHelper.renderFontRightTopAligned(sb, STACK_FONT, "x" + this.relicStack, this.currentX + 30.0F * Settings.scale, this.currentY + 28.0F * Settings.scale, Color.WHITE);
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
        updateDescriptionOnStack(false);
    }

    @Override
    public Type savedType() {
        return Integer.TYPE;
    }
}