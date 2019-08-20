package riskOfSpire.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
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
import riskOfSpire.powers.BlazingPower;
import riskOfSpire.powers.GlacialPower;
import riskOfSpire.powers.OverloadingPower;
import riskOfSpire.util.TextureLoader;

public class DifficultyMeter {

    public static Texture DifficultyMeter = TextureLoader.getTexture("riskOfSpireResources/images/ui/DifficultyMeter.png");
    public static Texture DifficultyFrame = TextureLoader.getTexture("riskOfSpireResources/images/ui/DifficultyFrame.png");
    private static final TutorialStrings tutorialStrings = CardCrawlGame.languagePack.getTutorialString("DifficultyMeter");
    public static final String[] MSG = tutorialStrings.TEXT;
    public static final String[] LABEL = tutorialStrings.LABEL;
    private float XPosition = 32 * Settings.scale;
    private float YPosition = 700 * Settings.scale;
    private Hitbox hb = new Hitbox(XPosition, YPosition, 400 * Settings.scale, 44 * Settings.scale);
    private int Difficulty = 0;
    private int DifficultyIndex;
    private float DifficultyMod = 1;
    private float TimePassed = 0.0F;

    private final static int DIFFICULTY_THRESHOLD = 40;

    public void tick() {
        if (!AbstractDungeon.isScreenUp) {
            TimePassed += Gdx.graphics.getRawDeltaTime(); //SuperFastMode compatibility. Raw isn't patched by SFM
            if (TimePassed * DifficultyMod >= 6.0F) {
                TimePassed = 0;
                Difficulty++;
            }
            DifficultyIndex = MathUtils.ceil(Difficulty / DIFFICULTY_THRESHOLD + 1);
            if (DifficultyIndex > 9) {
                DifficultyIndex = 9;
            }
        }
    }

    public int getDifficulty() {
        return Difficulty;
    }

    public void setDifficulty(int D) {
        Difficulty = D;
    }

    public float getDifficultyMod() {
        return DifficultyMod;
    }

    public void setDifficultyMod(float D) {
        DifficultyMod = D;
    }

    public void render(SpriteBatch sb) {
        if (!(getDifficultyMod() == 0.0f)) {
            sb.setColor(Color.WHITE);
            sb.draw(DifficultyMeter, XPosition, YPosition, 400 * Settings.scale, 44 * Settings.scale);
            if (Difficulty <= 356) {
                sb.draw(DifficultyFrame, XPosition - 2 * Settings.scale + (Difficulty - (Difficulty % 2/*Attempt to fix strange wobbling*/)) * Settings.scale, YPosition, 44 * Settings.scale, 44 * Settings.scale);
            } else {
                sb.draw(DifficultyFrame, XPosition + 354 * Settings.scale, YPosition, 44 * Settings.scale, 44 * Settings.scale);
            }
            FontHelper.renderFontCentered(sb, FontHelper.deckCountFont, MSG[DifficultyIndex], XPosition + 200 * Settings.scale, YPosition - 40 * Settings.scale, Color.WHITE.cpy());
        }
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

    public void hideHitbox() {
        this.hb.resize(0f, 0f);
    }

    public void unhideHitbox() {
        this.hb.resize(400 * Settings.scale, 44 * Settings.scale);
    }

    public void PreUpgradeMonsterHealth(AbstractMonster m) {
        if (getDifficultyMod() > 0f) {
            float modifier = 1f; //To nerf the health gain on high health enemies as to not make it too crazy
            if (m.type == AbstractMonster.EnemyType.BOSS) {
                modifier = 0.4f;
            } else if (m.type == AbstractMonster.EnemyType.ELITE) {
                modifier = 0.7f;
            }

            m.maxHealth += MathUtils.round(((float) m.maxHealth * modifier) * this.Difficulty / 250F * AbstractDungeon.miscRng.random(0.8F, 1.2F));
            //TODO: Add alternatives like gaining strength and Regen
        }
    }

    public void UpgradeMonsterHealth(AbstractMonster m) {
        if (getDifficultyMod() > 0f) {
            float modifier = 1f; //To nerf the health gain on high health enemies as to not make it too crazy
            if (m.type == AbstractMonster.EnemyType.BOSS) {
                modifier = 0.4f;
            } else if (m.type == AbstractMonster.EnemyType.ELITE) {
                modifier = 0.7f;
            }
            m.currentHealth += MathUtils.round(((float) m.maxHealth * modifier) * this.Difficulty / 250F * AbstractDungeon.miscRng.random(0.8F, 1.2F));
            //TODO: Add alternatives like gaining strength and Regen
        }
    }

    public void SetElite(AbstractMonster m) {
        int r = AbstractDungeon.monsterRng.random(1, 100);
        if ((getDifficultyMod() == 0f && r <= 5) || (getDifficultyMod() > 0f && r <= 40 - (40 / Math.max((getDifficulty() / 100 + 1), 1)))) {
            r = r % 3;
            switch (r) {
                case 0:
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, null, new OverloadingPower(m)));
                    break;
                case 1:
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, null, new GlacialPower(m)));
                    break;
                default:
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, null, new BlazingPower(m)));
                    break;
            }
        }
    }
}
