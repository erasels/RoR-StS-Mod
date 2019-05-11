package riskOfSpire.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.util.TextureLoader;

import java.util.ArrayList;

public class DifficultyButton {
    public static Texture HoverTexture = TextureLoader.getTexture("riskOfSpireResources/images/ui/ButtonGlow.png");
    public static ArrayList<DifficultyButton> Buttons = new ArrayList<>();
    private static TutorialStrings ButtonStrings = CardCrawlGame.languagePack.getTutorialString("DifficultyButton");
    private static String[] LABEL = ButtonStrings.LABEL;
    public Texture texture;
    private float x;
    private float y;
    private float DifficultyMod;
    private String Name;
    private Hitbox hitbox;
    private boolean isselected = false;

    public DifficultyButton(String texturepath, float x, float y, float DifficultyMod, String Name) {
        this.texture = TextureLoader.getTexture(texturepath);
        this.x = x;
        this.y = y;
        this.DifficultyMod = DifficultyMod;
        this.Name = Name;
        this.hitbox = new Hitbox(x * Settings.scale, y * Settings.scale, 50.0F * Settings.scale, 50.0F * Settings.scale);
    }

    public float getDifficultyMod() {
        return DifficultyMod;
    }

    public void setDifficultyMod(float amount) {
        DifficultyMod = amount;
    }

    public void setSelected() {
        for (DifficultyButton d : Buttons) {
            d.isselected = false;
        }
        isselected = true;
        RiskOfSpire.DifficultyMeter.setDifficultyMod(DifficultyMod);
    }


    public void render(SpriteBatch sb) {
        if (this.isselected) {
            sb.draw(HoverTexture, x * Settings.scale, y * Settings.scale, 50.0F * Settings.scale, 50.0F * Settings.scale);
        }
        sb.draw(texture, x * Settings.scale, y * Settings.scale, 50.0F * Settings.scale, 50.0F * Settings.scale);
    }

    public void update() {
        this.hitbox.update();
        if ((InputHelper.justClickedLeft) && (this.hitbox.hovered)) {
            this.hitbox.clickStarted = true;
        }
        if (this.hitbox.hovered) {
            TipHelper.renderGenericTip(x * Settings.scale, (y - 50) * Settings.scale, LABEL[0], Name);
        }
        if (this.hitbox.clicked) {
            CardCrawlGame.sound.play("UI_CLICK_1");
            for (DifficultyButton d : Buttons) {
                d.isselected = false;
            }
            isselected = true;
            RiskOfSpire.DifficultyMeter.setDifficultyMod(DifficultyMod);
            this.hitbox.clicked = false;
        }
        if (this.hitbox.justHovered) {
            CardCrawlGame.sound.play("UI_HOVER");
        }
    }
}
