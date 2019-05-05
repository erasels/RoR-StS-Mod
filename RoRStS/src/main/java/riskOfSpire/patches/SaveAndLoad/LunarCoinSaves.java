package riskOfSpire.patches.SaveAndLoad;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import riskOfSpire.RiskOfSpire;

public class LunarCoinSaves {
    @SpirePatch(
            cls = "com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue",
            method = "loadSaveString",
            paramtypes = {"java.lang.String"}
            )
    public static class LoadGame1 {

        public static void Prefix(String filePath) {
            RiskOfSpire.loadData();
        }
    }

    @SpirePatch(
            cls = "com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue",
            method = "loadSaveFile",
            paramtypes = {"java.lang.String"}
            )
    public static class LoadGame2 {
        public static void Prefix(String filePath) {
            RiskOfSpire.loadData();
        }
    }

    @SpirePatch(cls = "com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue", method = "save")
    public static class SaveGame {
        public static void Prefix(SaveFile save) {
            RiskOfSpire.saveData();
        }
    }

    @SpirePatch(cls = "com.megacrit.cardcrawl.saveAndContinue.SaveAndContinue", method = "deleteSave")
    public static class DeleteSave {
        public static void Prefix(AbstractPlayer p) {
            RiskOfSpire.clearData();
        }
    }
}
