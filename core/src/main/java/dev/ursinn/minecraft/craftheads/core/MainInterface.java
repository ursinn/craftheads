package dev.ursinn.minecraft.craftheads.core;

import dev.ursinn.minecraft.craftheads.core.utils.Categories;
import dev.ursinn.minecraft.craftheads.core.utils.CraftHeadsMessageKeys;
import dev.ursinn.utils.minecraft.checker.UpdateChecker;

public interface MainInterface {

    float getDefaultHeadPrice();

    float getOwnHeadPrice();

    float getOtherHeadPrice();

    String messageFormatter(CraftHeadsMessageKeys localMessageKeys);

    UpdateChecker getUpdateChecker();

    Categories getCategories();
}
