package riskOfSpire.patches.relics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;
import javassist.CtBehavior;
import riskOfSpire.RiskOfSpire;


@SpirePatch(
        clz = TipHelper.class,
        method = "renderTipBox",
        paramtypez = { float.class, float.class, SpriteBatch.class, String.class, String.class }
)
public class ColoredRarityTips {
    private static final float TEXT_OFFSET_X;
    private static final float HEADER_OFFSET_Y;
    private static final Color BASE_COLOR = new Color(1.0F, 0.9725F, 0.8745F, 1.0F);

    private static Color recolor;
    private static boolean enabled = false;

    @SpirePrefixPatch
    public static void getTitle(float x, float y, SpriteBatch sb, @ByRef String[] originalTitle, String description)
    {
        //start displaying a tip - Disable recolor.
        enabled = false;
        //Check if title requires recoloring
        if (originalTitle[0].startsWith(RiskOfSpire.makeID("@RECOLOR@"))) {
            enabled = true;

            originalTitle[0] = originalTitle[0].replace(RiskOfSpire.makeID("@RECOLOR@"), "");
            char colorChar = originalTitle[0].charAt(0);
            originalTitle[0] = originalTitle[0].substring(1); //Set title to proper title so the box is correctly sized
            recolor = Settings.GOLD_COLOR; //Get recolor color
            switch (colorChar)
            {
                case 'r':
                    recolor = Color.RED.cpy();
                    break;
                case 'g':
                    recolor = new Color(0.57647058823f,0.88235294117f,0.43137254902f,1.0f);
                    break;
                case 'b':
                    recolor = new Color(0.07058823529f,0.74901960784f,1.0f,1.0f);
                    break;
                case 'w':
                    recolor = BASE_COLOR.cpy();
                    break;
                case 'o':
                    recolor = Color.ORANGE.cpy();
                    break;
                case 'y':
                    recolor = Color.YELLOW.cpy();
                    break;
            }
        }
    }

    @SpireInsertPatch(
            locator = Locator.class,
            localvars = { "title" }
    )
    public static SpireReturn renderAlternateColor(float x, float y, SpriteBatch sb, String originalTitle, String description, @ByRef String[] title)
    {
        if (enabled)
        {
            FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont, title[0], x + TEXT_OFFSET_X, y + HEADER_OFFSET_Y, recolor);
            title[0] = ""; //Clear title so it isn't re-rendered in base method.
        }
        return SpireReturn.Continue();
    }

    private static class Locator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(FontHelper.class, "renderFontLeftTopAligned");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }

    static
    {
        TEXT_OFFSET_X = 22.0F * Settings.scale;
        HEADER_OFFSET_Y = 12.0F * Settings.scale;
    }
}
