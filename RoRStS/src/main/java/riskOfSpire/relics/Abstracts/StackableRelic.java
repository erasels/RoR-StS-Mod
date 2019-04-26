package riskOfSpire.relics.Abstracts;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import riskOfSpire.RiskOfSpire;

public abstract class StackableRelic extends AbstractRelic {
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

    public void onRelicGet(AbstractRelic r) {
    }
}