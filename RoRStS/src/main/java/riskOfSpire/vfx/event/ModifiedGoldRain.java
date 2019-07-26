package riskOfSpire.vfx.event;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.ShineLinesEffect;

public class ModifiedGoldRain extends AbstractGameEffect {
    private static final float RAW_IMG_WIDTH = 32.0F;
    private static final float IMG_WIDTH = RAW_IMG_WIDTH * Settings.scale;
    private TextureAtlas.AtlasRegion img;
    private boolean isPickupable = false;
    public boolean pickedup = false;
    private float x;
    private float y;
    private float landingY;
    private boolean willBounce = false;
    private boolean hasBounced = true;
    private float bounceY;
    private float bounceX;
    private float vY = -0.2F, vX = 0.0F;
    private float gravity = -0.3F;
    private Hitbox hitbox;

    public ModifiedGoldRain() {
        if (MathUtils.randomBoolean()) {
            img = ImageMaster.COPPER_COIN_1;
        } else {
            img = ImageMaster.COPPER_COIN_2;
        }
        willBounce = (MathUtils.random(3) != 0);
        if (willBounce) {
            hasBounced = false;
            bounceY = MathUtils.random(1.0F, 4.0F);
            bounceX = MathUtils.random(-3.0F, 3.0F);
        }
        y = Settings.HEIGHT * MathUtils.random(1.1F, 1.3F) - img.packedHeight / 2.0F;
        x = MathUtils.random(Settings.WIDTH * 0.3F, Settings.WIDTH * 0.95F) - img.packedWidth / 2.0F;
        landingY = MathUtils.random(AbstractDungeon.floorY - Settings.HEIGHT * 0.05F, AbstractDungeon.floorY + Settings.HEIGHT * 0.08F);
        rotation = MathUtils.random(360.0F);
        scale = Settings.scale;
    }

    public void update() {
        if (!isPickupable) {
            x += vX * Gdx.graphics.getDeltaTime() * 60.0F;
            y += vY * Gdx.graphics.getDeltaTime() * 60.0F;
            vY += gravity;
            if (y < landingY)
                if (hasBounced) {
                    y = landingY;
                    isPickupable = true;
                    hitbox = new Hitbox(x - IMG_WIDTH * 2.0F, y - IMG_WIDTH * 2.0F, IMG_WIDTH * 4.0F, IMG_WIDTH * 4.0F);
                } else {
                    if (MathUtils.random(1) == 0)
                        hasBounced = true;
                    y = landingY;
                    vY = bounceY;
                    vX = bounceX;
                    bounceY *= 0.5F;
                    bounceX *= 0.3F;
                }
        } else if (!pickedup) {
            pickedup = true;
            isDone = true;
            playGainGoldSFX();
            AbstractDungeon.effectsQueue.add(new ShineLinesEffect(x, y));
        }
    }

    private void playGainGoldSFX() {
        int roll = MathUtils.random(4);
        switch (roll) {
            case 0:
                CardCrawlGame.sound.play("GOLD_GAIN", 0.1F);
                return;
            case 1:
                CardCrawlGame.sound.play("GOLD_GAIN_3", 0.1F);
                return;
            case 2:
                CardCrawlGame.sound.play("BLOOD_SPLAT", 0.1F);
                return;
            case 3:
                CardCrawlGame.sound.play("BLOOD_SWISH", 0.1F);
                return;
        }
        CardCrawlGame.sound.play("GOLD_GAIN_5", 0.1F);
    }

    public void render(SpriteBatch sb) {
        sb.setColor(Color.FIREBRICK);
        sb.draw(img, x, y, img.packedWidth / 2.0F, img.packedHeight / 2.0F, img.packedWidth, img.packedHeight, scale, scale, rotation);
        if (hitbox != null)
            hitbox.render(sb);
    }

    public void dispose() {
    }
}
