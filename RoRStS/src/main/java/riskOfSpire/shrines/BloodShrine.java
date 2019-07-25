package riskOfSpire.shrines;

public class BloodShrine extends AbstractShrineEvent {
    public BloodShrine(String title, String body, String imgUrl) {
        super(title, body, imgUrl);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        //Code here

        //Once the leave button is pressed
        super.buttonEffect(buttonPressed);
    }
}
