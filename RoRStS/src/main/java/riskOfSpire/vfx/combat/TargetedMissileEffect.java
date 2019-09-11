package riskOfSpire.vfx.combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class TargetedMissileEffect extends AbstractGameEffect {
    private static final float FREE_TIME = 0.4f;

    private static final float LOCK_ON_TIME = 0.4f;
    private static final float FAST_LOCK_ON_TIME = 0.2f;

    private TextureAtlas.AtlasRegion img;

    private float x;
    private float y;
    private float sX;
    private float sY;
    private float vX;
    private float vY;
    private float aX;
    private float aY;
    private float tX;
    private float tY;

    public AbstractCreature target;
    public DamageInfo dmg;

    private float freeDuration;
    private boolean lockedOn;
    private boolean finalApproach;

    public TargetedMissileEffect(float x, float y, AbstractCreature target, DamageInfo info, Color c)
    {
        this.img = ImageMaster.DAGGER_STREAK;

        //start position
        this.x = x;
        this.y = y;

        float mult = Settings.FAST_MODE ? 2.0f : 1.0f;

        this.vX = MathUtils.random(0 * Settings.scale, 300 * Settings.scale) * (MathUtils.randomBoolean() ? 1 : -1) * mult;
        this.vY = MathUtils.random(300 * Settings.scale, 600 * Settings.scale) * mult;

        this.aX = vX * MathUtils.random(0.2f, 0.8f);
        this.aY = vY * MathUtils.random(0.2f, 0.8f);

        freeDuration = FREE_TIME / mult;
        duration = freeDuration;
        lockedOn = false;
        finalApproach = false;

        this.target = target;
        this.dmg = info;

        this.color = c;

        this.scale = 0.2f;
    }

    @Override
    public void update() {
        if (target.isDeadOrEscaped())
        {
            if (!AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead() && duration > 0.0f)
                target = AbstractDungeon.getRandomMonster();
            else
                target = null;

            if (target == null)
            {
                AbstractDungeon.effectsQueue.add(new FlashAtkImgEffect(x, y, AbstractGameAction.AttackEffect.FIRE, false));

                this.isDone = true;
                return;
            }
            else //new target.
            {
                if (lockedOn)
                {
                    this.sX = this.x;
                    this.sY = this.y;
                    this.tX = target.hb.cX;
                    this.tY = this.y + Math.abs(tX - sX) * MathUtils.random(0.4f, 0.5f);

                    this.startingDuration = this.duration;
                    finalApproach = false;
                }
            }
        }

        if (lockedOn)
        {
            float lastX = x;
            float lastY = y;
            if (this.duration > 0)
            {
                this.x = Interpolation.pow3Out.apply(tX, sX, this.duration / this.startingDuration);
                if (this.duration > startingDuration / 2.0f)
                {
                    this.y = Interpolation.linear.apply(tY, sY, this.duration / this.startingDuration);
                }
                else
                {
                    if (!finalApproach)
                    {
                        finalApproach = true;
                        this.sY = y;
                        this.tY = target.hb.cY;
                    }

                    this.y = Interpolation.pow3Out.apply(tY, sY, this.duration / (this.startingDuration / 2.0f));
                }
                this.duration -= Gdx.graphics.getDeltaTime();
            }
            else if (!this.isDone)
            {
                this.x = tX;
                this.y = tY;
                this.isDone = true;

                AbstractDungeon.effectsQueue.add(new FlashAtkImgEffect(tX, tY, AbstractGameAction.AttackEffect.FIRE, false));
                this.target.tint.color = Color.RED.cpy();
                this.target.tint.changeColor(Color.WHITE.cpy());
            }

            this.rotation = MathUtils.atan2(y - lastY, x - lastX) * 180.0f / MathUtils.PI;
        }

        if (freeDuration > 0)
        {
            this.x += vX * Gdx.graphics.getDeltaTime();
            this.y += vY * Gdx.graphics.getDeltaTime();

            this.rotation = MathUtils.atan2(vY, vX) * 180.0f / MathUtils.PI;

            this.vX += aX * Gdx.graphics.getDeltaTime();
            this.vY += aY * Gdx.graphics.getDeltaTime();

            freeDuration -= Gdx.graphics.getDeltaTime();
        }
        else if (!lockedOn)
        {
            lockedOn = true;
            this.sX = this.x;
            this.sY = this.y;
            this.tX = target.hb.cX;
            this.tY = this.y + Math.abs(tX - sX) * MathUtils.random(0.4f, 0.5f);

            this.duration = this.startingDuration = Settings.FAST_MODE ? FAST_LOCK_ON_TIME : LOCK_ON_TIME;
        }
        /*if (!this.isDone)
        {
            AbstractDungeon.effectsQueue.add(new MissileTrailEffect(this.x, this.y, , this.color));
        }*/
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.draw(this.img, this.x - this.img.packedWidth / 2.0f, this.y - this.img.packedHeight / 2.0f, this.img.packedWidth / 2.0F, this.img.packedHeight / 2.0F, this.img.packedWidth, this.img.packedHeight, this.scale, this.scale * 3F, this.rotation);

        sb.setBlendFunction(770, 1);
        sb.draw(this.img, this.x - this.img.packedWidth / 2.0f, this.y - this.img.packedHeight / 2.0f, this.img.packedWidth / 2.0F, this.img.packedHeight / 2.0F, this.img.packedWidth, this.img.packedHeight, this.scale * 0.75F, this.scale * 1.5F, this.rotation);

        sb.setBlendFunction(770, 771);
    }

    @Override
    public void dispose() {

    }
}
