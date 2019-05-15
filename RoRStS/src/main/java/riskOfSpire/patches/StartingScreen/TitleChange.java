package riskOfSpire.patches.StartingScreen;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.scenes.TitleBackground;
import riskOfSpire.util.TextureLoader;

public class TitleChange {
    @SpirePatch(clz = TitleBackground.class, method = SpirePatch.CONSTRUCTOR)
    public static class InsertCustomLogo {
        @SpirePrefixPatch
        public static void patch(TitleBackground __instance) {
            ReflectionHacks.setPrivate(__instance, TitleBackground.class, "titleLogoImg", TextureLoader.getTexture("riskOfSpireResources/images/screens/RoRStS.png"));
            ReflectionHacks.setPrivate(__instance, TitleBackground.class, "W", TextureLoader.getTexture("riskOfSpireResources/images/screens/RoRStS.png").getWidth());
            ReflectionHacks.setPrivate(__instance, TitleBackground.class, "H", TextureLoader.getTexture("riskOfSpireResources/images/screens/RoRStS.png").getHeight());
        }
    }
}
