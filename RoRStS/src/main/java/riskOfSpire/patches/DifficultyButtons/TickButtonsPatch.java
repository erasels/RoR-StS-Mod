package riskOfSpire.patches.DifficultyButtons;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import riskOfSpire.ui.DifficultyButton;

@SpirePatch(clz = CharacterSelectScreen.class, method = "updateButtons")
public class TickButtonsPatch {
    @SpirePostfixPatch
    public static void patch(CharacterSelectScreen __instance) {
        for (DifficultyButton D : DifficultyButton.Buttons) {
            D.update();
        }
    }
}
