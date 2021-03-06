package riskOfSpire.util;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.ui.panels.TopPanel;

public class StringManipulationUtilities {
    public static String ordinalNaming(int num) {
        if(Settings.language == Settings.GameLanguage.ENG) {
            return num + TopPanel.getOrdinalNaming(num);
        }
        return Integer.toString(num);
    }
}
