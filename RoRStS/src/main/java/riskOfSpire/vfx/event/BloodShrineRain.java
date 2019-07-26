package riskOfSpire.vfx.event;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.TouchPickupGold;

public class BloodShrineRain extends AbstractGameEffect {
    private int amount;
    private int min;
    private int max;
    private float staggerTimer;

    public BloodShrineRain(int amount) {
        this.staggerTimer = 0.0F;
        this.amount = amount;
        if (amount < 100) {
            this.min = 1;
            this.max = 7;
        } else {
            this.min = 3;
            this.max = 18;
        }
    }

    public void update() {
        this.staggerTimer -= Gdx.graphics.getDeltaTime();
        if (this.staggerTimer < 0.0F) {
            int goldToSpawn = MathUtils.random(this.min, this.max);
            if (goldToSpawn <= this.amount) {
                this.amount -= goldToSpawn;
            } else {
                goldToSpawn = this.amount;
                this.isDone = true;
            }
            for (int i = 0; i < goldToSpawn; i++)
                AbstractDungeon.effectsQueue.add(new ModifiedGoldRain());
            this.staggerTimer = MathUtils.random(0.3F);
        }
    }

    public void render(SpriteBatch sb) {
    }

    public void dispose() {
    }
}
