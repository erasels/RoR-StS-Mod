package riskOfSpire.vfx.combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import riskOfSpire.actions.unique.relicEffects.WobblyMissileAction;

public class WobblyMissileEffect extends AbstractGameEffect {
    private static final float EFFECT_DUR = 1.0f;
    private float x;
    private float y;
    private float targetX;
    private float targetY;
    private float straightStartX;
    private float straightStartY;
    private float speed;
    private float initialRotation;
    private TextureAtlas.AtlasRegion img;
    private boolean soundPlayed = false;
    private WobblyMissileAction parentAction;
    private Color effectColor;

    public WobblyMissileEffect(float originX, float originY, float targetX, float targetY, Color effectColor) {
        this(originX, originY, targetX, targetY, null, effectColor);
    }

    public WobblyMissileEffect(float originX, float originY, float targetX, float targetY, WobblyMissileAction parentAction, Color effectColor, float speed) {
        this.img = ImageMaster.WOBBLY_LINE;
        this.rotation = MathUtils.random(180.0f) + 90.0f;
        this.initialRotation = this.rotation;
        this.x = originX - this.img.packedWidth / 2.0f;
        this.y = originY - this.img.packedHeight / 2.0f;
        this.targetX = targetX - this.img.packedWidth / 2.0f;
        this.targetY = targetY - this.img.packedHeight / 2.0f;
        this.duration = EFFECT_DUR;
        this.speed = speed * Settings.scale;
        this.parentAction = parentAction;
        this.effectColor = effectColor;
    }

    public WobblyMissileEffect(float originX, float originY, float targetX, float targetY, WobblyMissileAction parentAction, Color effectColor) {
        this(originX, originY, targetX, targetY, parentAction, effectColor, 1200.0f);
    }

        @Override
    public void update() {
        if (!this.soundPlayed) {
            CardCrawlGame.sound.playV("ATTACK_WHIFF_1", 2.0f);
            this.soundPlayed = true;
        }
        this.duration -= Gdx.graphics.getDeltaTime();
        Vector2 currentPosition = new Vector2(this.x, this.y);
        Vector2 targetPosition = new Vector2(targetX, targetY);
        float targetDirection = targetPosition.sub(currentPosition).angle();
        if (this.duration > 0.5f) {
            if (targetDirection - initialRotation > 180f) {
                this.rotation = Interpolation.circle.apply(this.initialRotation + 360f, targetDirection, 1.0f - ((this.duration - 0.5f) * 2.0f));
            } else {
                this.rotation = Interpolation.circle.apply(this.initialRotation, targetDirection, 1.0f - ((this.duration - 0.5f) * 2.0f));
            }
            Vector2 tmp = new Vector2(MathUtils.cosDeg(this.rotation), MathUtils.sinDeg(this.rotation));
            tmp.x *= this.speed * Gdx.graphics.getDeltaTime();
            tmp.y *= this.speed * Gdx.graphics.getDeltaTime();
            this.x += tmp.x;
            this.y += tmp.y;
            this.straightStartX = this.x;
            this.straightStartY = this.y;
            AbstractDungeon.effectsQueue.add(new WobblyMissileTrailEffect(this.x + this.img.packedWidth / 2.0f, this.y + this.img.packedHeight / 2.0f, effectColor.cpy()));
        } else if (this.duration < 0.0f) {
            this.isDone = true;
        } else {
            if (this.rotation != targetDirection) {
                this.rotation = targetDirection;
            }
            this.x = Interpolation.linear.apply(this.straightStartX, targetX, 1.0f - (this.duration) * 2);
            this.y = Interpolation.linear.apply(this.straightStartY, targetY, 1.0f - (this.duration) * 2);
            AbstractDungeon.effectsQueue.add(new WobblyMissileTrailEffect(this.x + this.img.packedWidth / 2.0f, this.y + this.img.packedHeight / 2.0f, effectColor.cpy()));
        }
        if (isDone && parentAction != null) {
            parentAction.doDamage = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(effectColor);
        sb.draw(this.img, this.x, this.y, this.img.packedWidth / 2.0f, this.img.packedHeight / 2.0f, this.img.packedWidth, this.img.packedHeight, 0.5f, 0.5f, this.rotation + 90f);
    }

    @Override
    public void dispose() {
    }
}