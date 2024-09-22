package com.dolthhaven.easeldoesit;

@SuppressWarnings("unused")
public enum ToDoList {
    SCROLLABLE_PAINTING_INDEX(false),
    EASEL_WITH_PAINTING_SPRITE(false);

    private final boolean isDone;

    ToDoList(boolean isDone) {
        this.isDone = isDone;
    }

    public boolean isDone() {
        return isDone;
    }
}
