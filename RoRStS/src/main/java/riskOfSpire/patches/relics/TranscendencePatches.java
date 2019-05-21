package riskOfSpire.patches.relics;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import riskOfSpire.RiskOfSpire;
import riskOfSpire.util.TextureLoader;

public class TranscendencePatches {
    @SpirePatch(
            clz= TopPanel.class,
            method=SpirePatch.CLASS
    )
    public static class TranscendenceField {
        public static SpireField<Boolean> hasTranscendence = new SpireField<>(() -> false);
    }

    @SpirePatch(
            clz= TopPanel.class,
            method="renderHP"
    )
    public static class AltHPRender {
        @SpirePrefixPatch
        public static SpireReturn Prefix(TopPanel __instance, SpriteBatch sb) {
            if(!TranscendenceField.hasTranscendence.get(__instance)) {
                return SpireReturn.Continue();
            }

            if(hpIconX == 0) {
                hpIconX = (float)ReflectionHacks.getPrivate(__instance, TopPanel.class, "hpIconX");
            }

            sb.setColor(Color.WHITE);
            sb.draw(img, hpIconX, ICON_Y, ICON_W, ICON_W);
            String tmp;
            if(AbstractDungeon.player.currentHealth != AbstractDungeon.player.maxHealth) {
                tmp = "("+ AbstractDungeon.player.currentHealth + ") " + AbstractDungeon.player.maxHealth;
            } else {
                tmp = Integer.toString(AbstractDungeon.player.maxHealth);
            }
            FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, tmp, hpIconX + HP_NUM_OFFSET_X, INFO_TEXT_Y, Color.SKY);

            __instance.hpHb.render(sb);
            return SpireReturn.Return(null);
        }
    }


    private static float hpIconX;
    public static final Texture img = TextureLoader.getTexture(RiskOfSpire.assetPath("images/ui/") + "hpIcon.png");
    public static float ICON_W = 64.0F * Settings.scale;
    public static float ICON_Y = (float)Settings.HEIGHT - ICON_W;
    public static float INFO_TEXT_Y = (float)Settings.HEIGHT - 24.0F * Settings.scale;
    public static float HP_NUM_OFFSET_X = 60.0F * Settings.scale;
}
