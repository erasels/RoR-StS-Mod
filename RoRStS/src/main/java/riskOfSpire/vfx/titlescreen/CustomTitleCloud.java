package riskOfSpire.vfx.titlescreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import riskOfSpire.util.TextureLoader;

import java.util.Random;

public class CustomTitleCloud {
    private Texture IMG = TextureLoader.getTexture("riskOfSpireResources/images/vfx/CloudVfx1.png");
    private float posy;
    private float posx;
    private float rotation;

    public CustomTitleCloud(float StartingPos) {
        Random r = new Random();
        this.posx = StartingPos;
        this.posy = Settings.HEIGHT / 2 + r.nextFloat() * 100.0F * Settings.scale - 250.0F * Settings.scale;
        this.rotation = r.nextFloat() * 360.0F * Settings.scale;
    }

    public void update() {
        if (this.posx > Settings.WIDTH) {
            Random r = new Random();
            this.posx = 0 - IMG.getWidth() * Settings.scale;
            this.posy = Settings.HEIGHT / 2 + r.nextFloat() * 100.0F * Settings.scale - 250.0F * Settings.scale;
            this.rotation = r.nextFloat() * 360.0F * Settings.scale;
        }
        this.posx += Gdx.graphics.getDeltaTime() * Settings.scale * 450F;
    }

    public void render(SpriteBatch sb) {
        sb.draw(IMG, posx, posy, 400.0F * Settings.scale / 2, 400.0F * Settings.scale / 2, 400.0F * Settings.scale, 400.0F * Settings.scale, 1, 1, rotation, 0, 0, 400, 400, false, false);
    }
}
