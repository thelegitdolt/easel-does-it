package com.dolthhaven.easeldoesit;

public enum ToDoList {
    REGISTER_EASEL_BASIC_BLOCK_PROPERTIES(true),
    MAKE_EASEL_HAVE_A_BLANK_GUI(true),
    MAKE_THE_EASEL_GUI_ABLE_TO_CONVERT_PAINTINGS_TO_A_DIAMOND_ITEMSTACK(true),
    MAKE_THE_EASEL_GUI_ABLE_TO_SAVE_1_PAINTING_TYPE(true),
    MAKE_THE_EASEL_GUI_DISPLAY_THE_SAVED_PAINTING_TYPE(true),
    EASEL_SCREEN_BUTTONS(true),
    EASEL_GUI_SCROLLING_BETWEEN_DIFFERENT_PAINTINGS_OF_THE_SAME_SIZE(true),
    ABLE_TO_CHOOSE_THE_EASEL_GUI_WITH_BUTTONS(true),
    LEVIS_IDEA_ABOUT_GRAYED_OUT_DIMENSION_BUTTONS(false),
    VISUAL_POLISH(false),
    SCROLL_BAR(false);

    private final boolean isDone;

    ToDoList(boolean isDone) {
        this.isDone = isDone;
    }

    public boolean isDone() {
        return isDone;
    }
}
