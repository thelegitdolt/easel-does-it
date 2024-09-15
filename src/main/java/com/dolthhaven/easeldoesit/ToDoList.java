package com.dolthhaven.easeldoesit;

@SuppressWarnings("unused")
public enum ToDoList {
    CLICKABLE_PAGE_BUTTONS(false),
    POLISH_PICKERS(false);

    private final boolean isDone;

    ToDoList(boolean isDone) {
        this.isDone = isDone;
    }

    public boolean isDone() {
        return isDone;
    }
}
