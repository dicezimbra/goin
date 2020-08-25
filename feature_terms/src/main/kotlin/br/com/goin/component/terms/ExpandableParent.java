package br.com.goin.component.terms;

public class ExpandableParent {

    private String text;
    private int drawableId;



    public String getText() {
        return text;
    }

    public int getDrawableId() {
        return drawableId;
    }


    public ExpandableParent(String text, int drawableId) {
        this.text = text;
        this.drawableId = drawableId;

    }
}
