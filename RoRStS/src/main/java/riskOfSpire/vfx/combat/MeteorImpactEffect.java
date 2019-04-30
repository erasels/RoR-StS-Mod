package riskOfSpire.vfx.combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.DarkOrbActivateEffect;

public class MeteorImpactEffect extends AbstractGameEffect {
    private float waitTimer;
    private float x;
    private float y;
    private float vX;
    private float vY;
    private float floorY;
    private Texture img;
    private int salvoCount;

    private static Texture img1;
    private static Texture img2;
    private static Texture img3;

    public MeteorImpactEffect(int salvoCount, boolean flipped) {
        if (img1 == null || img2 == null || img3 == null)
        {
            img1 = ImageMaster.loadImage("images/orbs/d1.png");
            img2 = ImageMaster.loadImage("images/orbs/d2.png");
            img3 = ImageMaster.loadImage("images/orbs/d3.png");
        }

        this.salvoCount = salvoCount;
        switch(MathUtils.random(2)) {
            case 0:
                this.img = img1;
                break;
            case 1:
                this.img = img2;
                break;
            default:
                this.img = img3;
        }

        this.waitTimer = MathUtils.random(0.0F, 0.5F);
        if (flipped) {
            this.x = MathUtils.random(420.0F, 1420.0F) * Settings.scale - 48.0F;
            this.vX = MathUtils.random(-600.0F, -900.0F);
            this.vX += (float)salvoCount * 5.0F;
        } else {
            this.x = MathUtils.random(500.0F, 1500.0F) * Settings.scale - 48.0F;
            this.vX = MathUtils.random(600.0F, 900.0F);
            this.vX -= (float)salvoCount * 5.0F;
        }

        this.y = (float)Settings.HEIGHT + MathUtils.random(100.0F, 300.0F) - 48.0F;
        this.vY = MathUtils.random(2500.0F, 4000.0F);
        this.vY -= (float)salvoCount * 10.0F;
        this.vY *= Settings.scale;
        this.vX *= Settings.scale;
        this.duration = 2.0F;
        this.scale = MathUtils.random(1.0F, 1.5F);
        this.scale += (float)salvoCount * 0.04F;
        this.vX *= this.scale;
        this.scale *= Settings.scale;
        this.color = new Color(0.9F, 0.9F, 1.0F, MathUtils.random(0.9F, 1.0F));
        Vector2 derp = new Vector2(this.vX, this.vY);
        if (flipped) {
            this.rotation = derp.angle() + 225.0F - (float)salvoCount / 3.0F;
        } else {
            this.rotation = derp.angle() - 45.0F + (float)salvoCount / 3.0F;
        }

        this.renderBehind = MathUtils.randomBoolean();
        this.floorY = AbstractDungeon.floorY + MathUtils.random(-200.0F, 50.0F) * Settings.scale;
    }

    public void update() {
        this.waitTimer -= Gdx.graphics.getDeltaTime();
        if (this.waitTimer <= 0.0F) {
            this.x += this.vX * Gdx.graphics.getDeltaTime();
            this.y -= this.vY * Gdx.graphics.getDeltaTime();
            if (this.y < this.floorY) {
                float pitch = 0.8F;
                pitch -= (float)this.salvoCount * 0.025F;
                pitch += MathUtils.random(-0.2F, 0.2F);
                CardCrawlGame.sound.playA("ORB_DARK_EVOKE", pitch);

                for(int i = 0; i < 4; ++i) {
                    AbstractDungeon.effectsQueue.add(new DarkOrbActivateEffect(this.x, this.y));
                }

                this.isDone = true;
            }

        }
    }

    public void render(SpriteBatch sb) {
        if (this.waitTimer < 0.0F) {
            sb.setBlendFunction(770, 1);
            sb.setColor(this.color);
            sb.draw(this.img, this.x, this.y, 48.0F, 48.0F, 96.0F, 96.0F, this.scale, this.scale, this.rotation, 0, 0, 96, 96, false, false);
            sb.setBlendFunction(770, 771);
        }

    }

    public void dispose() {
    }
}