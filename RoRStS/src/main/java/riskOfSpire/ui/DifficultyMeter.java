package riskOfSpire.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import riskOfSpire.util.TextureLoader;

public class DifficultyMeter {

    public static Texture DifficultyMeter = TextureLoader.getTexture("riskOfSpireResources/images/ui/DifficultyMeter.png");
    public static Texture DifficultyFrame = TextureLoader.getTexture("riskOfSpireResources/images/ui/DifficultyFrame.png");
    private int Difficulty = 0;
    private float TimePassed = 0.0F;
    private boolean HasDifficultyChanged = false;

    public void tick() {
        TimePassed += Gdx.graphics.getDeltaTime();
        if (TimePassed >= 1.0F) // <- Will be a lot slower when finished, just that fast for debugging purposes
        {
            TimePassed = 0;
            Difficulty++;
            HasDifficultyChanged = false;
        }
    }

    public int getDifficulty() {
        //TODO: Save/Reset Difficulty
        return Difficulty;
    }

    public void setDifficulty(int D) {
        //TODO: Save/Reset Difficulty
        Difficulty = D;
    }

    public void render(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        sb.draw(DifficultyMeter, 32 * Settings.scale, 700 * Settings.scale, 400 * Settings.scale, 44 * Settings.scale);
        if (HasDifficultyChanged) {
            HasDifficultyChanged = false;
        } else {
            if (Difficulty <= 356) {
                sb.draw(DifficultyFrame, (30 + Difficulty) * Settings.scale, 700 * Settings.scale, 44 * Settings.scale, 44 * Settings.scale);
            } else {
                sb.draw(DifficultyFrame, (30 + 356) * Settings.scale, 700 * Settings.scale, 44 * Settings.scale, 44 * Settings.scale);
            }
        }
    }


    public void onBattleStart(AbstractMonster m) {
        m.increaseMaxHp(m.maxHealth * this.Difficulty / 200 * (int) Math.floor(AbstractDungeon.miscRng.random(0.8F, 1.2F)), false);
    }
}
