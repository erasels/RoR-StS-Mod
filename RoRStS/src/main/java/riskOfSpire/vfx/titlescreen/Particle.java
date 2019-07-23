package riskOfSpire.vfx.titlescreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import riskOfSpire.util.TextureLoader;

public class Particle extends AbstractGameEffect {
    private Texture img;
    private float posx;
    private float posy;
    private float size;
    private float speed;
    private float angle;

    public Particle() {
        int roll = MathUtils.random(2);
        switch (roll) {
            case 0:
                this.img = TextureLoader.getTexture("riskOfSpireResources/images/vfx/Particle1.png");
                break;
            case 1:
                this.img = TextureLoader.getTexture("riskOfSpireResources/images/vfx/Particle2.png");
                break;
            default:
                this.img = TextureLoader.getTexture("riskOfSpireResources/images/vfx/Particle3.png");
        }
        //this.img = TextureLoader.getTexture("riskOfSpireResources/images/vfx/Particle1.png");
        this.posx = MathUtils.random(0, Settings.WIDTH + Settings.HEIGHT * 3) - Settings.HEIGHT * 3;
        this.posy = Settings.HEIGHT;
        this.size = MathUtils.random(10, 25);
        this.speed = MathUtils.random(0.8F, 1.2F);
        this.angle = MathUtils.random(0.8F, 1.2F);
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(img, posx, posy, size * Settings.scale, size * Settings.scale);
    }

    public void update() {
        if (posx > Settings.WIDTH) {
            this.isDone = true;
        }
        if (posy < 0.0F - 20.0F * Settings.scale) {
            this.isDone = true;
        }
        this.posy -= Gdx.graphics.getDeltaTime() * Settings.scale * 50F * speed * angle;
        this.posx += Gdx.graphics.getDeltaTime() * Settings.scale * 150F * speed / angle;
    }

    public void dispose() {
    }
}
