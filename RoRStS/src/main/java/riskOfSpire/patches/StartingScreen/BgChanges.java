package riskOfSpire.patches.StartingScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.scenes.TitleBackground;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import riskOfSpire.util.TextureLoader;
import riskOfSpire.vfx.titlescreen.CustomSlowTitleCloud;
import riskOfSpire.vfx.titlescreen.CustomTitleCloud;
import riskOfSpire.vfx.titlescreen.Particle;

import java.util.ArrayList;
import java.util.Iterator;


public class BgChanges {
    public static Texture Bg1 = TextureLoader.getTexture("riskOfSpireResources/images/screens/rorbg1.png");
    public static Texture Spire = TextureLoader.getTexture("riskOfSpireResources/images/screens/spire2.png");
    public static Texture Bg2 = TextureLoader.getTexture("riskOfSpireResources/images/screens/rorbg2.png");
    public static Texture Clouds = TextureLoader.getTexture("riskOfSpireResources/images/screens/clouds1.png");
    public static ArrayList<CustomTitleCloud> VfxClouds = new ArrayList<>();
    public static ArrayList<CustomSlowTitleCloud> SlowVfxClouds = new ArrayList<>();
    public static ArrayList<Particle> VfxParticles = new ArrayList<>();
    private static float Dusttimer = 0.0F;

    @SpirePatch(clz = TitleBackground.class, method = "render")
    public static class CustomBgRender {

        @SpireInsertPatch(locator = Locator.class)
        public static void RenderBgPatch(TitleBackground __instance, SpriteBatch sb) {
            updateDust();
            sb.setColor(Color.WHITE);
            sb.draw(Bg2, 0, 0, Settings.WIDTH, Settings.HEIGHT);
            for (CustomSlowTitleCloud c : BgChanges.SlowVfxClouds) {
                c.update();
                c.render(sb);
            }
            sb.draw(Spire, Settings.WIDTH / 4, 0, 600 * Settings.scale, Settings.HEIGHT);
            for (CustomTitleCloud c : BgChanges.VfxClouds) {
                c.update();
                c.render(sb);
            }
            sb.draw(Clouds, 0, Settings.HEIGHT - 300 * Settings.scale, Settings.WIDTH, Settings.scale * 600);
            sb.draw(Bg1, 0, 0, Settings.WIDTH, Settings.HEIGHT);
            for (Particle p : VfxParticles) {
                p.render(sb);
            }
        }

        private static void updateDust() {
            Dusttimer -= Gdx.graphics.getDeltaTime();
            if (Dusttimer <= 0) {
                VfxParticles.add(new Particle());
                Dusttimer = 0.5F;
            }
            for (Iterator<Particle> p = VfxParticles.iterator(); p.hasNext(); ) {
                Particle effect = p.next();
                effect.update();
                if (effect.isDone) {
                    p.remove();
                }
            }
        }


        private static class Locator extends SpireInsertLocator {
            ArrayList<Matcher> Prerequisites = new ArrayList<>();

            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(
                        TitleBackground.class, "logoAlpha");
                return LineFinder.findAllInOrder(ctMethodToPatch, Prerequisites, finalMatcher);
            }
        }
    }
}
