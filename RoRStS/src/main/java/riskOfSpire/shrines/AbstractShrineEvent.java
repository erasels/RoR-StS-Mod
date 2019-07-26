package riskOfSpire.shrines;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public abstract class AbstractShrineEvent extends AbstractImageEvent {
    public AbstractRoom originalRoom;

    public AbstractShrineEvent(String title, String body, String imgUrl) {
        super(title, body, imgUrl);
    }
    //TODO: Set originalRoom to AD:room again (look at FOrkInTheRoad event

    //Call super.butonEffect to call this after buttonEffect is done
    @Override
    protected void buttonEffect(int buttonPressed) {
        AbstractDungeon.rs = AbstractDungeon.RenderScene.NORMAL;
        AbstractDungeon.currMapNode.room = originalRoom;
        GenericEventDialog.hide();
        //AbstractDungeon.fadeOut();
        openMap();
    }

    public abstract AbstractShrineEvent makeCopy();
}
