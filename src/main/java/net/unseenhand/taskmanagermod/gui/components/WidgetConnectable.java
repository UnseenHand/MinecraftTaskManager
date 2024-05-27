package net.unseenhand.taskmanagermod.gui.components;

import net.minecraft.client.gui.components.AbstractWidget;

public interface WidgetConnectable<T extends AbstractWidget> {
    void connectWidget(T widget);
}
