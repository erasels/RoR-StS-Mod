package riskOfSpire.vfx.combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class BubbleEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private TextureAtlas.AtlasRegion img;
    private String soundKey;

    public BubbleEffect(Color c, String soundKey) {
        if (this.img == null) {
            this.img = ImageMaster.CRYSTAL_IMPACT;
        }
        this.x = (AbstractDungeon.player.hb.cX - this.img.packedWidth / 2.0F);
        this.y = (AbstractDungeon.player.hb.cY - this.img.packedHeight / 2.0F);

        this.startingDuration = 1.5F;
        this.duration = this.startingDuration;
        this.scale = Settings.scale;
        this.color = c.cpy();
        this.renderBehind = true;
        this.soundKey = soundKey;
    }

    public void update() {
        if (this.duration == this.startingDuration) {
            CardCrawlGame.sound.play(soundKey, 0f);
        }
        this.duration -= Gdx.graphics.getDeltaTime();
        this.scale = (Interpolation.elasticIn.apply(4.0F, 0.01F, this.duration / this.startingDuration) * Settings.scale);
        if (this.duration < 0.0F) {
            this.isDone = true;
        }
    }

    public void render(SpriteBatch sb) {
        sb.setColor(color);
        sb.setBlendFunction(770, 1);
        sb.draw(this.img, this.x, this.y, this.img.packedWidth / 2.0F, this.img.packedHeight / 2.0F, this.img.packedWidth, this.img.packedHeight, this.scale * 1.15F, this.scale * 1.15F, 0.0F);
        sb.draw(this.img, this.x, this.y, this.img.packedWidth / 2.0F, this.img.packedHeight / 2.0F, this.img.packedWidth, this.img.packedHeight, this.scale, this.scale, 0.0F);
        sb.draw(this.img, this.x, this.y, this.img.packedWidth / 2.0F, this.img.packedHeight / 2.0F, this.img.packedWidth, this.img.packedHeight, this.scale * 0.85F, this.scale * 0.85F, 0.0F);
        sb.draw(this.img, this.x, this.y, this.img.packedWidth / 2.0F, this.img.packedHeight / 2.0F, this.img.packedWidth, this.img.packedHeight, this.scale * 0.7F, this.scale * 0.7F, 0.0F);

        sb.setBlendFunction(770, 771);
    }

    public void dispose() {
    }
}
