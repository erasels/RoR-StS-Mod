package riskOfSpire.relics.Abstracts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.patches.HitboxRightClick;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.GameCursor;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.patches.ForUsableRelics.UsableRelicSlot;
import riskOfSpire.relics.Interfaces.ModifyCooldownRelic;
import riskOfSpire.relics.Interfaces.MultiplyCooldownRelic;
import riskOfSpire.util.helpers.RiskOfRainRelicHelper;

import java.util.ArrayList;

public abstract class UsableRelic extends BaseRelic {
    private static RelicStrings usableRelicStrings = CardCrawlGame.languagePack.getRelicStrings(RiskOfSpire.makeID("UsableRelic"));

    public UsableRelic(String setId, String imgName, RelicTier tier, LandingSound sfx, boolean coolDownBased) {
        super(setId, "", tier, sfx);

        imgUrl = imgName;

        if (img == null || outlineImg == null) {
            img = ImageMaster.loadImage(RiskOfSpire.assetPath("images/relics/" + imgName));
            largeImg = ImageMaster.loadImage(RiskOfSpire.assetPath("images/largeRelics/" + imgName));
            outlineImg = ImageMaster.loadImage(RiskOfSpire.assetPath("images/relics/outline/" + imgName));
        }

        this.tips.add(new PowerTip(usableRelicStrings.NAME, usableRelicStrings.DESCRIPTIONS[0]));

        //if (coolDownBased) { //This is no good, because the usable relic tip is important as it explains that they are right clicked to use.
        this.counter = 0; //cooldown.
        //}
        this.beginLongPulse();
    }

    public UsableRelic(String setId, String imgName, RelicTier tier, LandingSound sfx) {
        this(setId, imgName, tier, sfx, true);
    }


        @Override
    public void loadLargeImg()
    {
        if (this.largeImg == null) {
            largeImg = ImageMaster.loadImage(RiskOfSpire.assetPath("images/relicsBig/" + imgUrl));
        }
    }

    public UsableRelic(String setId, Texture img, Texture outline, RelicTier tier, LandingSound sfx) {
        super(setId, "", tier, sfx);
        img.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        outline.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        this.img = img;
        largeImg = img;
        outlineImg = outline;

        this.counter = 0; //cooldown.
        this.beginLongPulse();
    }

    public String getCooldownString()
    {
        return usableRelicStrings.DESCRIPTIONS[1] + getFinalCooldown() + (getFinalCooldown() == 1 ? usableRelicStrings.DESCRIPTIONS[2] : usableRelicStrings.DESCRIPTIONS[3]);
    }

    public abstract boolean isUsable(); //Whether or not it has an active effect, or just toggles.

    public abstract int getBaseCooldown(); //Base cooldown, pre-modifiers.

    public void updateDescriptionWhenNeeded() {
        this.description = this.getUpdatedDescription();
        ArrayList<PowerTip> tmp = new ArrayList<>();
        this.tips.forEach(pT -> {
            if (!pT.header.equals(this.name) && !pT.header.startsWith(RiskOfSpire.makeID("@RECOLOR@"))) tmp.add(pT);
        });
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.tips.addAll(tmp);
        this.initializeTips();
    }

    @Override
    protected void initializeTips() {
        super.initializeTips();

        if (tips.size() > 0 && tips.get(0).header.toLowerCase().equals(name.toLowerCase())) {
            tips.get(0).header = RiskOfSpire.makeID("@RECOLOR@") + getColorChar() + tips.get(0).header;
        }
    }

    public char getColorChar()
    {
        return 'o';
    }

    public int getFinalCooldown() {
        float cooldown = getBaseCooldown();
        if (AbstractDungeon.player != null) {
            ArrayList<MultiplyCooldownRelic> multipliers = new ArrayList<>(); //to avoid iterating over ALL relics twice
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                if (r instanceof ModifyCooldownRelic) {
                    cooldown = ((ModifyCooldownRelic) r).modifyCooldown(cooldown);
                }
                if (r instanceof MultiplyCooldownRelic) {
                    multipliers.add((MultiplyCooldownRelic) r);
                }
            }
            for (MultiplyCooldownRelic r : multipliers) {
                cooldown = r.modifyCooldown(cooldown);
            }
        }
        if (cooldown < 1 && getBaseCooldown() > 0)
            return 1;
        if (cooldown < 0) //No negative cd.
            return 0;
        return MathUtils.floor(cooldown);
    }

    @Override
    public int getPrice() {
        switch(this.tier) {
            case COMMON:
                return 75;
            case RARE:
            case SHOP:
                return 150;
            case SPECIAL:
                return 300;
            case BOSS:
                return 999;
            default:
                return 100;
        }
    }

    public void activateCooldown() {
        setCounter(getFinalCooldown());
    }

    @Override
    public void setCounter(int counter) {
        super.setCounter(counter);
        if (this.counter == 0) {
            this.beginLongPulse();
        } else {
            this.stopPulse();
        }
    }

    public void updateCooldown() {
        if (this.counter > 0) {
            counter--;
            if (this.counter == 0) {
                this.beginLongPulse();
            }
        }
    }

    public void reduceCooldown(int cd) {
        if (this.counter > 0) {
            counter-=cd;
            if (this.counter <= 0) {
                counter = 0;
                this.beginLongPulse();
            }
        }
    }

    public void onRightClick() {

    }

    public void onRightClickInCombat() {

    }


    public void normalUpdate() {
        if (this.flashTimer != 0.0F) {
            this.flashTimer -= Gdx.graphics.getDeltaTime();
            if (this.flashTimer < 0.0F) {
                if (this.pulse) {
                    this.flashTimer = 1.0F;
                } else {
                    this.flashTimer = 0.0F;
                }
            }
        }

        this.hb.update();

        if (this.hb.hovered) {
            if (AbstractDungeon.topPanel.potionUi.isHidden) {
                this.scale = Settings.scale * 1.25F;
                CardCrawlGame.cursor.changeType(GameCursor.CursorType.INSPECT);
            }
            if (InputHelper.justClickedLeft) {
                this.hb.clickStarted = true;
            }
        } else {
            this.scale = MathHelper.scaleLerpSnap(this.scale, Settings.scale);
        }

        if (this.hb.clicked || this.hb.hovered && CInputActionSet.select.isJustPressed()) {
            CardCrawlGame.relicPopup.open(this, AbstractDungeon.player.relics);
            CInputActionSet.select.unpress();
            this.hb.clicked = false;
            this.hb.clickStarted = false;
        }

        if (HitboxRightClick.rightClicked.get(this.hb)) {
            onRightClick();
            if (AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
                if (!AbstractDungeon.actionManager.turnHasEnded)
                    onRightClickInCombat();
            }
        }
    }

    public void normalRender(SpriteBatch sb) {
        this.renderOutline(sb, false);
        if (this.hb.hovered) {
            this.renderTip(sb);
        }

        sb.setColor(Color.WHITE);
        //TODO: Would be nice if this could be largeImg but it NPEs at switchTexture at texture.getWidth() if I just change the below to largeImg
        sb.draw(this.img, this.currentX - 64.0F, this.currentY - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, this.scale, this.scale, 0, 0, 0, 128, 128, false, false);
        this.renderCounter(sb, false);

        this.renderFlash(sb, false);
    }

     @Override
     public boolean canSpawn() {
        return !(UsableRelicSlot.usableRelic.get(AbstractDungeon.player) != null && UsableRelicSlot.usableRelic.get(AbstractDungeon.player).relicId.equals(this.relicId));
     }

    @Override
    public void instantObtain(AbstractPlayer p, int slot, boolean callOnEquip) {
        this.isDone = true;
        this.isObtained = true;

        this.currentX = START_X;
        this.currentY = START_Y;
        this.targetX = this.currentX;
        this.targetY = this.currentY;
        this.hb.move(this.currentX, this.currentY);
        if (callOnEquip) {
            this.onEquip();
            this.relicTip();
        }

        UsableRelicSlot.usableRelic.set(p, this);

        UnlockTracker.markRelicAsSeen(this.relicId);
        RiskOfRainRelicHelper.removeFromPool(this);

        notifyRelicGet();
    }

    @Override
    public void instantObtain() {
        this.playLandingSFX();
        this.isDone = true;
        this.isObtained = true;
        this.currentX = START_X;
        this.currentY = START_Y;
        this.targetX = this.currentX;
        this.targetY = this.currentY;
        this.flash();
        this.hb.move(this.currentX, this.currentY);
        this.onEquip();
        this.relicTip();

        UsableRelicSlot.usableRelic.set(AbstractDungeon.player, this);
        RiskOfRainRelicHelper.removeFromPool(this);

        UnlockTracker.markRelicAsSeen(this.relicId);

        notifyRelicGet();
    }

    @Override
    public void obtain() {
        this.hb.hovered = false;
        this.targetX = START_X;
        this.targetY = START_Y;
        this.relicTip();

        UsableRelicSlot.usableRelic.set(AbstractDungeon.player, this);

        UnlockTracker.markRelicAsSeen(this.relicId);
        RiskOfRainRelicHelper.removeFromPool(this);

        notifyRelicGet();
    }

    @Override
    public void onRelicGet(AbstractRelic r) {
        updateDescriptionWhenNeeded();
    }

    private static float START_X = 64.0F * Settings.scale;
    private static float START_Y = Settings.HEIGHT - 250.0F * Settings.scale;
}
