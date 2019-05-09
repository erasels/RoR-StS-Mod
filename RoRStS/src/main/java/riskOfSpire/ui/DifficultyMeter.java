package riskOfSpire.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import riskOfSpire.util.TextureLoader;

public class DifficultyMeter {

    public static Texture DifficultyMeter = TextureLoader.getTexture("riskOfSpireResources/images/ui/DifficultyMeter.png");
    public static Texture DifficultyFrame = TextureLoader.getTexture("riskOfSpireResources/images/ui/DifficultyFrame.png");
    private static final TutorialStrings tutorialStrings = CardCrawlGame.languagePack.getTutorialString("Difficulty");
    public static final String[] MSG = tutorialStrings.TEXT;
    public static final String[] LABEL = tutorialStrings.LABEL;
    private float XPosition = 32 * Settings.scale;
    private float YPosition = 700 * Settings.scale;
    private Hitbox hb = new Hitbox(XPosition, YPosition, 400 * Settings.scale, 44 * Settings.scale);
    private int Difficulty = 0;
    private int DifficultyIndex;
    private float TimePassed = 0.0F;

    public void tick() {
        TimePassed += Gdx.graphics.getDeltaTime();
        if (TimePassed >= 1.0F) // <- Will be a lot slower when finished, just that fast for debugging purposes
        {
            TimePassed = 0;
            Difficulty++;
        }
        DifficultyIndex = (int) Math.ceil(Difficulty / 40 + 1);
        if (DifficultyIndex > 9) {
            DifficultyIndex = 9;
        }
    }
    public int getDifficulty() {
        return Difficulty;
    }

    public void setDifficulty(int D) {
        Difficulty = D;
    }

    public void render(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        sb.draw(DifficultyMeter, XPosition, YPosition, 400 * Settings.scale, 44 * Settings.scale);
        if (Difficulty <= 356) {
            sb.draw(DifficultyFrame, XPosition - 2 * Settings.scale + (Difficulty - (Difficulty % 2/*Attempt to fix strange wobbling*/)) * Settings.scale, YPosition, 44 * Settings.scale, 44 * Settings.scale);
        } else {
            sb.draw(DifficultyFrame, XPosition + 354 * Settings.scale, YPosition, 44 * Settings.scale, 44 * Settings.scale);
        }
        FontHelper.renderFontCentered(sb, FontHelper.deckCountFont, MSG[DifficultyIndex], XPosition + 200 * Settings.scale, YPosition - 40 * Settings.scale, Color.WHITE.cpy());
    }

    public void updatePositions() {
        this.hb.update();
        if ((this.hb.hovered) && (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) && (!AbstractDungeon.isScreenUp)) {
            TipHelper.renderGenericTip(XPosition, YPosition, LABEL[0] + " (" + MSG[DifficultyIndex] + ")", MSG[0]);
        }
        if (this.hb.hovered && (!AbstractDungeon.isScreenUp)) {
            if (InputHelper.justClickedLeft) {
                this.hb.clickStarted = true;
            }
        }
        if (this.hb.clickStarted) {
            XPosition = InputHelper.mX - 200 * Settings.scale;
            YPosition = InputHelper.mY;
            hb.update(XPosition, YPosition);
            this.hb.clicked = false;
        }
    }
    public void onBattleStart(AbstractMonster m) {
        m.increaseMaxHp(m.maxHealth * this.Difficulty / 200 * (int) Math.floor(AbstractDungeon.miscRng.random(0.8F, 1.2F)), false);
    }
}
