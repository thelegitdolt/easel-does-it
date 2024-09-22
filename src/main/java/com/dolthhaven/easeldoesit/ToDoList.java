package com.dolthhaven.easeldoesit;

@SuppressWarnings("unused")
public enum ToDoList {
    SCROLLABLE_PAINTING_INDEX(false),
    EASEL_SAVES_PERMANENTLY_EVEN_IF_MENU_CLICKED_OFF(false),
    EASEL_WITH_PAINTING_SPRITE(false),
    VARIANTED_PAINTING_ITEM_ALWAYS_DROPS(false);

    private final boolean isDone;

    ToDoList(boolean isDone) {
        this.isDone = isDone;
    }

    public boolean isDone() {
        return isDone;
    }
}
