package riskOfSpire.patches.DifficultyButtons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import riskOfSpire.ui.DifficultyButton;

import java.util.ArrayList;


@SpirePatch(clz = CharacterSelectScreen.class, method = "render")
public class RenderButtonsPatch {
    private static TutorialStrings ButtonStrings = CardCrawlGame.languagePack.getTutorialString("DifficultyButton");
    private static String[] LABEL = ButtonStrings.LABEL;
    @SpireInsertPatch(locator = riskOfSpire.patches.DifficultyButtons.RenderButtonsPatch.Locator.class)
    public static void patch(CharacterSelectScreen __instance, SpriteBatch sb) {
        FontHelper.renderFontLeft(sb, FontHelper.cardTitleFont_small_L, LABEL[0], Settings.WIDTH - 230.0F * Settings.scale, 120.0F * Settings.scale, Color.WHITE.cpy());
        for (DifficultyButton D : DifficultyButton.Buttons) {
            D.render(sb);
        }
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(
                    CharacterSelectScreen.class, "cancelButton");
            return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
        }
    }
}