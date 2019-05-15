package riskOfSpire.patches.StartingScreen;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.vfx.scene.LogoFlameEffect;

@SpirePatch(clz = LogoFlameEffect.class, method = SpirePatch.CONSTRUCTOR)
public class RemoveFlames {
    @SpirePrefixPatch
    public static void pathc(LogoFlameEffect __instance) {
        __instance.isDone = true;
    }
}
