package riskOfSpire.vfx.combat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;

public class MeteorRainEffect extends AbstractGameEffect {
    private int salvoCount;
    private boolean flipped = false;

    public MeteorRainEffect(int salvoCount, boolean flipped) {
        this.salvoCount = 5 + salvoCount;
        this.flipped = flipped;
        if (this.salvoCount > 50) {
            this.salvoCount = 50;
        }

    }

    public void update() {
        CardCrawlGame.sound.playA("ORB_FROST_CHANNEL", -0.25F - (float)this.salvoCount / 200.0F);
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.LONG, true);
        AbstractDungeon.effectsQueue.add(new BorderLongFlashEffect(Color.ORANGE.cpy()));

        for(int i = 0; i < this.salvoCount; ++i) {
            AbstractDungeon.effectsQueue.add(new MeteorImpactEffect(this.salvoCount, this.flipped));
        }

        this.isDone = true;
    }

    public void render(SpriteBatch sb) {
    }

    public void dispose() {
    }
}