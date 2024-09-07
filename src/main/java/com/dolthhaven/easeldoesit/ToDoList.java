package com.dolthhaven.easeldoesit;

public enum ToDoList {
    REGISTER_EASEL_BASIC_BLOCK_PROPERTIES(false),
    MAKE_EASEL_HAVE_A_BLANK_GUI(false),
    MAKE_THE_EASEL_GUI_ABLE_TO_CONVERT_PAINTINGS_TO_A_DIAMOND_ITEMSTACK(false),
    MAKE_THE_EASEL_GUI_ABLE_TO_SAVE_1_PAINTING_TYPE(false),
    MAKE_THE_EASEL_GUI_DISPLAY_THE_SAVED_PAINTING_TYPE(false);

    private final boolean isDone;

    private ToDoList(boolean isDone) {
        this.isDone = isDone;
    }

    public boolean isDone() {
        return isDone;
    }
}
